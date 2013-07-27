package org.flixel.plugin.flxbox2d.controllers;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * The buoyancy calculations originated from wck which in turn came from Boris the Brave's work. iforce2d has an
 * excellent
 * write-up on buoyancy here: https://www.iforce2d.net/b2dtut/buoyancy.
 * <p>
 * 
 * Tips:<br>
 * - Set the surface normal in the direction 'out' from the fluid<br>
 * - Set the fluid velocity if you want the fluid to have a 'current', like a stream or river<br>
 * - Set the gravity according to your world's gravity<br>
 * - Set the surface height to the Box2D height of the fluid. Make sure you are using world coordinates!<br>
 * - Linear drag should be roughly 5x the fluid density<br>
 * - Extremely low density objects may 'popcorn' from the fluid. Adjust density and linear drag to compensate. If you
 * have a mix of low and high density objects... well, good luck.<br>
 * - This project is integrated with https://github.com/tescott/RubeLoader. Use hot-code replacement / debug mode to
 * experiment with different objects and different densities<br>
 * 
 * @author tescott
 * 
 */
public class B2BuoyancyController extends B2Controller
{

   // The outer surface normal. Change this to point out from the surface. Assume up.
   public final Vector2 mSurfaceNormal = new Vector2();
   // Fluid velocity, for drag calculations. Creates a directional 'current' for the fluid.
   public final Vector2 mFluidVelocity = new Vector2();
   // Gravity vector of the fluid. Used to provide upward force within the fluid
   public final Vector2 mGravity = new Vector2();
   // Linear drag co-efficient. Recommend that this is about 5x the angular drag.
   public float mLinearDrag;
   // Angular drag co-efficient
   public float mAngularDrag;
   // The height of the Box2D fluid surface at the normal
   public float mSurfaceHeight;
   // The fluid density
   public float mFluidDensity;

   // If false, bodies are assumed to be uniformly dense, otherwise use the
   // shapes' densities
   public boolean mUseDensity = true;

   //
   // Shared values
   //
   private static final Vector2 mTmp = new Vector2(); // scratch value for various calculations
   private static final Vector2 mSC = new Vector2(); //
   private static final Vector2 mAreac = new Vector2(); // centroid of the area
   private static final Vector2 mMassc = new Vector2(); // centroid of the mass

   //
   // Default values
   //
   public static final Vector2 DEFAULT_SURFACE_NORMAL = new Vector2(0, 1); // point up
   public static final Vector2 DEFAULT_FLUID_VELOCITY = new Vector2(0, 0); // zero velocity / no current
   public static final Vector2 DEFAULT_FLUID_GRAVITY = new Vector2(0, -9.8f); // standard gravity
   public static final float DEFAULT_SURFACE_HEIGHT = 0; // Box2d height of the surface
   public static final float DEFAULT_FLUID_DENSITY = 2f;
   public static final float DEFAULT_LINEAR_DRAG = 5f;
   public static final float DEFAULT_ANGULAR_DRAG = 2f;
   
   private static final boolean DEBUG_BUOYANCY = false;

   public B2BuoyancyController()
   {
      this(DEFAULT_SURFACE_NORMAL, DEFAULT_FLUID_VELOCITY, DEFAULT_FLUID_GRAVITY, 
            DEFAULT_SURFACE_HEIGHT, DEFAULT_FLUID_DENSITY, DEFAULT_LINEAR_DRAG, DEFAULT_ANGULAR_DRAG);
   }

   public B2BuoyancyController(Vector2 surfaceNormal, Vector2 fluidVelocity, Vector2 gravity, 
                               float surfaceHeight, float fluidDensity, float linearDrag, float angularDrag)
   {
      mSurfaceNormal.set(surfaceNormal);
      mFluidVelocity.set(fluidVelocity);
      mGravity.set(gravity);
      mSurfaceHeight = surfaceHeight;
      mFluidDensity = fluidDensity;
      mLinearDrag = linearDrag;
      mAngularDrag = angularDrag;
      mControllerType = B2Controller.BUOYANCY_CONTROLLER;
   }

   /**
    * @param f - fixture that is affected
    * @return true if force was applied, false otherwise.
    */
   private boolean ApplyToFixture(Fixture f)
   {
      float shapeDensity = mUseDensity ? f.getDensity() : mFluidDensity;
      
      // don't bother with buoyancy on sensors or fixtures with no density
      if (f.isSensor() || (shapeDensity == 0))
      {
         return false;
      }
      Body body = f.getBody();
      mAreac.set(Vector2.Zero);
      mMassc.set(Vector2.Zero);
      float area = 0;

      // Get shape for displacement area calculations
      Shape shape = f.getShape();

      mSC.set(Vector2.Zero);
      float sarea;
      switch (shape.getType())
      {
         case Circle:
            sarea = B2ShapeExtensions.ComputeSubmergedArea((CircleShape) shape,mSurfaceNormal, mSurfaceHeight, body.getTransform(), mSC);
            break;

         case Chain:
            sarea = B2ShapeExtensions.ComputeSubmergedArea((ChainShape) shape,mSurfaceNormal, mSurfaceHeight, body.getTransform(), mSC);
            break;

         case Edge:
            sarea = B2ShapeExtensions.ComputeSubmergedArea((EdgeShape) shape,mSurfaceNormal, mSurfaceHeight, body.getTransform(), mSC);
            break;

         case Polygon:
            sarea = B2ShapeExtensions.ComputeSubmergedArea((PolygonShape) shape, mSurfaceNormal, mSurfaceHeight, body.getTransform(),mSC);
            break;

         default:
            sarea = 0;
            break;
      }

      area += sarea;
      mAreac.x += sarea * mSC.x;
      mAreac.y += sarea * mSC.y;
      float mass = sarea * shapeDensity;
      mMassc.x += sarea * mSC.x * shapeDensity;
      mMassc.y += sarea * mSC.y * shapeDensity;

      mAreac.x /= area;
      mAreac.y /= area;
      mMassc.x /= mass;
      mMassc.y /= mass;
      if (area < Float.MIN_VALUE)
      {
         return false;
      }

      if (DEBUG_BUOYANCY)
      {
         // Run debug w/HCR to see the effects of different fluid densities / linear drag
         mFluidDensity = 2f;
         mLinearDrag = 5;
         mAngularDrag = 2;
      }
      
      // buoyancy force.
      mTmp.set(mGravity).scl(-mFluidDensity * area);
      body.applyForce(mTmp, mMassc, true); // multiply by -density to invert gravity
      
      // linear drag.
      mTmp.set(body.getLinearVelocityFromWorldPoint(mAreac).sub(mFluidVelocity).scl(-mLinearDrag * area));
      body.applyForce(mTmp, mAreac, true);
      
      // angular drag.
      float bodyMass = body.getMass();
      if (bodyMass < 1) // prevent a huge torque from being generated...
      {
         bodyMass = 1;
      }
      float torque = -body.getInertia() / bodyMass * area * body.getAngularVelocity() * mAngularDrag;
      body.applyTorque(torque, true);
      return true;
   }

   @Override
   public void step(float timeStep)
   {
      if (m_bodyList != null)
      {
         for (int i = 0; i < m_bodyList.size; i++)
         {
            ArrayList<Fixture> fixtureList = m_bodyList.get(i).getFixtureList();
            for (int j = 0; j < fixtureList.size(); j++)
            {
               ApplyToFixture(fixtureList.get(j));
            }
         }
      }
   }
}

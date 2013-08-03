package org.flixel.plugin.flxbox2d.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class B2GravityController extends B2Controller
{
   private final Vector2 mControllerGravity = new Vector2();
   
   private static final Vector2 mTmp = new Vector2(); // shared field
   
   public static final Vector2 DEFAULT_GRAVITY = new Vector2(0,9.8f); // 'normal' gravity is negative, this is anti-gravity
   
   public B2GravityController()
   {
      this(DEFAULT_GRAVITY);
   }
   
   public B2GravityController(Vector2 gravity)
   {
      mControllerGravity.set(gravity);
      mControllerType = B2Controller.GRAVITY_CONTROLLER;
   }
   
   /**
    * This sets the controller gravity.  Invert the signs if you want to make it different from the world gravity
    * 
    * @param gravityX
    * @param gravityY
    */
   public void setGravity(float gravityX, float gravityY)
   {
      mControllerGravity.x = gravityX;
      mControllerGravity.y = gravityY;
   }
   
   public void step(float timeStep)
   {
      if (m_bodyList != null)
      {
         for (int i = 0; i < m_bodyList.size; i++)
         {
            Body body = m_bodyList.get(i);
            mTmp.set(mControllerGravity).scl(body.getMass());
            body.applyForce(mTmp, body.getPosition(), true);
         }
      }
   }
}

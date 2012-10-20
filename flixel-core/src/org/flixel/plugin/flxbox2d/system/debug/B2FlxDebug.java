package org.flixel.plugin.flxbox2d.system.debug;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.dynamics.joints.B2FlxJoint;
import org.flixel.plugin.flxbox2d.dynamics.joints.B2FlxMouseJoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;

/**
 * A plugin for rendering debug shapes. It renders joints and contact points.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxDebug extends FlxBasic
{
	/**
	 * The color for shapes that are non active.
	 */
	public static int SHAPE_NOT_ACTIVE = 0xFF80804D;
	/**
	 * The color for static shapes.
	 */
	public static int SHAPE_STATIC = 0xFF80F080;
	/**
	 * The color for kinematic shapes.
	 */
	public static int SHAPE_KINEMATIC = 0xFF8080E6;
	/**
	 * The color for sleeping shapes.
	 */
	public static int SHAPE_NOT_AWAKE = 0xFF999999;
	/**
	 * The color for awaked shapes.
	 */
	public static int SHAPE_AWAKE = 0xFFE6B3B3;
	/**
	 * The color for joints.
	 */
	public static int JOINT_COLOR = 0xFF80CCCC;
	/**
	 * The color for AABB (bounding box).
	 */
	public static int AABB_COLOR = 0xFFFF00FF;
	/**
	 * Whether to draw bodies or not. Default is true.
	 */
	public static boolean drawBodies;
	/**
	 * Whether to draw joints or not. Default is true.
	 */
	public static boolean drawJoints;
	/**
	 * Whether to draw AABB (bounding box) or not. Default is false.
	 */
	public static boolean drawAABBs;
	/**
	 * Whether to draw inactive bodies or not. Default is false.
	 */
	public static boolean drawInactiveBodies;
	/**
	 * Whether to draw contact points or not. Default is true;
	 */
	public static boolean drawCollisions;
	
	/**
	 * An array of joints.
	 */
	static Array<B2FlxJoint> joints;
	/**
	 * An array of mouses. // TODO: not sure if an array is needed.
	 */
	static Array<B2FlxMouseJoint> mouses;
	
	public B2FlxDebug()
	{
		joints = new Array<B2FlxJoint>();
		mouses = new Array<B2FlxMouseJoint>();
		drawBodies = true;
		drawJoints = true;
		drawCollisions = true;
	}
	
	/**
	 * Draws joints and/or contact points.
	 */
	@Override
	public void draw()
	{
		if(!FlxG.visualDebug)
			return;
		if(drawJoints)
		{
			for(int i = 0; i < joints.size; i++)
			{
				joints.get(i).drawDebug();
			}			
			for(int i = 0; i < mouses.size; i++)
			{
				mouses.get(i).drawDebug();
			}
		}
		
		if(drawCollisions)
		{
			ShapeRenderer renderer = FlxG.flashGfx.getShapeRenderer();
			renderer.end();
			renderer.setProjectionMatrix(FlxG.batch.getProjectionMatrix().scale(32f, 32f, 0));
			
			if(Gdx.gl10 != null)
				Gdx.gl10.glPointSize(3);
			renderer.begin(ShapeType.Point);
			int length = B2FlxB.world.getContactList().size();
			for(int i = 0; i < length; i++)
				drawContact(renderer, B2FlxB.world.getContactList().get(i));
			renderer.end();
			if(Gdx.gl10 != null)
				Gdx.gl10.glPointSize(1);			
		}
	}
	
	/**
	 * Draw contact points.
	 * @param renderer	The shape renderer.
	 * @param contact	The contact.
	 */
	private void drawContact(ShapeRenderer renderer, Contact contact) 
	{
		WorldManifold worldManifold = contact.getWorldManifold();
		if(worldManifold.getNumberOfContactPoints() == 0) 
			return;
		Vector2 point = worldManifold.getPoints()[0];
		point.x -= FlxG.camera.scroll.x / B2FlxB.RATIO;
		point.y -= FlxG.camera.scroll.y / B2FlxB.RATIO;
		renderer.point(point.x, point.y, 0);
	}
	
	/**
	 * Adds a joint to the renderer.
	 * @param joint		The joint that needs to be drawn in the debug.
	 */
	public static void addJoint(B2FlxJoint joint)
	{
		joints.add(joint);
	}
	
	/**
	 * Adds a mouse joint to the renderer.
	 * @param mouse		The mouse joint that needs to be drawn in the debug.
	 */
	public static void addMouseJoint(B2FlxMouseJoint mouse)
	{
		mouses.add(mouse);
	}
	
	/**
	 * Cleans up the memory.
	 */
	@Override
	public void destroy()
	{
		joints.clear();
		joints = null;
		mouses.clear();
		mouses = null;
	}
}


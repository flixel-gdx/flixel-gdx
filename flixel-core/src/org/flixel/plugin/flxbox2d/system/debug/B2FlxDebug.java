package org.flixel.plugin.flxbox2d.system.debug;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.FlxU;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.WorldManifold;

/**
 * A plugin for rendering joints and contact points.
 * To activate, set <code>FlxG.debug</code> to true in the subclass of FlxGame.
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
	 * The color for contacts.
	 */
	public static int CONTACT_COLOR = 0xFF80CCCC;
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
	 * Constructor
	 */
	public B2FlxDebug()
	{		
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
		if(!FlxG.visualDebug || B2FlxB.world == null)
			return;
		if(drawJoints)
		{
			for(int i = 0; i < B2FlxB.flxJoints.size; i++)
			{
				B2FlxB.flxJoints.get(i).drawDebug();
			}
		}
		if(drawCollisions)
		{
			ShapeRenderer renderer = FlxG.flashGfx.getShapeRenderer();
			renderer.end();
			renderer.setProjectionMatrix(FlxG.batch.getProjectionMatrix().scale(32f, 32f, 0));
									
			renderer.begin(ShapeType.Filled);
			float[] rgba = FlxU.getRGBA(CONTACT_COLOR);
			renderer.setColor(rgba[0] * 0.00392f, rgba[1] * 0.00392f, rgba[2] * 0.00392f, 1);
			int length = B2FlxB.world.getContactList().size();
			for(int i = 0; i < length; i++)
				drawContact(renderer, B2FlxB.world.getContactList().get(i));
			renderer.end();
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
		renderer.circle(point.x, point.y, .05f, 360);
	}
}


package org.flixel.plugin;

import org.flixel.FlxBasic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Orientation;

/**
 * Keep track of the accelerometer and orientation of the device.
 * 
 * @author Ka Wing Chin
 */
public class SensorManager extends FlxBasic
{
	/**
	 * The value of the accelerometer on its x-axis. ranges between [-10,10].
	 */
	public static float x;
	/**
	 * The value of the accelerometer on its x-axis. ranges between [-10,10].
	 */
	public static float y;
	/**
	 * The value of the accelerometer on its y-axis. ranges between [-10,10].
	 */
	public static float z;
	/**
	 * The azimuth is the angle of the device's orientation around the z-axis.
	 * The positive z-axis points towards the earths center.
	 */
	public static float azimuth;
	/**
	 * The pitch is the angle of the device's orientation around the x-axis. The
	 * positive x-axis roughly points to the west and is orthogonal to the z- and y-axis.
	 */
	public static float pitch;
	/**
	 * The roll is the angle of the device's orientation around the y-axis. The
	 * positive y-axis points to the magnetic north pole of the earth.
	 */
	public static float roll;
	/**
	 * The rotation of the device with respect to its native orientation.
	 */
	public static int rotation;
	/**
	 * The native orientation of the device.
	 */
	public static Orientation orientation;

	/**
	 * Updates the values of the accelerometer and orientation of the device.
	 */
	@Override
	public void update()
	{
		x = Gdx.input.getAccelerometerX();
		y = Gdx.input.getAccelerometerY();
		z = Gdx.input.getAccelerometerZ();
		azimuth = Gdx.input.getAzimuth();
		pitch = Gdx.input.getPitch();
		roll = Gdx.input.getRoll();
		orientation = Gdx.input.getNativeOrientation();
		rotation = Gdx.input.getRotation();
	}
}

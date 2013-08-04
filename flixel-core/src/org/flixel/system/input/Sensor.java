package org.flixel.system.input;

import com.badlogic.gdx.Gdx;

/**
 * Keep track of the accelerometer and orientation of the device.
 * 
 * @author Ka Wing Chin
 */
public class Sensor
{
	/**
	 * The value of the accelerometer on its x-axis. ranges between [-10,10].
	 */
	public float x;
	/** 
	 * The value of the accelerometer on its x-axis. ranges between [-10,10].
	 */
	public float y;
	/**
	 * The value of the accelerometer on its y-axis. ranges between [-10,10].
	 */
	public float z;
	/**
	 * The azimuth is the angle of the device's orientation around the z-axis. The positive z-axis points towards the earths center.
	 */
	public float azimuth;
	/**
	 * The pitch is the angle of the device's orientation around the x-axis. The positive x-axis roughly points to the west and is orthogonal to the z- and y-axis.
	 */
	public float pitch;
	/**
	 * The roll is the angle of the device's orientation around the y-axis. The positive y-axis points to the magnetic north pole of the earth.
	 */
	public float roll;
	/**
	 * The rotation of the device with respect to its native orientation.
	 */
	public int rotation;
		
	/**
	 * Updates the values of the accelerometer and orientation of the device.
	 */
	public void update()
	{
		x = Gdx.input.getAccelerometerX();
		y = Gdx.input.getAccelerometerY();
		z = Gdx.input.getAccelerometerZ();
		azimuth = Gdx.input.getAzimuth();
		pitch = Gdx.input.getPitch();
		roll = Gdx.input.getRoll();
		rotation = Gdx.input.getRotation();
	}
}

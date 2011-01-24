package org.flixel;

import java.util.ArrayList;


public class FlxU
{
	/**
	 * Helps to eliminate false collisions and/or rendering glitches caused by
	 * rounding errors
	 */
	public static double roundingError = 0.0000001;

	/**
	 * The last quad tree you generated will be stored here for reference or
	 * whatever.
	 */
	static public FlxQuadTree quadTree;
	
	
   

	
	/**
	 * Opens a web page in a new tab or window.
	 * 
	 * @param URL
	 * The address of the web page.
	 */
	/*
	 * static public void openURL(URL:String) { navigateToURL(new
	 * URLRequest(URL), "_blank"); }
	 */

	static public float abs(float N)
	{
		return (N > 0) ? N : -N;
	}

	public static float floor(float N)
	{
		float n = N;
		return (N > 0) ? (n) : ((n != N) ? (n - 1) : (n));
	}

	static public float ceil(float N)
	{
		float n = N;
		return (N > 0) ? ((n != N) ? (n + 1) : (n)) : (n);
	}

	static public float min(float N1, float N2)
	{
		return (N1 <= N2) ? N1 : N2;
	}

	static public float max(float N1, float N2)
	{
		return (N1 >= N2) ? N1 : N2;
	}

	/**
	 * Generates a random number. NOTE: To create a series of predictable random
	 * numbers, add the random number you generate each time to the
	 * <code>Seed</code> value before calling <code>random()</code> again.
	 * 
	 * @param Seed
	 *            A user-provided value used to calculate a predictable random
	 *            number.
	 * 
	 * @return A <code>Number</code> between 0 and 1.
	 */
	static public float random(float Seed)
	{
		//if(Float.isNaN(Seed))
			//return (float) Math.random();
		//else
		{
			// Make sure the seed value is OK
			if(Seed == 0)
				Seed = Integer.MIN_VALUE;
			if(Seed >= 1)
			{
				if((Seed % 1) == 0)
					Seed /= Math.PI;
				Seed %= 1;
			}
			else
				if(Seed < 0)
					Seed = (Seed % 1) + 1;

			// Then do an LCG thing and return a predictable random number
			return ((69621 * (Seed * 0x7FFFFFFF)) % 0x7FFFFFFF) / 0x7FFFFFFF;
		}
	}
	
	static public float random()
	{
		return random((float) Math.random());
	}

	/**
	 * Useful for finding out how long it takes to execute specific blocks of
	 * code.
	 * 
	 * @return A <code>uint</code> to be passed to
	 *         <code>FlxU.endProfile()</code>.
	 */
	static public long startProfile()
	{
		return System.currentTimeMillis();
	}

	
	public static FlxPoint rotatePoint(float X, int Y, int PivotX, int PivotY, float Angle, FlxPoint P)
	{
		double sin = 0;
		double cos = 0;
		double radians = Angle * -0.017453293;
		while(radians < -3.14159265)
			radians += 6.28318531;
		while(radians > 3.14159265)
			radians = radians - 6.28318531;

		if(radians < 0)
		{
			sin = 1.27323954 * radians + .405284735 * radians * radians;
			if(sin < 0)
				sin = .225 * (sin * -sin - sin) + sin;
			else
				sin = .225 * (sin * sin - sin) + sin;
		}
		else
		{
			sin = 1.27323954 * radians - 0.405284735 * radians * radians;
			if(sin < 0)
				sin = .225 * (sin * -sin - sin) + sin;
			else
				sin = .225 * (sin * sin - sin) + sin;
		}

		radians += 1.57079632;
		if(radians > 3.14159265)
			radians = radians - 6.28318531;
		if(radians < 0)
		{
			cos = 1.27323954 * radians + 0.405284735 * radians * radians;
			if(cos < 0)
				cos = .225 * (cos * -cos - cos) + cos;
			else
				cos = .225 * (cos * cos - cos) + cos;
		}
		else
		{
			cos = 1.27323954 * radians - 0.405284735 * radians * radians;
			if(cos < 0)
				cos = .225 * (cos * -cos - cos) + cos;
			else
				cos = .225 * (cos * cos - cos) + cos;
		}

		float dx = X - PivotX;
		float dy = PivotY - Y;
		if(P == null)
			P = new FlxPoint();
		P.x = (float) (PivotX + cos * dx - sin * dy);
		P.y = (float) (PivotY - sin * dx - cos * dy);
		return P;
	}

	public static FlxPoint rotatePoint(float X, int Y, int PivotX, int PivotY, float Angle)
	{
		double sin = 0;
		double cos = 0;
		double radians = Angle * -0.017453293;
		while(radians < -3.14159265)
			radians += 6.28318531;
		while(radians > 3.14159265)
			radians = radians - 6.28318531;

		if(radians < 0)
		{
			sin = 1.27323954 * radians + .405284735 * radians * radians;
			if(sin < 0)
				sin = .225 * (sin * -sin - sin) + sin;
			else
				sin = .225 * (sin * sin - sin) + sin;
		}
		else
		{
			sin = 1.27323954 * radians - 0.405284735 * radians * radians;
			if(sin < 0)
				sin = .225 * (sin * -sin - sin) + sin;
			else
				sin = .225 * (sin * sin - sin) + sin;
		}

		radians += 1.57079632;
		if(radians > 3.14159265)
			radians = radians - 6.28318531;
		if(radians < 0)
		{
			cos = 1.27323954 * radians + 0.405284735 * radians * radians;
			if(cos < 0)
				cos = .225 * (cos * -cos - cos) + cos;
			else
				cos = .225 * (cos * cos - cos) + cos;
		}
		else
		{
			cos = 1.27323954 * radians - 0.405284735 * radians * radians;
			if(cos < 0)
				cos = .225 * (cos * -cos - cos) + cos;
			else
				cos = .225 * (cos * cos - cos) + cos;
		}

		float dx = X - PivotX;
		float dy = PivotY - Y;

		FlxPoint P = new FlxPoint();
		P.x = (float) (PivotX + cos * dx - sin * dy);
		P.y = (float) (PivotY - sin * dx - cos * dy);
		return P;
	}

	/**
	 * Calculates the angle between a point and the origin (0,0).
	 * 
	 * @param X
	 *            The X coordinate of the point.
	 * @param Y
	 *            The Y coordinate of the point.
	 * 
	 * @return The angle in degrees.
	 */
	static public float getAngle(float X, float Y)
	{

		double c1 = 3.14159265 / 4;
		double c2 = 3 * c1;
		double ay = (Y < 0) ? -Y : Y;
		double angle = 0;
		if(X >= 0)
			angle = c1 - c1 * ((X - ay) / (X + ay));
		else
			angle = c2 - c1 * ((X + ay) / (ay - X));
		return (float) (((Y < 0) ? -angle : angle) * 57.2957796);
	}

	/**
	 * Generate a Flash <code>uint</code> color from RGBA components.
	 * 
	 * @param Red
	 *            The red component, between 0 and 255.
	 * @param Green
	 *            The green component, between 0 and 255.
	 * @param Blue
	 *            The blue component, between 0 and 255.
	 * @param Alpha
	 *            How opaque the color should be, either between 0 and 1 or 0
	 *            and 255.
	 * 
	 * @return The color as a <code>uint</code>.
	 */
	static public int getColor(int Red, int Green, int Blue, int Alpha)
	{
		return (((Alpha > 1) ? Alpha : (Alpha * 255)) & 0xFF) << 24 | (Red & 0xFF) << 16 | (Green & 0xFF) << 8 | (Blue & 0xFF);
	}

	/**
	 * Generate a Flash <code>uint</code> color from HSB components.
	 * 
	 * @param Hue
	 *            A number between 0 and 360, indicating position on a color
	 *            strip or wheel.
	 * @param Saturation
	 *            A number between 0 and 1, indicating how colorful or gray the
	 *            color should be. 0 is gray, 1 is vibrant.
	 * @param Brightness
	 *            A number between 0 and 1, indicating how bright the color
	 *            should be. 0 is black, 1 is full bright.
	 * @param Alpha
	 *            How opaque the color should be, either between 0 and 1 or 0
	 *            and 255.
	 * 
	 * @return The color as a <code>uint</code>.
	 */
	static public int getColorHSB(int Hue, int Saturation, int Brightness, int Alpha)
	{
		int red;
		int green;
		int blue;
		if(Saturation == 0.0)
		{
			red = Brightness;
			green = Brightness;
			blue = Brightness;
		}
		else
		{
			if(Hue == 360)
				Hue = 0;
			int slice = Hue / 60;
			int hf = Hue / 60 - slice;
			int aa = Brightness * (1 - Saturation);
			int bb = Brightness * (1 - Saturation * hf);
			int cc = (int) (Brightness * (1 - Saturation * (1.0 - hf)));
			switch(slice)
			{
			case 0:
				red = Brightness;
				green = cc;
				blue = aa;
				break;
			case 1:
				red = bb;
				green = Brightness;
				blue = aa;
				break;
			case 2:
				red = aa;
				green = Brightness;
				blue = cc;
				break;
			case 3:
				red = aa;
				green = bb;
				blue = Brightness;
				break;
			case 4:
				red = cc;
				green = aa;
				blue = Brightness;
				break;
			case 5:
				red = Brightness;
				green = aa;
				blue = bb;
				break;
			default:
				red = 0;
				green = 0;
				blue = 0;
				break;
			}
		}

		return (((Alpha > 1) ? Alpha : (Alpha * 255)) & 0xFF) << 24 | red * 255 << 16 | green * 255 << 8 | blue * 255;
	}

	/**
	 * Loads an array with the RGBA values of a Flash <code>uint</code> color.
	 * RGB values are stored 0-255. Alpha is stored as a floating point number
	 * between 0 and 1.
	 * 
	 * @param Color
	 *            The color you want to break into components.
	 * @param Results
	 *            An optional parameter, allows you to use an array that already
	 *            exists in memory to store the result.
	 * 
	 * @return An <code>Array</code> object containing the Red, Green, Blue and
	 *         Alpha values of the given color.
	 */
	static public int[] getRGBA(int Color, int[] Results)
	{
		//if(Results == null)
			//Results = new ArrayList<Integer>();
		
		Results[0] = (Color >> 16) & 0xFF;		
		Results[1] = (Color >> 8) & 0xFF;
		Results[2] = Color & 0xFF;
		Results[3] = ((Color >> 24) & 0xFF) / 255;
		return Results;
	}

	/**
	 * Loads an array with the HSB values of a Flash <code>uint</code> color.
	 * Hue is a value between 0 and 360. Saturation, Brightness and Alpha are as
	 * floating point numbers between 0 and 1.
	 * 
	 * @param Color
	 *            The color you want to break into components.
	 * @param Results
	 *            An optional parameter, allows you to use an array that already
	 *            exists in memory to store the result.
	 * 
	 * @return An <code>Array</code> object containing the Red, Green, Blue and
	 *         Alpha values of the given color.
	 */
	static public int[] getHSB(int Color, int[] Results)
	{
//		if(Results == null)
//			Results = new ArrayList<Integer>();

		int red = ((Color >> 16) & 0xFF) / 255;
		int green = ((Color >> 8) & 0xFF) / 255;
		int blue = ((Color) & 0xFF) / 255;

		int m = (red > green) ? red : green;
		int dmax = (m > blue) ? m : blue;
		m = (red > green) ? green : red;
		int dmin = (m > blue) ? blue : m;
		int range = dmax - dmin;

		Results[0] = 0;
		Results[1] = 0;
		Results[2] = dmax;

		if(dmax != 0)
			Results[1] = range / dmax;
		if(Results[1] != 0)
		{
			if(red == dmax)
				Results[0] = (green - blue) / range;
			else
				if(green == dmax)
					Results[0] = 2 + (blue - red) / range;
				else
					if(blue == dmax)
						Results[0] = 4 + (red - green) / range;

			Results[0] *= 60;
			if(Results[0] < 0)
				Results[0] += 360;
		}
		Results[3] = ((Color >> 24) & 0xFF) / 255;
		return Results;
	}

	/**
	 * Get the <code>String</code> name of any <code>Object</code>.
	 * 
	 * @param Obj
	 *            The <code>Object</code> object in question.
	 * @param Simple
	 *            Returns only the class name, not the package or packages.
	 * 
	 * @return The name of the <code>Class</code> as a <code>String</code>
	 *         object.
	 */
	@SuppressWarnings("unchecked")
	static public String getClassName(Class Obj)
	{
		/*
		 * var s:String = getQualifiedClassName(Obj); s = s.replace("::",".");
		 * if(Simple) s = s.substr(s.lastIndexOf(".")+1); return s;
		 */

		String s = Obj.getName();
		if(s.lastIndexOf('.') > 0)
		{
			s = s.substring(s.lastIndexOf('.') + 1);
		}

		return s;
	}

	/**
	 * Look up a <code>Class</code> object by its string name.
	 * 
	 * @param Name
	 *            The <code>String</code> name of the <code>Class</code> you are
	 *            interested in.
	 * 
	 * @return A <code>Class</code> object.
	 */
	@SuppressWarnings("unchecked")
	static public Class getClass(String Name)
	{
		return Name.getClass();
	}

	/**
	 * A tween-like function that takes a starting velocity and some other
	 * factors and returns an altered velocity.
	 * 
	 * @param Velocity
	 *            Any component of velocity (e.g. 20).
	 * @param Acceleration
	 *            Rate at which the velocity is changing. 0 is default.
	 * @param Drag
	 *            Really kind of a deceleration, this is how much the velocity
	 *            changes if Acceleration is not set. 0 is default.
	 * @param Max
	 *            An absolute value cap for the velocity. 10000 is default.
	 * 
	 * @return The altered Velocity value.
	 */
	static public float computeVelocity(float Velocity, float Acceleration, float Drag, float Max)
	{
		if(Acceleration != 0)
			Velocity += Acceleration * FlxG.elapsed;
		else
			if(Drag != 0)
			{
				float d = Drag * FlxG.elapsed;
				if(Velocity - d > 0)
					Velocity = Velocity - d;
				else
					if(Velocity + d < 0)
						Velocity += d;
					else
						Velocity = 0;
			}
		if((Velocity != 0) && (Max != 10000))
		{
			if(Velocity > Max)
				Velocity = Max;
			else
				if(Velocity < -Max)
					Velocity = -Max;
		}
		return Velocity;
	}
	
	

	/**
	 * Call this function to specify a more efficient boundary for your game
	 * world. This boundary is used by <code>overlap()</code> and
	 * <code>collide()</code>, so it can't hurt to have it be the right size!
	 * Flixel will invent a size for you, but it's pretty huge - 256x the size
	 * of the screen, whatever that may be. Leave width and height empty if you
	 * want to just update the game world's position.
	 * 
	 * @param X 		The X-coordinate of the left side of the game world.
	 * @param Y 		The Y-coordinate of the top of the game world.
	 * @param Width 	Desired width of the game world.
	 * @param Height 	Desired height of the game world.
	 * @param Divisions Pass a non-zero value to set <code>quadTreeDivisions</code>. Default value is 3.
	 */
	public static void setWorldBounds(float X, float Y, int Width, int Height, int Divisions)
	{
		if(FlxQuadTree.bounds == null)
			FlxQuadTree.bounds = new FlxRect(0, 0);
		FlxQuadTree.bounds.x = X;
		FlxQuadTree.bounds.y = Y;
		if(Width > 0)
			FlxQuadTree.bounds.width = Width;
		if(Height > 0)
			FlxQuadTree.bounds.height = Height;
		if(Divisions > 0)
			FlxQuadTree.divisions = Divisions;
	}
	
	public static void setWorldBounds(float X, float Y, int Width, int Height)
	{
		setWorldBounds(X, Y, Width, Height, 3);
	}
	
	public static void setWorldBounds(float X, float Y, int Width)
	{
		setWorldBounds(X, Y, Width, 0, 3);
	}
	
	public static void setWorldBounds(float X, float Y)
	{
		setWorldBounds(X, Y, 0, 0, 3);
	}
	
	public static void setWorldBounds(float X)
	{
		setWorldBounds(X, 0, 0, 0, 3);
	}
	
	public static void setWorldBounds()
	{
		setWorldBounds(0, 0, 0, 0, 3);
	}
	

	/**
	 * Call this function to see if one <code>FlxObject</code> overlaps another.
	 * Can be called with one object and one group, or two groups, or two
	 * objects, whatever floats your boat! It will put everything into a quad
	 * tree and then check for overlaps. For maximum performance try bundling a
	 * lot of objects together using a <code>FlxGroup</code> (even bundling
	 * groups together!) NOTE: does NOT take objects' scrollfactor into account.
	 * 
	 * @param Object1
	 *            The first object or group you want to check.
	 * @param Object2
	 *            The second object or group you want to check. If it is the
	 *            same as the first, flixel knows to just do a comparison within
	 *            that group.
	 * @param Callback
	 *            A function with two <code>FlxObject</code> parameters - e.g.
	 * 
	 *            <code>myOverlapFunction(Object1:FlxObject,Object2:FlxObject);</code>
	 *            If no function is provided, <code>FlxQuadTree</code> will call
	 *            <code>kill()</code> on both objects.
	 */
	static public boolean overlap(FlxObject Object1, FlxObject Object2, FlxOverlapListener Callback)
	{
		if((Object1 == null) || !Object1.exists ||
				(Object2 == null) || !Object2.exists)
			return false;

		quadTree = new FlxQuadTree(FlxQuadTree.bounds.x, FlxQuadTree.bounds.y, FlxQuadTree.bounds.width,
				FlxQuadTree.bounds.height);
		quadTree.add(Object1, FlxQuadTree.A_LIST);

		if(Object1 == Object2)
			return quadTree.overlap(false, Callback);
		quadTree.add(Object2, FlxQuadTree.B_LIST);
		return quadTree.overlap(true, Callback);
	}

	static public boolean overlap(FlxObject Object1, FlxObject Object2)
	{
		return overlap(Object1, Object2, null);
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> collides with
	 * another. Can be called with one object and one group, or two groups, or
	 * two objects, whatever floats your boat! It will put everything into a
	 * quad tree and then check for collisions. For maximum performance try
	 * bundling a lot of objects together using a <code>FlxGroup</code> (even
	 * bundling groups together!) NOTE: does NOT take objects' scrollfactor into
	 * account.
	 * 
	 * @param Object1
	 *            The first object or group you want to check.
	 * @param Object2
	 *            The second object or group you want to check. If it is the
	 *            same as the first, flixel knows to just do a comparison within
	 *            that group.
	 */
	static public boolean collide(FlxObject Object1, FlxObject Object2)
	{
		if(		(Object1 == null) || !Object1.exists ||
				(Object2 == null) || !Object2.exists)
				return false;
		quadTree = new FlxQuadTree(FlxQuadTree.bounds.x, FlxQuadTree.bounds.y, FlxQuadTree.bounds.width, FlxQuadTree.bounds.height);
		quadTree.add(Object1, FlxQuadTree.A_LIST);

		boolean match = Object1.equals(Object2);

		if(!match)
			quadTree.add(Object2, FlxQuadTree.B_LIST);

				
		boolean cx = quadTree.overlap(!match, new FlxOverlapListener()
		{			
			@Override
			public boolean overlap(FlxObject Object1, FlxObject Object2)
			{
				return solveXCollision(Object1, Object2);				
			}
		});
		
		boolean cy = quadTree.overlap(!match, new FlxOverlapListener()
		{			
			@Override
			public boolean overlap(FlxObject Object1, FlxObject Object2)
			{
				return solveYCollision(Object1, Object2);
			}
		});
		return cx || cy;
	}
	
	

	/**
	 * This quad tree callback function can be used externally as well. Takes
	 * two objects and separates them along their X axis (if
	 * possible/reasonable).
	 * 
	 * @param Object1
	 *            The first object or group you want to check.
	 * @param Object2
	 *            The second object or group you want to check.
	 */
	static public boolean solveXCollision(FlxObject Object1, FlxObject Object2)
	{
		//Avoid messed up collisions ahead of time
		float o1 = Object1.colVector.x;
		float o2 = Object2.colVector.x;
		if(o1 == o2)
			return false;
		
		//Give the objects a heads up that we're about to resolve some collisions
		Object1.preCollide(Object2);
		Object2.preCollide(Object1);

		//Basic resolution variables
		boolean f1;
		boolean f2;
		float overlap;
		boolean hit = false;
		boolean p1hn2;
		
		//Directional variables
		boolean obj1Stopped = o1 == 0;
		boolean obj1MoveNeg = o1 < 0;
		boolean obj1MovePos = o1 > 0;
		boolean obj2Stopped = o2 == 0;
		boolean obj2MoveNeg = o2 < 0;
		boolean obj2MovePos = o2 > 0;
		
		//Offset loop variables
		int i1;
		int i2;
		FlxRect obj1Hull = Object1.colHullX;
		FlxRect obj2Hull = Object2.colHullX;
		ArrayList<FlxPoint> co1 = Object1.colOffsets;
		ArrayList<FlxPoint> co2 = Object2.colOffsets;		
		int l1 = co1.size();
		int l2 = co2.size();
		float ox1;
		float oy1;
		float ox2;
		float oy2;
		float r1;
		float r2;
		float sv1;
		float sv2;
		
		//Decide based on object's movement patterns if it was a right-side or left-side collision
		p1hn2 = ((obj1Stopped && obj2MoveNeg) || (obj1MovePos && obj2Stopped) || (obj1MovePos && obj2MoveNeg) || //the obvious cases
				(obj1MoveNeg && obj2MoveNeg && (((o1>0)?o1:-o1) < ((o2>0)?o2:-o2))) || //both moving left, obj2 overtakes obj1
				(obj1MovePos && obj2MovePos && (((o1>0)?o1:-o1) > ((o2>0)?o2:-o2))) ); //both moving right, obj1 overtakes obj2
		
		//Check to see if these objects allow these collisions
		if(p1hn2?(!Object1.collideRight || !Object2.collideLeft):(!Object1.collideLeft || !Object2.collideRight))
			return false;
		
		//this looks insane, but we're just looping through collision offsets on each object
		i1 = 0;
		while(i1 < l1)
		{
			ox1 = co1.get(i1).x;
			oy1 = co1.get(i1).y;			
			obj1Hull.x += ox1;
			obj1Hull.y += oy1;
			i2 = 0;
			while(i2 < l2)
			{
				ox2 = co2.get(i2).x;
				oy2 = co2.get(i2).y;				
				obj2Hull.x += ox2;
				obj2Hull.y += oy2;
				
				//See if it's a actually a valid collision
				if( (obj1Hull.x + obj1Hull.width  < obj2Hull.x + roundingError) ||
					(obj1Hull.x + roundingError > obj2Hull.x + obj2Hull.width) ||
					(obj1Hull.y + obj1Hull.height < obj2Hull.y + roundingError) ||
					(obj1Hull.y + roundingError > obj2Hull.y + obj2Hull.height) )
				{
					obj2Hull.x = obj2Hull.x - ox2;
					obj2Hull.y = obj2Hull.y - oy2;
					i2++;
					continue;
				}

				//Calculate the overlap between the objects
				if(p1hn2)
				{
					if(obj1MoveNeg)
						r1 = obj1Hull.x + Object1.colHullY.width;
					else
						r1 = obj1Hull.x + obj1Hull.width;
					if(obj2MoveNeg)
						r2 = obj2Hull.x;
					else
						r2 = obj2Hull.x + obj2Hull.width - Object2.colHullY.width;
				}
				else
				{
					if(obj2MoveNeg)
						r1 = -obj2Hull.x - Object2.colHullY.width;
					else
						r1 = -obj2Hull.x - obj2Hull.width;
					if(obj1MoveNeg)
						r2 = -obj1Hull.x;
					else
						r2 = -obj1Hull.x - obj1Hull.width + Object1.colHullY.width;
				}
				overlap = r1 - r2;
				
				//Slightly smarter version of checking if objects are 'fixed' in space or not
				f1 = Object1.getFixed();
				f2 = Object2.getFixed();
				if(f1 && f2)
				{
					f1 = f1 == ((Object1.colVector.x == 0) && (o1 == 0));
					f2 = f2 == ((Object2.colVector.x == 0) && (o2 == 0));
					
					
					
					//f1 &&= (Object1.colVector.x == 0) && (o1 == 0);
					//f2 &&= (Object2.colVector.x == 0) && (o2 == 0);
				}

				//Last chance to skip out on a bogus collision resolution
				if( (overlap == 0) ||
					((!f1 && ((overlap>0)?overlap:-overlap) > obj1Hull.width*0.8)) ||
					((!f2 && ((overlap>0)?overlap:-overlap) > obj2Hull.width*0.8)) )
				{
					obj2Hull.x = obj2Hull.x - ox2;
					obj2Hull.y = obj2Hull.y - oy2;
					i2++;
					continue;
				}
				hit = true;
				
				//Adjust the objects according to their flags and stuff
				sv1 = Object2.velocity.x;
				sv2 = Object1.velocity.x;
				if(!f1 && f2)
				{
					if(Object1._group)
						Object1.reset(Object1.x - overlap,Object1.y);
					else
						Object1.x = Object1.x - overlap;
				}
				else if(f1 && !f2)
				{
					if(Object2._group)
						Object2.reset(Object2.x + overlap,Object2.y);
					else
						Object2.x += overlap;
				}
				else if(!f1 && !f2)
				{
					overlap /= 2;
					if(Object1._group)
						Object1.reset(Object1.x - overlap,Object1.y);
					else
						Object1.x = Object1.x - overlap;
					if(Object2._group)
						Object2.reset(Object2.x + overlap,Object2.y);
					else
						Object2.x += overlap;
					sv1 *= 0.5;
					sv2 *= 0.5;
				}
				if(p1hn2)
				{
					Object1.hitRight(Object2,sv1);
					Object2.hitLeft(Object1,sv2);
				}
				else
				{
					Object1.hitLeft(Object2,sv1);
					Object2.hitRight(Object1,sv2);
				}
				
				//Adjust collision hulls if necessary
				if(!f1 && (overlap != 0))
				{
					if(p1hn2)
						obj1Hull.width = (int) (obj1Hull.width - overlap);
					else
					{
						obj1Hull.x = obj1Hull.x - overlap;
						obj1Hull.width += overlap;
					}
					Object1.colHullY.x = Object1.colHullY.x - overlap;
				}
				if(!f2 && (overlap != 0))
				{
					if(p1hn2)
					{
						obj2Hull.x += overlap;
						obj2Hull.width = (int) (obj2Hull.width - overlap);
					}
					else
						obj2Hull.width += overlap;
					Object2.colHullY.x += overlap;
				}
				obj2Hull.x = obj2Hull.x - ox2;
				obj2Hull.y = obj2Hull.y - oy2;
				i2++;
			}
			obj1Hull.x = obj1Hull.x - ox1;
			obj1Hull.y = obj1Hull.y - oy1;
			i1++;
		}

		return hit;
	}

	/**
	 * This quad tree callback function can be used externally as well. Takes
	 * two objects and separates them along their Y axis (if
	 * possible/reasonable).
	 * 
	 * @param Object1
	 *            The first object or group you want to check.
	 * @param Object2
	 *            The second object or group you want to check.
	 */
	static public boolean solveYCollision(FlxObject Object1, FlxObject Object2)
	{
		//Avoid messed up collisions ahead of time
		float o1 = Object1.colVector.y;
		float o2 = Object2.colVector.y;
		if(o1 == o2)
			return false;
		
		//Give the objects a heads up that we're about to resolve some collisions
		Object1.preCollide(Object2);
		Object2.preCollide(Object1);
		
		//Basic resolution variables
		boolean f1;
		boolean f2;
		float overlap;
		boolean hit = false;
		boolean p1hn2;
		
		//Directional variables
		boolean obj1Stopped = o1 == 0;
		boolean obj1MoveNeg = o1 < 0;
		boolean obj1MovePos = o1 > 0;
		boolean obj2Stopped = o2 == 0;
		boolean obj2MoveNeg = o2 < 0;
		boolean obj2MovePos = o2 > 0;
		
		//Offset loop variables
		int i1;
		int i2;
		FlxRect obj1Hull = Object1.colHullY;
		FlxRect obj2Hull = Object2.colHullY;
		ArrayList<FlxPoint> co1 = Object1.colOffsets;
		ArrayList<FlxPoint> co2 = Object2.colOffsets;
		int l1 = co1.size();
		int l2 = co2.size();
		float ox1;
		float oy1;
		float ox2;
		float oy2 = 0;
		float r1;
		float r2;
		float sv1;
		float sv2;
		
		//Decide based on object's movement patterns if it was a top or bottom collision
		p1hn2 = ((obj1Stopped && obj2MoveNeg) || (obj1MovePos && obj2Stopped) || (obj1MovePos && obj2MoveNeg) || //the obvious cases
			(obj1MoveNeg && obj2MoveNeg && (((o1>0)?o1:-o1) < ((o2>0)?o2:-o2))) || //both moving up, obj2 overtakes obj1
			(obj1MovePos && obj2MovePos && (((o1>0)?o1:-o1) > ((o2>0)?o2:-o2))) ); //both moving down, obj1 overtakes obj2
		
		//Check to see if these objects allow these collisions
		if(p1hn2?(!Object1.collideBottom || !Object2.collideTop):(!Object1.collideTop || !Object2.collideBottom))
			return false;
		
		//this looks insane, but we're just looping through collision offsets on each object
		i1 = 0;
		while(i1 < l1)
		{
			ox1 = co1.get(i1).x;
			oy1 = co1.get(i1).y;
			obj1Hull.x += ox1;
			obj1Hull.y += oy1;
			i2 = 0;
			while(i2 < l2)
			{				
				ox2 = co2.get(i2).x;
				ox2 = co2.get(i2).y;
				obj2Hull.x += ox2;
				obj2Hull.y += oy2;
				
				//See if it's a actually a valid collision
				if( (obj1Hull.x + obj1Hull.width  < obj2Hull.x + roundingError) ||
					(obj1Hull.x + roundingError > obj2Hull.x + obj2Hull.width) ||
					(obj1Hull.y + obj1Hull.height < obj2Hull.y + roundingError) ||
					(obj1Hull.y + roundingError > obj2Hull.y + obj2Hull.height) )
				{
					obj2Hull.x = obj2Hull.x - ox2;
					obj2Hull.y = obj2Hull.y - oy2;
					i2++;
					continue;
				}
				
				//Calculate the overlap between the objects
				if(p1hn2)
				{
					if(obj1MoveNeg)
						r1 = obj1Hull.y + Object1.colHullX.height;
					else
						r1 = obj1Hull.y + obj1Hull.height;
					if(obj2MoveNeg)
						r2 = obj2Hull.y;
					else
						r2 = obj2Hull.y + obj2Hull.height - Object2.colHullX.height;
				}
				else
				{
					if(obj2MoveNeg)
						r1 = -obj2Hull.y - Object2.colHullX.height;
					else
						r1 = -obj2Hull.y - obj2Hull.height;
					if(obj1MoveNeg)
						r2 = -obj1Hull.y;
					else
						r2 = -obj1Hull.y - obj1Hull.height + Object1.colHullX.height;
				}
				overlap = r1 - r2;
				
				//Slightly smarter version of checking if objects are 'fixed' in space or not
				f1 = Object1.getFixed();
				f2 = Object2.getFixed();
				if(f1 && f2)
				{
					f1 = f1 == ((Object1.colVector.x == 0) && (o1 == 0));
					f2 = f2 == ((Object2.colVector.x == 0) && (o2 == 0));					
					//f1 &&= (Object1.colVector.x == 0) && (o1 == 0);
					//f2 &&= (Object2.colVector.x == 0) && (o2 == 0);
				}
				
				//Last chance to skip out on a bogus collision resolution
				if( (overlap == 0) ||
					((!f1 && ((overlap>0)?overlap:-overlap) > obj1Hull.height*0.8)) ||
					((!f2 && ((overlap>0)?overlap:-overlap) > obj2Hull.height*0.8)) )
				{
					obj2Hull.x = obj2Hull.x - ox2;
					obj2Hull.y = obj2Hull.y - oy2;
					i2++;
					continue;
				}
				hit = true;
				
				//Adjust the objects according to their flags and stuff
				sv1 = Object2.velocity.y;
				sv2 = Object1.velocity.y;
				if(!f1 && f2)
				{
					if(Object1._group)
						Object1.reset(Object1.x, Object1.y - overlap);
					else
						Object1.y = Object1.y - overlap;
				}
				else if(f1 && !f2)
				{
					if(Object2._group)
						Object2.reset(Object2.x, Object2.y + overlap);
					else
						Object2.y += overlap;
				}
				else if(!f1 && !f2)
				{
					overlap /= 2;
					if(Object1._group)
						Object1.reset(Object1.x, Object1.y - overlap);
					else
						Object1.y = Object1.y - overlap;
					if(Object2._group)
						Object2.reset(Object2.x, Object2.y + overlap);
					else
						Object2.y += overlap;
					sv1 *= 0.5;
					sv2 *= 0.5;
				}
				if(p1hn2)
				{
					Object1.hitBottom(Object2,sv1);
					Object2.hitTop(Object1,sv2);
				}
				else
				{
					Object1.hitTop(Object2,sv1);
					Object2.hitBottom(Object1,sv2);
				}
				
				//Adjust collision hulls if necessary
				if(!f1 && (overlap != 0))
				{
					if(p1hn2)
					{
						obj1Hull.y = obj1Hull.y - overlap;
						
						//This code helps stuff ride horizontally moving platforms.
						if(f2 && Object2.moves)
						{
							sv1 = Object2.colVector.x;
							Object1.x += sv1;
							obj1Hull.x += sv1;
							Object1.colHullX.x += sv1;
						}
					}
					else
					{
						obj1Hull.y = obj1Hull.y - overlap;
						obj1Hull.height += overlap;
					}
				}
				if(!f2 && (overlap != 0))
				{
					if(p1hn2)
					{
						obj2Hull.y += overlap;
						obj2Hull.height = (int) (obj2Hull.height - overlap);
					}
					else
					{
						obj2Hull.height += overlap;
					
						//This code helps stuff ride horizontally moving platforms.
						if(f1 && Object1.moves)
						{
							sv2 = Object1.colVector.x;
							Object2.x += sv2;
							obj2Hull.x += sv2;
							Object2.colHullX.x += sv2;
						}
					}
				}
				obj2Hull.x = obj2Hull.x - ox2;
				obj2Hull.y = obj2Hull.y - oy2;
				i2++;
			}
			obj1Hull.x = obj1Hull.x - ox1;
			obj1Hull.y = obj1Hull.y - oy1;
			i1++;
		}
		
		return hit;
	}


	
}

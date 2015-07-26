package org.flixel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class FlxU
{
	/**
	 * Opens a web page in a new tab or window.
	 * MUST be called from the UI thread or else badness.
	 * 
	 * @param	URL		The address of the web page.
	 */
	static public void openURL(String URL)
	{
		Gdx.net.openURI(URL);
	}

	/**
	 * Calculate the absolute value of a number.
	 * 
	 * @param	Value	Any number.
	 * 
	 * @return	The absolute value of that number.
	 */
	static public float abs(float Value)
	{
		return (Value>0)?Value:-Value;
	}

	/**
	 * Round down to the next whole number. E.g. floor(1.7) == 1, and floor(-2.7) == -2.
	 * 
	 * @param	Value	Any number.
	 * 
	 * @return	The rounded value of that number.
	 */
	static public int floor(float Value)
	{
		int number = (int)Value;
		return (Value>0)?(number):((number!=Value)?(number-1):(number));
	}

	/**
	 * Round up to the next whole number. E.g. ceil(1.3) == 2, and ceil(-2.3) == -3.
	 * 
	 * @param	Value	Any number.
	 * 
	 * @return	The rounded value of that number.
	 */
	static public int ceil(float Value)
	{
		int number = (int)Value;
		return (Value>0)?((number!=Value)?(number+1):(number)):(number);
	}

	/**
	 * Round to the closest whole number. E.g. round(1.7) == 2, and round(-2.3) == -2.
	 * 
	 * @param	Value	Any number.
	 * 
	 * @return	The rounded value of that number.
	 */
	static public int round(float Value)
	{
		return (int)(Value+((Value>0)?0.5f:-0.5f));
	}

	/**
	 * Figure out which number is smaller.
	 * 
	 * @param	Number1		Any number.
	 * @param	Number2		Any number.
	 * 
	 * @return	The smaller of the two numbers.
	 */
	static public float min(float Number1,float Number2)
	{
		return (Number1 <= Number2)?Number1:Number2;
	}

	/**
	 * Figure out which number is larger.
	 * 
	 * @param	Number1		Any number.
	 * @param	Number2		Any number.
	 * 
	 * @return	The larger of the two numbers.
	 */
	static public float max(float Number1,float Number2)
	{
		return (Number1 >= Number2)?Number1:Number2;
	}

	/**
	 * Bound a number by a minimum and maximum.
	 * Ensures that this number is no smaller than the minimum,
	 * and no larger than the maximum.
	 * 
	 * @param	Value	Any number.
	 * @param	Min		Any number.
	 * @param	Max		Any number.
	 * 
	 * @return	The bounded value of the number.
	 */
	static public float bound(float Value,float Min,float Max)
	{
		float lowerBound = (Value<Min)?Min:Value;
		return (lowerBound>Max)?Max:lowerBound;
	}

	/**
	 * Generates a random number based on the seed provided.
	 * 
	 * @param	Seed	A number between 0 and 1, used to generate a predictable random number (very optional).
	 * 
	 * @return	A <code>Number</code> between 0 and 1.
	 */
	static public float srand(float Seed)
	{
		return FlxU.abs((float)((69621 * (int)(Seed * 0x7FFFFFFF)) % 0x7FFFFFFF) / 0x7FFFFFFF);
	}

	/**
	 * Shuffles the entries in an array into a new random order.
	 * <code>FlxG.shuffle()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.shuffle()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * 
	 * @param	Objects			A Flash <code>Array</code> object containing...stuff.
	 * @param	HowManyTimes	How many swaps to perform during the shuffle operation.  Good rule of thumb is 2-4 times as many objects are in the list.
	 * 
	 * @return	The same Flash <code>Array</code> object that you passed in in the first place.
	 */
	static public <T> Array<T> shuffle(Array<T> Objects,int HowManyTimes)
	{
		int i = 0;
		int index1;
		int index2;
		T object;
		while(i < HowManyTimes)
		{
			index1 = (int)(Math.random()*Objects.size);
			index2 = (int)(Math.random()*Objects.size);
			object = Objects.get(index2);
			Objects.set(index2, Objects.get(index1));
			Objects.set(index1, object);
			i++;
		}
		return Objects;
	}

	/**
	 * Fetch a random entry from the given array.
	 * Will return null if random selection is missing, or array has no entries.
	 * <code>FlxG.getRandom()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.getRandom()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * 
	 * @param	Objects		A Flash array of objects.
	 * @param	StartIndex	Optional offset off the front of the array. Default value is 0, or the beginning of the array.
	 * @param	Length		Optional restriction on the number of values you want to randomly select from.
	 * 
	 * @return	The random object that was selected.
	 */
	static public <T> T getRandom(Array<T> Objects,int StartIndex,int Length)
	{
		if(Objects != null)
		{
			int l = Length;
			if((l == 0) || (l > Objects.size - StartIndex))
				l = Objects.size - StartIndex;
			if(l > 0)
				return Objects.get(StartIndex + (int)(Math.random()*l));
		}
		return null;
	}

	/**
	 * Fetch a random entry from the given array.
	 * Will return null if random selection is missing, or array has no entries.
	 * <code>FlxG.getRandom()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.getRandom()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * 
	 * @param	Objects		A Flash array of objects.
	 * @param	StartIndex	Optional offset off the front of the array. Default value is 0, or the beginning of the array.
	 * 
	 * @return	The random object that was selected.
	 */
	static public <T> T getRandom(Array<T> Objects,int StartIndex)
	{
		return getRandom(Objects,StartIndex,0);
	}

	/**
	 * Fetch a random entry from the given array.
	 * Will return null if random selection is missing, or array has no entries.
	 * <code>FlxG.getRandom()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.getRandom()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * 
	 * @param	Objects		A Flash array of objects.
	 * 
	 * @return	The random object that was selected.
	 */
	static public <T> T getRandom(Array<T> Objects)
	{
		return getRandom(Objects,0,0);
	}

	/**
	 * Just grabs the current "ticks" or time in milliseconds that has passed since the Flash Player started up.
	 * Useful for finding out how long it takes to execute specific blocks of code.
	 * 
	 * @return	A <code>uint</code> to be passed to <code>FlxU.endProfile()</code>.
	 */
	static public long getTicks()
	{
		return System.currentTimeMillis();
	}

	/**
	 * Takes two "ticks" timestamps and formats them into the number of seconds that passed as a String.
	 * Useful for logging, debugging, the watch window, or whatever else.
	 * 
	 * @param	StartTicks	The first timestamp from the system.
	 * @param	EndTicks	The second timestamp from the system.
	 * 
	 * @return	A <code>String</code> containing the formatted time elapsed information.
	 */
	static public String formatTicks(int StartTicks, int EndTicks)
	{
		return ((EndTicks-StartTicks)/1000)+"s";
	}

	/**
	 * Generate a Flash <code>uint</code> color from RGBA components.
	 * 
	 * @param	Red     The red component, between 0 and 255.
	 * @param	Green   The green component, between 0 and 255.
	 * @param	Blue    The blue component, between 0 and 255.
	 * @param	Alpha   How opaque the color should be, either between 0 and 1 or 0 and 255.
	 * 
	 * @return	The color as a <code>uint</code>.
	 */
	static public int makeColor(int Red, int Green, int Blue, float Alpha)
	{
		return ((int)((Alpha>1)?Alpha:(Alpha * 255)) & 0xFF) << 24 | (Red & 0xFF) << 16 | (Green & 0xFF) << 8 | (Blue & 0xFF);
	}

	/**
	 * Generate a Flash <code>uint</code> color from RGBA components.
	 * 
	 * @param	Red     The red component, between 0 and 255.
	 * @param	Green   The green component, between 0 and 255.
	 * @param	Blue    The blue component, between 0 and 255.
	 * 
	 * @return	The color as a <code>uint</code>.
	 */
	static public int makeColor(int Red, int Green, int Blue)
	{
		return makeColor(Red, Green, Blue, 1.0f);
	}

	/**
	 * Generate a Flash <code>uint</code> color from HSB components.
	 * 
	 * @param	Hue			A number between 0 and 360, indicating position on a color strip or wheel.
	 * @param	Saturation	A number between 0 and 1, indicating how colorful or gray the color should be. 0 is gray, 1 is vibrant.
	 * @param	Brightness	A number between 0 and 1, indicating how bright the color should be. 0 is black, 1 is full bright.
	 * @param	Alpha		How opaque the color should be, either between 0 and 1 or 0 and 255.
	 * 
	 * @return	The color as a <code>uint</code>.
	 */
	static public int makeColorHSB(float Hue,float Saturation,float Brightness,float Alpha)
	{
		float red;
		float green;
		float blue;
		if(Saturation == 0.0f)
		{
			red   = Brightness;
			green = Brightness;
			blue  = Brightness;
		}
		else
		{
			if(Hue == 360)
				Hue = 0;
			int slice = (int) (Hue/60);
			float hf = Hue/60 - slice;
			float aa = Brightness*(1 - Saturation);
			float bb = Brightness*(1 - Saturation*hf);
			float cc = Brightness*(1 - Saturation*(1.0f - hf));
			switch (slice)
			{
				case 0: red = Brightness; green = cc;   blue = aa;  break;
				case 1: red = bb;  green = Brightness;  blue = aa;  break;
				case 2: red = aa;  green = Brightness;  blue = cc;  break;
				case 3: red = aa;  green = bb;   blue = Brightness; break;
				case 4: red = cc;  green = aa;   blue = Brightness; break;
				case 5: red = Brightness; green = aa;   blue = bb;  break;
				default: red = 0;  green = 0;    blue = 0;   break;
			}
		}

		return ((int)((Alpha>1)?Alpha:(Alpha * 255)) & 0xFF) << 24 | (int)(red*255) << 16 | (int)(green*255) << 8 | (int)(blue*255);
	}

	/**
	 * Generate a Flash <code>uint</code> color from HSB components.
	 * 
	 * @param	Hue			A number between 0 and 360, indicating position on a color strip or wheel.
	 * @param	Saturation	A number between 0 and 1, indicating how colorful or gray the color should be. 0 is gray, 1 is vibrant.
	 * @param	Brightness	A number between 0 and 1, indicating how bright the color should be. 0 is black, 1 is full bright.
	 * 
	 * @return	The color as a <code>uint</code>.
	 */
	public int makeColorHSB(int Hue,int Saturation,int Brightness)
	{
		return makeColorHSB(Hue,Saturation,Brightness,1.0f);
	}

	/**
	 * Loads an array with the RGBA values of a Flash <code>uint</code> color.
	 * RGB values are stored 0-255.  Alpha is stored as a floating point number between 0 and 1.
	 * 
	 * @param	Color	The color you want to break into components.
	 * @param	Results	An optional parameter, allows you to use an array that already exists in memory to store the result.
	 * 
	 * @return	An <code>Array</code> object containing the Red, Green, Blue and Alpha values of the given color.
	 */
	static public float[] getRGBA(int Color,float[] Results)
	{
		if(Results == null)
			Results = new float[4];
		Results[0] = (Color >> 16) & 0xFF;
		Results[1] = (Color >> 8) & 0xFF;
		Results[2] = Color & 0xFF;
		Results[3] = (float)((Color >> 24) & 0xFF) / 255;
		return Results;
	}

	/**
	 * Loads an array with the RGBA values of a Flash <code>uint</code> color.
	 * RGB values are stored 0-255.  Alpha is stored as a floating point number between 0 and 1.
	 * 
	 * @param	Color	The color you want to break into components.
	 * 
	 * @return	An <code>Array</code> object containing the Red, Green, Blue and Alpha values of the given color.
	 */
	static public float[] getRGBA(int Color)
	{
		return getRGBA(Color,null);
	}

	/**
	 * Loads an array with the HSB values of a Flash <code>uint</code> color.
	 * Hue is a value between 0 and 360.  Saturation, Brightness and Alpha
	 * are as floating point numbers between 0 and 1.
	 * 
	 * @param	Color	The color you want to break into components.
	 * @param	Results	An optional parameter, allows you to use an array that already exists in memory to store the result.
	 * 
	 * @return	An <code>Array</code> object containing the Red, Green, Blue and Alpha values of the given color.
	 */
	static public float[] getHSB(int Color,float[] Results)
	{
		if(Results == null)
			Results = new float[4];

		float red = (float)((Color >> 16) & 0xFF) / 255;
		float green = (float)((Color >> 8) & 0xFF) / 255;
		float blue = (float)((Color) & 0xFF) / 255;

		float m = (red>green)?red:green;
		float dmax = (m>blue)?m:blue;
		m = (red>green)?green:red;
		float dmin = (m>blue)?blue:m;
		float range = dmax - dmin;

		Results[2] = dmax;
		Results[1] = 0;
		Results[0] = 0;

		if(dmax != 0)
			Results[1] = range / dmax;
		if(Results[1] != 0)
		{
			if(red == dmax)
				Results[0] = (green - blue) / range;
			else if(green == dmax)
				Results[0] = 2 + (blue - red) / range;
			else if(blue == dmax)
				Results[0] = 4 + (red - green) / range;
			Results[0] *= 60;
			if(Results[0] < 0)
				Results[0] += 360;
		}
		Results[3] = (float)((Color >> 24) & 0xFF) / 255;
		return Results;
	}

	/**
	 * Loads an array with the HSB values of a Flash <code>uint</code> color.
	 * Hue is a value between 0 and 360.  Saturation, Brightness and Alpha
	 * are as floating point numbers between 0 and 1.
	 * 
	 * @param	Color	The color you want to break into components.
	 * 
	 * @return	An <code>Array</code> object containing the Red, Green, Blue and Alpha values of the given color.
	 */
	static public float[] getHSB(int Color)
	{
		return getHSB(Color,null);
	}

	/**
	 * Converts a AARRGGBB format color to a libgdx/OpenGL rgba8888 format color.
	 * 
	 * @param	Color	The color to convert.
	 * 
	 * @return	The converted color.
	 */
	static public int argbToRgba(int Color)
	{
		return (Color << 8) | (Color >>> 24);
	}

	/**
	 * Multiplies two colors together. Colors should be in AARRGGBB format.
	 * 
	 * @param	Color1	The first color to multiply.
	 * @param	Color2	The second color to multiply.
	 * 
	 * @return	The multiplied colors.
	 */
	static public int multiplyColors(int Color1, int Color2)
	{
		int r = (int) (((Color1 >> 16) & 0xFF) * ((Color2 >> 16) & 0xFF) * 0.00392f);
		int g = (int) (((Color1 >> 8) & 0xFF) * ((Color2 >> 8) & 0xFF) * 0.00392f);
		int b = (int) ((Color1 & 0xFF) * (Color2 & 0xFF) * 0.00392f);
		int a = (int) (((Color1 >> 24) & 0xFF) * ((Color2 >> 24) & 0xFF) * 0.00392f);

		return FlxU.makeColor(r, g, b, a);
	}

	/**
	 * Format seconds as minutes with a colon, an optionally with milliseconds too.
	 * 
	 * @param	Seconds		The number of seconds (for example, time remaining, time spent, etc).
	 * @param	ShowMS		Whether to show milliseconds after a "." as well.  Default value is false.
	 * 
	 * @return	A nicely formatted <code>String</code>, like "1:03".
	 */
	static public String formatTime(float Seconds,boolean ShowMS)
	{
		String timeString = (int)(Seconds/60) + ":";
		int timeStringHelper = (int)(Seconds)%60;
		if(timeStringHelper < 10)
			timeString += "0";
		timeString += timeStringHelper;
		if(ShowMS)
		{
			timeString += ".";
			timeStringHelper = (int)((Seconds-(int)(Seconds))*100);
			if(timeStringHelper < 10)
				timeString += "0";
			timeString += timeStringHelper;
		}
		return timeString;
	}

	/**
	 * Format seconds as minutes with a colon, an optionally with milliseconds too.
	 * 
	 * @param	Seconds		The number of seconds (for example, time remaining, time spent, etc).
	 * 
	 * @return	A nicely formatted <code>String</code>, like "1:03".
	 */
	static public String formatTime(int Seconds)
	{
		return formatTime(Seconds, false);
	}

	/**
	 * Generate a comma-separated string from an array.
	 * Especially useful for tracing or other debug output.
	 * 
	 * @param	AnyArray	Any <code>Array</code> object.
	 * 
	 * @return	A comma-separated <code>String</code> containing the <code>.toString()</code> output of each element in the array.
	 */
	static public <T> String formatArray(Array<T> AnyArray)
	{
		if((AnyArray == null) || (AnyArray.size <= 0))
			return "";
		String string = AnyArray.get(0).toString();
		int i = 1;
		int l = AnyArray.size;
		while(i < l)
			string += ", " + AnyArray.get(i++).toString();
		return string;
	}

	/**
	 * Automatically commas and decimals in the right places for displaying money amounts.
	 * Does not include a dollar sign or anything, so doesn't really do much
	 * if you call say <code>var results:String = FlxU.formatMoney(10,false);</code>
	 * However, very handy for displaying large sums or decimal money values.
	 * 
	 * @param	Amount			How much moneys (in dollars, or the equivalent "main" currency - i.e. not cents).
	 * @param	ShowDecimal		Whether to show the decimals/cents component. Default value is true.
	 * @param	EnglishStyle	Major quantities (thousands, millions, etc) separated by commas, and decimal by a period.  Default value is true.
	 * 
	 * @return	A nicely formatted <code>String</code>.  Does not include a dollar sign or anything!
	 */
	static public String formatMoney(float Amount,boolean ShowDecimal,boolean EnglishStyle)
	{
		int helper;
		int amount = (int)Amount;
		String string = "";
		String comma = "";
		String zeroes = "";
		while(amount > 0)
		{
			if((string.length() > 0) && comma.length() <= 0)
			{
				if(EnglishStyle)
					comma = ",";
				else
					comma = ".";
			}
			zeroes = "";
			helper = amount - (int)(amount/1000f)*1000;
			amount /= 1000f;
			if(amount > 0)
			{
				if(helper < 100)
					zeroes += "0";
				if(helper < 10)
					zeroes += "0";
			}
			string = zeroes + helper + comma + string;
		}
		if(ShowDecimal)
		{
			amount = (int)(Amount*100)-((int)(Amount)*100);
			string += (EnglishStyle?".":",") + amount;
			if(amount < 10)
				string += "0";
		}
		return string;
	}

	/**
	 * Automatically commas and decimals in the right places for displaying money amounts.
	 * Does not include a dollar sign or anything, so doesn't really do much
	 * if you call say <code>var results:String = FlxU.formatMoney(10,false);</code>
	 * However, very handy for displaying large sums or decimal money values.
	 * 
	 * @param	Amount			How much moneys (in dollars, or the equivalent "main" currency - i.e. not cents).
	 * @param	ShowDecimal		Whether to show the decimals/cents component. Default value is true.
	 * 
	 * @return	A nicely formatted <code>String</code>.  Does not include a dollar sign or anything!
	 */
	static public String formatMoney(int Amount,boolean ShowDecimal)
	{
		return formatMoney(Amount,ShowDecimal,true);
	}

	/**
	 * Automatically commas and decimals in the right places for displaying money amounts.
	 * Does not include a dollar sign or anything, so doesn't really do much
	 * if you call say <code>var results:String = FlxU.formatMoney(10,false);</code>
	 * However, very handy for displaying large sums or decimal money values.
	 * 
	 * @param	Amount			How much moneys (in dollars, or the equivalent "main" currency - i.e. not cents).
	 * 
	 * @return	A nicely formatted <code>String</code>.  Does not include a dollar sign or anything!
	 */
	static public String formatMoney(int Amount)
	{
		return formatMoney(Amount,true,true);
	}

	/**
	 * Get the <code>String</code> name of any <code>Object</code>.
	 * 
	 * @param	Obj		The <code>Object</code> object in question.
	 * @param	Simple	Returns only the class name, not the package or packages.
	 * 
	 * @return	The name of the <code>Class</code> as a <code>String</code> object.
	 */
	static public String getClassName(Object Obj,boolean Simple)
	{
		return Simple ? ClassReflection.getSimpleName(Obj.getClass()) : Obj.getClass().getName();
	}

	/**
	 * Get the <code>String</code> name of any <code>Object</code>.
	 * 
	 * @param	Obj		The <code>Object</code> object in question.
	 * 
	 * @return	The name of the <code>Class</code> as a <code>String</code> object.
	 */
	static public String getClassName(Object Obj)
	{
		return getClassName(Obj,false);
	}

	/**
	 * Check to see if two objects have the same class name.
	 * 
	 * @param	Object1		The first object you want to check.
	 * @param	Object2		The second object you want to check.
	 * 
	 * @return	Whether they have the same class name or not.
	 */
	static public boolean compareClassNames(Object Object1,Object Object2)
	{
		return getClassName(Object1).equals(getClassName(Object2));
	}

	/**
	 * Look up a <code>Class</code> object by its string name.
	 * 
	 * @param	Name	The <code>String</code> name of the <code>Class</code> you are interested in.
	 * 
	 * @return	A <code>Class</code> object.
	 */
	static public Class<?> getClass(String Name)
	{
		try
		{
			return ClassReflection.forName(Name);
		}
		catch(ReflectionException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * A tween-like function that takes a starting velocity
	 * and some other factors and returns an altered velocity.
	 * 
	 * @param	Velocity		Any component of velocity (e.g. 20).
	 * @param	Acceleration	Rate at which the velocity is changing.
	 * @param	Drag			Really kind of a deceleration, this is how much the velocity changes if Acceleration is not set.
	 * @param	Max				An absolute value cap for the velocity.
	 * 
	 * @return	The altered Velocity value.
	 */
	static public float computeVelocity(float Velocity, float Acceleration, float Drag, float Max)
	{
		if(Acceleration != 0)
			Velocity += Acceleration*FlxG.elapsed;
		else if(Drag != 0)
		{
			float drag = Drag*FlxG.elapsed;
			if(Velocity - drag > 0)
				Velocity = Velocity - drag;
			else if(Velocity + drag < 0)
				Velocity += drag;
			else
				Velocity = 0;
		}
		if((Velocity != 0) && (Max != 10000))
		{
			if(Velocity > Max)
				Velocity = Max;
			else if(Velocity < -Max)
				Velocity = -Max;
		}
		return Velocity;
	}

	/**
	 * A tween-like function that takes a starting velocity
	 * and some other factors and returns an altered velocity.
	 * 
	 * @param	Velocity		Any component of velocity (e.g. 20).
	 * @param	Acceleration	Rate at which the velocity is changing.
	 * @param	Drag			Really kind of a deceleration, this is how much the velocity changes if Acceleration is not set.
	 * 
	 * @return	The altered Velocity value.
	 */
	static public float computeVelocity(float Velocity, float Acceleration, float Drag)
	{
		return computeVelocity(Velocity, Acceleration, Drag, 10000);
	}

	/**
	 * A tween-like function that takes a starting velocity
	 * and some other factors and returns an altered velocity.
	 * 
	 * @param	Velocity		Any component of velocity (e.g. 20).
	 * @param	Acceleration	Rate at which the velocity is changing.
	 * 
	 * @return	The altered Velocity value.
	 */
	static public float computeVelocity(float Velocity, float Acceleration)
	{
		return computeVelocity(Velocity, Acceleration, 0, 10000);
	}

	/**
	 * A tween-like function that takes a starting velocity
	 * and some other factors and returns an altered velocity.
	 * 
	 * @param	Velocity		Any component of velocity (e.g. 20).
	 * 
	 * @return	The altered Velocity value.
	 */
	static public float computeVelocity(float Velocity)
	{
		return computeVelocity(Velocity, 0, 0, 10000);
	}

	//*** NOTE: THESE LAST THREE FUNCTIONS REQUIRE FLXPOINT ***//

	/**
	 * Rotates a point in 2D space around another point by the given angle.
	 * 
	 * @param	X		The X coordinate of the point you want to rotate.
	 * @param	Y		The Y coordinate of the point you want to rotate.
	 * @param	PivotX	The X coordinate of the point you want to rotate around.
	 * @param	PivotY	The Y coordinate of the point you want to rotate around.
	 * @param	Angle	Rotate the point by this many degrees.
	 * @param	Point	Optional <code>FlxPoint</code> to store the results in.
	 * 
	 * @return	A <code>FlxPoint</code> containing the coordinates of the rotated point.
	 */
	public static FlxPoint rotatePoint(float X, float Y, float PivotX, float PivotY, float Angle, FlxPoint Point)
	{
		float sin = 0;
		float cos = 0;
		float radians = Angle * -0.017453293f;
		while(radians < -3.14159265f)
			radians += 6.28318531f;
		while(radians > 3.14159265f)
			radians = radians - 6.28318531f;

		if(radians < 0)
		{
			sin = 1.27323954f * radians + .405284735f * radians * radians;
			if(sin < 0)
				sin = .225f * (sin *-sin - sin) + sin;
			else
				sin = .225f * (sin * sin - sin) + sin;
		}
		else
		{
			sin = 1.27323954f * radians - 0.405284735f * radians * radians;
			if(sin < 0)
				sin = .225f * (sin *-sin - sin) + sin;
			else
				sin = .225f * (sin * sin - sin) + sin;
		}

		radians += 1.57079632f;
		if(radians > 3.14159265f)
			radians = radians - 6.28318531f;
		if(radians < 0)
		{
			cos = 1.27323954f * radians + 0.405284735f * radians * radians;
			if(cos < 0)
				cos = .225f * (cos *-cos - cos) + cos;
			else
				cos = .225f * (cos * cos - cos) + cos;
		}
		else
		{
			cos = 1.27323954f * radians - 0.405284735f * radians * radians;
			if(cos < 0)
				cos = .225f * (cos *-cos - cos) + cos;
			else
				cos = .225f * (cos * cos - cos) + cos;
		}

		float dx = X-PivotX;
		float dy = PivotY+Y; //Y axis is inverted in flash, normally this would be a subtract operation
		if(Point == null)
			Point = new FlxPoint();
		Point.x = PivotX + cos*dx - sin*dy;
		Point.y = PivotY - sin*dx - cos*dy;
		return Point;
	}

	/**
	 * Rotates a point in 2D space around another point by the given angle.
	 * 
	 * @param	X		The X coordinate of the point you want to rotate.
	 * @param	Y		The Y coordinate of the point you want to rotate.
	 * @param	PivotX	The X coordinate of the point you want to rotate around.
	 * @param	PivotY	The Y coordinate of the point you want to rotate around.
	 * @param	Angle	Rotate the point by this many degrees.
	 * 
	 * @return	A <code>FlxPoint</code> containing the coordinates of the rotated point.
	 */
	public static FlxPoint rotatePoint(float X, float Y, float PivotX, float PivotY, float Angle)
	{
		return rotatePoint(X, Y, PivotX, PivotY, Angle, null);
	}

	/**
	 * Calculates the angle between two points.  0 degrees points straight up.
	 * 
	 * @param	Point1		The X coordinate of the point.
	 * @param	Point2		The Y coordinate of the point.
	 * 
	 * @return	The angle in degrees, between -180 and 180.
	 */
	static public float getAngle(FlxPoint Point1, FlxPoint Point2)
	{
		float x = Point2.x - Point1.x;
		float y = Point2.y - Point1.y;
		if((x == 0) && (y == 0))
			return 0;
		float c1 = 3.14159265f * 0.25f;
		float c2 = 3 * c1;
		float ay = (y < 0)?-y:y;
		float angle = 0;
		if(x >= 0)
			angle = c1 - c1 * ((x - ay) / (x + ay));
		else
			angle = c2 - c1 * ((x + ay) / (ay - x));
		angle = ((y < 0) ? -angle : angle) * 57.2957796f;
		if(angle > 90)
			angle = angle - 270;
		else
			angle += 90;
		return angle;
	};

	/**
	 * Calculate the distance between two points.
	 * 
	 * @param	Point1	A <code>FlxPoint</code> object referring to the first location.
	 * @param	Point2	A <code>FlxPoint</code> object referring to the second location.
	 * 
	 * @return	The distance between the two points as a floating point <code>Number</code> object.
	 */
	static public float getDistance(FlxPoint Point1,FlxPoint Point2)
	{
		float dx = Point1.x - Point2.x;
		float dy = Point1.y - Point2.y;
		return (float)Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Returns the value of the first argument raised to the power of the second argument.
	 * NOTE: before you use this code you have to test it if the approximation is good enough for you!
	 * 
	 * @param	Base		The base.
	 * @param	Exponent	The exponent.
	 * 
	 * @return	The result.
	 */
	public static float pow(double Base, double Exponent)
	{
		final int tmp = (int) (Double.doubleToLongBits(Base) >> 32);
		final int tmp2 = (int) (Exponent * (tmp - 1072632447) + 1072632447);
		return (float) Double.longBitsToDouble(((long) tmp2) << 32);
	}
}

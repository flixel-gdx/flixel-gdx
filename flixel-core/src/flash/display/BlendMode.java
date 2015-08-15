package flash.display;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.GL20;

/**
 * A class that provides constant values for visual blend mode effects.
 * 
 * @author Thomas Weston
 */
public class BlendMode
{
	/**
	 * Adds the values of the constituent colors of the display object to the colors of its background, applying a ceiling of 0xFF.
	 */
	public static final String ADD = "add";
	/**
	 * Applies the alpha value of each pixel of the display object to the background.
	 */
	public static final String ALPHA = "alpha";
	/**
	 * Selects the darker of the constituent colors of the display object and the colors of the background (the colors with the smaller values).
	 */
	public static final String DARKEN = "darken";
	/**
	 * Compares the constituent colors of the display object with the colors of its background, and subtracts the darker of the values of the two constituent colors from the lighter value.
	 */
	public static final String DIFFERENCE = "difference";
	/**
	 * Erases the background based on the alpha value of the display object.
	 */
	public static final String ERASE = "erase";
	/**
	 * Adjusts the color of each pixel based on the darkness of the display object.
	 */
	public static final String HARDLIGHT = "hardlight";
	/**
	 * Inverts the background.
	 */
	public static final String INVERT = "invert";
	/**
	 * Forces the creation of a transparency group for the display object.
	 */
	public static final String LAYER = "layer";
	/**
	 * Selects the lighter of the constituent colors of the display object and the colors of the background (the colors with the larger values).
	 */
	public static final String LIGHTEN = "lighten";
	/**
	 * Multiplies the values of the display object constituent colors by the constituent colors of the background color, and normalizes by dividing by 0xFF, resulting in darker colors.
	 */
	public static final String MULTIPLY = "multiply";
	/**
	 * The display object appears in front of the background.
	 */
	public static final String NORMAL = "normal";
	/**
	 * Adjusts the color of each pixel based on the darkness of the background.
	 */
	public static final String OVERLAY = "overlay";
	/**
	 * Multiplies the complement (inverse) of the display object color by the complement of the background color, resulting in a bleaching effect.
	 */
	public static final String SCREEN = "screen";
	/**
	 * Uses a shader to define the blend between objects.
	 */
	public static final String SHADER = "shader";
	/**
	 * Subtracts the values of the constituent colors in the display object from the values of the background color, applying a floor of 0.
	 */
	public static final String SUBTRACT = "subtract";

	/**
	 * Internal, helps convert between strings and openGL blend modes.
	 */
	private static Map<String, int[]> _blendMap;

	/**
	 * Convert a Flash blend mode string to openGL compatible values.
	 * 
	 * @param	blendmode	The blend mode string.
	 * @return	An array containing the openGL blend values.
	 */
	public static int[] getOpenGLBlendMode(String blendmode)
	{
		int[] openGLBlendmode = _blendMap.get(blendmode);
		if(openGLBlendmode == null)
			openGLBlendmode = _blendMap.get(NORMAL);

		return openGLBlendmode;
	}

	/**
	 * Add a new blendmode.
	 * 
	 * @param	name		The name used to identify this blendmode.
	 * @param	sFactor		The source scale factor.
	 * @param	dFactor		The destination scale factor.
	 */
	public static void addBlendMode(String name, int sFactor, int dFactor)
	{
		_blendMap.put(name, new int[]{sFactor, dFactor});
	}

	/**
	 * Initializes the map
	 */
	static
	{
		_blendMap = new HashMap<String, int[]>(15);

		addBlendMode(ADD, GL20.GL_ONE, GL20.GL_ONE);
		addBlendMode(ALPHA, GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		//addBlendMode(DARKEN, GL20.GL_DST_COLOR, GL20.GL_ZERO);
		//addBlendMode(DIFFERENCE, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		addBlendMode(ERASE, GL20.GL_ZERO, GL20.GL_SRC_ALPHA);
		//addBlendMode(HARDLIGHT, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//addBlendMode(INVERT, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//addBlendMode(LAYER, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//addBlendMode(LIGHTEN, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		addBlendMode(MULTIPLY, GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
		addBlendMode(NORMAL, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//addBlendMode(OVERLAY, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		addBlendMode(SCREEN, GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		//addBlendMode(SHADER, );
		//addBlendMode(SUBTRACT, );
	}
}

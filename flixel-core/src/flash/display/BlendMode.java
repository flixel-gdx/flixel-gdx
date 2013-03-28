package flash.display;

import com.badlogic.gdx.graphics.GL10;

public class BlendMode
{
//	public static final int[] PASS_THROUGH;
	
	public static final int[] DISABLE = new int[]{GL10.GL_ZERO, GL10.GL_ZERO};
	/**
	 * The display object appears in front of the background.
	 */
	public static final int[] NORMAL = new int[]{GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA};
//	public static final int[] DISSOLVE;
	
	/**
	 * Selects the darker of the constituent colors of the display object and the colors of the background (the colors with the smaller values).
	 * Also known as MODULATE.
	 */
	public static final int[] DARKEN = new int[]{GL10.GL_DST_COLOR, GL10.GL_ZERO};
	/**
	 * Selects the darker of the constituent colors of the display object and the colors of the background (the colors with the smaller values).
	 * Also known as DARKEN.
	 */
	public static final int[] MODULATE = DARKEN;
	/**
	 *  Multiplies the values of the display object constituent colors by the constituent colors of the background color, and normalizes by dividing by 0xFF, resulting in darker colors.
	 */
	public static final int[] MULTIPLY = new int[]{GL10.GL_DST_COLOR, GL10.GL_ONE_MINUS_SRC_ALPHA};
//	public static final int[] COLOR_BURN;
//	public static final int[] LINEAR_BURN;
//	public static final int[] DARKER_COLOR;
			
	/**
	 * Selects the lighter of the constituent colors of the display object and the colors of the background (the colors with the larger values).
	 */
//	public static final int[] LIGHTEN;
	/**
	 * Multiplies the complement (inverse) of the display object color by the complement of the background color, resulting in a bleaching effect.
	 */
	public static final int[] SCREEN = new int[]{GL10.GL_ONE_MINUS_DST_COLOR, GL10.GL_ONE};
//	public static final int[] COLOR_DODGE;
	/**
	 * Adds the values of the constituent colors of the display object to the colors of its background, applying a ceiling of 0xFF.
	 * Also known as LINEAR_DODGE.
	 */
	public static final int[] ADD = new int[]{GL10.GL_ONE, GL10.GL_ONE};
	/**
	 * Adds the values of the constituent colors of the display object to the colors of its background, applying a ceiling of 0xFF.
	 * Also known as ADD.
	 */
	public static final int[] LINEAR_DODGE = ADD;
//	public static final int[] LIGHTER_COLOR;
	
	/**
	 * Adjusts the color of each pixel based on the darkness of the background.
	 */
//	public static final int[] OVERLAY;
//	public static final int[] SOFT_LIGHT;
	/**
	 * Adjusts the color of each pixel based on the darkness of the display object.
	 */
//	public static final int[] HARD_LIGHT;
//	public static final int[] VIVID_LIGHT;
//	public static final int[] LINEAR_LIGHT;
//	public static final int[] PIN_LIGHT;
//	public static final int[] HARD_MIX;
	
	/**
	 *  Compares the constituent colors of the display object with the colors of its background, and subtracts the darker of the values of the two constituent colors from the lighter value.
	 */
//	public static final int[] DIFFERENCE;
//	public static final int[] EXCLUSION;
	/**
	 * Subtracts the values of the constituent colors in the display object from the values of the background color, applying a floor of 0.
	 */
//	public static final int[] SUBTRACT;
//	public static final int[] DIVIDE;
	
//	public static final int[] HUE;
//	public static final int[] SATURATION;
//	public static final int[] COLOR;
//	public static final int[] LUMINOSITY;
	
//	public static final int[] AVERAGE;
//	public static final int[] BEHIND;
//	public static final int[] CLEAR;
//	public static final int[] INVERSE_DIFFERENCE;
//	public static final int[] OPACITY;
	public static final int[] PARTICLE = new int[]{GL10.GL_SRC_ALPHA, GL10.GL_ONE}; // TODO: Never heard of this one.
	
	/**
	 * Forces the creation of a transparency group for the display object.
	 */
//	public static final String LAYER;
	/**
	 * Inverts the background.
	 */
//	public static final String INVERT;
	/**
	 * Applies the alpha value of each pixel of the display object to the background.
	 */
//	public static final String ALPHA;
	/**
	 * Erases the background based on the alpha value of the display object.
	 */
//	public static final String ERASE;
	/**
	 * Uses a shader to define the blend between objects.
	 */
//	public static final String SHADER;
	
	
}

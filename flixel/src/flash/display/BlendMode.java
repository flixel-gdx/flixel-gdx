package flash.display;

import com.badlogic.gdx.graphics.GL10;

public class BlendMode
{
	public static final int[] NORMAL = new int[]{GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA};
//	public static final String LAYER = "layer";
	public static final int[] MULTIPLY = new int[]{GL10.GL_DST_COLOR, GL10.GL_ONE_MINUS_SRC_ALPHA};
	public static final int[] SCREEN = new int[]{GL10.GL_ONE_MINUS_DST_COLOR, GL10.GL_ONE};
//	public static final String LIGHTEN = "lighten";
//	public static final String DARKEN = "darken";
//	public static final String ADD = "add";
//	public static final String SUBTRACT = "subtract";
//	public static final String DIFFERENCE = "difference";
//	public static final String INVERT = "invert";
//	public static final String OVERLAY = "overlay";
//	public static final String HARDLIGHT = "hardlight";
//	public static final String ALPHA = "alpha";
//	public static final String ERASE = "erase";
//	public static final String SHADER = "shader";
	public static final int[] LINEAR_DODGE = new int[]{GL10.GL_ONE, GL10.GL_ONE};
}

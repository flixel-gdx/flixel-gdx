package flash.display;

import java.util.Iterator;

import org.flixel.FlxG;
import org.flixel.FlxSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Blend modes in OpenGL ES 2.0. If you don't the shaders anymore, you must dispose them by 
 * calling <code>BlendModeGl20.destroy()</code>.
 * Requires GLES20.
 * 
 * Documentation are from:
 * PEGTOP: http://www.pegtop.net/delphi/articles/blendmodes/
 * Yaldex: http://www.yaldex.com/open-gl/ch19lev1sec6.html
 * Photoblogstop: http://photoblogstop.com/photoshop/photoshop-blend-modes-explained
 * 
 * @author Ka Wing Chin
 */
public class BlendModeGL20
{
	/**
	 * Holds the shader programs.
	 */
	private static final ObjectMap<String, ShaderProgram> shaders;
	/**
	 * The path to the shaders.
	 */
	private static final String PATH = "org/flixel/data/shaders/blend/";
	/**
	 * The default vertex shader that will be used.
	 */
	private static final String VERTEX = Gdx.files.classpath(PATH + "vertex.glsl").readString();	
	
	/**
	 * Adds the values of the constituent colors of the display object to the colors of its background, applying a ceiling of 0xFF.
	 */
	public static final String ADD = "add.glsl";
	/**
	 * Adds the two images and divides by two. The result is the same as NORMAL when the opacity is set to 0.5. This operation is commutative.
	 */
	public static final String AVERAGE = "average.glsl";
	/**
	 * Chooses the blend value only where the base image is completely transparent (i.e., base.a = 0.0). You can think of the base image as a piece of clear acetate, and the effect of this mode is as if you were painting the blend image on the back of the acetateonly the areas painted behind transparent pixels are visible.
	 */
	public static final String BEHIND = "behind.glsl";
	/**
	 * Always uses the blend value, and the alpha value of result is set to 0 (transparent). This blend mode is more apt to be used with drawing tools than on complete images.
	 */
	public static final String CLEAR = "clear.glsl";
	/**
	 * A single color channel is extracted from the blend image which is cyan, the other color channels are taken from the base image.
	 */
	public static final String CMY_C = "cmy_c.glsl";
	/**
	 * A single color channel is extracted from the blend image which is magenta, the other color channels are taken from the base image.
	 */
	public static final String CMY_M = "cmy_m.glsl";
	/**
	 * A single color channel is extracted from the blend image which is yellow, the other color channels are taken from the base image.
	 */
	public static final String CMY_Y = "cmy_y.glsl";
	/**
	 * Keeps the color of the active layer, and blends the hue and saturation (the color) of the active layer with the luminance of the lower layers.
	 */
	public static final String COLOR = "color.glsl";
	/**
	 * Darkens the base color as indicated by the blend color by decreasing luminance. There is no effect if the blend value is white.
	 */
	public static final String COLOR_BURN = "color_burn.glsl";
	/**
	 * Brightens the base color as indicated by the blend color by increasing luminance. There is no effect if the blend value is black.
	 */
	public static final String COLOR_DODGE = "color_dodge.glsl";
	/**
	 * Selects the darker of the constituent colors of the display object and the colors of the background (the colors with the smaller values).
	 */
	public static final String DARKEN = "darken.glsl";
	/**
	 * Compares the constituent colors of the display object with the colors of its background, and subtracts the darker of the values of the two constituent colors from the lighter value.
	 */
	public static final String DIFFERENCE = "difference.glsl";
	/**
	 * Either blend or base is chosen randomly at every pixel. The value of Opacity is used as a probability factor for choosing the blend value.
	 */
	//public static final String DISSOLVE = "dissolve.glsl";
	/**
	 * Is similar to DIFFERENCE, but it produces an effect that is lower in contrast (softer).
	 */
	public static final String EXCLUSION = "exclusion.glsl";
	/**
	 * This mode is a variation of reflect mode (base and blend color swapped).
	 */
	public static final String GLOW = "glow.glsl";
	/**
	 * Adjusts the color of each pixel based on the darkness of the display object.
	 */
	public static final String HARD_LIGHT = "hard_light.glsl";
	/**
	 * Uses the Linear Light blend mode set to a threshold, so for each RGB color channel, pixels in each channel are converted to either all black or all white.
	 */
	public static final String HARD_MIX = "hard_mix.glsl";
	/**
	 * Keeps the Hue of the active layer, and blends the luminance and saturation of the underlying layers.
	 */
	public static final String HUE = "hue.glsl";
	/**
	 * Same as color burn mode, but the base and blend color are swapped.
	 */
	public static final String INVERSE_COLOR_BURN = "inverse_color_burn.glsl";
	/**
	 * Same as color dodge mode, but the base and blend color are swapped.
	 */
	public static final String INVERSE_COLOR_DODGE = "inverse_color_dodge.glsl";
	/**
	 * Performs the "opposite" of DIFFERENCE. Blend values of white and black produce the same results as for DIFFERENCE (white inverts and black has no effect), but colors in between white and black become lighter instead of darker.
	 */
	public static final String INVERSE_DIFFERENCE = "inverse_difference.glsl";
	/**
	 * Selects the lighter of the constituent colors of the display object and the colors of the background (the colors with the larger values).
	 */
	public static final String LIGHTEN = "lighten.glsl";
	/**
	 * Adds the values of the constituent colors of the display object to the colors of its background, applying a ceiling of 0xFF.
	 */
	public static final String LINEAR_DODGE = "add.glsl";	
	/**
	 * Darker than Multiply, but less saturated than Color Burn.
	 */
	public static final String LINEAR_BURN = "linear_burn.glsl";
	/**
	 * Uses a combination of the Linear Dodge blend mode on the lighter pixels, and the Linear Burn blend mode on the darker pixels.
	 */
	public static final String LINEAR_LIGHT = "linear_light.glsl";
	/**
	 * Keeps the luminance of the active layer, and blends it with hue and saturation (the color) of the composite view of the layers below
	 */
	public static final String LUMINOSITY = "luminosity.glsl";
	/**
	 * Multiplies the values of the display object constituent colors by the constituent colors of the background color, and normalizes by dividing by 0xFF, resulting in darker colors.
	 */
	public static final String MULTIPLY = "multiply.glsl";
	/**
	 * This one is the "opposite" of difference mode. Note that it is NOT difference mode inverted, because black and white return the same result, but colors between become brighter instead of darker.
	 */
	public static final String NEGATION = "negation.glsl";
	/**
	 * The display object appears in front of the background.
	 */
	public static final String NORMAL = "normal.glsl";
	/**
	 * An opacity value in the range [0,1] can also specify the relative contribution of the base image and the computed result. The result value from any of the preceding formulas can be further modified to compute the effect of the opacity value
	 */
	public static final String OPACITY = "opacity.glsl";
	/**
	 * Adjusts the color of each pixel based on the darkness of the background.
	 */
	public static final String OVERLAY = "overlay.glsl";
	/**
	 * 
	 */
	public static final String PHOENIX = "phoenix.glsl";
	/**
	 * Uses a combination of the Lighten blend mode on the lighter pixels, and the Darken blend mode on the darker pixels.
	 */
	public static final String PIN_LIGHT = "pin_light.glsl";
	/**
	 * This mode is useful when adding shining objects or light zones to images.
	 */
	public static final String REFLECT = "reflect.glsl";
	/**
	 * A single color channel is extracted from the blend image which is red, the other color channels are taken from the base image.
	 */
	public static final String RGB_R = "rgb_r.glsl";
	/**
	 * A single color channel is extracted from the blend image which is green, the other color channels are taken from the base image.
	 */
	public static final String RGB_G = "rgb_g.glsl";
	/**
	 * A single color channel is extracted from the blend image which is blue, the other color channels are taken from the base image.
	 */
	public static final String RGB_B = "rgb_b.glsl";
	/**
	 * Keeps the saturation of the active layer, and blends the luminosity and hue from the underlying layers.
	 */
	public static final String SATURATION = "saturation.glsl";
	/**
	 * Multiplies the complement (inverse) of the display object color by the complement of the background color, resulting in a bleaching effect.
	 */
	public static final String SCREEN = "screen.glsl";
	/**
	 * Produces an effect similar to a soft (diffuse) light shining through the blend image and onto the base image. The resulting image is essentially a muted combination of the two images.
	 */
	public static final String SOFT_LIGHT = "soft_light.glsl";
	/**
	 * Subtracts the values of the constituent colors in the display object from the values of the background color, applying a floor of 0.
	 */
	public static final String SUBSTRACT = "substract.glsl";
	/**
	 * Uses a combination of the Color Dodge Mode on the lighter pixels, and the Color Burn blend mode on the darker pixels.
	 */
	public static final String VIVID_LIGHT = "vivid_light.glsl";
			
	/**
	 * Initializes the array.
	 */
	static
	{
		shaders = new ObjectMap<String, ShaderProgram>();
	}
	
	/**
	 * Set the shader and blend values to the sprite. It will be used during <code>FlxSprite.draw()</code>.
	 * @param name		The name of the blend mode, e.g. <code>BlendModeGL20.ADD</code>.
	 * @param base		The base image (background).
	 * @param blend		The blend image (layer).
	 */
	public static void blend(String name, FlxSprite base, FlxSprite blend)
	{
		ShaderProgram shader = createProgram(name);
		base.blendGL20 = shader;
		base.spriteBlend = blend;
	}
	
	/**
	 * Creates a shader program from the default vertex and blend mode.
	 * You may want to create the shader during <code>FlxState.create()</code> and apply it later to the 
	 * @param name		The name of the blend mode, e.g. <code>BlendModeGL20.ADD</code>.
	 * @param opacity	The opacity that will be used in gl_FragColor. Used by Opacity blend.
	 * @return			The program shader that is created.
	 */
	public static ShaderProgram createProgram(String name, float opacity)
	{
		ShaderProgram shader;
		if(shaders.containsKey(name))
			shader = shaders.get(name);
		else
		{
			shader = new ShaderProgram(VERTEX, Gdx.files.classpath(PATH + name).readString());
			FlxG.isShaderCompiled(shader);			
			shaders.put(name, shader);
			
			shader.begin();
			if(shader.hasUniform("u_texture"))
				shader.setUniformi("u_texture", 1);
			if(shader.hasUniform("u_texture1"))
				shader.setUniformi("u_texture1", 2);			
			if(shader.hasUniform("u_opacity"))
				shader.setUniformf("u_opacity", opacity);
			shader.end();
		}
		return shader;
	}
	
	/**
	 * Creates a shader program from the default vertex and blend mode.
	 * @param name		The name of the blend mode, e.g. <code>BlendModeGL20.ADD</code>.
	 * @return			The program shader that is created.
	 */
	public static ShaderProgram createProgram(String name)
	{
		return createProgram(name, 1f);
	}
	
	/**
	 * Disposes shaders.
	 */
	public static void destroy()
	{
		ShaderProgram shader;
		Iterator<ShaderProgram> entries = shaders.values().iterator();
		while(entries.hasNext())
		{
			shader = entries.next();
			shader.dispose();
			shader = null;
		}
		shaders.clear();
	}
}
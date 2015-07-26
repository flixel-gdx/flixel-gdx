package flash.display;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.event.IFlxShaderProgram;
import org.flixel.gles20.FlxShaderProgram;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Blend modes in OpenGL ES 2.0. If you don't use the shaders anymore, you must dispose them by 
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
	 * The path to the shaders.
	 */
	private static final String PATH = "org/flixel/data/shaders/blend/";
	/**
	 * The default vertex shader that will be used.
	 */
	public static final String VERTEX = "org/flixel/data/shaders/vertex.glsl";

	/**
	 * Adds the values of the constituent colors of the display object to the
	 * colors of its background, applying a ceiling of 0xFF.
	 */
	public static final String ADD = PATH + "add.glsl";
	/**
	 * Adds the two images and divides by two. The result is the same as NORMAL
	 * when the opacity is set to 0.5. This operation is commutative.
	 */
	public static final String AVERAGE = PATH + "average.glsl";
	/**
	 * Chooses the blend value only where the base image is completely
	 * transparent (i.e., base.a = 0.0). You can think of the base image as a
	 * piece of clear acetate, and the effect of this mode is as if you were
	 * painting the blend image on the back of the acetateonly the areas painted
	 * behind transparent pixels are visible.
	 */
	public static final String BEHIND = PATH + "behind.glsl";
	/**
	 * Always uses the blend value, and the alpha value of result is set to 0
	 * (transparent). This blend mode is more apt to be used with drawing tools
	 * than on complete images.
	 */
	public static final String CLEAR = PATH + "clear.glsl";
	/**
	 * A single color channel is extracted from the blend image which is cyan,
	 * the other color channels are taken from the base image.
	 */
	public static final String CMY_C = PATH + "cmy_c.glsl";
	/**
	 * A single color channel is extracted from the blend image which is
	 * magenta, the other color channels are taken from the base image.
	 */
	public static final String CMY_M = PATH + "cmy_m.glsl";
	/**
	 * A single color channel is extracted from the blend image which is yellow,
	 * the other color channels are taken from the base image.
	 */
	public static final String CMY_Y = PATH + "cmy_y.glsl";
	/**
	 * Keeps the color of the active layer, and blends the hue and saturation
	 * (the color) of the active layer with the luminance of the lower layers.
	 */
	public static final String COLOR = PATH + "color.glsl";
	/**
	 * Darkens the base color as indicated by the blend color by decreasing
	 * luminance. There is no effect if the blend value is white.
	 */
	public static final String COLOR_BURN = PATH + "color_burn.glsl";
	/**
	 * Brightens the base color as indicated by the blend color by increasing
	 * luminance. There is no effect if the blend value is black.
	 */
	public static final String COLOR_DODGE = PATH + "color_dodge.glsl";
	/**
	 * Selects the darker of the constituent colors of the display object and
	 * the colors of the background (the colors with the smaller values).
	 */
	public static final String DARKEN = PATH + "darken.glsl";
	/**
	 * Compares the constituent colors of the display object with the colors of
	 * its background, and subtracts the darker of the values of the two
	 * constituent colors from the lighter value.
	 */
	public static final String DIFFERENCE = PATH + "difference.glsl";
	/**
	 * Either blend or base is chosen randomly at every pixel. The value of
	 * Opacity is used as a probability factor for choosing the blend value.
	 */
	//public static final String DISSOLVE = "dissolve.glsl";
	/**
	 * Is similar to DIFFERENCE, but it produces an effect that is lower in
	 * contrast (softer).
	 */
	public static final String EXCLUSION = PATH + "exclusion.glsl";
	/**
	 * This mode is a variation of reflect mode (base and blend color swapped).
	 */
	public static final String GLOW = PATH + "glow.glsl";
	/**
	 * Adjusts the color of each pixel based on the darkness of the display
	 * object.
	 */
	public static final String HARD_LIGHT = PATH + "hard_light.glsl";
	/**
	 * Uses the Linear Light blend mode set to a threshold, so for each RGB
	 * color channel, pixels in each channel are converted to either all black
	 * or all white.
	 */
	public static final String HARD_MIX = PATH + "hard_mix.glsl";
	/**
	 * Keeps the Hue of the active layer, and blends the luminance and
	 * saturation of the underlying layers.
	 */
	public static final String HUE = PATH + "hue.glsl";
	/**
	 * Same as color burn mode, but the base and blend color are swapped.
	 */
	public static final String INVERSE_COLOR_BURN = PATH + "inverse_color_burn.glsl";
	/**
	 * Same as color dodge mode, but the base and blend color are swapped.
	 */
	public static final String INVERSE_COLOR_DODGE = PATH + "inverse_color_dodge.glsl";
	/**
	 * Performs the "opposite" of DIFFERENCE. Blend values of white and black
	 * produce the same results as for DIFFERENCE (white inverts and black has
	 * no effect), but colors in between white and black become lighter instead
	 * of darker.
	 */
	public static final String INVERSE_DIFFERENCE = PATH + "inverse_difference.glsl";
	/**
	 * Selects the lighter of the constituent colors of the display object and
	 * the colors of the background (the colors with the larger values).
	 */
	public static final String LIGHTEN = PATH + "lighten.glsl";
	/**
	 * Adds the values of the constituent colors of the display object to the
	 * colors of its background, applying a ceiling of 0xFF.
	 */
	public static final String LINEAR_DODGE = PATH + "add.glsl";
	/**
	 * Darker than Multiply, but less saturated than Color Burn.
	 */
	public static final String LINEAR_BURN = PATH + "linear_burn.glsl";
	/**
	 * Uses a combination of the Linear Dodge blend mode on the lighter pixels,
	 * and the Linear Burn blend mode on the darker pixels.
	 */
	public static final String LINEAR_LIGHT = PATH + "linear_light.glsl";
	/**
	 * Keeps the luminance of the active layer, and blends it with hue and
	 * saturation (the color) of the composite view of the layers below
	 */
	public static final String LUMINOSITY = PATH + "luminosity.glsl";
	/**
	 * Multiplies the values of the display object constituent colors by the
	 * constituent colors of the background color, and normalizes by dividing by
	 * 0xFF, resulting in darker colors.
	 */
	public static final String MULTIPLY = PATH + "multiply.glsl";
	/**
	 * This one is the "opposite" of difference mode. Note that it is NOT
	 * difference mode inverted, because black and white return the same result,
	 * but colors between become brighter instead of darker.
	 */
	public static final String NEGATION = PATH + "negation.glsl";
	/**
	 * The display object appears in front of the background.
	 */
	public static final String NORMAL = PATH + "normal.glsl";
	/**
	 * An opacity value in the range [0,1] can also specify the relative
	 * contribution of the base image and the computed result. The result value
	 * from any of the preceding formulas can be further modified to compute the
	 * effect of the opacity value
	 */
	public static final String OPACITY = PATH + "opacity.glsl";
	/**
	 * Adjusts the color of each pixel based on the darkness of the background.
	 */
	public static final String OVERLAY = PATH + "overlay.glsl";
	/**
	 * Subtracts the lighter pixel from the darker.
	 */
	public static final String PHOENIX = PATH + "phoenix.glsl";
	/**
	 * Uses a combination of the Lighten blend mode on the lighter pixels, and
	 * the Darken blend mode on the darker pixels.
	 */
	public static final String PIN_LIGHT = PATH + "pin_light.glsl";
	/**
	 * This mode is useful when adding shining objects or light zones to images.
	 */
	public static final String REFLECT = PATH + "reflect.glsl";
	/**
	 * A single color channel is extracted from the blend image which is red,
	 * the other color channels are taken from the base image.
	 */
	public static final String RGB_R = PATH + "rgb_r.glsl";
	/**
	 * A single color channel is extracted from the blend image which is green,
	 * the other color channels are taken from the base image.
	 */
	public static final String RGB_G = PATH + "rgb_g.glsl";
	/**
	 * A single color channel is extracted from the blend image which is blue,
	 * the other color channels are taken from the base image.
	 */
	public static final String RGB_B = PATH + "rgb_b.glsl";
	/**
	 * Keeps the saturation of the active layer, and blends the luminosity and
	 * hue from the underlying layers.
	 */
	public static final String SATURATION = PATH + "saturation.glsl";
	/**
	 * Multiplies the complement (inverse) of the display object color by the
	 * complement of the background color, resulting in a bleaching effect.
	 */
	public static final String SCREEN = PATH + "screen.glsl";
	/**
	 * Produces an effect similar to a soft (diffuse) light shining through the
	 * blend image and onto the base image. The resulting image is essentially a
	 * muted combination of the two images.
	 */
	public static final String SOFT_LIGHT = PATH + "soft_light.glsl";
	/**
	 * Subtracts the values of the constituent colors in the display object from
	 * the values of the background color, applying a floor of 0.
	 */
	public static final String SUBSTRACT = PATH + "substract.glsl";
	/**
	 * Uses a combination of the Color Dodge Mode on the lighter pixels, and the
	 * Color Burn blend mode on the darker pixels.
	 */
	public static final String VIVID_LIGHT = PATH + "vivid_light.glsl";

	/**
	 * Set the shader and blend values to the sprite.
	 * It will be used during <code>FlxSprite.renderBlend()</code>.
	 * Create the shader program via <code>BlendModeGL20.addBlendMode()</code>
	 * to load in to the <code>AssetManager</code>.
	 * 
	 * @param	Shader		The shader that will be used for the base.
	 * @param	Base		The base image (background).
	 * @param	Blend		The blend image (layer).
	 * 
	 * @return	The shader that was passed in.
	 */
	public static FlxShaderProgram blend(FlxShaderProgram Shader, FlxSprite Base, Texture Blend)
	{
		Base.blendGL20 = Shader;
		Base.blendTexture = Blend;
		return Shader;
	}

	/**
	 * Set the shader and blend values to the sprite.
	 * It will be used during <code>FlxSprite.renderBlend()</code>.
	 * Create the shader program via <code>BlendModeGL20.addBlendMode()</code>
	 * to load in to the <code>AssetManager</code>.
	 * 
	 * @param	Shader		The shader that will be used for the base.
	 * @param	Base		The base image (background).
	 * @param	Blend		The blend image (layer).
	 * 
	 * @return	The shader that was passed in.
	 */
	public static FlxShaderProgram blend(FlxShaderProgram Shader, FlxSprite Base, FlxSprite Blend)
	{
		return blend(Shader, Base, Blend.getTexture());
	}

	/**
	 * Set the shader and blend values to the sprite.
	 * It will be used during <code>FlxSprite.renderBlend()</code>.
	 * 
	 * @param	Name		The name of the blend mode, e.g. <code>BlendModeGL20.ADD</code>.
	 * @param	Base		The base image (background).
	 * @param	Blend		The blend image (layer).
	 * 
	 * @return	The shader that was passed in.
	 */
	public static FlxShaderProgram blend(String Name, FlxSprite Base, Texture Blend)
	{
		return blend(createProgram(Name), Base, Blend);
	}

	/**
	 * Set the shader and blend values to the sprite.
	 * It will be used during <code>FlxSprite.renderBlend()</code>.
	 * 
	 * @param	Name		The name of the blend mode, e.g. <code>BlendModeGL20.ADD</code>.
	 * @param	Base		The base image (background).
	 * @param	Blend		The blend image (layer).
	 * 
	 * @return	The shader that was passed in.
	 */
	public static FlxShaderProgram blend(String Name, FlxSprite Base, FlxSprite Blend)
	{
		return blend(Name, Base, Blend.getTexture());
	}

	/**
	 * Creates a shader program from the default vertex and blend mode.
	 * You may want to create the shader during <code>FlxState.create()</code> and apply it later.
	 * 
	 * @param	Name	The name of the blend mode, e.g. <code>BlendModeGL20.ADD</code>.
	 * 
	 * @return	The program shader that is created.
	 */
	public static FlxShaderProgram createProgram(String Name)
	{
		FlxShaderProgram shader;
		if(FlxG._cache.containsAsset(Name, FlxShaderProgram.class))
			shader = FlxG._cache.load(Name, FlxShaderProgram.class);
		else
		{
			shader = FlxG.loadShader(Name, VERTEX, Name);
			IFlxShaderProgram callback = new IFlxShaderProgram()
			{
				@Override
				public void loadShaderSettings(ShaderProgram shader)
				{
					shader.begin();
					if(shader.hasUniform("u_texture"))
						shader.setUniformi("u_texture", 1);
					if(shader.hasUniform("u_texture1"))
						shader.setUniformi("u_texture1", 2);
					shader.end(); // TODO: set texture coordinates
				}
			};
			callback.loadShaderSettings(shader);
			shader.callback = callback;
		}
		return shader;
	}

	/**
	 * Add a new blend mode.
	 * 
	 * @param	Name		The name of the blend mode
	 * @param	Vertex		The path of the vertex file.
	 * @param	Fragment	The path of the fragment file.
	 * 
	 * @return	The program shader.
	 */
	public static FlxShaderProgram addBlendMode(String Name, String Vertex, String Fragment)
	{
		FlxShaderProgram shader;
		if(FlxG._cache.containsAsset(Name, FlxShaderProgram.class))
			shader = FlxG._cache.load(Name, FlxShaderProgram.class);
		else
			shader = FlxG.loadShader(Name, Vertex, Fragment);
		return shader;
	}

	/**
	 * Add a new blend mode. Uses the default vertex file.
	 * 
	 * @param	Name		The name of the blend mode
	 * @param	Fragment	The path of the fragment file.
	 * 
	 * @return	The program shader.
	 */
	public static FlxShaderProgram addBlendMode(String Name, String Fragment)
	{
		return addBlendMode(Name, VERTEX, Fragment);
	}
}

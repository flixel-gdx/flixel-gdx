package flash.display;

import org.flixel.FlxG;
import org.flixel.FlxSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Blend modes in GLES20.
 *
 * @author Ka Wing Chin
 */
public class BlendModeGL20
{
	private final static ObjectMap<String, ShaderProgram> shaders;
	private static final String VERTEX = Gdx.files.internal("org/flixel/data/shaders/blend/vertex.glsl").readString();	
	
	public static final String ADD = "org/flixel/data/shaders/blend/add.glsl";
	public static final String CLEAR = "org/flixel/data/shaders/blend/clear.glsl";
	public static final String COLOR_BURN = "org/flixel/data/shaders/blend/color_burn.glsl";
	public static final String COLOR_DODGE = "org/flixel/data/shaders/blend/color_dodge.glsl";
	public static final String DARKEN = "org/flixel/data/shaders/blend/darken.glsl";
	public static final String DIFFERENCE = "org/flixel/data/shaders/blend/difference.glsl";	
	public static final String DISSOLVE = "org/flixel/data/shaders/blend/dissolve.glsl";
	public static final String EXCLUSION = "org/flixel/data/shaders/blend/exclusion.glsl";
	public static final String HARD_LIGHT = "org/flixel/data/shaders/blend/hard_light.glsl";
	public static final String INVERSE_DIFFERENCE = "org/flixel/data/shaders/blend/inverse_difference.glsl";	
	public static final String LIGHTEN = "org/flixel/data/shaders/blend/lighten.glsl";
	public static final String MULTIPLY = "org/flixel/data/shaders/blend/multiply.glsl";
	public static final String NORMAL = "org/flixel/data/shaders/blend/normal.glsl";
	public static final String OPACITY = "org/flixel/data/shaders/blend/opacity.glsl";
	public static final String OVERLAY = "org/flixel/data/shaders/blend/overlay.glsl";
	public static final String PHOENIX = "org/flixel/data/shaders/blend/phoenix.glsl";
	public static final String PIN_LIGHT = "org/flixel/data/shaders/blend/pin_light.glsl";
	public static final String SCREEN = "org/flixel/data/shaders/blend/screen.glsl";
	public static final String SOFT_LIGHT = "org/flixel/data/shaders/blend/soft_light.glsl";
	public static final String SUBSTRACT = "org/flixel/data/shaders/blend/substract.glsl";
	
	
	
	static
	{
		shaders = new ObjectMap<String, ShaderProgram>();
	}
	
	public static void blend(String name, FlxSprite base, FlxSprite blend)
	{
		ShaderProgram shader = createProgram(name);
		base.getTexture().bind(1);
		blend.getTexture().bind(2);
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0);
		base.shader = shader;
	}
	
	public static ShaderProgram createProgram(String name)
	{
		ShaderProgram shader;
		if(shaders.containsKey(name))
			shader = shaders.get(name);
		else
		{
			shader = new ShaderProgram(VERTEX, Gdx.files.internal(name).readString());
			FlxG.isShaderCompiled(shader);			
			shaders.put(name, shader);
			shader.begin();
			if(name.equals(NORMAL))
				shader.setUniformi("u_texture", 1);
			else if(name.equals(CLEAR))
				shader.setUniformi("u_texture", 1);
			else if(name.equals(DISSOLVE) || name.equals(OPACITY))
			{
				shader.setUniformf("u_opacity", 0.5f);
//				shader.setUniformf("u_noiseScale", 0.5f);		
			}
			else
			{
				shader.setUniformi("u_texture", 1);
				shader.setUniformi("u_texture1", 2);
			}
			shader.end();
		}
		return shader;
	}
	
	public static void destroy()
	{
		ShaderProgram shader;
		while(shaders.values().hasNext())
		{
			shader = shaders.values().next();
			shader.dispose();
			shader = null;
		}		
		shaders.clear();
	}
}


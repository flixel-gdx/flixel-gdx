package org.flixel.system.gdx.text;

import org.flixel.FlxG;
import org.flixel.FlxU;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;

import flash.display.BlendMode;
import flash.text.TextField;
import flash.text.TextFormat;

public class GdxTextField extends TextField
{
	/**
	 * The default vertex shader that will be used.
	 */
	private final String VERTEX = "org/flixel/data/shaders/vertex.glsl";
	/**
	 * The default fragment shader that will be used for distance field.
	 */
	public final String DISTANCE_FIELD_FRAGMENT = "org/flixel/data/shaders/distance_field.glsl";
	
	/**
	 * The current <code>TextFormat</code>
	 */
	private TextFormat _currentFormat;
	/**
	 * The text of this <code>TextField</code>.
	 */
	private String _text;
	
	/**
	 * Parameters that are used for generating the <code>BitmapFont</code>.
	 */
	private BitmapFontParameter _bitmapFontParameter;
	/**
	 * Internal reference to a libgdx <code>BitmapFontCache</code> object.
	 */
	private BitmapFontCache _fontCache;
	/**
	 * The shader program that is used for distance field.
	 */
	private ShaderProgram _distanceFieldShader;
	/**
	 * Internal tracker whether the distance field is enabled.
	 */
	private boolean _distanceFieldEnabled;
	/**
	 * The padding that is set for generating the bitmap font.
	 */
	private int _padding;
	/**
	 * The smoothness of the font.
	 */
	private float _smoothness;
	/**
	 * Internal, for tracking if the scale has changed and the cache needs to be updated.
	 */
	private float _lastScaleX;
	/**
	 * Internal, for tracking if the scale has changed and the cache needs to be updated.
	 */
	private float _lastScaleY;
	
	public GdxTextField()
	{
		_text = "";
		
		_currentFormat = new TextFormat("");
		defaultTextFormat = new TextFormat("");
		
		_bitmapFontParameter = new BitmapFontParameter();
		_bitmapFontParameter.flip = true;
		
		_lastScaleX = scaleX = 1;
		_lastScaleY = scaleY = 1;
	}

	@Override
	public void setText(String text)
	{
		_text = text;
		setTextFormat(defaultTextFormat);
		
		calcFrame();
	}
	
	@Override
	public String getText()
	{
		return _text;
	}
	
	@Override
	public void setTextFormat(TextFormat format)
	{
		String font = format.font != null && format.font.length() > 0 ? format.font : _currentFormat.font;
		String align = format.align != null ? format.align : "left";
		float size = format.size;
		int color = format.color;
		
		if(!font.equals(_currentFormat.font) || size != _currentFormat.size)
		{
			try
			{
				_fontCache = new BitmapFontCache(FlxG.loadFont(font, FlxU.round(size), _bitmapFontParameter));
			}
			catch(Exception e)
			{
				FlxG.log(e.getMessage());
				_fontCache = new BitmapFontCache(FlxG.loadFont("org/flixel/data/font/nokiafc.fnt", 22, _bitmapFontParameter));
			}
		}

		if(!align.equals(_currentFormat.align))
		{
			_currentFormat.align = align;
			calcFrame();
		}
		
		_currentFormat = new TextFormat(font, size, color, false, false, false, "", "", align);
	}
	
	public void calcFrame()
	{
		if(_fontCache != null)
		{
			GlyphLayout layout = _fontCache.setText(_text, 2, 3, width, flashAlignToGdx(_currentFormat.align), true);
			height = layout.height + 4;
		}
	}
	
	public void render()
	{
		// scaling
		if(_lastScaleX != scaleX || _lastScaleY != scaleY)
		{
			_fontCache.getFont().getData().setScale(scaleX, scaleY);
			_lastScaleX = scaleX; _lastScaleY = scaleY;
			calcFrame();
		}

		// position
		_fontCache.setPosition(x, y);

		// rotation
		if(rotation != 0)
		{
			Matrix4 transform = FlxG.batch.getTransformMatrix();
			rotateMatrix(transform, rotation);
			FlxG.batch.setTransformMatrix(transform);
		}

		// blending
		if(blendMode != null)
		{
			int[] blendFunc = BlendMode.getOpenGLBlendMode(blendMode);
			FlxG.batch.setBlendFunction(blendFunc[0], blendFunc[1]);
		}

		// distance field
		if(_distanceFieldEnabled)
			drawDistanceField();
		
		// color
		int color = _currentFormat.color;
		_fontCache.setColors(((color >> 16) & 0xFF) * 0.00392f, ((color >> 8) & 0xFF) * 0.00392f, (color & 0xFF) * 0.00392f, alpha);
		_fontCache.draw(FlxG.batch);

		// turn off distance field
		if(_distanceFieldEnabled)
			FlxG.batch.setShader(null);

		// undo rotation
		if(rotation != 0)
		{
			Matrix4 transform = FlxG.batch.getTransformMatrix();
			rotateMatrix(transform, -rotation);
			FlxG.batch.setTransformMatrix(transform);
		}
	}

	private void rotateMatrix(Matrix4 matrix, float rotation)
	{
		Matrix4 rotationMatrix = FlxG.batch.getTransformMatrix();
		rotationMatrix.translate(x + (width / 2), y + (height / 2), 0);
		rotationMatrix.rotate(0, 0, 1, rotation);
		rotationMatrix.translate(-(x + (width / 2)), -(y + (height / 2)), 0);
	}
	
	/**
	 * Sets whether the font should render as distance field.
	 * 
	 * @param	Enabled		Whether the font should render as distance field or not.
	 * @param	Padding		The padding that is set to generate the bitmap font.
	 * @param	Smoothness	The smoothness between 0 and 1.
	 * @param	Name		The name of the shader.
	 * @param	Fragment	A custom fragment that will be used for creating the shader.
	 * @return	The shader program that will be used
	 */
	public ShaderProgram setDistanceField(boolean Enabled, int Padding, float Smoothness, String Name, String Fragment)
	{
		_distanceFieldEnabled = Enabled;
		if(!Enabled)
			return null;

		_padding = -Padding;
		_smoothness = Smoothness;

		_bitmapFontParameter.genMipMaps = true;
		_bitmapFontParameter.minFilter = TextureFilter.MipMapLinearNearest;
		_bitmapFontParameter.magFilter = TextureFilter.Linear;

		return _distanceFieldShader = FlxG.loadShader(Name, VERTEX, Fragment);
	}
	
	/**
	 * Sets whether the font should render as distance field.
	 * 
	 * @param	Enabled		Whether the font should render as distance field or not.
	 * @param	Padding		The padding that is set to generate the bitmap font.
	 * @param	Smoothness	The smoothness between 0 and 1.
	 * @param	Name		The name of the shader.
	 * @return	The shader program that will be used
	 */
	public ShaderProgram setDistanceField(boolean Enabled, int Padding, float Smoothness, String Name)
	{
		return setDistanceField(Enabled, Padding, Smoothness, Name, DISTANCE_FIELD_FRAGMENT);
	}

	/**
	 * Sets whether the font should render as distance field.
	 * 
	 * @param	Enabled		Whether the font should render as distance field or not.
	 * @param	Padding		The padding that is set to generate the bitmap font.
	 * @param	Smoothness	The smoothness between 0 and 1.
	 * @return	The shader program that will be used
	 */
	public ShaderProgram setDistanceField(boolean Enabled, int Padding, float Smoothness)
	{
		return setDistanceField(Enabled, Padding, Smoothness, _currentFormat.font, DISTANCE_FIELD_FRAGMENT);
	}

	/**
	 * Sets the distance field shader in the batch.
	 */
	private void drawDistanceField()
	{
		FlxG.batch.flush();

		_distanceFieldShader.begin();
		float delta = 0.5f * MathUtils.clamp(_smoothness / scaleX, 0, 1);
		_distanceFieldShader.setUniformf("u_lower", 0.5f - delta);
		_distanceFieldShader.setUniformf("u_upper", 0.5f + delta);
		_distanceFieldShader.end();
		FlxG.batch.setShader(_distanceFieldShader);

		_fontCache.setPosition(x, y + (scaleX * _padding));
	}
	
	private int flashAlignToGdx(String align)
	{
		if("right".equals(align))
			return Align.right;
		else if("center".equals(align))
			return Align.center;
		else
			return Align.left;
	}
}

package org.flixel;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;

import com.badlogic.gdx.utils.Align;
import flash.display.BlendMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Extends <code>FlxSprite</code> to support rendering text. Can tint, fade,
 * rotate and scale just like a sprite. Doesn't really animate though, as far as
 * I know. Also does nice pixel-perfect centering on pixel fonts as long as they
 * are only one liners.
 *
 * @author Ka Wing Chin
 * @author Thomas Weston
 */
public class FlxText extends FlxSprite {
	/**
	 * The default vertex shader that will be used.
	 */
	private final String VERTEX = "org/flixel/data/shaders/vertex.glsl";
	/**
	 * The default fragment shader that will be used for distance field.
	 */
	public final String DISTANCE_FIELD_FRAGMENT = "org/flixel/data/shaders/distance_field.glsl";
	/**
	 * Internal reference to a libgdx <code>BitmapFontCache</code> object.
	 */
	protected BitmapFontCache _textField;
	/**
	 * Internal tracker for the text shadow color, default is clear/transparent.
	 */
	protected int _shadow;
	/**
	 * Internal tracker for the alignment of the text.
	 */
	protected HAlignment _alignment;
	/**
	 * Internal reference to the text to be drawn.
	 */
	protected CharSequence _text;
	/**
	 * Internal helper for rotation.
	 */
	protected Matrix4 _matrix;
	/**
	 * Internal reference to the font.
	 */
	protected String _font;
	/**
	 * Internal tracker for the size of the text.
	 */
	protected int _size;

	/**
	 * Internal tracker for the x-position of the shadow, default is 1.
	 */
	protected float _shadowX;
	/**
	 * Internal tracker for the y-position of the shadow, default is 1.
	 */
	protected float _shadowY;

	/**
	 * Parameters that is used for generating the <code>BitmapFont</code>.
	 */
	private BitmapFontParameter _bitmapFontParameter;
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
	 * Creates a new <code>FlxText</code> object at the specified position.
	 *
	 * @param X            The X position of the text.
	 * @param Y            The Y position of the text.
	 * @param Width        The width of the text object (height is determined
	 *                     automatically).
	 * @param Text         The actual text you would like to display initially.
	 * @param EmbeddedFont Whether this text field uses embedded fonts or not.
	 */
	public FlxText(float X, float Y, int Width, String Text, boolean EmbeddedFont) {
		super(X, Y);

		if (Text == null)
			Text = "";

		width = frameWidth = Width;
		_text = Text;
		allowCollisions = NONE;
		_bitmapFontParameter = new BitmapFontParameter();
		_bitmapFontParameter.flip = true;
		setFormat("org/flixel/data/font/nokiafc22.ttf", 8, 0xFFFFFF, "left", 0, 1f, 1f);
	}

	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 *
	 * @param X     The X position of the text.
	 * @param Y     The Y position of the text.
	 * @param Width The width of the text object (height is determined
	 *              automatically).
	 * @param Text  The actual text you would like to display initially.
	 */
	public FlxText(float X, float Y, int Width, String Text) {
		this(X, Y, Width, Text, true);
	}

	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 *
	 * @param X     The X position of the text.
	 * @param Y     The Y position of the text.
	 * @param Width The width of the text object (height is determined
	 *              automatically).
	 */
	public FlxText(float X, float Y, int Width) {
		this(X, Y, Width, null, true);
	}

	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy() {
		_textField.clear();
		_textField = null;
		_text = null;
		_bitmapFontParameter = null;
		_distanceFieldShader = null;
		super.destroy();
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @param Font        The name of the font face for the text display.
	 * @param Size        The size of the font (in pixels essentially).
	 * @param Color       The color of the text in 0xRRGGBB format.
	 * @param Alignment   A string representing the desired alignment
	 *                    ("left,"right" or "center").
	 * @param ShadowColor An int representing the desired text shadow color
	 *                    0xAARRGGBB format.
	 * @param ShadowX     The x-position of the shadow, default is 1.
	 * @param ShadowY     The y-position of the shadow, default is 1.
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX, float ShadowY) {
		if (Font == null)
			Font = _font;

		if (!Font.equals(_font) || Size != _size) {
			try {
				_textField = new BitmapFontCache(FlxG.loadFont(Font, FlxU.round(Size), _bitmapFontParameter));
			} catch (Exception e) {
				FlxG.log(e.getMessage());
				_textField = new BitmapFontCache(FlxG.loadFont("org/flixel/data/font/nokiafc.fnt", 22, _bitmapFontParameter));
			}

			_font = Font;
			_size = FlxU.round(Size);
		}

		setColor(Color);
		if (Alignment != null) // GWT doesn't support Locale. May cause problems?
			_alignment = HAlignment.valueOf(Alignment.toUpperCase());// Locale.ENGLISH));
		_shadow = ShadowColor;
		_shadowX = ShadowX;
		_shadowY = ShadowY;

		calcFrame();

		return this;
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @param Font        The name of the font face for the text display.
	 * @param Size        The size of the font (in pixels essentially).
	 * @param Color       The color of the text in 0xRRGGBB format.
	 * @param Alignment   A string representing the desired alignment
	 *                    ("left,"right" or "center").
	 * @param ShadowColor An int representing the desired text shadow color in
	 *                    0xAARRGGBB format.
	 * @param ShadowX     The x-position of the shadow, default is 1.
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX) {
		return setFormat(Font, Size, Color, Alignment, ShadowColor, ShadowX, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @param Font        The name of the font face for the text display.
	 * @param Size        The size of the font (in pixels essentially).
	 * @param Color       The color of the text in 0xRRGGBB format.
	 * @param Alignment   A string representing the desired alignment
	 *                    ("left,"right" or "center").
	 * @param ShadowColor An int representing the desired text shadow color in
	 *                    0xAARRGGBB format.
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor) {
		return setFormat(Font, Size, Color, Alignment, ShadowColor, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @param Font      The name of the font face for the text display.
	 * @param Size      The size of the font (in pixels essentially).
	 * @param Color     The color of the text in 0xRRGGBB format.
	 * @param Alignment A string representing the desired alignment
	 *                  ("left,"right" or "center").
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat(String Font, float Size, int Color, String Alignment) {
		return setFormat(Font, Size, Color, Alignment, 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @param Font  The name of the font face for the text display.
	 * @param Size  The size of the font (in pixels essentially).
	 * @param Color The color of the text in 0xRRGGBB format.
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat(String Font, float Size, int Color) {
		return setFormat(Font, Size, Color, null, 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @param Font The name of the font face for the text display.
	 * @param Size The size of the font (in pixels essentially).
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat(String Font, float Size) {
		return setFormat(Font, Size, 0xFFFFFFFF, null, 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @param Font The name of the font face for the text display.
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat(String Font) {
		return setFormat(Font, 8, 0xFFFFFFFF, null, 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 *
	 * @return This FlxText instance (nice for chaining stuff together, if
	 * you're into that).
	 */
	public FlxText setFormat() {
		return setFormat(null, 8, 0xFFFFFFFF, null, 0, 1f, 1f);
	}

	/**
	 * The text being displayed.
	 */
	public String getText() {
		return _text.toString();
	}

	/**
	 * The text being displayed.
	 */
	public void setText(CharSequence Text) {
		_text = Text;
		calcFrame();
	}

	/**
	 * The size of the text being displayed.
	 */
	public float getSize() {
		return _size;
	}

	/**
	 * The size of the text being displayed.
	 */
	public void setSize(float Size) {
		if (Size == _size)
			return;

		setFormat(_font, Size, _color, getAlignment(), _shadow, _shadowX, _shadowY);
	}

	/**
	 * The font used for this text.
	 */
	public String getFont() {
		return _font;
	}

	/**
	 * The font used for this text.
	 */
	public void setFont(String Font, float Size) {
		if (Font == null || Font.equals(_font))
			return;

		setFormat(Font, Size, _color, getAlignment(), _shadow, _shadowX, _shadowY);
	}

	/**
	 * The font used for this text.
	 */
	public void setFont(String Font) {
		setFont(Font, _size);
	}

	/**
	 * The alignment of the font ("left", "right", or "center").
	 */
	public String getAlignment() {
		return _alignment.toString().toLowerCase();// Locale.ENGLISH);
	}

	/**
	 * The alignment of the font ("left", "right", or "center").
	 */
	public void setAlignment(String Alignment) {
		if (Alignment == null)
			return;
		_alignment = HAlignment.valueOf(Alignment.toUpperCase());// Locale.ENGLISH));
		calcFrame();
	}

	/**
	 * The color of the text shadow in 0xAARRGGBB hex format.
	 */
	public int getShadow() {
		return _shadow;
	}

	/**
	 * The color of the text shadow in 0xAARRGGBB hex format.
	 */
	public void setShadow(int Color) {
		_shadow = Color;
	}

	/**
	 * The position of the text shadow.
	 *
	 * @param ShadowX The x-position
	 * @param ShadowY The y-position
	 */
	public void setShadow(float ShadowX, float ShadowY) {
		_shadowX = ShadowX;
		_shadowY = ShadowY;
	}

	/**
	 * The x-position of the text shadow.
	 */
	public void setShadowX(float ShadowX) {
		_shadowX = ShadowX;
	}

	/**
	 * The y-position of the text shadow.
	 */
	public void setShadowY(float ShadowY) {
		_shadowY = ShadowY;
	}

	/**
	 * Sets whether the font should render as distance field.
	 *
	 * @param Enabled    Whether the font should render as distance field or not.
	 * @param Padding    The padding that is set to generate the bitmap font.
	 * @param Smoothness The smoothness between 0 and 1.
	 * @param Name       The name of the shader.
	 * @param Fragment   A custom fragment that will be used for creating the
	 *                   shader.
	 * @return The shader program that will be used
	 */
	public ShaderProgram setDistanceField(boolean Enabled, int Padding, float Smoothness, String Name, String Fragment) {
		_distanceFieldEnabled = Enabled;
		if (!Enabled)
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
	 * @param Enabled    Whether the font should render as distance field or not.
	 * @param Padding    The padding that is set to generate the bitmap font.
	 * @param Smoothness The smoothness between 0 and 1.
	 * @param Name       The name of the shader.
	 * @return The shader program that will be used
	 */
	public ShaderProgram setDistanceField(boolean Enabled, int Padding, float Smoothness, String Name) {
		return setDistanceField(Enabled, Padding, Smoothness, Name, DISTANCE_FIELD_FRAGMENT);
	}

	/**
	 * Sets whether the font should render as distance field.
	 *
	 * @param Enabled    Whether the font should render as distance field or not.
	 * @param Padding    The padding that is set to generate the bitmap font.
	 * @param Smoothness The smoothness between 0 and 1.
	 * @return The shader program that will be used
	 */
	public ShaderProgram setDistanceField(boolean Enabled, int Padding, float Smoothness) {
		return setDistanceField(Enabled, Padding, Smoothness, _font, DISTANCE_FIELD_FRAGMENT);
	}

	/**
	 * Sets the distance field shader in the batch.
	 */
	private void drawDistanceField() {
		FlxG.batch.flush();

		_distanceFieldShader.begin();
		float delta = 0.5f * MathUtils.clamp(_smoothness / scale.x, 0, 1);
		_distanceFieldShader.setUniformf("u_lower", 0.5f - delta);
		_distanceFieldShader.setUniformf("u_upper", 0.5f + delta);
		_distanceFieldShader.end();
		FlxG.batch.setShader(_distanceFieldShader);

		_point.y += scale.x * _padding;
		_textField.setPosition(_point.x, _point.y);
	}

	@Override
	protected void calcFrame() {
		GlyphLayout bounds = _textField.setText(_text, 2, 3, width, _alignment.gdxValue, true);
		// bounds.height is shorter than it should be.
		// After some trial and error, adding seven seems to make it about right
		// in most cases.
		height = frameHeight = (int) FlxU.ceil(bounds.height + 7);
	}

	@Override
	public void draw() {
		if (_flickerTimer != 0) {
			_flicker = !_flicker;
			if (_flicker)
				return;
		}

		FlxCamera camera = FlxG._activeCamera;

		if (cameras == null)
			cameras = FlxG.cameras;

		if (!cameras.contains(camera, true))
			return;

		if (!onScreen(camera))
			return;

		_point.x = x - (camera.scroll.x * scrollFactor.x) - offset.x;
		_point.y = y - (camera.scroll.y * scrollFactor.y) - offset.y;
		_point.x += (_point.x > 0) ? 0.0000001f : -0.0000001f;
		_point.y += (_point.y > 0) ? 0.0000001f : -0.0000001f;

		// scaling
		if (scale.x != 1f || scale.y != 1f) {
			_textField.getFont().getData().setScale(scale.x, scale.y);
			calcFrame();
		}

		// position
		_textField.setPosition(_point.x, _point.y);

		// rotation
		if (angle != 0) {
			_matrix = FlxG.batch.getTransformMatrix().cpy();

			Matrix4 rotationMatrix = FlxG.batch.getTransformMatrix();
			rotationMatrix.translate(_textField.getX() + (width / 2), _textField.getY() + (height / 2), 0);
			rotationMatrix.rotate(0, 0, 1, angle);
			rotationMatrix.translate(-(_textField.getX() + (width / 2)), -(_textField.getY() + (height / 2)), 0);

			FlxG.batch.setTransformMatrix(rotationMatrix);
		}

		// blending
		if (blend != null && currentBlend != blend) {
			int[] blendFunc = BlendMode.getOpenGLBlendMode(blend);
			FlxG.batch.setBlendFunction(blendFunc[0], blendFunc[1]);
		} else if (FlxG.batchShader == null || ignoreBatchShader) {
			// OpenGL ES 2.0 shader render
			renderShader();
			// OpenGL ES 2.0 blend mode render
			renderBlend();
		}

		// distance field
		if (_distanceFieldEnabled)
			drawDistanceField();

		// Render shadow behind the text
		if (_shadow != 0) {
			// tinting
			int tintColor = FlxU.multiplyColors(_shadow, camera.getColor());
			_textField.setColors(((tintColor >> 16) & 0xFF) * 0.00392f, ((tintColor >> 8) & 0xFF) * 0.00392f, (tintColor & 0xFF) * 0.00392f,
													 ((_shadow >> 24) & 0xFF) * _alpha
														 * 0.00392f);
			_textField.translate(_shadowX, _shadowY);
			_textField.draw(FlxG.batch);
			_textField.translate(-_shadowX, -_shadowY);
		}

		// tinting
		int tintColor = FlxU.multiplyColors(_color, camera.getColor());
		_textField
			.setColors(((tintColor >> 16) & 0xFF) * 0.00392f, ((tintColor >> 8) & 0xFF) * 0.00392f, (tintColor & 0xFF) * 0.00392f, _alpha);

		_textField.draw(FlxG.batch);

		// turn off distance field
		if (_distanceFieldEnabled)
			FlxG.batch.setShader(null);

		// rotation
		if (angle != 0)
			FlxG.batch.setTransformMatrix(_matrix);

		_VISIBLECOUNT++;

		if (FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug(camera);
	}


	private static Map<String, Integer> gdx2flixelAlign = new HashMap<String, Integer>() {
		{
			put("left", Align.left);
			put("right", Align.right);
			put("center", Align.center);
		}
	};

	public enum HAlignment {
		LEFT("left"), RIGHT("right"), CENTER("center");
		public final int gdxValue;

		HAlignment(String s) {
			gdxValue = gdx2flixelAlign.get(s);
		}
	}
}

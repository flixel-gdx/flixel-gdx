package org.flixel.ui;

/**
 * <code>FlxLabel</code> differs from <code>FlxText</code> with the bounding. It
 * automatically adjust the dimensions to the bounding of the text.
 * 
 * @author Ka Wing Chin
 */
public class FlxLabel extends FlxUIComponent
{
	private final String ImgLabelTopLeft = "org/flixel/data/pack:label_topleft";
	private final String ImgLabelTopCenter = "org/flixel/data/pack:label_topcenter";
	private final String ImgLabelTopRight = "org/flixel/data/pack:label_topright";
	private final String ImgLabelMiddleLeft = "org/flixel/data/pack:label_middleleft";
	private final String ImgLabelMiddleCenter = "org/flixel/data/pack:label_middlecenter";
	private final String ImgLabelMiddleRight = "org/flixel/data/pack:label_middleright";
	private final String ImgLabelBottomLeft = "org/flixel/data/pack:label_bottomleft";
	private final String ImgLabelBottomCenter = "org/flixel/data/pack:label_bottomcenter";
	private final String ImgLabelBottomRight = "org/flixel/data/pack:label_bottomright";

	/**
	 * Create a new <code>FlxLabel</code> object.
	 * 
	 * @param X The x-position.
	 * @param Y The y-position.
	 * @param Text The text of the label.
	 * @param Width The width of the label. Default 0 is auto.
	 * @param Height The height of the label. Default 0 is auto.
	 */
	public FlxLabel(float X, float Y, FlxUISkin Skin, String Text, int Width, int Height)
	{
		super(X, Y, Skin, Text, Width, Height);
		this.skin.labelPosition = FlxUISkin.LABEL_NONE;
		if(Width > 0)
			label.width = Width;
	}

	@Override
	public void setDefaultSkin()
	{
		skin = new FlxUISkin();
		skin.HIGHLIGHT = -1;
		skin.PRESSED = -1;
		skin.DISABLED = -1;
		skin.setFormat(null, 8, 0xFFFFFF, "center");
		skin.labelVerticalAlign = "middle";
		skin.setNinePatch(FlxNinePatch.TOP_LEFT, ImgLabelTopLeft, 4, 4);
		skin.setNinePatch(FlxNinePatch.TOP_CENTER, ImgLabelTopCenter, 1, 4);
		skin.setNinePatch(FlxNinePatch.TOP_RIGHT, ImgLabelTopRight, 4, 4);
		skin.setNinePatch(FlxNinePatch.MIDDLE_LEFT, ImgLabelMiddleLeft, 4, 1);
		skin.setNinePatch(FlxNinePatch.MIDDLE_CENTER, ImgLabelMiddleCenter, 1, 1);
		skin.setNinePatch(FlxNinePatch.MIDDLE_RIGHT, ImgLabelMiddleRight, 4, 1);
		skin.setNinePatch(FlxNinePatch.BOTTOM_LEFT, ImgLabelBottomLeft, 4, 4);
		skin.setNinePatch(FlxNinePatch.BOTTOM_CENTER, ImgLabelBottomCenter, 1, 4);
		skin.setNinePatch(FlxNinePatch.BOTTOM_RIGHT, ImgLabelBottomRight, 4, 4);
		skin.labelOffset.y = 0;
		skin.labelOffset.x = -2;
	}
}

package flash.text;

/**
 * The TextField class is used to create display objects for text display and input.
 * 
 * @author Thomas Weston
 */
public abstract class TextField 
{
	/**
	 * Specifies the format applied to newly inserted text, such as text entered by a user or text inserted with the replaceSelectedText() method.
	 */
	public TextFormat defaultTextFormat;
		
	/**
	 * Indicates the alpha transparency value of the object specified.
	 */
	public float alpha;
	/**
	 * A value from the BlendMode class that specifies which blend mode to use.
	 */
	public String blendMode;
	/**
	 * Indicates the height of the display object, in pixels.
	 */
	public float height;
	/**
	 * Indicates the rotation of the DisplayObject instance, in degrees, from its original orientation.
	 */
	public float rotation;
	/**
	 * Indicates the horizontal scale (percentage) of the object as applied from the registration point.
	 */
	public float scaleX;
	/**
	 * Indicates the vertical scale (percentage) of an object as applied from the registration point of the object.
	 */
	public float scaleY;
	/**
	 * Indicates the width of the display object, in pixels.
	 */
	public float width;
	/**
	 * Indicates the x coordinate of the DisplayObject instance relative to the local coordinates of the parent DisplayObjectContainer.
	 */
	public float x;
	/**
	 * Indicates the y coordinate of the DisplayObject instance relative to the local coordinates of the parent DisplayObjectContainer.
	 */
	public float y;
	
	/**
	 * A string that is the current text in the text field.
	 */
	public abstract void setText(String text);
	/**
	 * A string that is the current text in the text field.
	 */
	public abstract String getText();
	
	/**
	 * Applies the text formatting that the format parameter specifies to the specified text in a text field.
	 *
	 * @param	format	A TextFormat object that contains character and paragraph formatting information.
	 */
	public abstract void setTextFormat(TextFormat format);
}

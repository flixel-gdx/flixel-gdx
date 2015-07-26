package flash.text;

/**
 * The TextFormat class represents character formatting information. Use the TextFormat class to create specific text formatting for text fields.
 * 
 * @author Thomas Weston
 */
public final class TextFormat
{
	/**
	 * Indicates the alignment of the paragraph.
	 */
	public String align;
	/**
	 * Indicates the color of the text.
	 */
	public int color;
	/**
	 * The name of the font for text in this text format, as a string.
	 */
	public String font;
	/**
	 * The size in pixels of text in this text format.
	 */
	public float size;
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 * @param	color		The color of text using this text format. A number containing three 8-bit RGB components; for example, 0xFF0000 is red, and 0x00FF00 is green.
	 * @param	bold		A Boolean value that indicates whether the text is boldface.
	 * @param	italic		A Boolean value that indicates whether the text is italicized.
	 * @param	underline	A Boolean value that indicates whether the text is underlined.
	 * @param	url			The URL to which the text in this text format hyperlinks. If <code>url</code> is an empty string, the text does not have a hyperlink.
	 * @param	target		The target window where the hyperlink is displayed. If the target window is an empty string, the text is displayed in the default target window <code>_self</code>. If the <code>url</code> parameter is set to an empty string or to the value <code>null</code>, you can get or set this property, but the property will have no effect.
	 * @param	align		The alignment of the paragraph, as a TextFormatAlign value.
	 */
	public TextFormat(String font, float size, int color, boolean bold, boolean italic, boolean underline, String url, String target, String align) 
	{
		this.font = font;
		this.size = size;
		this.color = color;
		this.align = align;
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 * @param	color		The color of text using this text format. A number containing three 8-bit RGB components; for example, 0xFF0000 is red, and 0x00FF00 is green.
	 * @param	bold		A Boolean value that indicates whether the text is boldface.
	 * @param	italic		A Boolean value that indicates whether the text is italicized.
	 * @param	underline	A Boolean value that indicates whether the text is underlined.
	 * @param	url			The URL to which the text in this text format hyperlinks. If <code>url</code> is an empty string, the text does not have a hyperlink.
	 * @param	target		The target window where the hyperlink is displayed. If the target window is an empty string, the text is displayed in the default target window <code>_self</code>. If the <code>url</code> parameter is set to an empty string or to the value <code>null</code>, you can get or set this property, but the property will have no effect.
	 */
	public TextFormat(String font, float size, int color, boolean bold, boolean italic, boolean underline, String url, String target) 
	{
		this(font, size, color, bold, italic, underline, url, target, "left");
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 * @param	color		The color of text using this text format. A number containing three 8-bit RGB components; for example, 0xFF0000 is red, and 0x00FF00 is green.
	 * @param	bold		A Boolean value that indicates whether the text is boldface.
	 * @param	italic		A Boolean value that indicates whether the text is italicized.
	 * @param	underline	A Boolean value that indicates whether the text is underlined.
	 * @param	url			The URL to which the text in this text format hyperlinks. If <code>url</code> is an empty string, the text does not have a hyperlink.
	 */
	public TextFormat(String font, float size, int color, boolean bold, boolean italic, boolean underline, String url) 
	{
		this(font, size, color, bold, italic, underline, url, "", "left");
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 * @param	color		The color of text using this text format. A number containing three 8-bit RGB components; for example, 0xFF0000 is red, and 0x00FF00 is green.
	 * @param	bold		A Boolean value that indicates whether the text is boldface.
	 * @param	italic		A Boolean value that indicates whether the text is italicized.
	 * @param	underline	A Boolean value that indicates whether the text is underlined.
	 */
	public TextFormat(String font, float size, int color, boolean bold, boolean italic, boolean underline) 
	{
		this(font, size, color, bold, italic, underline, "", "", "left");
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 * @param	color		The color of text using this text format. A number containing three 8-bit RGB components; for example, 0xFF0000 is red, and 0x00FF00 is green.
	 * @param	bold		A Boolean value that indicates whether the text is boldface.
	 * @param	italic		A Boolean value that indicates whether the text is italicized.
	 */
	public TextFormat(String font, float size, int color, boolean bold, boolean italic) 
	{
		this(font, size, color, bold, italic, false, "", "", "left");
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 * @param	color		The color of text using this text format. A number containing three 8-bit RGB components; for example, 0xFF0000 is red, and 0x00FF00 is green.
	 * @param	bold		A Boolean value that indicates whether the text is boldface.
	 */
	public TextFormat(String font, float size, int color, boolean bold) 
	{
		this(font, size, color, bold, false, false, "", "", "left");
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 * @param	color		The color of text using this text format. A number containing three 8-bit RGB components; for example, 0xFF0000 is red, and 0x00FF00 is green.
	 */
	public TextFormat(String font, float size, int color) 
	{
		this(font, size, color, false, false, false, "", "", "left");
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 * @param	size		An integer that indicates the size in pixels.
	 */
	public TextFormat(String font, float size) 
	{
		this(font, size, 0x000000, false, false, false, "", "", "left");
	}
	
	/**
	 * Creates a TextFormat object with the specified properties. You can then change the properties of the TextFormat object to change the formatting of text fields.
	 * 
	 * @param	font		The name of a font for text as a string.
	 */
	public TextFormat(String font) 
	{
		this(font, 12, 0x000000, false, false, false, "", "", "left");
	}
}

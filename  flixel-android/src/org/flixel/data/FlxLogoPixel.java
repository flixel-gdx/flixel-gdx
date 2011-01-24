package org.flixel.data;

import org.flixel.FlxGroup;
import org.flixel.FlxSprite;

//@desc		This automates the color-rotation effect on the 'f' logo during game launch, not used in actual game code
public class FlxLogoPixel extends FlxGroup
{
	private FlxSprite[] _layers;
	private int _curLayer;

	public FlxLogoPixel(int xPos, int yPos, int pixelSize, int index, int finalColor, int Size)
	{
		super();

		// Build up the color layers
		_layers = new FlxSprite[6];
		int[] colors = { 0xACFFFFFF, finalColor, 0xACFFFFFF, finalColor, 0xACFFFFFF };
		//new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFF00FFFF };

		FlxSprite background = new FlxSprite(xPos, yPos);
		background.createGraphic(Size, Size, finalColor);
		add(background);
		_layers[0] = background;

		int l = colors.length;
		for(int i = 0; i < l; i++)
		{
			FlxSprite coloredBlock = new FlxSprite(xPos, yPos);
			coloredBlock.createGraphic(Size, Size, colors[index]);
			add(coloredBlock);
			_layers[i+1] = coloredBlock;

			if(++index >= l)
				index = 0;
		}
		_curLayer = _layers.length - 1;
	}

	@Override
	public void update()
	{
		if(_curLayer == 0)
			return;
		if(_layers[_curLayer].getAlpha() >= .1f)
			_layers[_curLayer].setAlpha(_layers[_curLayer].getAlpha() - .1f);
		else
		{
			_layers[_curLayer].setAlpha(0);
			_curLayer--;
		}
	}
}

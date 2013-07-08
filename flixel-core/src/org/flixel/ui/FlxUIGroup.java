package org.flixel.ui;

import org.flixel.FlxBasic;
import org.flixel.FlxGroup;
import org.flixel.FlxPoint;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxUIGroup extends FlxGroup
{
	/**
	 * VERTICAL or HORIZONTAL.
	 */
	public static final int ALIGN_VERTICAL = 0;
	public static final int ALIGN_HORIZONTAL = 1;	
	public int align = ALIGN_VERTICAL;
	
	public float marginTop = 48;
	public float marginLeft = 100;
	public float x = 0;
	public float y = 0;	
	public FlxLabel label;
	public FlxPoint labelOffset;
	
	public FlxUIGroup(float X, float Y, String Label)
	{
		super(0);
		x = X;
		y = Y;
		if(Label != null)
		{
			this.label = new FlxLabel(X, Y, Label, 0, 0);
			y += this.label.getSize();
		}
	}
	
	public FlxUIGroup(float X, float Y)
	{
		this(X, Y, null);
	}
	
	public FlxUIGroup(float X)
	{
		this(X, 0, null);
	}
	
	public FlxUIGroup()
	{
		this(0, 0, null);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		if(label != null)
		{
			label.destroy();
			label = null;
		}
		labelOffset = null;
	}
	
	@Override
	public void draw()
	{
		super.draw();
		if(label != null)
			label.draw();
	}
		
	public FlxBasic add(FlxUIComponent Object)
	{
		FlxUIComponent object;
		for(int i = 0; i < length; i++)
		{
			if(members.get(i) instanceof FlxUIComponent)
			{
				object = (FlxUIComponent) members.get(i);
				if(object.ID.equals(Object.ID))
					throw new Error("FlxUIGroup: " + Object + " got same ID " + Object.ID);				
			}

		}
		if(align == ALIGN_VERTICAL)
		{
			Object.x += this.x;
			Object.y = this.y + (marginTop * length) + Object.y;
		}
		else
		{
			Object.x = this.x + (marginLeft * length) + Object.x;
			Object.y += this.y; 
		}
		return super.add(Object);		
	}
	
	public FlxUIComponent getComponent(String ID)
	{
		FlxUIComponent object;
		for(int i = 0; i < length; i++)
		{
			object = (FlxUIComponent) members.get(i);
			if(object.ID.equals(ID))
				return object;
		}		
		return null;		
	}
}


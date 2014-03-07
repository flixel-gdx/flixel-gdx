package org.flixel.ui;

import org.flixel.FlxGroup;

/**
 * Add <code>FlxUIComponent</code> in the group to align it horizontal or
 * vertical. The component requires an ID.
 * 
 * @author Ka Wing Chin
 */
public class FlxUIGroup extends FlxGroup
{
	/**
	 * Align components vertically.
	 */
	public static final int ALIGN_VERTICAL = 0;
	/**
	 * Align the components horizontally.
	 */
	public static final int ALIGN_HORIZONTAL = 1;
	/**
	 * The alignment, default is vertical.
	 */
	public int align = ALIGN_VERTICAL;
	/**
	 * The top empty space from each component.
	 */
	public float marginTop = 48;
	/**
	 * The left empty space from each component.
	 */
	public float marginLeft = 100;
	/**
	 * Tracks the starter x-position of the group.
	 */
	public float x = 0;
	/**
	 * Tracks the starter y-position of the group.
	 */
	public float y = 0;
	/**
	 * The label for the group.
	 */
	public FlxTextExt label;

	/**
	 * Creates a new <code>FlxUIGroup</code> object.
	 * 
	 * @param X The x-position.
	 * @param Y The y-position.
	 * @param Label The label.
	 */
	public FlxUIGroup(float X, float Y, String Label)
	{
		super(0);
		x = X;
		y = Y;
		if(Label != null)
		{
			label = new FlxTextExt(X, Y, 0, 0, Label);
			y += label.height;
		}
	}

	/**
	 * Creates a new <code>FlxUIGroup</code> object.
	 * 
	 * @param X The x-position.
	 * @param Y The y-position.
	 */
	public FlxUIGroup(float X, float Y)
	{
		this(X, Y, null);
	}

	/**
	 * Creates a new <code>FlxUIGroup</code> object.
	 * 
	 * @param X The x-position.
	 */
	public FlxUIGroup(float X)
	{
		this(X, 0, null);
	}

	/**
	 * Creates a new <code>FlxUIGroup</code> object.
	 */
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
	}

	@Override
	public void draw()
	{
		super.draw();
		if(label != null)
			label.draw();
	}

	/**
	 * Adds a <code>FlxUIComponent</code> to the group.
	 * 
	 * @param Object The component.
	 * @return The same component that has been added.
	 */
	public FlxUIComponent add(FlxUIComponent Object)
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
		return (FlxUIComponent) super.add(Object);
	}

	/**
	 * Get the component by ID.
	 * 
	 * @param ID The ID.
	 * @return The component, null if it doesn't exists.
	 */
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

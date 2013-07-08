package org.flixel.ui;

import org.flixel.ui.event.IFlxRadioButtonGroup;

import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Ka Wing Chin
 */
public class FlxRadioButtonGroup
{
	/**
	 * Array of all the <code>FlxBasic</code>s that exist in this group.
	 */
	public Array<FlxRadioButton> members;
	private FlxRadioButton _current;
	public IFlxRadioButtonGroup onChange;
	/**
	 * The number of entries in the members array.
	 * For performance and safety you should check this variable
	 * instead of members.length unless you really know what you're doing!
	 */
	public int length;

	/**
	 * Internal tracker for the maximum capacity of the group.
	 * Default is 0, or no max capacity.
	 */
	protected int _maxSize;
	
	public FlxRadioButtonGroup(int MaxSize)
	{
		members = new Array<FlxRadioButton>(MaxSize);
		length = 0;
		_maxSize = MaxSize;
	}
	
	public FlxRadioButtonGroup()
	{
		this(0);
	}
	
	/**
	 * Adds a new <code>FlxBasic</code> subclass (FlxBasic, FlxSprite, Enemy, etc) to the group.
	 * FlxGroup will try to replace a null member of the array first.
	 * Failing that, FlxGroup will add it to the end of the member array,
	 * assuming there is room for it, and doubling the size of the array if necessary.
	 *
	 * <p>WARNING: If the group has a maxSize that has already been met,
	 * the object will NOT be added to the group!</p>
	 *
	 * @param	Object		The object you want to add to the group.
	 *
	 * @return	The same <code>FlxBasic</code> object that was passed in.
	 */
	public FlxRadioButton add(FlxRadioButton Object)
	{
		//Don't bother adding an object twice.
		if(members.indexOf(Object, true) >= 0)
			return Object;
		
		//First, look for a null entry where we can add the object.
		int i = 0;
		int l = members.size;
		while(i < l)
		{
			if(members.get(i) == null)
			{
				members.set(i, Object);
				if(i >= length)
					length = i+1;
				return Object;
			}
			i++;
		}
		
		//Failing that, expand the array (if we can) and add the object.
		if(_maxSize > 0)
		{
			if(members.size >= _maxSize)
				return Object;
		}

		//If we made it this far, then we successfully grew the group,
		//and we can go ahead and add the object at the first open slot.
		members.add(Object);
		length = i+1;
		return Object;
	}	
	
	protected void onChange(FlxRadioButton RadioButton)
	{
		// Break if it's already selected.
		if(_current == RadioButton)
		{
			RadioButton.setActive(true);
			return;
		}
		
		FlxRadioButton object;
		for(int i = 0; i < members.size; i++)
		{
			object = members.get(i);
			object.setActive(false);
		}
		RadioButton.setActive(true);
		_current = RadioButton;
		if(onChange != null)
			onChange.callback();
	}
	
	public void setCheck(int Index)
	{		
		if(Index < members.size)
			onChange((FlxRadioButton) members.get(Index));
	}
	
	public void setCheck(FlxRadioButton RadioButton)
	{
		onChange(RadioButton);
	}

	public void destroy()
	{		
		_current = null;
		if(members != null)
		{
			FlxRadioButton radioButton;
			int i = 0;
			while(i < members.size)
			{
				radioButton = members.pop();
				if(radioButton != null)
					radioButton.destroy();
				++i;
			}
			members = null;
		}
	}

	public String getSelected()
	{
		return _current.ID;
	}
	
	public String getSelectedLabel()
	{
		return _current.label.getText();
	}
}

package org.flixel;

import java.util.ArrayList;




/**
 * This is an organizational class that can update and render a bunch of <code>FlxObject</code>s.
 * NOTE: Although <code>FlxGroup</code> extends <code>FlxObject</code>, it will not automatically
 * add itself to the global collisions quad tree, it will only add its members.
 */
public class FlxGroup extends FlxObject
{

	static public final int ASCENDING = -1;
	static public final int DESCENDING = 1;
	
	/**
	 * Array of all the <code>FlxObject</code>s that exist in this layer.
	 */
	public ArrayList<FlxObject> members;
	/**
	 * Helpers for moving/updating group members.
	 */
	protected FlxPoint _last;
	protected boolean _first;
	/**
	 * Helpers for sorting members.
	 */
	protected String _sortIndex;
	protected int _sortOrder;
	
	
	
	public FlxGroup()
	{
		super();
		_group = true;
		setSolid(false);
		members = new ArrayList<FlxObject>();
		_last = new FlxPoint();
		_first = true;
	}
	
	/**
	 * Adds a new <code>FlxObject</code> subclass (FlxSprite, FlxBlock, etc) to the list of children
	 *
	 * @param	Object			The object you want to add
	 * @param	ShareScroll		Whether or not this FlxObject should sync up with this layer's scrollFactor
	 *
	 * @return	The same <code>FlxObject</code> object that was passed in.
	 */
	public FlxObject add(FlxObject Object, boolean ShareScroll)
	{			
		if(members.indexOf(Object) < 0) 
			members.add(Object);			
		if(ShareScroll)
			Object.scrollFactor = scrollFactor;
		return Object;
	}
	
	public FlxObject add(FlxObject Object)
	{
		return add(Object, false);
	}
	
	/**
	 * Replaces an existing <code>FlxObject</code> with a new one.
	 * 
	 * @param	OldObject	The object you want to replace.
	 * @param	NewObject	The new object you want to use instead.
	 * 
	 * @return	The new object.
	 */
	public FlxObject replace(FlxObject OldObject,FlxObject NewObject)
	{		
		int index = members.indexOf(OldObject);
		if((index < 0) || (index >= members.size()))
			return null;
		members.set(index, NewObject);
		return NewObject;
	}
	
	/**
	 * Removes an object from the group.
	 * 
	 * @param	Object	The <code>FlxObject</code> you want to remove.
	 * @param	Splice	Whether the object should be cut from the array entirely or not.
	 * 
	 * @return	The removed object.
	 */
	public FlxObject remove(FlxObject Object, boolean Splice)
	{		
		int index = members.indexOf(Object);
		if((index < 0) || (index >= members.size()))
			return null;
		
		if(Splice)
			members.remove(index);
		//if(Splice)
			//members.splice(index,1);
		else
			members.set(index, null);
		return Object;
	}
	
	public FlxObject remove(FlxObject Object)
	{
		return remove(Object, false);
	}
	
	/**
	 * Call this function to sort the group according to a particular value and order.
	 * For example, to sort game objects for Zelda-style overlaps you might call
	 * <code>myGroup.sort("y",ASCENDING)</code> at the bottom of your
	 * <code>FlxState.update()</code> override.  To sort all existing objects after
	 * a big explosion or bomb attack, you might call <code>myGroup.sort("exists",DESCENDING)</code>.
	 * 
	 * @param	Index	The <code>String</code> name of the member variable you want to sort on.  Default value is "y".
	 * @param	Order	A <code>FlxGroup</code> constant that defines the sort order.  Possible values are <code>ASCENDING</code> and <code>DESCENDING</code>.  Default value is <code>ASCENDING</code>.  
	 */
	public void sort(String Index, int Order)
	{
		_sortIndex = Index;
		_sortOrder = Order;
		//members.sort(sortHandler);	// TODO: sortHandler
	}
	
	public void sort()
	{
		sort("y", ASCENDING);
	}
	
	/**
	 * Call this function to retrieve the first object with exists == false in the group.
	 * This is handy for recycling in general, e.g. respawning enemies.
	 * 
	 * @return	A <code>FlxObject</code> currently flagged as not existing.
	 */
	public FlxObject getFirstAvail()
	{
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{			
			o = members.get(i++);
			if((o != null) && !o.exists)
				return o;
		}
		return null;
	}

	
	/**
	 * Call this function to retrieve the first index set to 'null'.
	 * Returns -1 if no index stores a null object.
	 * 
	 * @return	An <code>int</code> indicating the first null slot in the group.
	 */
	public int getFirstNull()
	{
		int i = 0;
		int ml = members.size();
		while(i < ml)
		{
			if(members.get(i) == null)
				return i;
			else
				i++;
		}
		return -1;
	}
	
	/**
	 * Finds the first object with exists == false and calls reset on it.
	 * 
	 * @param	X	The new X position of this object.
	 * @param	Y	The new Y position of this object.
	 * 
	 * @return	Whether a suitable <code>FlxObject</code> was found and reset.
	 */
	public boolean resetFirstAvail(float X, float Y)
	{
		FlxObject o = getFirstAvail();
		if(o == null)
			return false;
		o.reset(X,Y);
		return true;
	}
	
	public boolean resetFirstAvail(float X)
	{
		return resetFirstAvail(X, 0);
	}
	
	public boolean resetFirstAvail()
	{
		return resetFirstAvail(0, 0);
	}
	
	/**
	 * Call this function to retrieve the first object with exists == true in the group.
	 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
	 * 
	 * @return	A <code>FlxObject</code> currently flagged as existing.
	 */
	public FlxObject getFirstExtant()
	{
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if((o != null) && o.exists)
				return o;
		}
		return null;
	}
	
	
	/**
	 * Call this function to retrieve the first object with dead == false in the group.
	 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
	 * 
	 * @return	A <code>FlxObject</code> currently flagged as not dead.
	 */
	public FlxObject getFirstAlive()
	{
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if((o != null) && o.exists && !o.dead)
				return o;
		}
		return null;
	}
	
	/**
	 * Call this function to retrieve the first object with dead == true in the group.
	 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
	 * 
	 * @return	A <code>FlxObject</code> currently flagged as dead.
	 */
	public FlxObject getFirstDead()
	{
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if((o != null) && o.dead)
				return o;
		}
		return null;
	}
	
	/**
	 * Call this function to find out how many members of the group are not dead.
	 * 
	 * @return	The number of <code>FlxObject</code>s flagged as not dead.  Returns -1 if group is empty.
	 */
	public int countLiving()
	{
		int count = -1;
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if(o != null)
			{
				if(count < 0)
					count = 0;
				if(o.exists && !o.dead)
					count++;
			}
		}
		return count;
	}
	
	/**
	 * Call this function to find out how many members of the group are dead.
	 * 
	 * @return	The number of <code>FlxObject</code>s flagged as dead.  Returns -1 if group is empty.
	 */
	public int countDead()
	{
		int count = -1;
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if(o != null)
			{
				if(count < 0)
					count = 0;
				if(o.dead)
					count++;
			}
		}
		return count;
	}
	
	
	/**
	 * Returns a count of how many objects in this group are on-screen right now.
	 * 
	 * @return	The number of <code>FlxObject</code>s that are on screen.  Returns -1 if group is empty.
	 */
	public int countOnScreen()
	{
		int count = -1;
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if(o != null)
			{
				if(count < 0)
					count = 0;
				if(o.onScreen())
					count++;
			}
		}
		return count;
	}		
	
	/**
	 * Returns a member at random from the group.
	 * 
	 * @return	A <code>FlxObject</code> from the members list.
	 */
	public FlxObject getRandom()
	{
		int c = 0;
		FlxObject o = null;
		int l = members.size();
		int i = (int) (FlxU.random()*l);
		while((o == null) && (c < members.size()))
		{			
			o = members.get((++i)%1);
			c++;
		}
		return o;
	}
	
	/**
	 * Internal function, helps with the moving/updating of group members.
	 */
	protected void saveOldPosition()
	{
		if(_first)
		{
			_first = false;
			_last.x = 0;
			_last.y = 0;
			return;
		}
		_last.x = x;
		_last.y = y;
	}
	
	/**
	 * Internal function that actually goes through and updates all the group members.
	 * Depends on <code>saveOldPosition()</code> to set up the correct values in <code>_last</code> in order to work properly.
	 */
	protected void updateMembers()
	{
		float mx = 0;
		float my = 0;
		boolean moved = false;
		if((x != _last.x) || (y != _last.y))
		{
			moved = true;
			mx = x - _last.x;
			my = y - _last.y;
		}
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if((o != null) && o.exists)
			{
				if(moved)
				{
					if(o._group)
						o.reset(o.x+mx,o.y+my);
					else
					{
						o.x += mx;
						o.y += my;
					}
				}
				if(o.active)
					o.update();
				
				if(moved && o.getSolid())
				{
					o.colHullX.width += ((mx>0)?mx:-mx);
					if(mx < 0)
						o.colHullX.x += mx;
					o.colHullY.x = x;
					o.colHullY.height += ((my>0)?my:-my);
					if(my < 0)
						o.colHullY.y += my;
					o.colVector.x += mx;
					o.colVector.y += my;
				}
			}
		}
	}
	
	
	/**
	 * Automatically goes through and calls update on everything you added,
	 * override this function to handle custom input and perform collisions.
	 */
	@Override
	public void update()
	{
		saveOldPosition();
		updateMotion();
		updateMembers();
		updateFlickering();		
	}

	
	/**
	 * Internal function that actually loops through and renders all the group members.
	 */
	protected void renderMembers()
	{
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if((o != null) && o.exists && o.visible)
				o.render();
		}
	}
	
	/**
	 * Automatically goes through and calls render on everything you added,
	 * override this loop to control render order manually.
	 */
	@Override
	public void render()
	{
		renderMembers();		
	}
	
	
	/**
	 * Internal function that calls kill on all members.
	 */
	protected void killMembers()
	{
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if(o != null)
				o.kill();
		}
	}
	
	/**
	 * Calls kill on the group and all its members.
	 */
	@Override
	public void kill()
	{
		killMembers();
		super.kill();
	}
	
	/**
	 * Internal function that actually loops through and destroys each member.
	 */
	protected void destroyMembers()
	{
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if(o != null)
				o.destroy();
			//members.remove(o); // This has been added.
		}
		//members.length = 0;
		members = new ArrayList<FlxObject>();
	}
	
	/**
	 * Override this function to handle any deleting or "shutdown" type operations you might need,
	 * such as removing traditional Flash children like Sprite objects.
	 */
	@Override
	public void destroy() 
	{
		destroyMembers();
		super.destroy();
	}
	
	
	/**
	 * If the group's position is reset, we want to reset all its members too.
	 * 
	 * @param	X	The new X position of this object.
	 * @param	Y	The new Y position of this object.
	 */
	@Override
	public void reset(float X, float Y)
	{
		saveOldPosition();
		super.reset(X,Y);
		float mx = 0;
		float my = 0;
		boolean moved = false;
		if((x != _last.x) || (y != _last.y))
		{
			moved = true;
			mx = x - _last.x;
			my = y - _last.y;
		}
		int i = 0;
		FlxObject o;
		int ml = members.size();
		while(i < ml)
		{
			o = members.get(i++);
			if((o != null) && o.exists)
			{
				if(moved)
				{
					if(o._group)
						o.reset(o.x+mx,o.y+my);
					else
					{
						o.x += mx;
						o.y += my;
						if(getSolid())
						{
							o.colHullX.width += ((mx>0)?mx:-mx);
							if(mx < 0)
								o.colHullX.x += mx;
							o.colHullY.x = x;
							o.colHullY.height += ((my>0)?my:-my);
							if(my < 0)
								o.colHullY.y += my;
							o.colVector.x += mx;
							o.colVector.y += my;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Helper function for the sort process.
	 * 
	 * @param 	Obj1	The first object being sorted.
	 * @param	Obj2	The second object being sorted.
	 * 
	 * @return	An integer value: -1 (Obj1 before Obj2), 0 (same), or 1 (Obj1 after Obj2).
	 */
	protected int sortHandler(FlxObject Obj1, FlxObject Obj2)
	{
		// TODO: sortHandler
		/*if(Obj1[_sortIndex] < Obj2[_sortIndex])
			return _sortOrder;
		else if(Obj1[_sortIndex] > Obj2[_sortIndex])
			return -_sortOrder;*/
		return 0;
	}
	
	/*int getIndex(FlxObject[] array, FlxObject Object)
	{
		int l = array.length;
		
		for(int i = 0; i < l; i++)
		{
			if(members[i].equals(Object))
				return i;
		}
		return -1;
	}*/
	
	/*FlxObject[] splice(FlxObject[] array, int startIndex, int endIndex)
	{
		int l = array.length;		
		for(int i = endIndex; i < l; i++)
		{
			Arrays.asList(array).remove(startIndex);	
		}		
		return array;
	}*/
	

}

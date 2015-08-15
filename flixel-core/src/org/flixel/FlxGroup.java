package org.flixel;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * This is an organizational class that can update and render a bunch of <code>FlxBasic</code>s.
 * NOTE: Although <code>FlxGroup</code> extends <code>FlxBasic</code>, it will not automatically
 * add itself to the global collisions quad tree, it will only add its members.
 * 
 * @author Ka Wing Chin
 * @author Thomas Weston
 */
public class FlxGroup extends FlxBasic
{
	/**
	 * Use with <code>sort()</code> to sort in ascending order.
	 */
	static public final int ASCENDING = -1;
	/**
	 * Use with <code>sort()</code> to sort in descending order.
	 */
	static public final int DESCENDING = 1;

	/**
	 * Array of all the <code>FlxBasic</code>s that exist in this group.
	 */
	public Array<FlxBasic> members;
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
	/**
	 * Internal helper variable for recycling objects a la <code>FlxEmitter</code>.
	 */
	protected int _marker;

	/**
	 * Helper for sort.
	 */
	protected String _sortIndex;
	/**
	 * Helper for sort.
	 */
	protected int _sortOrder;

	/**
	 * Constructor
	 */
	public FlxGroup(int MaxSize)
	{
		super();
		members = new Array<FlxBasic>(MaxSize);
		length = 0;
		_maxSize = MaxSize;
		_marker = 0;
		_sortIndex = null;
	}

	/**
	 * Constructor
	 */
	public FlxGroup()
	{
		this(0);
	}

	/**
	 * Override this function to handle any deleting or "shutdown" type operations you might need,
	 * such as removing traditional Flash children like Sprite objects.
	 */
	@Override
	public void destroy()
	{
		if(members != null)
		{
			FlxBasic basic;
			int i = 0;
			while(i < length)
			{
				basic = members.get(i++);
				if(basic != null)
					basic.destroy();
			}
			members.clear();
			members = null;
		}
		_sortIndex = null;
	}

	/**
	 * Just making sure we don't increment the active objects count.
	 */
	@Override
	public void preUpdate()
	{
	}

	/**
	 * Automatically goes through and calls update on everything you added.
	 */
	@Override
	public void update()
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if((basic != null) && basic.exists && basic.active)
			{
				basic.preUpdate();
				basic.update();
				basic.postUpdate();
			}
		}
	}

	/**
	 * Automatically goes through and calls render on everything you added.
	 */
	@Override
	public void draw()
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if((basic != null) && basic.exists && basic.visible)
				basic.draw();
		}
	}

	/**
	 * The maximum capacity of this group.  Default is 0, meaning no max capacity, and the group can just grow.
	 */
	public int getMaxSize()
	{
		return _maxSize;
	}

	/**
	 * The maximum capacity of this group.  Default is 0, meaning no max capacity, and the group can just grow.
	 */
	public void setMaxSize(int Size)
	{
		_maxSize = Size;
		if(_marker >= _maxSize)
			_marker = 0;
		if((_maxSize == 0) || (members == null) || (_maxSize >= members.size))
			return;

		//If the max size has shrunk, we need to get rid of some objects
		FlxBasic basic;
		int i = _maxSize;
		int l = members.size;
		while(i < l)
		{
			basic = members.get(i++);
			if(basic != null)
				basic.destroy();
		}
		members.truncate(length = _maxSize);
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
	public FlxBasic add(FlxBasic Object)
	{
		if(Object == null)
		{
			FlxG.log("WARNING: Cannot add 'null' object to a FlxGroup.");
			return null;
		}

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

	/**
	 * Recycling is designed to help you reuse game objects without always re-allocating or "newing" them.
	 * 
	 * <p>If you specified a maximum size for this group (like in FlxEmitter),
	 * then recycle will employ what we're calling "rotating" recycling.
	 * Recycle() will first check to see if the group is at capacity yet.
	 * If group is not yet at capacity, recycle() returns a new object.
	 * If the group IS at capacity, then recycle() just returns the next object in line.</p>
	 * 
	 * <p>If you did NOT specify a maximum size for this group,
	 * then recycle() will employ what we're calling "grow-style" recycling.
	 * Recycle() will return either the first object with exists == false,
	 * or, finding none, add a new object to the array,
	 * doubling the size of the array if necessary.</p>
	 * 
	 * <p>WARNING: If this function needs to create a new object,
	 * and no object class was provided, it will return null
	 * instead of a valid object!</p>
	 * 
	 * @param	ObjectClass		The class type you want to recycle (e.g. FlxSprite, EvilRobot, etc). Do NOT "new" the class in the parameter!
	 * 
	 * @return	A reference to the object that was created.  Don't forget to cast it back to the Class you want (e.g. myObject = (myObjectClass) myGroup.recycle(myObjectClass);).
	 */
	public FlxBasic recycle(Class<? extends FlxBasic> ObjectClass)
	{
		FlxBasic basic;
		if(_maxSize > 0)
		{
			if(length < _maxSize)
			{
				if(ObjectClass == null)
					return null;
				try
				{
					return add(ClassReflection.newInstance(ObjectClass));
				}
				catch(Exception e)
				{
					throw new RuntimeException(e);
				}
			}
			else
			{
				basic = members.get(_marker++);
				if(_marker >= _maxSize)
					_marker = 0;
				return basic;
			}
		}
		else
		{
			basic = getFirstAvailable(ObjectClass);
			if(basic != null)
				return basic;
			if(ObjectClass == null)
				return null;
			try
			{
				return add(ClassReflection.newInstance(ObjectClass));
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Recycling is designed to help you reuse game objects without always re-allocating or "newing" them.
	 * 
	 * <p>If you specified a maximum size for this group (like in FlxEmitter),
	 * then recycle will employ what we're calling "rotating" recycling.
	 * Recycle() will first check to see if the group is at capacity yet.
	 * If group is not yet at capacity, recycle() returns a new object.
	 * If the group IS at capacity, then recycle() just returns the next object in line.</p>
	 * 
	 * <p>If you did NOT specify a maximum size for this group,
	 * then recycle() will employ what we're calling "grow-style" recycling.
	 * Recycle() will return either the first object with exists == false,
	 * or, finding none, add a new object to the array,
	 * doubling the size of the array if necessary.</p>
	 * 
	 * <p>WARNING: If this function needs to create a new object,
	 * and no object class was provided, it will return null
	 * instead of a valid object!</p>
	 * 
	 * @param	ObjectClass		The class type you want to recycle (e.g. FlxSprite, EvilRobot, etc). Do NOT "new" the class in the parameter!
	 * 
	 * @return	A reference to the object that was created.  Don't forget to cast it back to the Class you want (e.g. myObject = (myObjectClass) myGroup.recycle(myObjectClass);).
	 */
	public FlxBasic recycle()
	{
		return recycle(null);
	}

	/**
	 * Removes an object from the group.
	 * 
	 * @param	Object	The <code>FlxBasic</code> you want to remove.
	 * @param	Splice	Whether the object should be cut from the array entirely or not.
	 * 
	 * @return	The removed object.
	 */
	public FlxBasic remove(FlxBasic Object,boolean Splice)
	{
		int index = members.indexOf(Object, true);
		if((index < 0) || (index >= members.size))
			return null;
		if(Splice)
		{
			members.removeIndex(index);
			length--;
		}
		else
			members.set(index, null);
		return Object;
	}

	/**
	 * Removes an object from the group.
	 * 
	 * @param	Object	The <code>FlxBasic</code> you want to remove.
	 * 
	 * @return	The removed object.
	 */
	public FlxBasic remove(FlxBasic Object)
	{
		return remove(Object,false);
	}

	/**
	 * Replaces an existing <code>FlxBasic</code> with a new one.
	 * 
	 * @param	OldObject	The object you want to replace.
	 * @param	NewObject	The new object you want to use instead.
	 * 
	 * @return	The new object.
	 */
	public FlxBasic replace(FlxBasic OldObject,FlxBasic NewObject)
	{
		int index = members.indexOf(OldObject, true);
		if((index < 0) || (index >= members.size))
			return null;
		members.set(index, NewObject);
		return NewObject;
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
	public void sort(String Index,int Order)
	{
		_sortIndex = Index;
		_sortOrder = Order;
		members.sort(sortHandler);
	}

	/**
	 * Call this function to sort the group according to a particular value and order.
	 * For example, to sort game objects for Zelda-style overlaps you might call
	 * <code>myGroup.sort("y",ASCENDING)</code> at the bottom of your
	 * <code>FlxState.update()</code> override.  To sort all existing objects after
	 * a big explosion or bomb attack, you might call <code>myGroup.sort("exists",DESCENDING)</code>.
	 * 
	 * @param	Index	The <code>String</code> name of the member variable you want to sort on.  Default value is "y".
	 */
	public void sort(String Index)
	{
		sort(Index,ASCENDING);
	}

	/**
	 * Call this function to sort the group according to a particular value and order.
	 * For example, to sort game objects for Zelda-style overlaps you might call
	 * <code>myGroup.sort("y",ASCENDING)</code> at the bottom of your
	 * <code>FlxState.update()</code> override.  To sort all existing objects after
	 * a big explosion or bomb attack, you might call <code>myGroup.sort("exists",DESCENDING)</code>.
	 * 
	 */
	public void sort()
	{
		sort("y",ASCENDING);
	}

	/**
	 * Go through and set the specified variable to the specified value on all members of the group.
	 * 
	 * @param	VariableName	The string representation of the variable name you want to modify, for example "visible" or "scrollFactor".
	 * @param	Value			The value you want to assign to that variable.
	 * @param	Recurse			Default value is true, meaning if <code>setAll()</code> encounters a member that is a group, it will call <code>setAll()</code> on that group rather than modifying its variable.
	 */
	public void setAll(String VariableName,Object Value,boolean Recurse)
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if(basic != null)
			{
				if(Recurse && (basic instanceof FlxGroup))
					((FlxGroup) basic).setAll(VariableName,Value,Recurse);
				else
				{
					try
					{
						ClassReflection.getField(basic.getClass(), VariableName).set(basic, Value);

					}
					catch(Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * Go through and set the specified variable to the specified value on all members of the group.
	 * 
	 * @param	VariableName	The string representation of the variable name you want to modify, for example "visible" or "scrollFactor".
	 * @param	Value			The value you want to assign to that variable.
	 */
	public void setAll(String VariableName,Object Value)
	{
		setAll(VariableName,Value,true);
	}

	/**
	 * Go through and call the specified function on all members of the group.
	 * Currently only works on functions that have no required parameters.
	 * 
	 * @param	FunctionName	The string representation of the function you want to call on each object, for example "kill()" or "init()".
	 * @param	Recurse			Default value is true, meaning if <code>callAll()</code> encounters a member that is a group, it will call <code>callAll()</code> on that group rather than calling the group's function.
	 */
	public void callAll(String FunctionName,boolean Recurse)
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if(basic != null)
			{
				if(Recurse && (basic instanceof FlxGroup))
					((FlxGroup) basic).callAll(FunctionName, Recurse);
				else
				{
					try
					{
						ClassReflection.getMethod(basic.getClass(), FunctionName).invoke(basic);
					}
					catch(Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/**
	 * Go through and call the specified function on all members of the group.
	 * Currently only works on functions that have no required parameters.
	 * 
	 * @param	FunctionName	The string representation of the function you want to call on each object, for example "kill()" or "init()".
	 */
	public void callAll(String FunctionName)
	{
		callAll(FunctionName,true);
	}

	/**
	 * Call this function to retrieve the first object with exists == false in the group.
	 * This is handy for recycling in general, e.g. respawning enemies.
	 * 
	 * @param	ObjectClass		An optional parameter that lets you narrow the results to instances of this particular class.
	 * 
	 * @return	A <code>FlxBasic</code> currently flagged as not existing.
	 */
	public FlxBasic getFirstAvailable(Class<? extends FlxBasic> ObjectClass)
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if((basic != null) && !basic.exists && ((ObjectClass == null) || (ClassReflection.isInstance(ObjectClass, basic))))
				return basic;
		}
		return null;
	}

	/**
	 * Call this function to retrieve the first object with exists == false in the group.
	 * This is handy for recycling in general, e.g. respawning enemies.
	 * 
	 * @return	A <code>FlxBasic</code> currently flagged as not existing.
	 */
	public FlxBasic getFirstAvailable()
	{
		return getFirstAvailable(null);
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
		int l = members.size;
		while(i < l)
		{
			if(members.get(i) == null)
				return i;
			else
				i++;
		}
		return -1;
	}

	/**
	 * Call this function to retrieve the first object with exists == true in the group.
	 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
	 * 
	 * @return	A <code>FlxBasic</code> currently flagged as existing.
	 */
	public FlxBasic getFirstExtant()
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if((basic != null) && basic.exists)
				return basic;
		}
		return null;
	}

	/**
	 * Call this function to retrieve the first object with alive == true in the group.
	 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
	 * 
	 * @return	A <code>FlxBasic</code> currently flagged as not dead.
	 */
	public FlxBasic getFirstAlive()
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if((basic != null) && basic.exists && basic.alive)
				return basic;
		}
		return null;
	}

	/**
	 * Call this function to retrieve the first object with alive == false in the group.
	 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
	 * 
	 * @return	A <code>FlxBasic</code> currently flagged as dead.
	 */
	public FlxBasic getFirstDead()
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if((basic != null) && !basic.alive)
				return basic;
		}
		return null;
	}

	/**
	 * Call this function to find out how many members of the group are not dead.
	 * 
	 * @return	The number of <code>FlxBasic</code>s flagged as not dead.  Returns -1 if group is empty.
	 */
	public int countLiving()
	{
		int count = -1;
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if(basic != null)
			{
				if(count < 0)
					count = 0;
				if(basic.exists && basic.alive)
					count++;
			}
		}
		return count;
	}

	/**
	 * Call this function to find out how many members of the group are dead.
	 * 
	 * @return	The number of <code>FlxBasic</code>s flagged as dead.  Returns -1 if group is empty.
	 */
	public int countDead()
	{
		int count = -1;
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if(basic != null)
			{
				if(count < 0)
					count = 0;
				if(!basic.alive)
					count++;
			}
		}
		return count;
	}

	/**
	 * Returns a member at random from the group.
	 * 
	 * @param	StartIndex	Optional offset off the front of the array. Default value is 0, or the beginning of the array.
	 * @param	Length		Optional restriction on the number of values you want to randomly select from.
	 * 
	 * @return	A <code>FlxBasic</code> from the members list.
	 */
	public FlxBasic getRandom(int StartIndex,int Length)
	{
		if(Length == 0)
			Length = length;
		return FlxU.getRandom(members,StartIndex,Length);
	}

	/**
	 * Returns a member at random from the group.
	 * 
	 * @param	StartIndex	Optional offset off the front of the array. Default value is 0, or the beginning of the array.
	 * 
	 * @return	A <code>FlxBasic</code> from the members list.
	 */
	public FlxBasic getRandom(int StartIndex)
	{
		return getRandom(StartIndex,0);
	}

	/**
	 * Returns a member at random from the group.
	 * 
	 * @return	A <code>FlxBasic</code> from the members list.
	 */
	public FlxBasic getRandom()
	{
		return getRandom(0,0);
	}

	/**
	 * Remove all instances of <code>FlxBasic</code> subclass (FlxSprite, FlxBlock, etc) from the list.
	 * WARNING: does not destroy() or kill() any of these objects!
	 */
	public void clear()
	{
		length = 0;
		members.clear();
	}

	/**
	 * Calls kill on the group's members and then on the group itself.
	 */
	@Override
	public void kill()
	{
		FlxBasic basic;
		int i = 0;
		while(i < length)
		{
			basic = members.get(i++);
			if((basic != null) && basic.exists)
				basic.kill();
		}
		super.kill();
	}

	/**
	 * Helper function for the sort process.
	 * 
	 * @param	Obj1	The first object being sorted.
	 * @param	Obj2	The second object being sorted.
	 * 
	 * @return	An integer value: -1 (Obj1 before Obj2), 0 (same), or 1 (Obj1 after Obj2).
	 */
	// TODO: sortHandler only works with floats
	protected Comparator<FlxBasic> sortHandler = new Comparator<FlxBasic>()
	{
		@Override
		public int compare(FlxBasic Obj1,FlxBasic Obj2)
		{
			try
			{
				if((Float) ClassReflection.getField(Obj1.getClass(), _sortIndex).get(Obj1) < (Float) ClassReflection.getField(Obj2.getClass(), _sortIndex).get(Obj2))
					return _sortOrder;
				else if((Float) ClassReflection.getField(Obj1.getClass(), _sortIndex).get(Obj1) > (Float) ClassReflection.getField(Obj2.getClass(), _sortIndex).get(Obj2))
					return -_sortOrder;
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
			return 0;
		}
	};
}

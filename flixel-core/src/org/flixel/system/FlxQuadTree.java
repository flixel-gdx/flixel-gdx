package org.flixel.system;

import org.flixel.FlxBasic;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxRect;
import org.flixel.event.IFlxCollision;
import org.flixel.event.IFlxObject;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * A fairly generic quad tree structure for rapid overlap checks.
 * FlxQuadTree is also configured for single or dual list operation.
 * You can add items either to its A list or its B list.
 * When you do an overlap check, you can compare the A list to itself,
 * or the A list against the B list. Handy for different things!
 * 
 * @author Ka Wing Chin
 */
public class FlxQuadTree extends FlxRect
{
	/**
	 * Flag for specifying that you want to add an object to the A list.
	 */
	static public final int A_LIST = 0;
	/**
	 * Flag for specifying that you want to add an object to the B list.
	 */
	static public final int B_LIST = 1;

	/**
	 * Controls the granularity of the quad tree.  Default is 6 (decent performance on large and small worlds).
	 */
	static public int divisions;

	/**
	 * Whether this branch of the tree can be subdivided or not.
	 */
	protected boolean _canSubdivide;

	/**
	 * Refers to the internal A and B linked lists,
	 * which are used to store objects in the leaves.
	 */
	protected FlxList _headA;
	/**
	 * Refers to the internal A and B linked lists,
	 * which are used to store objects in the leaves.
	 */
	protected FlxList _tailA;
	/**
	 * Refers to the internal A and B linked lists,
	 * which are used to store objects in the leaves.
	 */
	protected FlxList _headB;
	/**
	 * Refers to the internal A and B linked lists,
	 * which are used to store objects in the leaves.
	 */
	protected FlxList _tailB;

	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	static protected int _min;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected FlxQuadTree _northWestTree;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected FlxQuadTree _northEastTree;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected FlxQuadTree _southEastTree;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected FlxQuadTree _southWestTree;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _leftEdge;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _rightEdge;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _topEdge;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _bottomEdge;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _halfWidth;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _halfHeight;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _midpointX;
	/**
	 * Internal, governs and assists with the formation of the tree.
	 */
	protected float _midpointY;

	/**
	 * Internal, used to reduce recursive method parameters during object placement and tree formation.
	 */
	static protected FlxObject _object;
	/**
	 * Internal, used to reduce recursive method parameters during object placement and tree formation.
	 */
	static protected float _objectLeftEdge;
	/**
	 * Internal, used to reduce recursive method parameters during object placement and tree formation.
	 */
	static protected float _objectTopEdge;
	/**
	 * Internal, used to reduce recursive method parameters during object placement and tree formation.
	 */
	static protected float _objectRightEdge;
	/**
	 * Internal, used to reduce recursive method parameters during object placement and tree formation.
	 */
	static protected float _objectBottomEdge;

	/**
	 * Internal, used during tree processing and overlap checks.
	 */
	static protected int _list;
	/**
	 * Internal, used during tree processing and overlap checks.
	 */
	static protected boolean _useBothLists;
	/**
	 * Internal, used during tree processing and overlap checks.
	 */
	static protected IFlxObject _processingCallback;
	/**
	 * Internal, used during tree processing and overlap checks.
	 */
	static protected IFlxCollision _notifyCallback;
	/**
	 * Internal, used during tree processing and overlap checks.
	 */
	static protected FlxList _iterator;

	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _objectHullX;
	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _objectHullY;
	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _objectHullWidth;
	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _objectHullHeight;

	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _checkObjectHullX;
	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _checkObjectHullY;
	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _checkObjectHullWidth;
	/**
	 * Internal, helpers for comparing actual object-to-object overlap - see <code>overlapNode()</code>.
	 */
	static protected float _checkObjectHullHeight;

	/**
	 * Internal, a pool of <code>FlxQuadTree</code>s to prevent constant <code>new</code> calls.
	 */
	static private Pool<FlxQuadTree> _pool = new Pool<FlxQuadTree>()
	{
		@Override
		protected FlxQuadTree newObject()
		{
			return new FlxQuadTree();
		}
	};

	/**
	 * Get a new Quad Tree node from the pool.
	 * 
	 * @param	X			The X-coordinate of the point in space.
	 * @param	Y			The Y-coordinate of the point in space.
	 * @param	Width		Desired width of this node.
	 * @param	Height		Desired height of this node.
	 * @param	Parent		The parent branch or node. Pass null to create a root.
	 * 
	 * @return	A new <code>FlxQuadTree</code>.
	 */
	static public FlxQuadTree getNew(float X, float Y, float Width, float Height, FlxQuadTree Parent)
	{
		FlxQuadTree quadTree = _pool.obtain();
		quadTree.init(X,Y,Width,Height,Parent);
		return quadTree;
	}

	/**
	 * Has to be public for GWT reflection.
	 */
	public FlxQuadTree()
	{

	}

	/**
	 * Instantiate a new Quad Tree node.
	 * 
	 * @param	X			The X-coordinate of the point in space.
	 * @param	Y			The Y-coordinate of the point in space.
	 * @param	Width		Desired width of this node.
	 * @param	Height		Desired height of this node.
	 * @param	Parent		The parent branch or node. Pass null to create a root.
	 */
	protected void init(float X, float Y, float Width, float Height, FlxQuadTree Parent)
	{
		make(X,Y,Width,Height);
		_headA = _tailA = FlxList.getNew();
		_headB = _tailB = FlxList.getNew();

		//Copy the parent's children (if there are any)
		if(Parent != null)
		{
			FlxList iterator;
			FlxList ot;
			if(Parent._headA.object != null)
			{
				iterator = Parent._headA;
				while(iterator != null)
				{
					if(_tailA.object != null)
					{
						ot = _tailA;
						_tailA = FlxList.getNew();
						ot.next = _tailA;
					}
					_tailA.object = iterator.object;
					iterator = iterator.next;
				}
			}
			if(Parent._headB.object != null)
			{
				iterator = Parent._headB;
				while(iterator != null)
				{
					if(_tailB.object != null)
					{
						ot = _tailB;
						_tailB = FlxList.getNew();
						ot.next = _tailB;
					}
					_tailB.object = iterator.object;
					iterator = iterator.next;
				}
			}
		}
		else
			_min = (int)((width + height)/(2*divisions));
		_canSubdivide = (width > _min) || (height > _min);

		//Set up comparison/sort helpers
		_northWestTree = null;
		_northEastTree = null;
		_southEastTree = null;
		_southWestTree = null;
		_leftEdge = x;
		_rightEdge = x+width;
		_halfWidth = width/2f;
		_midpointX = _leftEdge+_halfWidth;
		_topEdge = y;
		_bottomEdge = y+height;
		_halfHeight = height/2f;
		_midpointY = _topEdge+_halfHeight;
	}

	/**
	 * Instantiate a new Quad Tree node.
	 * 
	 * @param	X			The X-coordinate of the point in space.
	 * @param	Y			The Y-coordinate of the point in space.
	 * @param	Width		Desired width of this node.
	 * @param	Height		Desired height of this node.
	 */
	protected void init(float X, float Y, float Width, float Height)
	{
		init(X, Y, Width, Height, null);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		if(_headA != null)
			_headA.destroy();
		_headA = null;
		//if(_tailA != null)
		//	_tailA.destroy();
		_tailA = null;
		if(_headB != null)
			_headB.destroy();
		_headB = null;
		//if(_tailB != null)
		//	_tailB.destroy();
		_tailB = null;

		if(_northWestTree != null)
			_northWestTree.destroy();
		_northWestTree = null;
		if(_northEastTree != null)
			_northEastTree.destroy();
		_northEastTree = null;
		if(_southEastTree != null)
			_southEastTree.destroy();
		_southEastTree = null;
		if(_southWestTree != null)
			_southWestTree.destroy();
		_southWestTree = null;

		_object = null;
		_processingCallback = null;
		_notifyCallback = null;

		_pool.free(this);
	}

	/**
	 * Load objects and/or groups into the quad tree, and register notify and processing callbacks.
	 * 
	 * @param	ObjectOrGroup1	Any object that is or extends FlxObject or FlxGroup.
	 * @param	ObjectOrGroup2	Any object that is or extends FlxObject or FlxGroup.  If null, the first parameter will be checked against itself.
	 * @param	NotifyCallback	A function with the form <code>myFunction(Object1:FlxObject,Object2:FlxObject):void</code> that is called whenever two objects are found to overlap in world space, and either no ProcessCallback is specified, or the ProcessCallback returns true.
	 * @param	ProcessCallback	A function with the form <code>myFunction(Object1:FlxObject,Object2:FlxObject):Boolean</code> that is called whenever two objects are found to overlap in world space.  The NotifyCallback is only called if this function returns true.  See FlxObject.separate().
	 */
	public void load(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2, IFlxCollision NotifyCallback, IFlxObject ProcessCallback)
	{
		add(ObjectOrGroup1, A_LIST);
		if(ObjectOrGroup2 != null)
		{
			add(ObjectOrGroup2, B_LIST);
			_useBothLists = true;
		}
		else
			_useBothLists = false;
		_notifyCallback = NotifyCallback;
		_processingCallback = ProcessCallback;
	}

	/**
	 * Load objects and/or groups into the quad tree, and register notify and processing callbacks.
	 * 
	 * @param	ObjectOrGroup1	Any object that is or extends FlxObject or FlxGroup.
	 * @param	ObjectOrGroup2	Any object that is or extends FlxObject or FlxGroup.  If null, the first parameter will be checked against itself.
	 * @param	NotifyCallback	A function with the form <code>myFunction(Object1:FlxObject,Object2:FlxObject):void</code> that is called whenever two objects are found to overlap in world space, and either no ProcessCallback is specified, or the ProcessCallback returns true.
	 */
	public void load(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2, IFlxCollision NotifyCallback)
	{
		load(ObjectOrGroup1, ObjectOrGroup2, NotifyCallback, null);
	}

	/**
	 * Load objects and/or groups into the quad tree, and register notify and processing callbacks.
	 * 
	 * @param	ObjectOrGroup1	Any object that is or extends FlxObject or FlxGroup.
	 * @param	ObjectOrGroup2	Any object that is or extends FlxObject or FlxGroup.  If null, the first parameter will be checked against itself.
	 */
	public void load(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2)
	{
		load(ObjectOrGroup1, ObjectOrGroup2, null, null);
	}

	/**
	 * Call this function to add an object to the root of the tree.
	 * This function will recursively add all group members, but
	 * not the groups themselves.
	 * 
	 * @param	ObjectOrGroup	FlxObjects are just added, FlxGroups are recursed and their applicable members added accordingly.
	 * @param	List			A <code>int</code> flag indicating the list to which you want to add the objects.  Options are <code>A_LIST</code> and <code>B_LIST</code>.
	 */
	public void add(FlxBasic ObjectOrGroup, int List)
	{
		_list = List;
		if(ObjectOrGroup instanceof FlxGroup)
		{
			int i = 0;
			FlxBasic basic;
			Array<FlxBasic> members = ((FlxGroup) ObjectOrGroup).members;
			int l = ((FlxGroup) (ObjectOrGroup)).length;
			while(i < l)
			{
				basic = members.get(i++);
				if((basic != null) && basic.exists)
				{
					if(basic instanceof FlxGroup)
						add(basic,List);
					else if(basic instanceof FlxObject)
					{
						_object = (FlxObject) basic;
						if(_object.exists && _object.allowCollisions > 0)
						{
							_objectLeftEdge = _object.x;
							_objectTopEdge = _object.y;
							_objectRightEdge = _object.x + _object.width;
							_objectBottomEdge = _object.y + _object.height;
							addObject();
						}
					}
				}
			}
		}
		else
		{
			_object = (FlxObject) ObjectOrGroup;
			if(_object.exists && _object.allowCollisions > 0)
			{
				_objectLeftEdge = _object.x;
				_objectTopEdge = _object.y;
				_objectRightEdge = _object.x + _object.width;
				_objectBottomEdge = _object.y + _object.height;
				addObject();
			}
		}
	}

	/**
	 * Internal function for recursively navigating and creating the tree
	 * while adding objects to the appropriate nodes.
	 */
	protected void addObject()
	{
		//If this quad (not its children) lies entirely inside this object, add it here
		if(!_canSubdivide || ((_leftEdge >= _objectLeftEdge) && (_rightEdge <= _objectRightEdge) && (_topEdge >= _objectTopEdge) && (_bottomEdge <= _objectBottomEdge)))
		{
			addToList();
			return;
		}

		//See if the selected object fits completely inside any of the quadrants
		if((_objectLeftEdge > _leftEdge) && (_objectRightEdge < _midpointX))
		{
			if((_objectTopEdge > _topEdge) && (_objectBottomEdge < _midpointY))
			{
				if(_northWestTree == null)
					_northWestTree = FlxQuadTree.getNew(_leftEdge,_topEdge,_halfWidth,_halfHeight,this);
				_northWestTree.addObject();
				return;
			}
			if((_objectTopEdge > _midpointY) && (_objectBottomEdge < _bottomEdge))
			{
				if(_southWestTree == null)
					_southWestTree = FlxQuadTree.getNew(_leftEdge,_midpointY,_halfWidth,_halfHeight,this);
				_southWestTree.addObject();
				return;
			}
		}
		if((_objectLeftEdge > _midpointX) && (_objectRightEdge < _rightEdge))
		{
			if((_objectTopEdge > _topEdge) && (_objectBottomEdge < _midpointY))
			{
				if(_northEastTree == null)
					_northEastTree = FlxQuadTree.getNew(_midpointX,_topEdge,_halfWidth,_halfHeight,this);
				_northEastTree.addObject();
				return;
			}
			if((_objectTopEdge > _midpointY) && (_objectBottomEdge < _bottomEdge))
			{
				if(_southEastTree == null)
					_southEastTree = FlxQuadTree.getNew(_midpointX,_midpointY,_halfWidth,_halfHeight,this);
				_southEastTree.addObject();
				return;
			}
		}

		//If it wasn't completely contained we have to check out the partial overlaps
		if((_objectRightEdge > _leftEdge) && (_objectLeftEdge < _midpointX) && (_objectBottomEdge > _topEdge) && (_objectTopEdge < _midpointY))
		{
			if(_northWestTree == null)
				_northWestTree = FlxQuadTree.getNew(_leftEdge,_topEdge,_halfWidth,_halfHeight,this);
			_northWestTree.addObject();
		}
		if((_objectRightEdge > _midpointX) && (_objectLeftEdge < _rightEdge) && (_objectBottomEdge > _topEdge) && (_objectTopEdge < _midpointY))
		{
			if(_northEastTree == null)
				_northEastTree = FlxQuadTree.getNew(_midpointX,_topEdge,_halfWidth,_halfHeight,this);
			_northEastTree.addObject();
		}
		if((_objectRightEdge > _midpointX) && (_objectLeftEdge < _rightEdge) && (_objectBottomEdge > _midpointY) && (_objectTopEdge < _bottomEdge))
		{
			if(_southEastTree == null)
				_southEastTree = FlxQuadTree.getNew(_midpointX,_midpointY,_halfWidth,_halfHeight,this);
			_southEastTree.addObject();
		}
		if((_objectRightEdge > _leftEdge) && (_objectLeftEdge < _midpointX) && (_objectBottomEdge > _midpointY) && (_objectTopEdge < _bottomEdge))
		{
			if(_southWestTree == null)
				_southWestTree = FlxQuadTree.getNew(_leftEdge,_midpointY,_halfWidth,_halfHeight,this);
			_southWestTree.addObject();
		}
	}

	/**
	 * Internal function for recursively adding objects to leaf lists.
	 */
	protected void addToList()
	{
		FlxList ot;
		if(_list == A_LIST)
		{
			if(_tailA.object != null)
			{
				ot = _tailA;
				_tailA = FlxList.getNew();
				ot.next = _tailA;
			}
			_tailA.object = _object;
		}
		else
		{
			if(_tailB.object != null)
			{
				ot = _tailB;
				_tailB = FlxList.getNew();
				ot.next = _tailB;
			}
			_tailB.object = _object;
		}
		if(!_canSubdivide)
			return;
		if(_northWestTree != null)
			_northWestTree.addToList();
		if(_northEastTree != null)
			_northEastTree.addToList();
		if(_southEastTree != null)
			_southEastTree.addToList();
		if(_southWestTree != null)
			_southWestTree.addToList();
	}

	/**
	 * <code>FlxQuadTree</code>'s other main function.  Call this after adding objects
	 * using <code>FlxQuadTree.load()</code> to compare the objects that you loaded.
	 * 
	 * @return	Whether or not any overlaps were found.
	 */
	public boolean execute()
	{
		boolean overlapProcessed = false;
		FlxList iterator;

		if(_headA.object != null)
		{
			iterator = _headA;
			while(iterator != null)
			{
				_object = iterator.object;
				if(_useBothLists)
					_iterator = _headB;
				else
					_iterator = iterator.next;
				if(_object.exists && (_object.allowCollisions > 0) && (_iterator != null) && (_iterator.object != null) && _iterator.object.exists && overlapNode())
				{
					overlapProcessed = true;
				}
				iterator = iterator.next;
			}
		}

		//Advance through the tree by calling overlap on each child
		if((_northWestTree != null) && _northWestTree.execute())
			overlapProcessed = true;
		if((_northEastTree != null) && _northEastTree.execute())
			overlapProcessed = true;
		if((_southEastTree != null) && _southEastTree.execute())
			overlapProcessed = true;
		if((_southWestTree != null) && _southWestTree.execute())
			overlapProcessed = true;

		return overlapProcessed;
	}

	/**
	 * An internal function for comparing an object against the contents of a node.
	 * 
	 * @return	Whether or not any overlaps were found.
	 */
	protected boolean overlapNode()
	{
		//Walk the list and check for overlaps
		boolean overlapProcessed = false;
		FlxObject checkObject;
		while(_iterator != null)
		{
			if(!_object.exists || (_object.allowCollisions <= 0))
				break;

			checkObject = _iterator.object;
			if((_object == checkObject) || ((_object != null) && (_object.equals(checkObject))) || !checkObject.exists || (checkObject.allowCollisions <= 0))
			{
				_iterator = _iterator.next;
				continue;
			}

			//calculate bulk hull for _object
			_objectHullX = (_object.x < _object.last.x)?_object.x:_object.last.x;
			_objectHullY = (_object.y < _object.last.y)?_object.y:_object.last.y;
			_objectHullWidth = _object.x - _object.last.x;
			_objectHullWidth = _object.width + ((_objectHullWidth>0)?_objectHullWidth:-_objectHullWidth);
			_objectHullHeight = _object.y - _object.last.y;
			_objectHullHeight = _object.height + ((_objectHullHeight>0)?_objectHullHeight:-_objectHullHeight);

			//calculate bulk hull for checkObject
			_checkObjectHullX = (checkObject.x < checkObject.last.x)?checkObject.x:checkObject.last.x;
			_checkObjectHullY = (checkObject.y < checkObject.last.y)?checkObject.y:checkObject.last.y;
			_checkObjectHullWidth = checkObject.x - checkObject.last.x;
			_checkObjectHullWidth = checkObject.width + ((_checkObjectHullWidth>0)?_checkObjectHullWidth:-_checkObjectHullWidth);
			_checkObjectHullHeight = checkObject.y - checkObject.last.y;
			_checkObjectHullHeight = checkObject.height + ((_checkObjectHullHeight>0)?_checkObjectHullHeight:-_checkObjectHullHeight);

			//check for intersection of the two hulls
			if(	(_objectHullX + _objectHullWidth > _checkObjectHullX) &&
				(_objectHullX < _checkObjectHullX + _checkObjectHullWidth) &&
				(_objectHullY + _objectHullHeight > _checkObjectHullY) &&
				(_objectHullY < _checkObjectHullY + _checkObjectHullHeight) )
			{
				//Execute callback functions if they exist
				if((_processingCallback == null) || _processingCallback.callback(_object,checkObject))
				{
					overlapProcessed = true;
					if(_notifyCallback != null)
						_notifyCallback.callback(_object,checkObject);
				}
			}
			_iterator = _iterator.next;
		}

		return overlapProcessed;
	}
}

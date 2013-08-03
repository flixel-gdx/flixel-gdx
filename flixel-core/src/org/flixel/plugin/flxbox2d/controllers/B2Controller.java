package org.flixel.plugin.flxbox2d.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class B2Controller
{
   protected Array<Body> m_bodyList;
   public int mControllerType;
   boolean mIsActive; // defines whether or not the controller is presently active 
   
   /*
    * Controller types currently implemented
    */
   public static final int UNKNOWN_CONTROLLER = 0;
   public static final int BUOYANCY_CONTROLLER = 1;
   public static final int GRAVITY_CONTROLLER = 2;

   public void step(float timeStep)
   {
   }

   // public void draw(DebugDraw debugDraw) { }

   public void addBody(Body body)
   {
      if (m_bodyList == null)
      {
         m_bodyList = new Array<Body>();
      }

      // if the list does not already contain this body...
      if (m_bodyList.contains(body, true) == false)
      {
         m_bodyList.add(body);
      }
   }

   public void removeBody(Body body)
   {
      if (m_bodyList != null)
      {
         m_bodyList.removeValue(body, true);
      }
   }

   public void clear()
   {
      if (m_bodyList != null)
      {
         m_bodyList.clear();
      }
      m_bodyList = null;
   }

   public Array<Body> getBodyList()
   {
      return m_bodyList;
   }
   
   public boolean isActive()
   {
      return mIsActive;
   }
   
   public void setIsActive(boolean isActive)
   {
      mIsActive = isActive;
   }
}

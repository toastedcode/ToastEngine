package gameEngine;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;

public class ViewManager
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      viewList = new ArrayList<View>();
   }
   
   
   public static View createView(
      // A unique identifier for a view.
      String viewId,
      // A rectangle specifying what part of the world will be rendered.
      Rectangle worldRect,         
      // A rectangle specifying where this view will be rendered on the screen.
      Rectangle screenRect)
   {
      View view = null;
      
      // Does this View already exist in the map?
      if (viewExists(viewId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate view id [%s] found.", viewId)); 
      }      
      else
      {
         // Create the new View    
         view = new View(viewId, worldRect, screenRect);

         // Add to the View list.
         addView(view);
      }
      
      return (view);
   }
   

   public static View createView(
      XmlNode node)
   {
      // Create a new View object using the specified XML node to initialize it.
      View view = new View(node);
      
      // Add the View to the draw list.
      // Note, this is kept z-ordered for use in drawing.
      addView(view);

      return (view);
   }
   
   
   public static void destroyView(
      String viewId)
   {
      removeView(viewId);
   }
   
   
   public static boolean viewExists(
      String viewId)
   {
      // Initialize the return value.
      boolean exists = false;
      
      for (View checkView : viewList)
      {
         if (checkView.getViewId() == viewId)
         {
            exists = true;
            break;
         }
      }

      return (exists);      
   }   
   
   
   public static View getView(
      String viewId)
   {
      // Initialize the return value.
      View view = null;
      
      for (View checkView : viewList)
      {
         if (checkView.getViewId() == viewId)
         {
            view = checkView;
            break;
         }
      }
      
      if (view == null)
      {
         logger.log(Level.WARNING, String.format("Could not find View [%s].", viewId));         
      }
      
      return (view);
   }
   
   
   public static View getView(
      Point screenPosition)
   {
      View view = null;
      
      for (View entry : viewList)
      {
         if ((entry.isEnabled() == true) &&
             (entry.getScreenRect().contains(screenPosition) == true))
         {
            view = entry;
            break;
         }
      }
      
      return (view);
   }   
   
   
   // An operation that performs all updates on all Views.
   // (Called from within the main game loop.)   
   public static void updateViews(
      // A value representing the elapsed time (in milliseconds) since the last update.         
      long elapsedTime)
   {
      for (View view : viewList)
      {
         view.update(elapsedTime);
      }
   }
   
   
   public static Iterator<View> getIterator()
   {
      return (viewList.iterator());
   }
   
   
   public static Point getWorldPosition(
      Point screenPosition)
   {
      Point worldPosition = null;
      
      View view = getView(screenPosition);
      if (view != null)
      {
         worldPosition = view.screenToWorld(screenPosition);         
      }
      
      return (worldPosition);
   }

   
   public static void save(
      XmlNode node)
   {
      // Loop through the View map and save each entry.
      for (View entry : viewList)
      {
         entry.save(node);
      }
   }      
  
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   private static void addView(
      View view)
   {
      // Add the View to the draw list.
      // Note, this is kept z-ordered for use in drawing.
      viewList.add(view);
      Collections.sort(viewList, Z_ORDER_COMPARATOR);  
   }

      
   private static void removeView(
      String viewId)
   {
      // Remove from View list.
      if (viewExists(viewId) == false)
      {
         logger.log(Level.WARNING, String.format("Could not find View [%s].", viewId));            
      }
      else
      {
         viewList.remove(getView(viewId));
      }
   }      
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(EventManager.class.getName());
   
   static final Comparator<View> Z_ORDER_COMPARATOR = new Comparator<View>()
   {
      public int compare(View view1, View view2)
      {
         return(((view1.getZOrder() > view2.getZOrder()) ? -1 :
                 (view1.getZOrder() == view2.getZOrder()) ? 0 : 1));
      }
   };   
   
   // An list of Views used in rendering the game, ordered by zOrder.
   private static List<View> viewList;
}

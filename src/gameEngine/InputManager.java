package gameEngine;

import gameEngine.SelectionManager;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Point;

public class InputManager
{
   public static void initialize()
   {
      mousePosition = new Point(0, 0);
   }   
   
   
   public static void handleKeyPressed(
      KeyEvent e)
   {
      Event event = new Event("eventKeyPressed");
      event.addPayload("keyEvent", e);
      EventManager.broadcastEvent(event);
   }
   
   
   public static void handleKeyReleased(
      KeyEvent e)
   {
      Event event = new Event("eventKeyReleased");
      event.addPayload("keyEvent", e);
      EventManager.broadcastEvent(event);      
   }
   
   
   public static void handleMousePressed(
      MouseEvent e)
   {
      mousePosition.x = e.getX();
      mousePosition.y = e.getY();
      
      Sprite clickedSprite = getClickedSprite(e);
      
      if (clickedSprite != null)
      {
         Event event = new Event("eventMOUSE_PRESSED");
         event.addPayload("mouseEvent", e);
         EventManager.sendEvent(event, clickedSprite.getSpriteId());
      }
      
      SelectionManager.getInstance().mousePressed(e);
   }
      
      
   public static void handleMouseReleased(
      MouseEvent e)
   {
      mousePosition.x = e.getX();
      mousePosition.y = e.getY();
      
      Sprite clickedSprite = getClickedSprite(e);
      
      if (clickedSprite != null)
      {
         Event event = new Event("eventMOUSE_RELEASED");
         event.addPayload("mouseEvent", e);
         EventManager.sendEvent(event, clickedSprite.getSpriteId());
      }
      
      SelectionManager.getInstance().mouseReleased(e);
   }
   
   
   public static void handleMouseMoved(
      MouseEvent e)         
   {
      mousePosition.x = e.getX();
      mousePosition.y = e.getY();
   }
   
   
   public static void handleMouseDragged(
      MouseEvent e)
   {
      SelectionManager.getInstance().mouseDragged(e);
   }

   
   public static Point getMousePosition()
   {
      return (mousePosition);
   }

   
   public static Point getMouseWorldPosition()
   {
      return (ViewManager.getWorldPosition(mousePosition));
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
  
   static Point mousePosition;
   
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   private static Sprite getClickedSprite(
      MouseEvent e)
   {
      Sprite clickedSprite = null;
      
      Point mousePosition = new Point(e.getX(), e.getY());
      Point worldMousePosition = ViewManager.getWorldPosition(mousePosition);
      
      // Get a list of Sprites that contain the clicked position.
      SpriteList sprites = CollisionManager.checkIntersection(worldMousePosition);
      
      // Loop through the Sprites until we find the top-most one that has it's collision enabled.
      for (Sprite sprite : sprites)
      {
         if ((sprite.getCollision() != null) &&
             (sprite.getCollision().isEnabled == true))
         {
            clickedSprite = sprite;
            break;
         }
      }
      
      return (clickedSprite);
   }   
}
package gameEngine;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import gameEngine.*;

public class SelectionManager
{
   // **************************************************************************
   //                             Public Operations
   // **************************************************************************
   
   public static SelectionManager getInstance()
   {
      if (instance == null)
      {
         instance = new SelectionManager();
      }
      
      return (instance);
   }
   
   
   public void mousePressed(
      MouseEvent e)
   {
      int modifiers = e.getModifiers();
      //boolean altPressed = ((modifiers & InputEvent.ALT_MASK) != 0);
      boolean ctrlPressed = ((modifiers & InputEvent.CTRL_MASK) != 0);
      //boolean shiftPressed = ((modifiers & InputEvent.SHIFT_MASK) != 0);
      
      Point mousePosition = new Point(e.getX(), e.getY());
      Point worldMousePosition = ViewManager.getWorldPosition(mousePosition);
      pressedPosition = mousePosition;
      draggedPosition = mousePosition;
      
      System.out.format("Clicked: (%d, %d)\n", mousePosition.x, mousePosition.y);
      
      // Get Sprites at the mouse position.
      SpriteList intersectedSprites = CollisionManager.checkIntersection(worldMousePosition);

      // Find the topmost visible Sprite.
      Sprite topSprite = null;
      for (Sprite sprite : intersectedSprites)
      {
         if ((sprite.isVisible() == true) &&
             (sprite.isSelectable() == true))
         {
            topSprite = sprite;
            break;
         }
      } 
      
      // Select/deselect
      if (topSprite == null)
      {
         SpriteManager.clearSelectedSprites();
      }
      else
      {
         if (ctrlPressed == false)
         {
            SpriteManager.clearSelectedSprites();
         }
         SpriteManager.selectSprite(topSprite);            
      }
   }
   
   
   public void mouseReleased(
      MouseEvent e)
   {
      // Snap to grid, if necessary.
      if ((grid != null) &&
          (grid.getSnapToGrid() == true))      
      {
         SpriteList selectedSprites = SpriteManager.getSelectedSprites();         
         for (Sprite sprite : selectedSprites)
         {
            if (sprite.isDraggable() == true)
            {
               sprite.setPosition(grid.snapToGrid(sprite.getPosition()));
            }
         }
      }
   }
   
   public void mouseDragged(
      MouseEvent e)
   {
      //Point mousePosition = ViewManager.getWorldPosition(new Point(e.getX(), e.getY()));
      Point mousePosition = new Point(e.getX(), e.getY());
      
      Point worldMousePosition = ViewManager.getWorldPosition(mousePosition);
      Point worldDraggedPosition = ViewManager.getWorldPosition(draggedPosition);
      Point delta = new Point((worldMousePosition.x - worldDraggedPosition.x),
                              (worldMousePosition.y - worldDraggedPosition.y));
      
      System.out.format("mouseDragged: screen (%d, %d) old (%d, %d) new (%d, %d), delta (%d, %d)\n",
                        mousePosition.x, mousePosition.y,
                        worldDraggedPosition.x, worldDraggedPosition.y,
                        worldMousePosition.x, worldMousePosition.y,
                        delta.x, delta.y);

      // Get selected Sprites.
      SpriteList selectedSprites = SpriteManager.getSelectedSprites();
      for (Sprite sprite : selectedSprites)
      {
         if (sprite.isDraggable() == true)
         {         
            Point2D newPosition = 
               new Point2D.Double((sprite.getPosition().getX() + delta.x),
                                  (sprite.getPosition().getY() + delta.y));
   
            sprite.setPosition(newPosition);
         }
      }
      
      draggedPosition = mousePosition;
   }   
      
      
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private SelectionManager()
   {
      initialize();
   }
   
   
   private void initialize()
   {
      pressedPosition = null;
      draggedPosition = null;
   }   
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
      
   private final static Logger logger = Logger.getLogger(SelectionManager.class.getName());
   
   // An instance of the SelectionManager class, used in implementing the Singleton pattern.
   private static SelectionManager instance;
   
   private Point pressedPosition;
   
   private Point draggedPosition;
   
   private Grid grid;
}

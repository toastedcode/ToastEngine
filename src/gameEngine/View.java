package gameEngine;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;

public class View
{
   // **************************************************************************
   //                             Public Operations
   // **************************************************************************
   
   // Constructor
   public View(
      // A unique identifer for a view.
      String initViewId,
      // A rectangle specifying what part of the world will be rendered.
      Rectangle initWorldRect,         
      // A rectangle specifying where this view will be rendered on the screen.
      Rectangle initScreenRect)
   {
      initialize();
      
      viewId = initViewId;
      isEnabled = true;      
      screenRect = initScreenRect;
      worldRect = initWorldRect;
   }

   
   // Constructor
   public View(
      XmlNode node)
   {
      initialize(node);
   }
   
   
   public void save(
     XmlNode node)
   {
      // Temporary node for appending children.
      XmlNode childNode = null;
      
      // Create the view node.
      XmlNode viewNode = node.appendChild("view");
      
      // viewId
      viewNode.setAttribute("id", viewId);
      
      // isEnabled
      viewNode.appendChild("isEnabled", String.valueOf(isEnabled));
     
      // screenRect
      childNode = viewNode.appendChild("screenRect");
      XmlUtils.setRectangle(childNode, screenRect);
      
      // worldRect
      childNode = viewNode.appendChild("worldRect");
      XmlUtils.setRectangle(childNode, worldRect);
   }

   
   public String getViewId()
   {
      return (viewId);
   }
   
   
   public void enable()
   {
      isEnabled = true;
   }
   
   
   public void disable()
   {
      isEnabled = false;
   }   
   
   
   public boolean isEnabled()
   {
      return (isEnabled);
   }
   
   
   public Rectangle getScreenRect()
   {
      return (screenRect);
   }
   
   
   public Rectangle getWorldRect()
   {
      return (worldRect);
   }

   
   public int getZOrder()
   {
      return (zOrder);
   }

   
   public void setZOrder(
      int initZOrder)
   {
      zOrder = initZOrder;
   }

   
   public Rectangle worldToScreen(
      Rectangle rectangle)
   {
      double scaleX = ((double)screenRect.width / (double)worldRect.width); 
      double scaleY = ((double)screenRect.height / (double)worldRect.height);
      
      Rectangle transformedRectangle = new Rectangle(rectangle);
      transformedRectangle.setLocation(worldToScreen(transformedRectangle.getLocation()));
      transformedRectangle.setSize((int)(rectangle.width * scaleX), (int)(rectangle.height * scaleY));
      
      return (transformedRectangle);
   }
   
   
   public Point worldToScreen(
      Point point)
   {
      double scaleX = ((double)screenRect.width / (double)worldRect.width); 
      double scaleY = ((double)screenRect.height / (double)worldRect.height);
      
      // Math notes:
      // We subtract the worldRect x/y to transform to view coordinates.
      // We add screenRect x/y and scale to transform to screen coordinates.
      Point transformedPoint = new Point((int)((point.x - worldRect.x) * scaleX + screenRect.x), 
                                         (int)((point.y - worldRect.y) * scaleY + screenRect.y));
      
      
      return (transformedPoint);
   }   

   
   public Shape worldToScreen(
      Shape shape)
   {
      double scaleX = ((double)screenRect.width / (double)worldRect.width); 
      double scaleY = ((double)screenRect.height / (double)worldRect.height);
      
      AffineTransform transform = new AffineTransform(IDENTITY_TRANSFORM);
      
      transform.translate(screenRect.x, screenRect.y);
      transform.scale(scaleX, scaleY);
      transform.translate((worldRect.getX() * -1), (worldRect.getY() * -1));
      
      return (transform.createTransformedShape(shape));
   }
   
   
   public Point screenToWorld(
      Point point)
   {
      double scaleX = ((double)worldRect.width / (double)screenRect.width); 
      double scaleY = ((double)worldRect.height / (double)screenRect.height);
      
      
      // Math notes:
      // We add the worldRect x/y to transform to view coordinates.
      // We subtract screenRect x/y and scale to transform to world coordinates.
      Point transformedPoint = new Point((int)((point.x - screenRect.x) * scaleX + worldRect.x), 
                                         (int)((point.y - screenRect.y) * scaleY + worldRect.y));
      
      return (transformedPoint);
   }

   
   // Returns the screenRect as a Shape for clipping.
   public Shape getClip()
   {
      return (screenRect);
   }

   
   public void setTrackingSpriteId(
      String initTrackingSpriteId)
   {
      trackingSpriteId = initTrackingSpriteId;
   }
   
   
   public void startTracking()
   {
      isTracking = true;
   }
    
   
   public void stopTracking()
   {
      isTracking = false;
   }   

   //
   // Game loop
   //
   
   // An operation that performs all updates to the View.
   // (Called from within the main game loop.)
   public void update(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.
      long elapsedTime)
   {
      // Set the position of the view based on
      if (isTracking == true)
      {
         trackSprite();
      }
      
   }

   
   // An operation that draws the View to the back-buffer.
   // (Called from within the main game loop.)
   public void draw()
   {
      // Nothing to draw.
      
   }   
   
   // **************************************************************************
   //                            Private Operations
   // **************************************************************************
   
   private void initialize()
   {
      viewId = null;
      isEnabled = true;
      screenRect = null;
      worldRect = null;
      trackingSpriteId = null;
      isTracking = false;      
   }
   
   
   private void initialize(
      XmlNode node)      
   {
      // Initialize all attributes to their default values.
      initialize();
      
      // scriptId
      viewId = node.getAttribute("id");
      
      // isEnabled
      if (node.hasChild("isEnabled") == true)
      {
         isEnabled = node.getChild("isEnabled").getBoolValue();
      }
      
      // zOrder
      if (node.hasChild("zOrder") == true)
      {
         zOrder = node.getChild("zOrder").getIntValue();
      }
      
      // worldRect
      if (node.hasChild("worldRect") == true)
      {
         worldRect = XmlUtils.getRectangle(node.getChild("worldRect"));
      }
      
      // screenRect
      if (node.hasChild("screenRect") == true)
      {
         screenRect = XmlUtils.getRectangle(node.getChild("screenRect"));
        
      }
      
      // trackingSpriteId
      if (node.hasChild("trackingSpriteId") == true)
      {
         trackingSpriteId = node.getChild("trackingSpriteId").getValue();
         isTracking = true;
      }      
   }     
   
   
   private void trackSprite()
   {
      if (SpriteManager.spriteExists(trackingSpriteId) == false)
      {
         logger.log(Level.SEVERE, "Cannot find tracking Sprite [%s].", trackingSpriteId);
         stopTracking();
      }
      else
      {
         // Retrieve the position of the tracking Sprite.
         Point2D spritePosition = SpriteManager.getSprite(trackingSpriteId).getPosition();
         
         // Determine the new position of View, such that it is centered on the tracking Sprite.
         int xPos = (int)spritePosition.getX() - (int)(worldRect.getWidth() / 2);
         int yPos = (int)spritePosition.getY() - (int)(worldRect.getHeight() / 2);
         
         // Retrieve the world dimensions.
         Dimension worldDimensions = LevelManager.getInstance().getCurrentLevel().getWorldDimensions();
         
         // Determine the bounds of the View based on the world dimensions.
         int minX = 0;
         int minY = 0;
         int maxX = (int)worldDimensions.getWidth() - (int)worldRect.getWidth();
         int maxY = (int)worldDimensions.getHeight() - (int)worldRect.getHeight();
         
         // Restrict to the correct world boundaries.
         xPos = (xPos < minX) ? minX : xPos;
         yPos = (yPos < minY) ? minY : yPos;
         xPos = (xPos > maxX) ? maxX : xPos;
         yPos = (yPos > maxY) ? maxY : yPos;
              
         // Set the new position.
         worldRect.setLocation(xPos, yPos);
      }
   }
   
   
   // **************************************************************************
   //                            Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(LevelManager.class.getName());   
   
   // An identity transformation
   private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();
   
   // A unique identifer for a view.
   private String viewId;
   
   // A flag indicating if the view is enabled for drawing.
   private boolean isEnabled;
   
   // A rectangle defining the screen mapping of the view.
   private Rectangle screenRect;
   
   // A rectangle defining the world mapping of the view.   
   private Rectangle worldRect;
   
   // The z-order of the View, used in ordering the draw list.
   private int zOrder;
   
   // The Sprite from which the View will determine it's position.
   private String trackingSpriteId;
   
   // A flag indicating if the View is currently tracking a Sprite.
   private boolean isTracking;
}

package gameEngine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.StringTokenizer;

import simpleXml.XmlNode;
import simpleXml.XmlNodeList;

public class Collision implements Cloneable
{
   // **************************************************************************
   //                                  Typedefs
   // **************************************************************************   
   
   public enum CollisionType
   {
      COLLISION_IMAGE,
      COLLISION_POINT,   // TODO: Unsupported
      COLLISION_CIRCLE,  // TODO: Unsupported
      COLLISION_POLYGON
   }
      
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public Collision(
      Path2D initPolygon,
      Sprite initParentSprite)
   {
      initialize();
      
      polygon = initPolygon;
      parentSprite = initParentSprite;
   }   
   
   
   public Collision(
      Rectangle rectangle,
      Sprite initParentSprite)
   {
      initialize();
      
      polygon = getPolygonFromRectangle(rectangle);
      parentSprite = initParentSprite;

   }   
   
   
   public Collision(
      XmlNode node,
      Sprite initParentSprite)
   {
      initialize();
      
      parentSprite = initParentSprite;
      
      // collisionType
      if (node.hasChild("collisionType") == true)
      {
         collisionType = CollisionType.valueOf(node.getChild("collisionType").getValue());
      }
      
      // isEnabled
      if (node.hasChild("isEnabled") == true)
      {
         isEnabled = node.getChild("isEnabled").getBoolValue();
      }
      
      // include
      if (node.hasChild("include") == true)
      {
         XmlNodeList childNodes = node.getChildren("class");
         for (int i = 0; i < childNodes.getLength(); i++)
         {
            includeList.add(childNodes.item(i).getValue());
         }
      }
      
      // exclude
      if (node.hasChild("exclude") == true)
      {
         XmlNodeList childNodes = node.getChildren("class");
         for (int i = 0; i < childNodes.getLength(); i++)
         {
            excludeList.add(childNodes.item(i).getValue());
         }
      }
      
      // polygon
      if (node.hasChild("polygon") == true)
      {
         polygon = XmlUtils.getPolygon(node.getChild("polygon"));
      }
      // rectangle
      else if (node.hasChild("rectangle") == true)
      {
         polygon = getPolygonFromRectangle(XmlUtils.getRectangle(node.getChild("rectangle")));
      }
      // file
      else if (node.hasChild("filename") == true)
      {
         polygon = getPolygonFromFile(node.getChild("filename").getValue());
      }
      // Use the Sprite dimensions as a default collision.
      else
      {
         polygon = getPolygonFromRectangle(
                      new Rectangle(
                         parentSprite.getImageWidth(), 
                         parentSprite.getImageHeight()));
      }
      
      // isSolid
      if (node.hasChild("isSolid") == true)
      {
         isSolid = node.getChild("isSolid").getBoolValue();
      }
      
   }   
   
   
   public Collision clone(
      Sprite initParentSprite)
   {
      Collision clonedCollision = new Collision();
      
      clonedCollision.collisionType = collisionType;
      clonedCollision.polygon = (Path2D)polygon.clone();
      clonedCollision.isEnabled = isEnabled;
      clonedCollision.isSolid = isSolid;
      clonedCollision.includeList = new ArrayList<String>(includeList);
      clonedCollision.excludeList = new ArrayList<String>(excludeList);
      clonedCollision.parentSprite = initParentSprite;
      
      return (clonedCollision);
   }
   
   
   public void save(
      XmlNode node)
   {
      // Temporary node for appending children.
      XmlNode childNode = null;
      
      // Create the collision node.
      XmlNode collisionNode = node.appendChild("collision");
      
      // collisionType
      if (collisionType != CollisionType.COLLISION_IMAGE)
      {
         collisionNode.appendChild("collisionType", collisionType.toString());
      }
      
      // isEnabled
      if (isEnabled == false)
      {
         collisionNode.appendChild("isEnabled", String.valueOf(isEnabled));
      }

      // isSolid
      collisionNode.appendChild("isSolid", String.valueOf(isSolid));
      
      // include
      if (includeList.isEmpty() == false)
      {
         childNode = collisionNode.appendChild("include");
         
         for (String spriteClass : includeList)
         {
            childNode.appendChild("class").setValue(spriteClass);
         }
      }
      
      // exclude
      if (includeList.isEmpty() == false)
      {
         childNode = collisionNode.appendChild("exclude");
         
         for (String spriteClass : excludeList)
         {
            childNode.appendChild("class").setValue(spriteClass);
         }
      }
      
      // polygon
      if (collisionType == CollisionType.COLLISION_POLYGON)
      {
         childNode = collisionNode.appendChild("polygon");
         XmlUtils.setPolygon(childNode, polygon);
      }
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
   
   
   public boolean collidesWithClassSet(
      Set<String> spriteClassSet)
   {
      boolean collides = true;
      
      if (includeList.isEmpty() == false)
      {
         collides = false;
         
         for (String spriteClass : spriteClassSet)
         {
            collides |= (includeList.contains(spriteClass) == true);
         }
      }
      else if (excludeList.isEmpty() == false)
      {
         collides = true;
         
         for (String spriteClass : spriteClassSet)
         {
            collides &= (excludeList.contains(spriteClass) == false);
         }         
      }
      
      return (collides);
   }
   
   
   public Rectangle2D getBounds()
   {
      return (getPolygon().getBounds2D());
   }
   
   
   public Path2D getPolygon()
   {
      // Retrieve the position and scale of the parent Sprite.
      Point2D position = parentSprite.getAbsolutePosition();
      Dimension scale = parentSprite.getScale();
      
      // Create a transformation matrix based on the parent's information.
      AffineTransform transform = new AffineTransform();
      transform.translate(position.getX(), position.getY());
      transform.scale(((double)scale.width / 100.0), ((double)scale.height / 100.0));
      
      Path2D poly = (Path2D)polygon.clone();
      poly.transform(transform);
      
      return (poly);
   }   

   
   public void draw()
   {
      Renderer.drawShape(getPolygon(), Color.WHITE, 100);
   }
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************   
   
   private Collision()
   {
      initialize();
   }   

   
   private void initialize()
   {
      collisionType = CollisionType.COLLISION_IMAGE;
      polygon = null;
      isEnabled = true;
      isSolid = false;
      parentSprite = null;
   }
 
   
   private Path2D getPolygonFromRectangle(
      Rectangle rectangle)
   {
      Path2D poly = new Path2D.Double();
      poly.moveTo(rectangle.x, rectangle.y);
      poly.lineTo((rectangle.x + rectangle.getWidth()), rectangle.y);
      poly.lineTo((rectangle.x + rectangle.getWidth()), (rectangle.y + rectangle.getHeight()));
      poly.lineTo(rectangle.x, (rectangle.y + rectangle.getHeight()));
      poly.closePath();
      
      return (poly);
   }
   
   
   private Path2D getPolygonFromList(
      List<Point2D> points)
   {
      Path2D poly = new Path2D.Double();
      
      if (points.size() > 0)
      {
         int index = 0;
         for(Point2D point : points)
         {
            if (index == 0)
            {
               poly.moveTo(point.getX(), point.getY());            
            }
            else
            {
               poly.lineTo(point.getX(), point.getY());            
            }
            
            index++;
         }
         
         //poly.closePath();
      }
      
      return (poly);
   }   
   
   
   private Path2D getPolygonFromFile(
      String filename)
   {
      Path2D poly = null;
      
      ResourceManager.FileType fileType = ResourceManager.getFileType(filename);
      
      switch (fileType)
      {
         case FILE_TYPE_CSV:
         {
            poly = getPolygonFromList(parseCsvFile(filename));            
            break;
         }
         
         case FILE_TYPE_AI:
         {
            poly = getPolygonFromList(parseIllustratorFile(filename));
            break;
         }
         
         default:
         {
            logger.log(Level.SEVERE, 
                       String.format("Invalid collision file type specified by file name [%s].", 
                                     filename));           
            break;
         }
      }
      
      return (poly);
   }
   
   
   private List<Point2D> parseCsvFile(
      String filename)
   {
      // Initialize the return value.
      List<Point2D> points = new ArrayList<Point2D>();
      
      StringTokenizer tokenizer = null;
      Point2D point = null;
      
      // Read the contents of the file.
      List<String> fileContents = ResourceManager.loadTextFile(filename);
      
      // Loop through the lines of the file.
      for (String line : fileContents)
      {
         tokenizer = new StringTokenizer(line, ",");
         
         if (tokenizer.countTokens() != 2)
         {
            // Skip this line.
         }
         else
         {
            // Create the point.
            point = new Point2D.Double();
            point.setLocation(java.lang.Double.valueOf(tokenizer.nextToken()), 
                              java.lang.Double.valueOf(tokenizer.nextToken()));
            
            // Add to our list.
            points.add(point);
         }
      }
      
      return (points);
   }
   
   
   private List<Point2D> parseIllustratorFile(
      String filename)
   {
      // Initialize the return value.
      List<Point2D> points = new ArrayList<Point2D>();
      
      StringTokenizer tokenizer = null;
      Point2D point = null;
      Rectangle boundingRectangle = null;
      
      // Read the contents of the file.
      List<String> fileContents = ResourceManager.loadTextFile(filename);
      
      // Loop through the lines of the file.
      for (String line : fileContents)
      {
         if (line.contains("%%BoundingBox") == true)
         {
            int startPos = line.lastIndexOf(":");
            if ((startPos > 0) &&
                (startPos < (line.length() - 1)))
            {
               tokenizer = new StringTokenizer(line.substring(startPos + 2), " ");
               
               if (tokenizer.countTokens() == 4)
               {
                  boundingRectangle = 
                     new Rectangle(Integer.valueOf(tokenizer.nextToken()),
                                   Integer.valueOf(tokenizer.nextToken()),
                                   Integer.valueOf(tokenizer.nextToken()),
                                   Integer.valueOf(tokenizer.nextToken()));
               }
            }
         }
         else if ((line.contains(" l") == true) ||
                  (line.contains(" m") == true))
         {
            tokenizer = new StringTokenizer(line, " ");
            
            // Create the point.
            point = new Point2D.Double();
            point.setLocation(java.lang.Double.valueOf(tokenizer.nextToken()), 
                              java.lang.Double.valueOf(tokenizer.nextToken()));
         
            // Translate from Illustrator coordinates to world coordinates.
            point.setLocation(point.getX(), boundingRectangle.getHeight() - point.getY());
         
            // Add to our list.
            // Break if we ever find a point that matches our first point.
            if ((points.size() > 0) &&
                (point.equals(points.get(0)) == true))
            {
               break;
            }
            else
            {
               points.add(point);
            }
         }
      }
      
      return (points);
   }   
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************   
   
   private final static Logger logger = 
      Logger.getLogger(ResourceManager.class.getName());   
   
   // The type of collision this object represents.
   CollisionType collisionType;
   
   // A polygon describing the vertices and edges used in determining Sprite 
   // collision.
   Path2D polygon;
   
   // A flag indicating if this Collision object should be used in checking
   // for Sprite collision.
   boolean isEnabled;
   
   // A flag indicating if a collision with this Collision object should stop
   // movement.
   boolean isSolid;
   
   // A list of Sprite classes in include when checking collisions.
   List<String> includeList = new ArrayList<>();
   
   // A list of Sprite classes in exclude when checking collisions.
   List<String> excludeList = new ArrayList<>();
   
   // The Sprite that owns this Collision object.
   Sprite parentSprite;
   
}

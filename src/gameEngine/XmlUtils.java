package gameEngine;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;
import simpleXml.XmlNodeList;

public class XmlUtils
{
   public static Vector2D getVector(
      XmlNode node)
   {
      Vector2D value = new Vector2D(0, 0);
      
      if (node.hasChild("x") == true)
      {
         value.x = node.getChild("x").getDoubleValue();
      }
      
      if (node.hasChild("y") == true)
      {
         value.y = node.getChild("y").getDoubleValue();
      }
      
      return (value);
   }
   
   
   public static Dimension getDimension(
      XmlNode node)
   {
      Dimension value = new Dimension(0, 0);

      if (node.hasChild("height") == true)
      {
         value.height = node.getChild("height").getIntValue();
      }
      
      if (node.hasChild("width") == true)
      {
         value.width = node.getChild("width").getIntValue();
      }
      
      return (value);
   }
   
   
   public static void setDimension(
      XmlNode node,
      Dimension value)
   {
      node.appendChild("height", String.valueOf(value.height));
      node.appendChild("width", String.valueOf(value.width));
   }
   
   
   public static Point2D getPoint2D(
      XmlNode node)
   {
      double x = 0.0;
      double y = 0.0;

      if (node.hasChild("x") == true)
      {
         x = node.getChild("x").getDoubleValue();
      }
      
      if (node.hasChild("y") == true)
      {
         y = node.getChild("y").getDoubleValue();
      }
      
      return (new Point2D.Double(x, y));
   }   
   
   
   public static void setPoint2D(
      XmlNode node,
      Point2D value)
   {
      node.appendChild("x", String.valueOf(Utils.round(value.getX(), 2)));
      node.appendChild("y", String.valueOf(Utils.round(value.getY(), 2)));
   }   
   
   
   public static Rectangle getRectangle(
      XmlNode node)
   {
      Rectangle value = new Rectangle();
      
      if (node.hasChild("x") == true)
      {
         value.x = node.getChild("x").getIntValue();
      }
      
      if (node.hasChild("y") == true)
      {
         value.y = node.getChild("y").getIntValue();
      }
      
      if (node.hasChild("height") == true)
      {
         value.height = node.getChild("height").getIntValue();
      }
      
      if (node.hasChild("width") == true)
      {
         value.width = node.getChild("width").getIntValue();
      }
      
      return (value);
   }
   
   public static void setRectangle(
      XmlNode node,
      Rectangle value)
   {
      node.appendChild("x", String.valueOf(value.x));
      node.appendChild("y", String.valueOf(value.y));
      node.appendChild("height", String.valueOf(value.height));
      node.appendChild("width", String.valueOf(value.width));
   }
   
   
   public static Path2D getPolygon(
      XmlNode node)
   {
      Path2D value = new Path2D.Double();
      XmlNodeList pointNodes;
      double x;
      double y;
      
      // Retrieve the child "point" nodes.
      pointNodes = node.getChildren("point");
      
      // We need at least two points to make a valid polygon.
      if (pointNodes.getLength() < 2)
      {
         logger.log(Level.WARNING, 
                    String.format("Invalid number of points specified in node [%s].", node.getName()));
      }
      else
      {
         // Loop through the point nodes.
         for (int i = 0; i < pointNodes.getLength(); i++)
         {
            // Initialize the point.
            x = 0;
            y = 0;
            
            if (pointNodes.item(i).hasChild("x") == true)
            {
               x = pointNodes.item(i).getChild("x").getDoubleValue();
            }
            
            if (pointNodes.item(i).hasChild("y") == true)
            {
               y = pointNodes.item(i).getChild("y").getDoubleValue();
            }
            
            // Add to the polygon.
            if (i == 0)
            {
               value.moveTo(x, y);
            }
            else
            {
               value.lineTo(x, y);                     
            }
         }  // end for (int i = 0; i < pointNodes.getLength(); i++)
         
         // Close the polygon.
         value.closePath();
         
      }  // end if (pointNodes.getLength() > 2)
      
      return (value);
   }
   
   
   public static void setPolygon(
      XmlNode node,
      Path2D polygon)
   {
      // Temporary node for appending children.
      XmlNode childNode = null;
      
      // Variables used in iterating through the points of polygon A.
      PathIterator it = null;
      double coords[] = new double[6];
      
      // Loop through the points of the polygon.
      it = polygon.getPathIterator(IDENTITY_TRANSFORM);
      while (it.isDone() == false)
      {
         // Get the current point.
         int segmentType = it.currentSegment(coords);
         
         if ((segmentType == PathIterator.SEG_MOVETO) ||
             (segmentType == PathIterator.SEG_LINETO))
         {
            childNode = node.appendChild("point");
            XmlUtils.setPoint2D(childNode, new Point2D.Double(coords[0], coords[1]));
         }
         else if (segmentType == PathIterator.SEG_CLOSE)
         {
            node.setAttribute("isClosed", String.valueOf(true));
         }
         
         // Next!
         it.next();  
         
      }  // end while (it.isDone() == false)
   }   
   
   
   public static void setVector2D(
      XmlNode node,
      Vector2D value)
   {
      node.appendChild("x", String.valueOf(Utils.round(value.x, 2)));
      node.appendChild("y", String.valueOf(Utils.round(value.y, 2)));
   }
   
   private final static Logger logger = Logger.getLogger(XmlUtils.class.getName());
   
   static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();
}

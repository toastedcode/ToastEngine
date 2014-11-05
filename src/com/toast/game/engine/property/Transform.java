package com.toast.game.engine.property;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import com.toast.game.common.CoordinatesType;

public class Transform extends Property
{
   public Transform()
   {
      super("transform");
      
      position = new Point2D.Double(0, 0);
      coordsType = CoordinatesType.WORLD;
      scale = 1.0;
      //rotation = 0;   
   }
   
   
   public Transform(
      Point2D.Double position,
      double scale)
   {
      super("transform");
      
      this.position = (Point2D.Double)position.clone();
      coordsType = CoordinatesType.WORLD;
      this.scale = scale;
      //rotation = 0;   
   }
  
   
   public Point2D.Double getPosition()
   {
      return ((Point2D.Double)position.clone());
   }
  
   
   public void setPosition(double x, double y)
   {
      this.position.setLocation(x, y);
   }

   
   public void setPosition(Point2D position)
   {
      this.position.setLocation(position.getX(), position.getY());
   }
   
   
   public int getZOrder()
   {
      return (zOrder);
   }
   
   
   public void setZOrder(int zOrder)
   {
      this.zOrder = zOrder;
   }

   
   public CoordinatesType getCoordsType()
   {
      return (coordsType);
   }

   
   public void setCoordsType(CoordinatesType coordsType)
   {
      this.coordsType = coordsType;
   }

   
   public double getScale()
   {
      return (scale);
   }

   
   public void setScale(double scale)
   {
      this.scale = scale;
   }
   
   
   public AffineTransform getTransform()
   {
      AffineTransform affineTransform = new AffineTransform();
      affineTransform.translate(position.getX(), position.getY());
      affineTransform.scale(scale, scale);
      
      return (affineTransform);
   }
   
   private Point2D.Double position;
   
   private int zOrder;
   
   private CoordinatesType coordsType;
   
   private double scale;

   //private int rotation;
}

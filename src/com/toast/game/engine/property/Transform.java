package com.toast.game.engine.property;

import java.awt.geom.Point2D;

import com.toast.game.common.CoordinatesType;
import com.toast.game.engine.component.Component;

public class Transform extends Component
{
   public Transform()
   {
      super();
      initialize();
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
   
   
   private void initialize()
   {
      position = new Point2D.Double(0, 0);
      coordsType = CoordinatesType.WORLD;
      scale = 1.0;
      //rotation = 0;      
   }
   
   private Point2D.Double position;
   
   private CoordinatesType coordsType;
   
   private double scale;

   //private int rotation;
}

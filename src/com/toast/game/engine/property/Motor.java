package com.toast.game.engine.property;

import java.awt.geom.Point2D;

import com.toast.game.engine.interfaces.Updatable;

public class Motor extends Property implements Updatable
{
   public Motor()
   {
      super("motor");
   }
   
   
   public void move(
      double deltaX,
      double deltaY)
   {
      this.deltaX += deltaX;
      this.deltaY += deltaY;
   }
   
   
   @Override
   public void update(long elapsedTime)
   {
      Transform transform = getTransform();
      
      if (transform != null)
      {
         // TODO: Collision checking.
         
         Point2D.Double position = transform.getPosition();
         transform.setPosition((position.getX() + deltaX),
                               (position.getX() + deltaY));
         
         deltaX = 0.0;
         deltaY = 0.0;
      }
   }

   
   private Transform getTransform()
   {
      return (null);
   }
   
   
   private double deltaX;
   
   private double deltaY;
}

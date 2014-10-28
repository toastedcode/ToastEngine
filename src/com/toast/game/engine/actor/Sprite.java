package com.toast.game.engine.actor;

import java.awt.Graphics;
import java.awt.Point;

import com.toast.game.common.ClassSet;
import com.toast.game.engine.component.Component;
import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.property.Display;
import com.toast.game.engine.property.Transform;

public class Sprite extends Actor implements Drawable
{
   public Sprite(
      String id, 
      ClassSet classSet)
   {
      super(id, classSet);
   }

   @Override
   public void draw(Graphics graphics, Point position, double scale)
   {
      Transform transform = getTransform();
      
      if (transform != null)
      {
         position.translate((int)transform.getPosition().getX(),
                            (int)transform.getPosition().getY());
         scale *= transform.getScale();
      }
      
      for (Component child : children)
      {
         // Draw children.
         if (child instanceof Drawable)
         {
            ((Drawable)child).draw(graphics, position, scale);
         }
      }
   }
   
   
   private Transform getTransform()
   {
      Transform transform = null;
      
      for (Component child : children)
      {
         if (child instanceof Transform)
         {
            transform = (Transform)child;
            break;
         }
      }
      
      return (transform);
   }
}

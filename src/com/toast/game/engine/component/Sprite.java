package com.toast.game.engine.component;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.property.Property;
import com.toast.game.engine.property.Transform;
import com.toast.xml.XmlNode;

public class Sprite extends Actor implements Drawable
{
   public Sprite(
      String id)
   {
      super(id);
   }
   
   
   public Sprite(XmlNode node)
   {
      super(node);
   }

   @Override
   public void draw(Graphics graphics)
   {
      Point2D.Double position = getPosition();
      double scale = getScale();

      // TODO: Find drawable properties and draw them, using the position and scale.
   }
   
   @Override
   public boolean isVisible()
   {
      return (true);
   }
   

   @Override
   public int getWidth()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public int getHeight()
   {
      // TODO Auto-generated method stub
      return 0;
   }   
   
   
   private Transform getTransform()
   {
      Transform transform = null;
      
      for (Property property : properties)
      {
         if (property instanceof Transform)
         {
            transform = (Transform)property;
            break;
         }
      }
      
      return (transform);
   }
}

package com.toast.game.engine.component;

import java.awt.Graphics;
import java.awt.Point;

import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.interfaces.Updatable;

public class Scene extends Component implements Drawable, Updatable
{
   public Scene(String id)
   {
      super(id);
   }

   
   @Override
   public void update(long elapsedTime)
   {
      for (Component child : children)
      {
         // Update children.
         if (child instanceof Updatable)
         {
            ((Updatable)child).update(elapsedTime);
         }
      }      
   }

   
   @Override
   public void draw(Graphics graphics, Point position, double scale)
   {
      // TODO Auto-generated method stub
      
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
}

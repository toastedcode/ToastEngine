package com.toast.game.engine;

import java.awt.Graphics;
import java.awt.Point;

import com.toast.game.engine.component.Scene;
import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.interfaces.Updatable;

public class Game implements Drawable, Updatable
{
   public Game()
   {
      
   }
   
   void loadScene(String filename)
   {
      
   }
   
   
   @Override
   public void update(long elapsedTime)
   {
      scene.update(elapsedTime);
   }

   
   @Override
   public void draw(Graphics graphics, Point position, double scale)
   {
      scene.draw(graphics, new Point(0, 0), 1.0);
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
   
   Scene scene;
}

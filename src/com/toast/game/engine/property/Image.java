package com.toast.game.engine.property;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.resource.Texture;

public class Image extends Property implements Drawable
{
   public Image(
      String id, 
      Texture texture)
   {
      super(id);
      TEXTURE = texture;
   }

   
   @Override
   public void draw(Graphics graphics)
   {
      Point position = new Point(0, 0);
      double scale = 1.0;
      
      Rectangle sourceRectangle = new Rectangle(0, 0, TEXTURE.getWidth(), TEXTURE.getHeight());
      
      Rectangle destinationRectangle = new Rectangle(position,
                                                     new Dimension((int)(TEXTURE.getWidth() * scale), 
                                                                   (int)(TEXTURE.getHeight() * scale)));
      
      ((Graphics2D)graphics).drawImage(
         TEXTURE.getBufferedImage(), 
         destinationRectangle.x, 
         destinationRectangle.y, 
         (destinationRectangle.x + destinationRectangle.width), 
         (destinationRectangle.y + destinationRectangle.height), 
         sourceRectangle.x, 
         sourceRectangle.y, 
         (sourceRectangle.x+ sourceRectangle.width), 
         (sourceRectangle.y + sourceRectangle.height), 
         null);  
   }
   
   
   public void draw(
      Graphics graphics,
      Point position,
      double scale)
   {
      Rectangle sourceRectangle = new Rectangle(0, 0, TEXTURE.getWidth(), TEXTURE.getHeight());
      
      Rectangle destinationRectangle = new Rectangle(position,
                                                     new Dimension((int)(TEXTURE.getWidth() * scale), 
                                                                   (int)(TEXTURE.getHeight() * scale)));
      
      ((Graphics2D)graphics).drawImage(
         TEXTURE.getBufferedImage(), 
         destinationRectangle.x, 
         destinationRectangle.y, 
         (destinationRectangle.x + destinationRectangle.width), 
         (destinationRectangle.y + destinationRectangle.height), 
         sourceRectangle.x, 
         sourceRectangle.y, 
         (sourceRectangle.x+ sourceRectangle.width), 
         (sourceRectangle.y + sourceRectangle.height), 
         null);  
   }   

   
   @Override
   public int getWidth()
   {
      return (TEXTURE.getWidth());
   }

   
   @Override
   public int getHeight()
   {
      return (TEXTURE.getHeight());
   }
   

   @Override
   public boolean isVisible()
   {
      // TODO Auto-generated method stub
      return false;
   }   
   
   private final Texture TEXTURE;
}

package com.toast.game.engine.property;

import gameEngine.LevelManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import com.toast.game.engine.interfaces.Drawable;

public class Grid extends Property implements Drawable
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public Grid(
      int interval,
      Color color)
   {
      super("grid");
      
      this.interval = interval;
      this.color = color;
   }
   
   
   public void setColor(
      Color color)
   {
      this.color = color;
   }
   
   
   public void draw(
      Graphics g,
      JComponent component)
   {
      if (LevelManager.getInstance().getCurrentLevel() != null)
      {
         // Set the drawing color and opacity.
         g.setColor(Color.WHITE);
         ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ((float)30 / 100.0f)));
         
         Dimension worldDimensions = LevelManager.getInstance().getCurrentLevel().getWorldDimensions();
         
         // Draw a grid.
         for (int x = 0; x < worldDimensions.getWidth(); x += interval)
         {
            g.drawLine(x, 0, x, (int)worldDimensions.getHeight());
         }
         
         for (int y = 0; y < worldDimensions.getHeight(); y += interval)
         {
            g.drawLine(0, y, (int)worldDimensions.getWidth(), y);
         }
      }
   }
   
   
   public Point2D.Double snapTo(
      Point2D.Double position)
   {
      double modulus = 0;
      double halfInterval = interval / 2;
      double x = position.getX();
      double y = position.getY();
     
      modulus = x % interval;
      if (modulus <= halfInterval)
      {
         x = ((x / interval) * interval);
      }
      else if (modulus > halfInterval)
      {
         x = (((x / interval) + 1) * interval);            
      }
      
      modulus = y % interval;
      if (modulus <= halfInterval)
      {
         y = ((y / interval) * interval);
      }
      else if (modulus > halfInterval)
      {
         y = (((y / interval) + 1) * interval);            
      }
      
      return (new Point2D.Double(x, y));
   }
   
   // **************************************************************************
   //                                   Drawable
   
   @Override
   public void draw(Graphics graphics)
   {
      // Set the drawing color and opacity.
      graphics.setColor(color);
      ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ((float)30 / 100.0f)));
      
      Rectangle drawArea = graphics.getClipBounds();
      
      // Draw a grid.
      for (int x = (int)drawArea.getX(); x < (int)drawArea.getWidth(); x += interval)
      {
         graphics.drawLine(x, 0, x, (int)drawArea.getHeight());
      }
      
      for (int y = (int)drawArea.getY(); y < (int)drawArea.getHeight(); y += interval)
      {
         graphics.drawLine(0, y, (int)drawArea.getWidth(), y);
      }
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


   @Override
   public boolean isVisible()
   {
      // TODO Auto-generated method stub
      return false;
   }   
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private int interval;
   
   private Color color;
}

package gameEngine;

import gameEngine.LevelManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

public class Grid
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public Grid(
      int initInterval,
      Color initColor)
   {
      isEnabled = true;
      interval = initInterval;
      color = initColor;
      snapToGrid = false;
   }

      
   public void enable()
   {
      isEnabled = true;
   }
   
   
   public void disable()
   {
      isEnabled = false;
   }
   
   
   public boolean isEnabled()
   {
      return (isEnabled);
   }
   
   
   public void setGridColor(
      Color initColor)
   {
      color = initColor;
   }
   
   
   public boolean getSnapToGrid()
   {
      return (snapToGrid);
   }
   
   
   public void setSnapToGrid(
      boolean initSnapToGrid)
   {
      snapToGrid = initSnapToGrid;
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
   
   
   public Point2D snapToGrid(
      Point2D point)
   {
      int modulus = 0;
      int halfInterval = interval / 2;
      int x = (int)point.getX();
      int y = (int)point.getY();
     
      if (snapToGrid == true)
      {
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
      }
      
      return (new Point2D.Double(x, y));
   }
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private int interval;
   
   private boolean isEnabled;
   
   private Color color;
   
   private boolean snapToGrid;
}

package gameEngine;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

public class Renderer
{
   // **************************************************************************
   //                           Public Operations
   // **************************************************************************
   
   // This operation initializes the graphics renderer by creating the 
   // back-buffer and creating a graphics context.
   static public void initialize(
         // The width of the application screen.
         int screenWidth,         
         // The height of the application screen.
         int screenHeight)
   {
      screenDimensions = new Dimension(screenWidth, screenHeight);
      
      backBuffer = new BufferedImage((int)screenDimensions.getWidth(), 
                                     (int)screenDimensions.getHeight(), 
                                     BufferedImage.TYPE_INT_RGB);

      graphics2D = backBuffer.createGraphics();
   }
   
   
   static public Graphics getGraphics()
   {
      return (graphics2D);
   }
   
   
   static public Dimension getScreenDimensions()
   {
      return (screenDimensions);
   }
   
   
   // This operation sets the current View used to render the scene.
   static public void setView(
      View view)
   {
      currentView = view;  
   }
   
   
   // This operation sets the current View used to render the scene.
   static public View getView()
   {
      return (currentView);  
   }
   
   
   // This operation retrieves a static instance of the Renderer class,
   // used in implementing the singleton pattern.
   public static Renderer getInstance() 
   {
      if(instance == null) 
      {
         instance = new Renderer();
      }
      return instance;
   }
   
   
   // This operation clear's the back-buffer and prepares for another round
   // of drawing.
   static public void clear()
   {
      // Set transformation matrix to identity
      graphics2D.setTransform(IDENTITY_TRANSFORM);

      // Erase the backBuffer;
      graphics2D.setPaint(Color.BLACK);
      graphics2D.fillRect(0, 
                          0, 
                          (int)screenDimensions.getWidth(), 
                          (int)screenDimensions.getHeight());
   }

   
   static public void paint(
      Graphics graphics,
      ImageObserver observer)
   {
      // Copy the backbuffer to the frontbuffer
      graphics.drawImage(backBuffer, 0, 0, observer);      
   }
   

   static public void drawImage(
         Image image, 
         Rectangle srcRectangle, 
         Rectangle destRectangle,
         CoordinatesType coordsType,
         int opacity)
   {
      if (image != null)
      {
         // If we have a view specified, use it to transform the destination rectangle.
         if (currentView != null)
         {
            graphics2D.setClip(currentView.getClip());
            
            if (coordsType != CoordinatesType.SCREEN)
            {
               destRectangle = currentView.worldToScreen(destRectangle);
            }
         }
         
         if ((srcRectangle.isEmpty() == false) &&
             (destRectangle.isEmpty() == false))
         {
            // Set the drawing opacity.
            graphics2D.setComposite(
               AlphaComposite.getInstance(
                     AlphaComposite.SRC_OVER, 
                     ((float)opacity / 100.0f)));               
            
            graphics2D.drawImage(
                  image.getBufferedImage(), 
                  destRectangle.x, 
                  destRectangle.y, 
                  (destRectangle.x + destRectangle.width), 
                  (destRectangle.y + destRectangle.height), 
                  srcRectangle.x, 
                  srcRectangle.y, 
                  (srcRectangle.x+ srcRectangle.width), 
                  (srcRectangle.y + srcRectangle.height), 
                  null);
         }
      }
      
   }

   
   static public void drawText(
      String string, 
      Point position,
      Color color,
      Font font,
      CoordinatesType coordsType,
      int opacity)
   {
      if (string != null)
      {
         // If we have a view specified, use it to transform the Shape.
         if (currentView != null)
         {
            graphics2D.setClip(currentView.getClip());
            
            if (coordsType != CoordinatesType.SCREEN)
            {
               position = currentView.worldToScreen(position);
            }
         }
         
         // Set the drawing opacity.
         graphics2D.setComposite(
            AlphaComposite.getInstance(
                  AlphaComposite.SRC_OVER, 
                  ((float)opacity / 100.0f)));            
         
         graphics2D.setColor(color);
         graphics2D.setFont(font);
         graphics2D.drawString(string, (int)position.getX(), (int)position.getY());
      }
      
   }

   
   static public void drawShape(
      Shape shape,
      Color color,
      int opacity)
   {
      // If we have a view specified, use it to transform the Shape.
      if (currentView != null)
      {
         graphics2D.setClip(currentView.getClip());
         shape = currentView.worldToScreen(shape);
      }
      
      // Set the drawing opacity.
      graphics2D.setComposite(
         AlphaComposite.getInstance(
               AlphaComposite.SRC_OVER, 
               ((float)opacity / 100.0f)));       
      
      graphics2D.setColor(color);
      graphics2D.draw(shape);   
   }
  
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private Renderer()
   {
      // Exists only to defeat instantiation.      
   }
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   // An identity transformation
   private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();   
   
   // An instance of the SpriteManager class, used in implementing the 
   // singleton pattern.
   private static Renderer instance = null;
   
   // The back-buffer.
   private static BufferedImage backBuffer;
   
   // The dimensions (height, width) of the screen.
   private static Dimension screenDimensions;
   
   // The graphics context for the backbuffer.  This is the class that does 
   // the drawing.
   private static Graphics2D graphics2D;
   
   // The current View object used to render the scene.
   private static View currentView;   
}

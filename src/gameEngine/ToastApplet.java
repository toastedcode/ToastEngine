package gameEngine;

import java.applet.Applet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;

public class ToastApplet extends Applet implements KeyListener, MouseListener, MouseMotionListener
{
   
   // **************************************************************************
   //                                  Public Operations
   // **************************************************************************
   
   // **************************************************************************
   //                           Applet operations   
   
   public void init()
   {
      // Add input listeners.
      addKeyListener(this);
      addMouseListener(this);
      addMouseMotionListener(this);
      
      // Create the Toast engine!
      gameEngine = new ToastEngine();
   }

   
   // This operation is called for the Applet when it is started (but after init).
   public void start()
   {
      // Initialize the engine, load the first level.
      gameEngine.initialize(getWidth(), getHeight());
      
      // Create the game loop on a separate thread.
      gameLoop = new Thread()
      {
         public void run()
         {
            gameLoop();
         }
      };
      
      // Go!
      gameLoop.start();
   }

   
   // This operation is called for the Applet when it is stopped.
   public void stop()
   {
      // Kill the game loop.
      gameLoop = null;
   }

   
   // This operation is called by the Applet to repaint the screen.
   // Note: Called in response to a repaint() request.
   public void update(Graphics graphics)
   {
      gameEngine.paint(graphics, this);
   }
   
   // **************************************************************************
   //                           Key Listener operations   
   
   public void keyPressed(
      KeyEvent e)
   {
      InputManager.handleKeyPressed(e);      
   }
   
   
   public void keyReleased(
         KeyEvent e)
   {
      InputManager.handleKeyReleased(e);           
   }
   
   
   public void keyTyped(
      KeyEvent e)
   {
   }   
   
   // **************************************************************************
   //                           Mouse Listener operations   
   
   public void mousePressed(MouseEvent e)
   {
      InputManager.handleMousePressed(e);          
   }

   
   public void mouseReleased(MouseEvent e)
   {
      InputManager.handleMouseReleased(e);        
   }

   
   public void mouseEntered(MouseEvent e)
   {
   }

   
   public void mouseExited(MouseEvent e)
   {
   }

   
   public void mouseClicked(MouseEvent e)
   {
   }
   
   
   public void mouseMoved(MouseEvent e)
   {
      InputManager.handleMouseMoved(e);      
   }   
   
   
   public void mouseDragged(MouseEvent e)
   {
   }
   
   // **************************************************************************
   //                                  Private Operations
   // **************************************************************************
   
   public void gameLoop()
   {
      while (true)
      {
         // Update all game objects.
         gameEngine.update();
         
         // Force the Applet to repaint.
         repaint();
         
         // Sleep
         // TODO: Base sleep time on target frame rate.
         try
         {
            Thread.sleep(1);
         }
         catch (Exception e)
         {
            // TODO: Logging.
         }
      }
   }   
   
   // **************************************************************************
   //                                  Private attributes
   // **************************************************************************
   
   // The thread that runs the main game loop.
   private Thread gameLoop;
   
   // The game engine.
   private ToastEngine gameEngine;
}

package gameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ToastApplication extends JFrame
{
   // **************************************************************************
   //                                    Main
   // **************************************************************************   
   
   public static void main(String[] args)
   {
      if (args.length != 3)
      {
         System.out.print("Invalid number of parameters.\n");
      }
      else
      {
         try
         {
            // Extract the application name and dimensions from the command line arguments.
            // Note: Must be final so they can be passed into the ToastApplication constructor below.
            final String appName = args[0];
            final int appWidth = Integer.parseInt(args[1]);
            final int appHeight = Integer.parseInt(args[2]);

            // Spawn the GUI thread.
            // Note: invokeLater() causes this to be executed on the event dispatch thread.
            SwingUtilities.invokeLater(
               new Runnable()
               {
                  public void run()
                  {
                     new ToastApplication(appName, appWidth, appHeight);
                  }
               });

         }
         catch(Exception e)
         {
            System.out.print("Invalid parameter value.\n");            
         }
         
         
      }  // end if (args.length < 3)
   }
   
   // **************************************************************************
   //                           Internal classes
   // **************************************************************************   
   
   protected class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener
   {
      // Constructor.
      public GamePanel(ToastEngine initEngine)
      {
         // Store the engine responsible for painting our GamePanel.
         engine = initEngine;
         
         setFocusable(true);
         
         // Add input listeners.
         addKeyListener(this);
         addMouseListener(this);
         addMouseMotionListener(this);
      }
      
      
      // This operation used a ToastEngine object to paint the contents of the panel. 
      public void paintComponent(Graphics g)
      {
         super.paintComponent(g);         
         engine.paint(g, this);
      };
      
      // **************************************************************************
      //                           Key Listener Operations   
      
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
      //                           Mouse Listener Operations   
      
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
      
      // **************************************************************************
      //                      Mouse Motion Listener Operations         
      
      public void mouseMoved(MouseEvent e)
      {
         InputManager.handleMouseMoved(e);      
      }   
      
      
      public void mouseDragged(MouseEvent e)
      {
         InputManager.handleMouseDragged(e);         
      }        
   
      // The ToastEngine object that will be used to render the panel.
      private ToastEngine engine;
   }
   
   // **************************************************************************
   //                           Public operations
   // **************************************************************************   
   
   // Constructor.
   public ToastApplication(
      String appName,
      int width,
      int height)
   {
      // Initialize JFrame parameters.
      setTitle(appName);
      setSize(width, height);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
      
      // Create the Toast engine!      
      gameEngine = new ToastEngine();      
      
      // Create the GUI.
      createGui();
      
      // Initialize the engine, load the first level.
      gameEngine.initialize(width, height);
      
      // Create the game loop on a separate thread.
      Thread loop = new Thread()
      {
         public void run()
         {
            gameLoop();
         }
      };
      
      // Go!
      loop.start();
   }

   // **************************************************************************
   //                           Private operations
   // **************************************************************************
   
   protected void createGui()
   {
      gamePanel = new GamePanel(gameEngine);
      getContentPane().add(gamePanel);
   }
   
   
   protected void gameLoop()
   {
      while (true)
      {
         // Update all game objects.
         gameEngine.update();
         
         // Force the Applet to repaint.
         repaint();
         
         // Sleep
         try
         {
            Thread.sleep(TimingManager.getSleepTime());
         }
         catch (Exception e)
         {
            // TODO: Logging.
         }
      }
   }   
   
   // **************************************************************************
   //                           Private attributes
   // **************************************************************************   
   
   // The game engine.
   protected ToastEngine gameEngine;
   
   // The Swing panel that will be used for displaying the game.
   protected GamePanel gamePanel;
}

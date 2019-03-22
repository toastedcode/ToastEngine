package com.toast.game.engine;

import java.awt.Graphics;
import javax.swing.JPanel;

import gameEngine.TimingManager;
import com.toast.game.engine.component.Component;
import com.toast.xml.XmlNode;
import com.toast.xml.exception.XmlFormatException;

public class Game extends Component
{
   public Game(
      String id)
   {
      super(id);
      
      createGamePanel();
      createGameLoop();
   }
   
   
   public Game(XmlNode node) throws XmlFormatException
   {
      super(node);
      
      createGamePanel();
      createGameLoop();
   }
   
   
   public JPanel getGamePanel()
   {
      return (gamePanel);
   }
   
   
   @SuppressWarnings("serial")
   private void createGamePanel()
   {
      gamePanel = new JPanel()
      {
         @Override
         public void paint(Graphics graphics)
         {
            Renderer.paint(graphics,  this);
         }
      };      
   }
   
   
   private void createGameLoop()
   {
      // Create the game loop on a separate thread.
      Thread loop = new Thread()
      {
         @Override
         public void run()
         {
            gameLoop();
         }
      };
      
      // Go!
      loop.start();        
   }
   
   
   private void gameLoop()
   {
      while (true)
      {
         // Get the elapsed time (in milliseconds) since the last update.         
         long elapsedTime = TimingManager.update();
        
         if (elapsedTime > 0)
         {
            update(elapsedTime);
            
            Renderer.clear();
            draw();
            
            // Force the game panel to repaint.
            gamePanel.repaint();            
         }
         
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
   
   JPanel gamePanel;
}

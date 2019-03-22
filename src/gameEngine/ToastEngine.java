package gameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Point;
import java.util.Iterator;

// ToastEngine is a simple 2D game engine
// Jason Tost
// Started 1/12/2011
public class ToastEngine
{
   // **************************************************************************
   //                           Public Operations
   // **************************************************************************   
   
   public ToastEngine()
   {
   }
   
   
   public void initialize(
      int width,
      int height)         
   {
      // Initialize our graphics renderer.
      Renderer.initialize(width, height);
      
      // Initialize our object managers.
      SpriteManager.initialize();
      ResourceManager.initialize();
      AnimationManager.initialize();
      EventManager.initialize();
      StateManager.initialize();
      ScriptManager.initialize();
      TimerManager.initialize();
      InputManager.initialize();
      TriggerManager.initialize();
      ViewManager.initialize();
      
      // Initialize global states.
      StateManager.setState("global", "isDebugMode", false);
      
      LevelManager.getInstance().loadLevelDescriptions();
      LevelManager.getInstance().advanceLevel();
      
      // Start the game clock.
      TimingManager.initialize();
   }
   
   
   // Called from the gameloop, this method updates all game logic
   public void update()
   {
      // Get the elapsed time (in milliseconds) since the last update.         
      long elapsedTime = TimingManager.update();
      
      boolean isPaused = ((StateManager.hasState("global", "isPaused") == true) &&
                          (Boolean.valueOf(StateManager.getState("global", "isPaused")) == true)); 
     
      if ((elapsedTime > 0) &&
          (isPaused == false))
      {
         // Update the current level.
         LevelManager.getInstance().updateLevel(elapsedTime);

         // Update the quadtree calculated in the CollisionManager.
         CollisionManager.update(elapsedTime);
         
         // Update all timers
         TimerManager.updateTimers(elapsedTime);
            
         // Update all triggers
         TriggerManager.updateTriggers(elapsedTime);
      
         // Update all sprites (animation, physics, logic)
         SpriteManager.updateSprites(elapsedTime);
            
         // Update the views.
         ViewManager.updateViews(elapsedTime);
      }
   }
   
   
   public void paint(Graphics graphics, ImageObserver observer)
   {
      // Clear the renderer.
      Renderer.clear();
      
      // Iterate through the map of views.
      Iterator<View> iterator = ViewManager.getIterator();
      while (iterator.hasNext() == true)
      {
         View view = iterator.next();
         
         // If this view is enabled ...
         if (view.isEnabled() == true)
         {
            // Set the view.
            Renderer.setView(view);

            // Draw all sprites.
            SpriteManager.drawSprites();
            
            // TODO: Move to Debugger class.
            if (Boolean.valueOf(StateManager.getState("global", "isDebugMode")) == true)
            {
               Renderer.drawText(String.format("Framerate: %1$.2f", TimingManager.getFrameRate()),
                                 new Point(20, 20),
                                 Color.WHITE,
                                 new Font( "SansSerif", Font.PLAIN, 12 ),
                                 CoordinatesType.SCREEN,
                                 100);
            }
            
            CollisionManager.drawQuadtree();
         }
      }  // while (iterator.hasNext() == true)

      // Paint to the screen from the backbuffer
      Renderer.paint(graphics, observer);
      
      // Update the frame count.
      TimingManager.incrementFrameCount();
   }
   
}

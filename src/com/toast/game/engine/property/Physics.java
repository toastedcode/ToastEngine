package com.toast.game.engine.property;

import com.toast.game.common.Vector2D;
import com.toast.game.engine.interfaces.Updatable;
import com.toast.xml.XmlNode;

public class Physics extends Property implements Updatable
{
   public Physics()
   {
      super("physics");
   }
   
   
   public Physics(XmlNode node)
   {
      super(node);
   }


   @Override
   public void update(long elapsedTime)
   {
      Vector2D translation = new Vector2D();
      
      double elapsedSeconds = (double)elapsedTime / (double)MILLISECONDS_PER_SECOND;
      
      // Apply gravity.
      velocity.x += gravity.x * elapsedSeconds;  
      velocity.y += gravity.y * elapsedSeconds;
      
      // Apply acceleration.
      velocity.x += acceleration.x * elapsedSeconds;  
      velocity.y += acceleration.y * elapsedSeconds;
      
      // Apply drag.
      velocity.x += velocity.x * -1.0 * (drag * elapsedSeconds);  
      
      // Determine the translation.
      translation.x = velocity.x * elapsedSeconds;
      translation.y = velocity.y * elapsedSeconds;
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private static final int MILLISECONDS_PER_SECOND = 1000;
   
   private boolean isEnabled;
   
   private int mass;
   
   private Vector2D velocity;

   private Vector2D acceleration;
   
   private Vector2D gravity;
   
   private double drag;
   
   private double friction;
   
   private double elasticity;
}

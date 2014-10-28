package com.toast.game.engine.actor;

import com.toast.game.common.ClassSet;

public class Timer extends Actor
{
   public Timer(
      String id,
      ClassSet classSet,
      double duration,
      boolean isRepeating)
   {
      super(id, classSet);
      
      this.duration = duration;
      this.isRepeating = isRepeating;
      endTime = System.currentTimeMillis() + duration;
   }
   
   
   @Override
   public void update(
      long elapsedTime)
   {
      if (System.currentTimeMillis() >= endTime)
      {
         // onTimeout();
         
         // Determine we should start this timer again.
         if (isRepeating == true)
         {
            endTime = System.currentTimeMillis() + duration;
         }
         else
         {
            // destroy();
         }
      }
   }

   private double endTime;
   
   private double duration;
   
   private boolean isRepeating;
}

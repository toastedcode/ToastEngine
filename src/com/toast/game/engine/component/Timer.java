package com.toast.game.engine.component;

import com.toast.xml.XmlNode;

public class Timer extends Actor
{
   public Timer(XmlNode node)
   {
      super(node);
      
      duration = node.getChild("duration").getDoubleValue();
      isRepeating = node.getChild("isRepeating").getBoolValue();
      endTime = System.currentTimeMillis() + duration;
   }
   
   
   public Timer(
      String id,
      double duration,
      boolean isRepeating)
   {
      super(id);
      
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

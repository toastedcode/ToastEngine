package gameEngine;

public class Timer
{
   public Timer(
      String initTimerId,
      double initDuration,
      boolean initIsRepeating,
      String initSpriteId,
      String initEventId)
   {
      timerId = initTimerId;
      duration = initDuration;
      endTime = System.currentTimeMillis() + duration;
      isRepeating = initIsRepeating;
      spriteId = initSpriteId;
      eventId = initEventId;
   }   
   
   
   public String getTimerId()
   {
      return (timerId);
   }   
   
   
   public void update(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.
      long elapsedTime)
   {
      if (System.currentTimeMillis() >= endTime)
      {
         // Send the event associated with this timer's expiration.
         if (spriteId == null)
         {
            EventManager.broadcastEvent(new Event(eventId));
         }
         else
         {
            EventManager.sendEvent(new Event(eventId), spriteId);            
         }
         
         // Determine we should start this timer again.
         if (isRepeating == true)
         {
            endTime = System.currentTimeMillis() + duration;
         }
         else
         {
            TimerManager.cancelTimer(timerId);
         }
      }
      
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   // A unique identify for a Timer object.
   String timerId;
   
   // A flag specifying if this Timer is repeating (true) or one-shot (false).
   private boolean isRepeating;
   
   // A value specifying how long each iteration of this Timer should last.
   private double duration;
   
   // A value specifying what time this Timer should fire.
   private double endTime;
   
   // The sprite id that should receive an event when this timer fires.
   // Note: If not set, the event will be broadcast.
   private String spriteId;   
   
   // The event id to send/broadcast when this timer fires.
   private String eventId;
}

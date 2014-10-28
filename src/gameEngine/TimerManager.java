package gameEngine;

import java.util.Map.Entry;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimerManager
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      timerMap = new LockableMap<String, Timer>();
   }
   
   
   public static String startTimer(
      double duration,
      boolean isRepeating,
      String eventId)
   {
      return (startTimer(duration, isRepeating, null, eventId));
   }
   

   public static String startTimer(
      double duration,
      boolean isRepeating,
      String spriteId,
      String eventId)
   {
      Timer timer = null;
      String timerId = getNextTimerId();
      
      if (timerMap.containsKey(timerId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate Timer id [%s] found.", timerId));
      }
      else
      {
         timer = new Timer(timerId, duration, isRepeating, spriteId, eventId);
         timerMap.put(timerId, timer);
      }
      
      return (timerId);
   }   
   
   
   public static void cancelTimer(
      String timerId)
   {
      if (timerMap.containsKey(timerId) == false)
      {
         logger.log(Level.WARNING, String.format("Could not find specified Timer id [%s].", timerId));         
      }
      else
      {
         timerMap.remove(timerId);         
      }

   }
   
   
   // An operation that performs all updates on all Timers.
   // (Called from within the main game loop.)   
   public static void updateTimers(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.         
      long elapsedTime)
   {
      Timer timer = null;
      
      timerMap.lock();
      
      Iterator<Entry<String, Timer>> iterator = timerMap.entrySet().iterator();
      while (iterator.hasNext() == true)
      {
         timer = iterator.next().getValue();
         timer.update(elapsedTime);
      }
      
      timerMap.unlock();
   }   

   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   private static String getNextTimerId()
   {
      final String TIMER_ID_PREFIX = "timer";
      
      // Increment the timer count.
      timerCount++;
      
      return (TIMER_ID_PREFIX + Integer.toString(timerCount));
   }   
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(EventManager.class.getName());
   
   // A counter used in creating Timer ids.
   private static int timerCount;
   
   // A map of animations used to quickly retrieving an animation by its id.
   private static LockableMap<String, Timer> timerMap;   

}

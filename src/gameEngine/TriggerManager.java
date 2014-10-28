package gameEngine;

import gameEngine.Trigger.AttributeType;
import gameEngine.Trigger.OperatorType;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;

public class TriggerManager
{
   // **************************************************************************
   //                                  Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      triggerMap = new LockableMap<String, Trigger>();
   }
   
   
   // This operation creates a new Trigger object and adds it into the Trigger map.
   public static Trigger createTrigger(
      String watchedSpriteId,
      String watchingSpriteId,
      AttributeType attribute,
      OperatorType operator,
      Object value,
      String eventId)
   {
      // Initialize the return value.
      Trigger trigger = null;
      
      // Get a unique trigger id.
      String triggerId = getNextTriggerId();
      
      if (triggerMap.containsKey(triggerId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate trigger id [%s] found.", triggerId)); 
      }
      else
      {
         // Create the new trigger.
         trigger = 
            new Trigger(triggerId,
                        watchedSpriteId,
                        watchingSpriteId,
                        attribute,
                        operator,
                        value,
                        eventId);
         
         // Add to the sprite map.
         triggerMap.put(triggerId, trigger);
      }
      
      return (trigger);
   }
   
   
   // This operation creates a new Trigger based on the information contained
   // in the specified XML node.
   static Trigger createTrigger(
      Node node)
   {
      // Create a new Script object using the specified XML node to 
      // initialize it.
      Trigger trigger = new Trigger(node);

      // If we don't yet have a valid Script id ...
      String triggerId = trigger.getTriggerId();
      if (triggerId == null)
      {
         // Generate a new Trigger id and assign it.
         // TODO:  Generate trigger ids automatically.
      }
   
      // Add to the Script map.
      triggerMap.put(triggerId, trigger);

      return (trigger);
   }
   
   
   public static void cancelTrigger(
      String triggerId)
   {
      if (triggerMap.containsKey(triggerId) == false)
      {
         logger.log(Level.WARNING, String.format("Trigger [%s] cannot be found.", triggerId));
      }
      else
      {
         triggerMap.remove(triggerId);
      }
      
   }
   
   
   public static Trigger getTrigger(
      String triggerId)
   {
      // Initialize the return value.
      Trigger trigger = null;
      
      if (triggerMap.containsKey(triggerId) == false)
      {
         logger.log(Level.WARNING, String.format("Trigger [%s] cannot be found.", triggerId));
      }
      else
      {
         trigger = triggerMap.get(triggerId);
      }      
      
      return (trigger);
   }
   
   
   // An operation that performs all updates on all Triggers.
   // (Called from within the main game loop.)   
   public static void updateTriggers(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.         
      long elapsedTime)
   {
      // Lock the internal lists against modification.
      triggerMap.lock();
      
      for (Map.Entry<String, Trigger> entry : triggerMap.entrySet())
      {
         entry.getValue().update(elapsedTime);
      }
      
      // Unlock the internal lists, flushing any additions/subtractions.
      triggerMap.unlock();
   }   
   
   
   public static void freeTriggers()
   {
      triggerMap.clear();
   }   
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private TriggerManager()
   {
      // Exists only to defeat instantiation.
   }
   
   private static String getNextTriggerId()
   {
      final String TRIGGER_ID_PREFIX = "trigger";
      
      // Increment the timer count.
      triggerCount++;
      
      return (TRIGGER_ID_PREFIX + Integer.toString(triggerCount));
   }      
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(TriggerManager.class.getName());
   
   // A counter used in creating Trigger ids.
   private static int triggerCount;
   
   // A map of Script objects used to quickly retrieving a Trigger by its id.
   private static LockableMap<String, Trigger> triggerMap;
   
}

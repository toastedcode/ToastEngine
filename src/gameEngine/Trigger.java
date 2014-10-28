package gameEngine;

import java.lang.Integer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

public class Trigger
{

   // **************************************************************************
   //                            Public Operations
   // **************************************************************************   

   public Trigger(
      String initTriggerId,
      String initWatchedSpriteId,
      String initWatchingSpriteId,
      AttributeType initAttribute,
      OperatorType initOperator,
      Object initValue,
      String initEventId)
   {
      triggerId = initTriggerId;
      watchedSpriteId = initWatchedSpriteId;
      watchingSpriteId = initWatchingSpriteId;
      attribute = initAttribute;
      operator = initOperator;
      value = initValue;
      eventId = initEventId;
      
      previousValue = null;
      isPreviouslyTriggered = false;
   }
   
   
   public Trigger(
      Node node)
   {
      // TODO: Implement
   }
   
   
   public String getTriggerId()
   {
      return (triggerId);
   }
   
   
   public void update(
     double elapsedTime)
   {
      checkTrigger();
      
   }
   
   // **************************************************************************
   //                            Public Attributes
   // **************************************************************************

   public enum AttributeType
   {
      CURRENT_FRAME,
      HEALTH
   }      
   
   public enum OperatorType
   {
      EQUALS,
      GREATER_THAN,
      LESS_THAN,
      CHANGED      
   }
   
   // **************************************************************************
   //                            Private Operations
   // **************************************************************************
   
   private void checkTrigger()
   {
      Sprite watchedSprite = SpriteManager.getSprite(watchedSpriteId);
      if (watchedSprite == null)
      {
         logger.log(Level.WARNING, 
                    String.format("Invalid watched Sprite id [%s].", watchedSpriteId));
         
         TriggerManager.cancelTrigger(triggerId);
      }
      else
      {
         boolean isTriggered = false;
         
         // Retrieve the current value of the watched attribute.
         Object currentValue = getCurrentValue();
         
         switch (operator)
         {
            case EQUALS:
            {
               isTriggered = (currentValue.equals(value));               
               break;
            }
            
            case GREATER_THAN:
            case LESS_THAN:
            {
               // Not supported yet.
               break;
            }
               
            case CHANGED:
            {
               if (previousValue != null)
               {
                  isTriggered = (previousValue.equals(currentValue) == false);
               }
               break;               
            }
            
            default:
            {
               // TODO: Logging
               break;  
            }
         }
         
         // React to a triggered change.
         if (isTriggered == true)
         {
            if (isPreviouslyTriggered == false)
            {
               sendTriggerEvent();
               isPreviouslyTriggered = true;
            }
         }
         else
         {
            isPreviouslyTriggered = false;            
         }
         
         // Remember the current value.
         previousValue = getCurrentValue();
         
      }
   }   
   
   
   Object getCurrentValue()
   {
      // Initialize the return value.
      Object currentValue = null;
      
      Sprite watchedSprite = SpriteManager.getSprite(watchedSpriteId);
      if (watchedSprite == null)
      {
         logger.log(Level.WARNING, 
               String.format("Invalid watched Sprite id [%s].", watchedSpriteId));
    
         TriggerManager.cancelTrigger(triggerId);
      }
      else
      {
         switch (attribute)
         {
            case CURRENT_FRAME:
            {
               currentValue = new Integer(watchedSprite.getCurrentFrame());
               break;
            }
            
            case HEALTH:
            {
               currentValue = new Integer(watchedSprite.getHealth());
               break;
            }            
            
            default:
            {
               break;            
            }
         }
      }
      
      return (currentValue);
   }
   
   
   void sendTriggerEvent()
   {
      Event event = new Event(eventId);
      
      if (watchingSpriteId == null)
      {
         EventManager.broadcastEvent(event);
      }
      else
      {
         EventManager.sendEvent(event, watchingSpriteId);         
      }
      
   }
     
   // **************************************************************************
   //                            Private Attributes
   // **************************************************************************
   
   // Logging
   private final static Logger logger = Logger.getLogger(Trigger.class.getName());      
   
   String triggerId;
   
   String watchedSpriteId;
   
   String watchingSpriteId;
   
   AttributeType attribute;
   
   OperatorType operator;
   
   Object value;
   
   String eventId;
   
   Object previousValue;
   
   boolean isPreviouslyTriggered;
   
}

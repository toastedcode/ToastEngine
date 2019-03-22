package gameEngine;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import simpleXml.XmlNode;

public class EventManager
{
   // **************************************************************************
   //                          Public Clases
   // **************************************************************************

   public class EventHandler implements Callable<Boolean>
   {
      public EventHandler(Event initEvent)
      {
         event = initEvent;
      }
      
      public Boolean call()
      {
         return (false);
      }
      
      Event event;
   }
   
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      spriteRegister = new HashMap<String, HashSet<String>>();
      handlerRegister = new HashMap<String, HashSet<EventHandler>>();
   }   
   
   
   public static void registerSpriteForEvent(
      String spriteId,
      String eventId)
   {
      // If we haven't registered any Sprites for this event, create the
      // Sprite set.
      if (spriteRegister.containsKey(eventId) == false)
      {
         spriteRegister.put(eventId, new HashSet<String>());
      }
      
      // Get the set of Sprites registered for this event.
      HashSet<String> registeredSprites = spriteRegister.get(eventId);
      
      // Check for duplicate registrations.
      if (registeredSprites.contains(spriteId) == true)
      {
         logger.log(
            Level.WARNING, 
            String.format("Sprite [%s] is already registered for event id [%s].", 
               spriteId,
               eventId));
      }
      else
      {
         registeredSprites.add(spriteId);
         
         logger.log(
               Level.INFO, 
               String.format("Sprite [%s] registered for event id [%s].", 
                  spriteId,
                  eventId));
      }

   }
   
   
   public static void unregisterSprite(
      String spriteId)
   {
   }   
   
   
   public static void broadcastEvent(
      Event event)
   {
      String spriteId = null;
      Sprite sprite = null;
      
      if (spriteRegister.containsKey(event.getEventId()) == true)
      {
         HashSet<String> registeredSprites = spriteRegister.get(event.getEventId());
         
         Iterator<String> it = registeredSprites.iterator();
         while(it.hasNext())
         {
            spriteId = it.next();
            sprite = SpriteManager.getSprite(spriteId);
            if (sprite == null)
            {
               logger.log(
                     Level.WARNING, 
                     String.format("Invalid sprite id [%s] found in event register for event id [%s].", 
                        spriteId,
                        event.getEventId()));               
            }
            else
            {
               sprite.queueEvent(event);
            }
         }         
      }
      
   }
   
   
   public static void sendEvent(
      Event event,
      String spriteId)
   {
      Sprite sprite = SpriteManager.getSprite(spriteId);
      if (sprite == null)
      {
         logger.log(
               Level.WARNING, 
               String.format("Invalid sprite id [%s] specified.", 
                  spriteId,
                  event.getEventId()));               
      }
      else
      {
         sprite.queueEvent(event);
      }
      
   }

   
   public static void freeEventRegister()
   {
      spriteRegister.clear();
      handlerRegister.clear();
   }
   
   
   public static void save(
      XmlNode node,
      String name)
   {
      XmlNode eventsNode = null;
      
      for (Map.Entry<String, HashSet<String>> entry : spriteRegister.entrySet())
      {
         if (entry.getValue().contains(name) == true)
         {
            if (eventsNode == null)
            {
               eventsNode = node.appendChild("events");
            }
            
            eventsNode.appendChild("eventId", entry.getKey());
         }
      }
   }   
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(EventManager.class.getName());      
   
   
   // A structure mapping Event ids to lists of Sprite ids.
   // This dictates which Sprites will receive broadcast events. 
   private static HashMap<String, HashSet<String>> spriteRegister;
   
   // A structure mapping Event ids to lists of Callable objects.
   // This provides a way for internal classes to handle events.
   // TODO: Is this necessary?
   private static HashMap<String, HashSet<EventHandler>> handlerRegister;
}

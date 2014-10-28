package gameEngine;

import java.util.HashMap;

public class Event
{
   // **************************************************************************
   //                             Public Classes
   // **************************************************************************
   
   @SuppressWarnings("serial")
   public class EventPayload extends HashMap<String, Object>
   {
   };
   
   // **************************************************************************
   //                             Public Operations
   // **************************************************************************

   public Event(
      String initEventId)
   {
      eventId = initEventId;
      payload = new EventPayload();
   }
   
   
   public String getEventId()
   {
      return (eventId);
   }
   
   
   public void addPayload(
      String name,
      Object value)
   {
      payload.put(name, value);
   }
   
   
   public EventPayload getPayload()
   {
      return (payload); 
   }
   
   
   public Object getPayload(
      String name)
   {
      Object value = null;
      
      if (payload.containsKey(name) == false)
      {
               
      }
      else
      {
         value = payload.get(name);
      }
      
      return (value); 
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private String eventId;
   
   private EventPayload payload;
   
}

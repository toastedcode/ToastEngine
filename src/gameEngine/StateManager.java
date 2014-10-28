package gameEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;

public class StateManager
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      stateMap = new HashMap<String, HashMap<String, String>>();
   }
   
   
   public static boolean hasState(
      String objectId,
      String stateId)
   {
      return ((stateMap.containsKey(objectId) == true) &&
              (stateMap.get(objectId).containsKey(stateId) == true));      
   }
   
   
   public static void setState(
      String objectId,
      String stateId,
      String value)
   {
      if (stateMap.containsKey(objectId) == false)
      {
         stateMap.put(objectId, new HashMap<String, String>());
      }
      
      stateMap.get(objectId).put(stateId, value);
   }
   
   
   public static void setState(
      String objectId,
      String stateId,
      boolean value)
   {
      setState(objectId, stateId, String.valueOf(value));
   }   
   
   
   public static void setState(
      String objectId,
      String stateId,
      int value)
   {
      setState(objectId, stateId, String.valueOf(value));      
   }   
   
   
   public static void setState(
      String objectId,
      String stateId,
      double value)
   {
      setState(objectId, stateId, String.valueOf(value));       
   }   
   
   
   public static String getState(
      String objectId,
      String stateId)
   {
      String value = null;
 
      // Verify that an entry exists for this Sprite/state.
      if (hasState(objectId, stateId) == false)
      {
         logger.log(Level.WARNING, 
                    String.format("State [%s] is not defined for object [%s].", stateId, objectId));            

      }
      else
      {
         value = stateMap.get(objectId).get(stateId);
      }
      
      return (value);
   }
   
   
   public static void save(
      XmlNode node,
      String name)
   {
      XmlNode childNode = null;
      
      if (stateMap.containsKey(name) == true)
      {
         HashMap<String, String> states = stateMap.get(name);
         for (Map.Entry<String, String> entry : states.entrySet())
         {
            childNode = node.appendChild("state", entry.getValue());
            childNode.setAttribute("id", entry.getKey());
         }
      }
   }
   
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(StateManager.class.getName());      
   
   
   // A structure mapping Event ids to lists of Sprite ids.
   // This dictates which Sprites will receive broadcast events. 
   private static HashMap<String, HashMap<String, String>> stateMap;   
}

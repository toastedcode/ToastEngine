package gameEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;

public class ScriptManager
{
   // **************************************************************************
   //                                  Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      scriptMap = new HashMap<String, Script>();
   }
   
   
   // This operation creates a new Script object and adds it into the Script 
   // map.
   public static Script createScript(
      // A unique string used to identify an animation.
      String scriptId,
      String fileName)
   {
      // Initialize the return value.
      Script script = null;
      
      // Does this sprite already exist in the map?
      if (scriptMap.containsKey(scriptId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate script id [%s] found.", scriptId)); 
         script = null;
      }
      else
      {
         // Create the new sprite.
         script = new Script(scriptId, fileName);
         
         // Add to the sprite map.
         scriptMap.put(scriptId, script);
      }
      
      return (script);
   }
   
   
   // This operation creates a new Script based on the information contained
   // in the specified XML node.
   static Script createScript(
      XmlNode node)
   {
      // Create a new Script object using the specified XML node to 
      // initialize it.
      Script script = new Script(node);

      // If we don't yet have a valid Script id ...
      String scriptId = script.getScriptId();
      if (scriptId == null)
      {
         // Generate a new Script id and assign it.
         // TODO:  Generate script ids automatically.
      }
   
      // Add to the Script map.
      scriptMap.put(scriptId, script);

      return (script);
   }
   
   
   public static Script getScript(
      String scriptId)
   {
      // Initialize the return value.
      Script script = null;
      
      if (scriptMap.containsKey(scriptId) == false)
      {
         logger.log(Level.WARNING, String.format("Script [%s] cannot be found.", scriptId));
      }
      else
      {
         script = scriptMap.get(scriptId);
      }      
      
      return (script);
   }
   
   
   public static void save(
      XmlNode node)
   {
      // Loop through the Script map and save each entry.
      for (Map.Entry<String, Script> entry : scriptMap.entrySet())
      {
         entry.getValue().save(node);
      }
   }   
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private ScriptManager()
   {
      // Exists only to defeat instantiation.
   }
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = 
      Logger.getLogger(ScriptManager.class.getName());   
   
   // A map of Script objects used to quickly retrieving an Script by its id.
   private static HashMap<String, Script> scriptMap;
   
}
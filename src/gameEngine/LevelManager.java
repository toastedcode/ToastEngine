package gameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import simpleXml.XmlDocument;
import simpleXml.XmlNode;
import simpleXml.XmlNodeList;

public class LevelManager
{
   // This operation retrieves an instance of the LevelManager class,
   // used in implementing the singleton pattern.
   public static LevelManager getInstance() 
   {
      if(instance == null) 
      {
         instance = new LevelManager();
      }
      
      return instance;
   }   

   
   public gameEngine.Level getCurrentLevel()
   {
      return (currentLevel);
   }
   
   
   public boolean levelExists(
      String levelId)
   {
      return (getLevelIndex(levelId) != sLEVEL_NOT_FOUND);
   }
   
   
   public boolean loadLevelDescriptions()
   {
      // Initialize the return value.
      boolean returnStatus = true;
   
      XmlDocument document = new XmlDocument();
      if (document.load("/resources/levels.xml") == false)
      {
         logger.log(java.util.logging.Level.SEVERE, "Failed to load XML document.");
         returnStatus = false;
      }
      else
      {
         // Gather all level nodes.
         XmlNodeList levelNodes = document.getRootNode().getChildren("level");
         
         if (levelNodes.getLength() > 0)
         {
            // Loop through the list of nodes
            for (int i = 0; i < levelNodes.getLength(); i++)
            {
               XmlNode levelNode = levelNodes.item(i);
               
               levelList.add(new LevelDescription(levelNode.getAttribute("id"),
                                                  levelNode.getChild("filename").getValue()));
            }
         }
      }
      
      return (returnStatus);
   }
   
   
   public boolean loadLevelById(
      String levelId)
   {
      boolean returnStatus = false;
      
      if (levelExists(levelId) == false)
      {
         logger.log(Level.WARNING, 
                    String.format("Undefined level [%s].", levelId));               
      }
      else
      {
         nextLevelFilename = null;
         nextLevelId = levelId;
         returnStatus = true;
      }
      
      return (returnStatus);
   }
  

   public boolean loadLevelByFilename(
      String filename)
   {
      nextLevelId = null;
      nextLevelFilename = filename;
      return (true);
   }
   
   
   public boolean advanceLevel()
   {
      boolean returnStatus = false;
      
      if (currentLevel == null)
      {
         if (levelList.size() == 0)
         {
            logger.log(Level.WARNING, 
                  String.format("No level descriptions were found."));               
         }
         else
         {
            nextLevelId = levelList.get(0).getLevelId();
            returnStatus = true;
         }
      }
      else
      {
         int index = getLevelIndex(currentLevel.getLevelId());
         if (index >= levelList.size())
         {
            logger.log(Level.WARNING, 
                       String.format("Cannot advance beyond last defined level [%s].", currentLevel.getLevelId()));               
         }
         else
         {
            index++;
            nextLevelId = levelList.get(index).getLevelId();
            returnStatus = true;
         }
      }
      
      return (returnStatus);
   }
   
   
   public void updateLevel(
      // A value representing the elapsed time (in milliseconds) since the last update.         
      long elapsedTime)
   {
      if (nextLevelId != null)
      {
         loadLevel(getLevelDescription(nextLevelId).getFilename());
         nextLevelId = null;
      }
      else if (nextLevelFilename != null)
      {
         loadLevel(nextLevelFilename);
         nextLevelFilename = null;
      }
   }
   
   // **************************************************************************
   //                           Private Classes
   // **************************************************************************
   
   private class LevelDescription extends Pair<String, String>
   {
      public LevelDescription(
         String levelId,
         String filename)
      {
         super(levelId, filename);
      }
      
      
      public String getLevelId()
      {
         return (getFirst());
      }

      
      public String getFilename()
      {
         return (getSecond());
      }
   }
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private LevelManager()
   {
      currentLevel = null;
      levelList = new ArrayList<LevelDescription>();
   }

   
   private boolean loadLevel(
      String filename)
   {
      logger.log(Level.INFO, 
                 String.format("Loading new level file [%s].", filename));               
      
      if (currentLevel != null)
      {
         currentLevel.destroy();
      }
      
      currentLevel = new gameEngine.Level(filename);
      SpriteManager.initializeSprites();
      
      Event event = new Event("eventLEVEL_LOADED");
      event.addPayload("levelId", currentLevel.getLevelId());
      EventManager.broadcastEvent(event);

      return (currentLevel != null);
   }
   
   
   private int getLevelIndex(
      String levelId)
   {
      int foundIndex = sLEVEL_NOT_FOUND;
      
      for (int index = 0; index < levelList.size(); index++)
      { 
         if (levelList.get(index).getLevelId().equals(levelId) == true)
         {
            foundIndex = index;
            break;
         }
      }
      
      return (foundIndex);
   }
   
   
   private LevelDescription getLevelDescription(
      String levelId)
   {
      LevelDescription LevelDescription = null;
      
      int index = getLevelIndex(levelId);
      if (index != -1)
      {
         LevelDescription = levelList.get(index);
      }
      
      return (LevelDescription);
   }
  

   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(LevelManager.class.getName());
   
   private final static int sLEVEL_NOT_FOUND = -1;
   
   // A static instance of the LevelManager class, used in implementing the 
   // singleton pattern.
   private static LevelManager instance = null;   
   
   // The current level being executed.
   private List<LevelDescription> levelList;
      
   // The current level being executed.
   private gameEngine.Level currentLevel;
   
   // The next level to load (by id).
   private String nextLevelId;
   
   // The next level to load (by filename).
   private String nextLevelFilename;
}

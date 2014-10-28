package gameEngine;

import java.awt.Dimension;
import java.util.logging.Logger;
import simpleXml.XmlDocument;
import simpleXml.XmlNode;
import simpleXml.XmlNodeList;

public class Level
{
   // **************************************************************************
   //                           Public Operations
   // **************************************************************************
   
   // Default constructor
   public Level()
   {
      document = null;
      levelId = null;
      worldDimensions = null;
   }
   
   
   // This operation creates a new Level object and initializes it using the
   // specified XML file.
   public Level(
      String fileName)
   {
      load(fileName);
   }
   
   
   public String getLevelId()
   {
      return (levelId);
   }
   
   
   public Dimension getWorldDimensions()
   {
      return (worldDimensions);
   }
   
   
   public boolean load(
      String filename)
   {
      boolean returnStatus = true;
      
      document = new XmlDocument();
      if (document.load(filename) == false)
      {
         logger.log(java.util.logging.Level.SEVERE, "Failed to load XML document."); 
         returnStatus = false;
      }
      else
      {
         // Get the root node of the document.
         XmlNode rootNode = document.getRootNode();
         
         // Store the level id.
         levelId = rootNode.getAttribute("id");
         
         // Store the world dimensions.
         if (rootNode.hasChild("worldDimensions") == true)
         {
            worldDimensions = XmlUtils.getDimension(rootNode.getChild("worldDimensions"));
         }
         else
         {
            worldDimensions = Renderer.getScreenDimensions();
         }
         
         // Store states associated with the level.
         XmlNodeList stateNodes = rootNode.getNodes("./state"); 
         for (int i = 0; i < stateNodes.getLength(); i++)
         {
           XmlNode stateNode = stateNodes.item(i);
           StateManager.setState("global", 
                                 stateNode.getAttribute("id"), 
                                 stateNode.getValue());
         }      
      
         // Load all resources defined in this level.
         returnStatus &= loadResources();
         
         // Create all animations defined in this level.
         returnStatus &= loadAnimations();
         
         // Create all Sprites defined in this level.
         returnStatus &= loadSprites();
         
         // Create all TileMaps defined in this level.
         returnStatus &= loadTileMaps();         
         
         // Create the view defined in this level.
         returnStatus &= loadViews();         
      }
      
      return (returnStatus);
   }
   
   
   public boolean save(
      String fileName)
   {
      boolean returnStatus = true;
      
      // A temporary node object.
      XmlNode node = null;
      
      // Create the document and root node.
      XmlDocument saveDoc = new XmlDocument();
      if (saveDoc.createRootNode("level") == false)
      {
         logger.log(java.util.logging.Level.WARNING, 
                    String.format("Failed to create root node for level [%s].", levelId));         
      }
      else
      {
         XmlNode rootNode = saveDoc.getRootNode();
         rootNode.setAttribute("id", levelId);
         
         // World dimensions
         node = saveDoc.getRootNode().appendChild("worldDimensions");
         XmlUtils.setDimension(node, worldDimensions);
         
         // Views.
         ViewManager.save(rootNode);
         
         // Global states
         StateManager.save(rootNode, "global");
         
         // Images/Animations
         AnimationManager.saveImages(rootNode);
         AnimationManager.saveAnimations(rootNode);
         
         // Scripts
         ScriptManager.save(rootNode);
         
         // Sfx
         SfxManager.getInstance().save(rootNode);
         
         // Sprites
         SpriteManager.save(rootNode);
         
         // Write to a file.
         returnStatus = saveDoc.save(fileName);
      }
      
      if (returnStatus == true)
      {
         logger.log(java.util.logging.Level.INFO, 
               String.format("Saved level [%s] to file [%s].", levelId, fileName));
      }
      else
      {
         logger.log(java.util.logging.Level.WARNING, 
               String.format("Failed to save level [%s].", levelId));
      }
      
      return (returnStatus);
   }
   
   
   public void destroy()
   {
      // Free up all level objects in memory.
      AnimationManager.freeImages();
      AnimationManager.freeAnimations();
      SfxManager.getInstance().freeSfx();
      SpriteManager.freeSprites();
      TriggerManager.freeTriggers();
      EventManager.freeEventRegister();
   }
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   private boolean loadResources()
   {
      boolean returnStatus = true;
      
      XmlNodeList resourceNodes = document.getRootNode().getNodes("//resource");
      
      // Loop through the list of nodes
      for (int i = 0; i < resourceNodes.getLength(); i++)
      {
        XmlNode node = resourceNodes.item(i);
        String resourceType = node.getAttribute("type");
        
        if (resourceType == null)
        {
           logger.log(java.util.logging.Level.WARNING, "Resource type was not specified.");
           returnStatus = false;
        }
        else if (resourceType.equalsIgnoreCase("image") == true)
        {
           returnStatus &= (AnimationManager.createImage(node) != null);
        }
        else if (resourceType.equalsIgnoreCase("script") == true)
        {
           returnStatus &= (ScriptManager.createScript(node) != null);
        }
        else if (resourceType.equalsIgnoreCase("sfx") == true)
        {
           returnStatus &= (SfxManager.getInstance().createSfx(node) != null);
        }
        else
        {
           logger.log(java.util.logging.Level.WARNING, 
                      String.format("Unrecognized resource type: \"%s\".", resourceType));       
        }
        
      }
      
      return (returnStatus);
   }
   
   
   private boolean loadAnimations()
   {
      boolean returnStatus = true;
      
      XmlNodeList animationNodes = document.getRootNode().getChildren("animation");
      
      // Loop through the list of nodes
      for (int i = 0; i < animationNodes.getLength(); i++)
      {
        XmlNode node = animationNodes.item(i);
        returnStatus &= (AnimationManager.createAnimation(node) != null);
      }
          
      return (returnStatus);
   }   

   
   private boolean loadSprites()
   {
      boolean returnStatus = true;
      
      XmlNodeList spriteNodes = document.getRootNode().getChildren("sprite");
      
      // Loop through the list of nodes
      for (int i = 0; i < spriteNodes.getLength(); i++)
      {
        XmlNode node = spriteNodes.item(i);
        
        if (Boolean.valueOf(node.getAttribute("isTemplate")) == true)
        {
           returnStatus &= (SpriteManager.createSpriteTemplate(node) == true);           
        }
        else
        {
           returnStatus &= (SpriteManager.createSprite(node) != null);
        }
      }
          
      return (returnStatus);
   }
   
   
   private boolean loadTileMaps()
   {
      boolean returnStatus = true;
      
      TileMap tileMap = null;
      
      XmlNodeList tileMapNodes = document.getRootNode().getChildren("tileMap");
      
      // Loop through the list of nodes
      for (int i = 0; i < tileMapNodes.getLength(); i++)
      {
        // Create Sprites at the positions defined in the TileMap. 
        tileMap = new TileMap(tileMapNodes.item(i));
        tileMap.createSprites();
      }
          
      return (returnStatus);
   }   
   
   
   private boolean loadViews()
   {
      boolean returnStatus = true;
      
      XmlNodeList viewNodes = document.getRootNode().getChildren("view");
      
      // Loop through the list of nodes
      for (int i = 0; i < viewNodes.getLength(); i++)
      {
         XmlNode node = viewNodes.item(i);
         returnStatus &= (ViewManager.createView(node) != null);           
      }
          
      return (returnStatus);
   }   
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(Level.class.getName());   
   
   // An XML document representing the level.
   XmlDocument document;
   
   // A unique identifier describing the level.
   String levelId;
   
   // The height/width of the world defined by this Level.
   Dimension worldDimensions;
   
}

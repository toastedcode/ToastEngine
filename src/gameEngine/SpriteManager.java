package gameEngine;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import simpleXml.XmlNode;

public class SpriteManager
{
   // **************************************************************************
   //                                  Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      spriteMap = new LockableMap<String, Sprite>();
      drawList = new SpriteList();
      templateList = new SpriteList();
      templateMap = new HashMap<String, XmlNode>();
      selectionList = new SpriteList();
      
      drawList.setComparator(Z_ORDER_DESCENDING_COMPARATOR);
      templateList.setComparator(ALPHABETICAL_COMPARATOR);
      selectionList.setComparator(Z_ORDER_ASCENDING_COMPARATOR);
   }


   public static boolean createSpriteTemplate(
      XmlNode node)
   {
      // Initialize the return value.
      boolean returnStatus = false;
      
      // Extract the spriteId.
      String spriteId = node.getAttribute("id");
      
      if (spriteId == null)
      {
         logger.log(Level.WARNING, String.format("No Sprite id specified in node [%s].", node.toString()));          
      }
      else if (templateMap.containsKey(spriteId) == true)
      {
         logger.log(Level.WARNING, String.format("Template already exists for Sprite [%s].", spriteId));         
      }
      else
      {
         templateMap.put(spriteId, node);
         returnStatus = true;
      }
      
      return (returnStatus);
   }
   
   
   // This operation creates a new Sprite object and adds it into the sprite 
   // map.
   public static Sprite createSprite(
      // A unique string used to identify a sprite.
      String spriteId)
   {
      // Initialize the return value.
      Sprite sprite = null;
      
      // Does this sprite already exist in the map?
      if (spriteMap.containsKey(spriteId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate sprite id [%s] found.", spriteId)); 
      }
      else
      {
         // Create the new sprite.
         sprite = new Sprite(spriteId);
         
         // Add the Sprite to the spriteMap and drawList.
         addSprite(sprite);
         
         logger.log(Level.INFO, String.format("Created sprite [%s]", spriteId));          
      }
      
      return (sprite);
   }
   
   
   // This operation creates a new Sprite based on the information contained
   // in the specified XML node.
   public static Sprite createSprite(
      XmlNode node)
   {
      Sprite sprite = null;
      
      try
      {
         //
         // Determine the type of Sprite to create.
         //
         
         Class<?> objectClass = Sprite.class;
         
         if (node.hasAttribute("objectClass") == true)
         {
            String className = node.getAttribute("objectClass");
            try
            {
               objectClass = Class.forName(className);
            }
            catch(ClassNotFoundException e)
            {
               logger.log(Level.WARNING, String.format("No object class \"%s\" defined.  Defaulting to Sprite class."));
            }
         }
               
         // Create a new Sprite object using the specified XML node to 
         // initialize it.
         //sprite = new Sprite(node);
         Class<?> parameters[] = {XmlNode.class};
         sprite = (Sprite)objectClass.getDeclaredConstructor(parameters).newInstance(node);
         
         // Retrieve the Sprite id.
         String spriteId = sprite.getSpriteId();
      
         // Add the Sprite to the appropriate lists.
         addSprite(sprite);
         
         logger.log(Level.INFO, String.format("Created sprite [%s]", spriteId));
      }
      catch(IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e)
      {
         logger.log(Level.WARNING, String.format("Failed to create Sprite.  Exception: %s", e.toString()));         
      }

      return (sprite);
   }

   
   // This operation creates a new Sprite using the properties of an existing
   // Sprite as a template.   
   public static Sprite cloneSprite(
      String spriteId,
      String parentSpriteId)
   {
      // Initialize the return value.
      Sprite sprite = null;
      
      // Does the template Sprite exist in the map?
      if (templateMap.containsKey(spriteId) == false)
      {
         logger.log(Level.WARNING, String.format("Could not find template Sprite [%s].", spriteId)); 
      }
      else
      {
         // Create the new sprite using an existing Sprite as a template.
         sprite = new Sprite(getNextSpriteId(), templateMap.get(spriteId));
         
         // Set the parent Sprite.
         sprite.setParentId(parentSpriteId);
                  
         // Add the Sprite to the spriteMap and drawList.
         addSprite(sprite);
         
         logger.log(Level.INFO, 
                    String.format("Cloned Sprite [%s] from template Sprite [%s]", 
                                  sprite.getSpriteId(), 
                                  spriteId));
         
         // Initialize.
         sprite.evaluateScript(Script.ScriptFunction.INIT);
      }
      
      return (sprite);
   }
   
   
   public static void destroySprite(
      String spriteId)
   {
      if (spriteExists(spriteId) == false)
      {
         logger.log(Level.WARNING, 
                    String.format("Attempt to destroy non-existing Sprite [%s]", spriteId));          
      }
      else
      {
         logger.log(Level.INFO, 
                    String.format("Destroying Sprite [%s]", 
                                  spriteId));
         
         Sprite sprite = getSprite(spriteId);
         
         sprite.evaluateScript(Script.ScriptFunction.DESTROY);
         
         removeSprite(getSprite(spriteId));
      }
   }
   
   
   public static boolean spriteExists(
      String spriteId)
   {
      return(spriteMap.containsKey(spriteId) == true);   
   }
   
   
   public static Sprite getSprite(
      String spriteId)
   {
      // Initialize the return value.
      Sprite sprite = null;
      
      if (spriteMap.containsKey(spriteId) == false)
      {
         logger.log(Level.WARNING, String.format("Sprite [%s] cannot be found.", spriteId));
      }
      else
      {
         sprite = spriteMap.get(spriteId);
      }      
      
      return (sprite);
   }
   
   
   public static SpriteList getSprites()
   {
      return (drawList);
   }
   
   
   static Iterator<Sprite> getIterator()
   {
     return (drawList.iterator());   
   }

   
   // An operation that performs initialization on all Sprites.
   public static void initializeSprites()
   {
      // Lock the internal lists against modification.
      drawList.lock();       
      
      for (Sprite sprite : drawList)
      {
         sprite.evaluateScript(Script.ScriptFunction.INIT);
      }
      
      // Unlock the internal lists, flushing any additions/subtractions.
      drawList.unlock();
   }   

   
   // An operation that performs all updates on all Sprites.
   // (Called from within the main game loop.)   
   public static void updateSprites(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.         
      long elapsedTime)
   {
      // Lock the internal lists against modification.
      drawList.lock();
      
      for (Sprite sprite : drawList)
      {
         sprite.update(elapsedTime);
      }
      
      // Unlock the internal lists, flushing any additions/subtractions.
      drawList.unlock();
   }
   
   
   // An operation that draws all visible Sprites.
   // (Called from within the main game loop.)   
   public static void drawSprites()
   {
      // Lock the internal lists against modification.
      drawList.lock();
      
      for (Sprite sprite : drawList)
      {
         sprite.draw();
      }
      
      // Unlock the internal lists, flushing any additions/subtractions.
      drawList.unlock();
   }
   
   
   public static void freeSprites()
   {
      spriteMap.lock();
      drawList.lock();
      templateList.lock();
      selectionList.lock();
      
      spriteMap.clear();
      drawList.clear();
      templateList.clear();
      templateMap.clear();
      selectionList.clear();
      
      spriteMap.unlock();
      drawList.unlock();
      templateList.unlock();
      selectionList.unlock();
   }
   
   
   public static void save(
      XmlNode node)
   {
      // Loop through the Sprite map and save each entry.
      for (Map.Entry<String, Sprite> entry : spriteMap.entrySet())
      {
         entry.getValue().save(node);
      }
   }
   
   // **************************************************************************
   //                            Sprite templates
   
   public static SpriteList getTemplates()
   {
      return (templateList);
   }
   
   // **************************************************************************
   //                            Sprite selection
   
   public static void selectSprite(
      Sprite sprite)
   {
      sprite.select();
      selectionList.add(sprite);
   }
   
   
   public static void selectSprites(
      SpriteList initSelectionList)
   {
      
      for (Sprite sprite : initSelectionList)
      {
         selectSprite(sprite);
      }
   }
   
   
   public static void deselectSprite(
      Sprite sprite)
   {
      sprite.deselect();
      selectionList.remove(sprite);
   }
   
   
   public static void clearSelectedSprites()
   {
      selectionList.lock();
      for (Sprite sprite : selectionList)
      {
         deselectSprite(sprite);
      }
      selectionList.unlock();
   }

   
   public static SpriteList getSelectedSprites()
   {
      return (selectionList);
   }
   
   // **************************************************************************
   //                           Public Attributes
   // **************************************************************************   
   
   static final Comparator<Sprite> Z_ORDER_DESCENDING_COMPARATOR = new Comparator<Sprite>()
   {
      public int compare(Sprite sprite1, Sprite sprite2)
      {
         return(((sprite1.getZOrder() > sprite2.getZOrder()) ? -1 :
                 (sprite1.getZOrder() == sprite2.getZOrder()) ? 0 : 1));
      }
   };
   
   static final Comparator<Sprite> Z_ORDER_ASCENDING_COMPARATOR = new Comparator<Sprite>()
   {
      public int compare(Sprite sprite1, Sprite sprite2)
      {
         return(((sprite1.getZOrder() < sprite2.getZOrder()) ? -1 :
                 (sprite1.getZOrder() == sprite2.getZOrder()) ? 0 : 1));
      }
   };   
   
   
   static final Comparator<Sprite> ALPHABETICAL_COMPARATOR = new Comparator<Sprite>()
   {
      public int compare(Sprite sprite1, Sprite sprite2)
      {
         return (sprite1.getSpriteId().compareToIgnoreCase(sprite2.getSpriteId()));
      }
   };
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private SpriteManager()
   {
      // Exists only to defeat instantiation.
   }
   
   
   private static String getNextSpriteId()
   {
      final String SPRITE_ID_PREFIX = "spr";
      
      // Increment the Sprite count.
      spriteCount++;
      
      return (SPRITE_ID_PREFIX + Integer.toString(spriteCount));
   }
   
   
   private static void addSprite(
      Sprite sprite)
   {
      String spriteId = sprite.getSpriteId();
      if (spriteMap.containsKey(spriteId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate Sprite id [%s] found.", spriteId));             
      }
      else
      {
         // Add to the Sprite map.
         spriteMap.put(spriteId, sprite);
         
         if (sprite.isTemplate() == true)
         {
            // Add the Sprite to the draw list.
            // Note: this is kept alphabetically ordered.
            templateList.add(sprite);
         }
         else
         {
            // Add the Sprite to the draw list.
            // Note: this is kept z-ordered for use in drawing.
            drawList.add(sprite);
         }
      }
   }
   
   
   private static void removeSprite(
      Sprite sprite)
   {
      // Remove from Sprite map.
      String spriteId = sprite.getSpriteId();
      if (spriteMap.containsKey(spriteId) == false)
      {
         logger.log(Level.WARNING, String.format("Sprite [%s] not found in Sprite map.", spriteId));            
      }
      else
      {
         spriteMap.remove(spriteId);
      }
      
      // Remove from the draw list.
      if (templateList.contains(sprite) == true)
      {
         templateList.remove(sprite);
      }
      else if (drawList.contains(sprite) == true)
      {
         drawList.remove(sprite);
      }
   }      
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = 
      Logger.getLogger(SpriteManager.class.getName());
   
   // A counter used in creating Sprite ids.
   private static int spriteCount;
   
   // A map of Sprites used to quickly retrieving a Sprite by its id.
   private static LockableMap<String, Sprite> spriteMap;
   
   // A z-ordered (descending) list of currently visible Sprites, used in drawing Sprites.
   private static SpriteList drawList;
   
   // A z-ordered (ascending) list of currently selected Sprites.
   private static SpriteList selectionList;
   
   // An alphabetically ordered list of "template" Sprites.
   private static SpriteList templateList;
   
   // A map of XML nodes used in creating Sprites from templates.
   private static Map<String, XmlNode> templateMap;
}

package gameEngine;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;
import simpleXml.XmlNodeList;

public class TileMap
{
   // **************************************************************************
   //                           Public Operations
   // **************************************************************************
   
   // Default constructor
   public TileMap()
   {
   }
   
   
   // Constructor, via XML node.
   public TileMap(
      XmlNode node)
   {
      // Initialize all attributes with values from the XML node.
      initialize(node);
   }
   
   
   // This operation creates Sprites based on the currently defined tiles.
   public void createSprites()
   {
      // Loop through each symbol in the 
      for (int row = 0; row < tileMapArray.size(); row++)
      {
         for (int col = 0; col < tileMapArray.get(row).length(); col++)
         {
            Character symbol = tileMapArray.get(row).charAt(col);
            if (Character.isWhitespace(symbol) == false)
            {
               String spriteId = spriteKey.get(symbol);
               if (spriteId == null)
               {
                  logger.log(Level.WARNING, String.format("No Sprite associated with symbol [%s]", symbol));               
               }
               else
               {
                  // Attempt to create a new Sprite based on the template specified by the TileMap.
                  Sprite clonedSprite = SpriteManager.cloneSprite(spriteId, null);
                  
                  if (clonedSprite != null)
                  {
                     // Calculate the position of this new Sprite.
                     Point2D spritePosition = 
                        new Point2D.Double(
                              (position.getX() + (col * tileDimensions.getWidth())),
                              (position.getY() + (row * tileDimensions.getHeight())));
                     
                     // Set the position.
                     clonedSprite.setPosition(spritePosition);
                  }
               }  // end if (spriteId == null)
            }  // end if (Character.isWhitespace(symbol) == false)
         }  // end for (int col = 0; col < tileMapArray.get(row).length(); col++)
      }  // end for (int row = 0; row < tileMapArray.size(); row++)
      
   }
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   private void initialize()
   {
      position = new Point2D.Double(0, 0);
      tileDimensions = new Dimension(0, 0);
      spriteKey = new HashMap<Character, String>();  
      tileMapArray = new ArrayList<String>();
      
   }

   
   private void initialize(
      XmlNode node)
   {
      // First, initialize all attributes to their default values.
      initialize();
      
      // position
      if (node.hasChild("position") == true)
      {
         position.setLocation(XmlUtils.getPoint2D(node.getChild("position")));
      }
      
      // tileDimensions
      setTileDimensions(XmlUtils.getDimension(node.getChild("tileDimensions")));
      
      // spriteKey
      if (node.hasChild("spriteKey") == true)
      {
         // Get the list of eventId child nodes
         XmlNodeList keyNodes = node.getChild("spriteKey").getChildren("key"); 
         
         // Loop through the list of nodes
         for (int i = 0; i < keyNodes.getLength(); i++)
         {
           XmlNode keyNode = keyNodes.item(i);
           Character symbol = keyNode.getChild("symbol").getCharacterValue();
           String spriteId = keyNode.getChild("spriteId").getValue();
           spriteKey.put(symbol, spriteId);
         }
      }
      
      // tileMapArray
      if (node.hasChild("filename") == true)
      {
         tileMapArray = ResourceManager.loadTextFile(node.getChild("filename").getValue());
      }
      
   }
  
   
   void setTileDimensions(
      Dimension initDimensions)
   {
      tileDimensions = initDimensions;   
   }
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(Level.class.getName());
   
   // The top, left corner of the TileMap.
   Point2D position;
   
   // The dimensions of a single tile in the TileMap.
   private Dimension tileDimensions;
   
   // A map of symbols to sprite ids.  
   // Each character in a TileMap array maps to a Sprite template, represented by a sprite id.
   private Map<Character, String> spriteKey;
   
   // An array of Strings, used to store the actual TileMap.
   // Note: Each character in a string represents an individual tile. 
   private List<String> tileMapArray;
   
}

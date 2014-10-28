package gameEngine;

import gameEngine.Animation.AnimationType;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;

public class AnimationManager
{
   // **************************************************************************
   //                             Public Operations
   // **************************************************************************
   
   // This operation creates the internal structures used in storing Animation.
   public static void initialize()
   {
      imageMap = new HashMap<String, Image>();
      animationMap = new HashMap<String, Animation>();
   }
   
   // **************************************************************************
   //                                  Images   
   
   public static Image createImage(
      XmlNode node)
   {
      Image image = new Image(node);
      
      String imageId = image.getImageId();
      if (imageId == null)
      {
         logger.log(Level.WARNING, String.format("No id specified for Image."));
         image = null;
      }
      else if (imageMap.containsKey(imageId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate Image id [%s] found.", imageId)); 
         image = null;
      }
      else
      {
         imageMap.put(imageId, image);         
      }
      
      return (image);      
   }     

   
   public static Image getImage(
      String imageId)
   {
      Image image = null;
      
      if (imageMap.containsKey(imageId) == false)
      {
         logger.log(Level.WARNING, 
                    String.format("Image [%s] cannot be found.", imageId));
      }
      else
      {
         image = imageMap.get(imageId);
      }
      
      return (image);
   }
   
   
   public static void freeImages()
   {
      imageMap.clear();
   }
   
   
   public static void saveImages(
      XmlNode node)
   {
      // Loop through the Image map and save each entry.
      for (Map.Entry<String, Image> entry : imageMap.entrySet())
      {
         entry.getValue().save(node);
      }
   }   
   
   // **************************************************************************
   //                                Animations
   
   // This operation creates a new Animation object and adds it into the animation map.
   public static Animation createAnimation(
      // A unique string used to identify an animation.
      String animationId,
      // The id of the image used to display this animation.
      String imageId,
      // The number of animation rows contained in the image.
      int numRows,
      // The number of animation columns contained in the image.
      int numColumns,
      // The first frame (in the image) that will be used in the animation.
      int startFrame,
      // The last frame (in the image) that will be used in the animation.
      int endFrame,
      // The direction of animation.
      // 0 == no animation 
      // 1 = forward
      // -1 = reverse
      int animationDirection,
      // An enumeration determining how the Animation should animate
      AnimationType animationType,
      // The speed (frames per second) at which the Animation should be played.      
      int frameRate)
   {
      // Initialize the return value.
      Animation animation = null;
      
      // Does this animation already exist in the map?
      if (animationMap.containsKey(animationId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate animation id [%s] found.", animationId)); 
      }      
      else
      {
         // Create the new animation.
         animation = new Animation(
               animationId,
               imageId,
               numRows,
               numColumns,
               startFrame,
               endFrame,
               animationDirection,
               animationType,
               frameRate);

            // Add to the sprite map.
            animationMap.put(animationId, animation);
      }

      return (animation);
   }
   
   
   // This operation creates a new Animation based on the information contained
   // in the specified XML node.
   static Animation createAnimation(
      XmlNode node)
   {
      // TODO: Perform validation on XML nodes.
      String animationId = node.getAttribute("id");
      String imageId = node.getChild("imageId").getValue();
      int numRows = node.getChild("numRows").getIntValue();
      int numColumns = node.getChild("numColumns").getIntValue();
      int startFrame = node.getChild("startFrame").getIntValue(); 
      int endFrame = node.getChild("endFrame").getIntValue(); 
      int animationDirection = node.getChild("animationDirection").getIntValue(); 
      AnimationType animationType = Animation.AnimationType.valueOf(node.getChild("animationType").getValue());
      int frameRate = node.getChild("frameRate").getIntValue();      
      
      return (createAnimation(
                 animationId,
                 imageId,
                 numRows,
                 numColumns,
                 startFrame,
                 endFrame,
                 animationDirection,
                 animationType,
                 frameRate));
   }
   
   
   // Retrieves the specified Animation from the animation map.
   public static Animation getAnimation(
      // The id of the Animation to retrieve.
      String animationId)
   {
      // Initialize the return value.
      Animation animation = null;
      
      if (animationMap.containsKey(animationId) == false)
      {
         logger.log(Level.WARNING, String.format("Animation [%s] cannot be found.", animationId));
      }
      else
      {
         animation = animationMap.get(animationId);
      }      
      
      return (animation);
   }
   
   
   public static void freeAnimations()
   {
      animationMap.clear();
   }
   
   
   public static void saveAnimations(
      XmlNode node)
   {
      // Loop through the Animation map and save each entry.
      for (Map.Entry<String, Animation> entry : animationMap.entrySet())
      {
         entry.getValue().save(node);
      }      
   }
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private AnimationManager()
   {
      // Exists only to defeat instantiation.
   }
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = 
      Logger.getLogger(AnimationManager.class.getName());
   
   // A map of images used to quickly retrieve an image by its id.
   private static Map<String, Image> imageMap;   
   
   // A map of animations used to quickly retrieve an animation by its id.
   private static HashMap<String, Animation> animationMap;
}
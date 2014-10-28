package gameEngine;

import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;

public class Animation
{
   // **************************************************************************
   //                          Public Attributes
   // **************************************************************************
   
   
   public enum AnimationType
   {
      ANIMATION_NONE,
      ANIMATION_NORMAL,
      ANIMATION_LOOP,
      ANIMATION_BOUNCE      
   }
   
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   // Constructor
   public Animation(
         String initAnimationId,
         String initImageId,
         int initNumRows,
         int initNumColumns,
         int initStartFrame,
         int initEndFrame,
         int initAnimationDirection,
         AnimationType initAnimationType,
         int initFrameRate)
   {
      animationId = initAnimationId;
      imageId = initImageId;
      numRows = initNumRows;
      numColumns = initNumColumns;
      startFrame = initStartFrame;
      endFrame = initEndFrame;
      animationDirection = initAnimationDirection;
      animationType = initAnimationType;
      frameRate = initFrameRate;
      
      // Validate the start/end frames.
      if (startFrame > endFrame)
      {
         logger.log(Level.WARNING, 
                    String.format("Invalid start [%d] and end [%d] frames for Animation [%s].", 
                                  startFrame,
                                  endFrame,
                                  animationId));
         
       
         // Disable the animation.
         animationType = AnimationType.ANIMATION_NONE;
         animationDirection = 0;
      }
      else
      {
         // Determine the animation direction based on the start and end frames.
         animationDirection = (endFrame >= startFrame) ? 1 : -1;
      }
   }
   
   
   public void save(
      XmlNode node)
   {
      // Create the animation node.
      XmlNode animationNode = node.appendChild("animation");
      
      // animationId
      animationNode.setAttribute("id", animationId);
      
      // imageId
      animationNode.appendChild("imageId", imageId);

      // numRows
      animationNode.appendChild("numRows", String.valueOf(numRows));
      
      // numColumns
      animationNode.appendChild("numColumns", String.valueOf(numColumns));
      
      // startFrame
      animationNode.appendChild("startFrame", String.valueOf(startFrame));
      
      // endFrame
      animationNode.appendChild("endFrame", String.valueOf(endFrame));
      
      // animationDirection
      animationNode.appendChild("animationDirection", String.valueOf(animationDirection));
      
      // animationType
      animationNode.appendChild("animationType", animationType.toString());
      
      // frameRate
      animationNode.appendChild("frameRate", String.valueOf(frameRate));      
   }

   
   public AnimationType getAnimationType()
   {
      return(animationType);
   }
   
   
   public String getAnimationId()
   {
      return (animationId);
   }   
   
   
   public String getImageId()
   {
      return (imageId);
   }
   
   
   public Image getImage()
   {
      return (AnimationManager.getImage(imageId));
   }
   
   
   public int getNumColumns()
   {
      return (numColumns);
   }

   
   public int getWidth()
   {
      return (getImage().getWidth() / numColumns);
   }
   
   
   public int getHeight()
   {
      return (getImage().getHeight() / numRows);
   }
   

   public boolean isValidFrame(
      int frame)
   {
      return ((frame >= startFrame) &&
              (frame <= endFrame));
   }   

   
   // This operation determines if it is time to advance the current frame of the animation.
   // TODO: This isn't right.  A very long frame rate will cause the animation not to advance at all.
   public boolean shouldAdvanceFrame(
      long elapsedTime)
   {
      return (elapsedTime >= (MILLISECONDS_PER_SECOND / frameRate));
   }

   
   // Determines the first frame of the animation.
   public int getFirstFrame()
   {
      int firstFrame = 0;
      
      if (animationDirection < 0)
      {
         firstFrame = endFrame;
      }
      else
      {
         firstFrame = startFrame;         
      }
      
      return (firstFrame);
   }
      
   
   // Determines the next frame of the animation, based on the supplied current animation frame. 
   public int getNextFrame(
      // The current animation frame.
      int currentFrame)
   {
      // Initialize the return value.
      int nextFrame = currentFrame;      
      
      switch (animationType)
      {
         case ANIMATION_NONE:
         {
            // No change.
            break;
         }
         
         case ANIMATION_NORMAL:
         {
            // Increment/decrement the current frame based on the animation direction.
            nextFrame = currentFrame += animationDirection;      

            if (animationDirection > 0)
            {
               nextFrame = (nextFrame > endFrame) ? endFrame : nextFrame;   
            }
            else if (animationDirection < 0)
            {
               nextFrame = (nextFrame < endFrame) ? endFrame : nextFrame;               
            }
            break;
         }

         case ANIMATION_LOOP:
         {
            // Increment/decrement the current frame based on the animation direction.
            nextFrame = currentFrame += animationDirection;      
            
            if (animationDirection > 0)
            {
               nextFrame = (nextFrame > endFrame) ? startFrame : nextFrame;   
            }
            else if (animationDirection < 0)
            {
               nextFrame = (nextFrame < endFrame) ? startFrame : nextFrame;               
            }            
            break;
         }

         case ANIMATION_BOUNCE:
         {
            // Increment/decrement the current frame based on the animation direction.
            nextFrame = currentFrame += animationDirection;      
            
            if ((nextFrame > endFrame) || (nextFrame < startFrame))
            {
               animationDirection *= -1;
               nextFrame = nextFrame + animationDirection;
            }
            break;
         }
      }
      
      return (nextFrame);
   }
   
   
   // Determines if the animation has finished, based on the supplied current frame.
   public boolean isFinished(
      int currentFrame)
   {
      // Does this animation have a logical end?
      boolean isFiniteAnimation = ((animationType == AnimationType.ANIMATION_NONE) ||
                                   (animationType == AnimationType.ANIMATION_NORMAL));
      
      // Determine the last frame of the animation.
      boolean isFinalFrame =        
         (animationDirection > 0) ? 
            (currentFrame == endFrame) :
            (currentFrame == startFrame);
               
      return ((isFiniteAnimation == true) && 
              (isFinalFrame == true));
   }
      
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   // Logging
   private final static Logger logger = Logger.getLogger(Animation.class.getName());     
   
   // A constant specifying the number of milliseconds in a second.
   private static final int MILLISECONDS_PER_SECOND = 1000;   
   
   // A unique identifier for an animation.   private String animationId;
   
   // The id of the image used to display this animation.
   // Note: This is typically a sprite map, containing multiple frames of an animation in one image.
   private String imageId;
   
   // The number of animation rows contained in the image.
   private int numRows;
   
   // The number of animation columns contained in the image.
   private int numColumns;
   
   // The first frame (in the image) that will be used in the animation.
   // Note: startFrame should always be <= endFrame.
   private int startFrame;
   
   // The last frame (in the image) that will be used in the animation.
   // Note: endFrame should always be >= startFrame.
   private int endFrame;
   
   // The direction of animation.
   // 0 == no animation 
   // 1 = forward
   // -1 = reverse
   private int animationDirection;
   
   // An enumeration determining how the Animation should animate.
   private AnimationType animationType;   
   
   // The speed (frames per second) at which the Animation should be played.
   private int frameRate;
}
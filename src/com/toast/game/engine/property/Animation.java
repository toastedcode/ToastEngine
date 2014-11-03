package com.toast.game.engine.property;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.interfaces.Transformable;
import com.toast.game.engine.interfaces.Updatable;
import com.toast.game.engine.resource.AnimationMap;
import com.toast.game.engine.resource.Texture;

public class Animation extends Property implements Drawable, Updatable
{
   // **************************************************************************
   //                          Public Attributes
   // **************************************************************************
   
   public enum AnimationType
   {
      NONE,
      NORMAL,
      LOOP,
      BOUNCE      
   }
   
   public enum AnimationDirection
   {
      FORWARD(1),
      REVERSE(-1);
      
      private AnimationDirection(int frameIncrement)
      {
         this.frameIncrement = frameIncrement;
      }
      
      public int getFrameIncrement()
      {
         return (frameIncrement);
      }
      
      private int frameIncrement;
   }
   
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public Animation(
      String id,
      Texture image,
      AnimationMap animationMap,
      String animationId,
      int frameRate)
   {
      super(id);
      
      TEXTURE = image;
      ANIMATION_MAP = animationMap;
      ANIMATION_ID = animationId;
      FRAME_RATE = frameRate;
      elapsedAnimationTime = 0;
      currentFrame = 0;
      animationDirection = AnimationDirection.FORWARD;
      animationType = AnimationType.NONE;
   }
   
   
   public void setCurrentFrame(
      int frame)
   {
      if (frame < 0)
      {
         frame = 0;
      }
      else if (frame > getEndFrame())
      {
         frame = getEndFrame();
      }
      
      currentFrame = frame;
   }
   
   
   public void start(
      AnimationType animationType,
      AnimationDirection animationDirection)
   {
      this.animationType = animationType;
      this.animationDirection = animationDirection;
   }
   
   
   public void pause()
   {
      animationType = AnimationType.NONE;
   }
   
   
   public void stop()
   {
      animationType = AnimationType.NONE;
      currentFrame = 0;
   }

   // **************************************************************************
   //                               Updatable   
   
   @Override
   public void update(
      long elapsedTime)
   {
      if (isEnabled() == true)
      {
         // Remember the current frame.
         int previousFrame = currentFrame;
         
         elapsedAnimationTime += elapsedTime;
         if (shouldAdvanceFrame(elapsedAnimationTime));
         {
            elapsedAnimationTime = 0;
            currentFrame = getNextFrame(currentFrame);
         }
         
         // Determine if our current animation just finished.
         if ((currentFrame != previousFrame) &&
             (isFinished(currentFrame) == true))
         {
            // Send the "animation finished" event.
            /*
            Event event = new Event("eventAnimationFinished");
            event.addPayload("animation", animation.getAnimationId());
            EventManager.sendEvent(event, spriteId);
            */
         }
      }
   }
   
   // **************************************************************************
   //                               Drawable
   
   @Override
   public int getWidth()
   {
      return (TEXTURE.getBufferedImage().getWidth());
   }
   
   
   @Override
   public int getHeight()
   {
      return (TEXTURE.getBufferedImage().getHeight());
   }      
   
   
   @Override
   public void draw(
      Graphics graphics)
   {
      if (isVisible() == true)
      {
         // Determine the position of the animation from the parent, if applicable.
         Point position = getPosition();
         double scale = getScale();
         
         // Retrieve the current frame.
         AnimationMap.Frame frame = ANIMATION_MAP.getFrame(ANIMATION_ID,  currentFrame);
         
         Rectangle sourceRectangle = new Rectangle(frame.getPosition(), frame.getDimension());
         
         Rectangle destinationRectangle = new Rectangle(position,
                                                        new Dimension((int)(frame.getDimension().getWidth() * scale), 
                                                                      (int)(frame.getDimension().getHeight() * scale)));
         
         ((Graphics2D)graphics).drawImage(
            TEXTURE.getBufferedImage(), 
            destinationRectangle.x, 
            destinationRectangle.y, 
            (destinationRectangle.x + destinationRectangle.width), 
            (destinationRectangle.y + destinationRectangle.height), 
            sourceRectangle.x, 
            sourceRectangle.y, 
            (sourceRectangle.x+ sourceRectangle.width), 
            (sourceRectangle.y + sourceRectangle.height), 
            null);
      }
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private int getStartFrame()
   {
      return (0);
   }
   
   
   private int getEndFrame()
   {
      return (ANIMATION_MAP.getNumberOfFrames(ANIMATION_ID) - 1);
   }
   
   
   // This operation determines if it is time to advance the current frame of the animation.
   // TODO: This isn't right.  A very long frame rate will cause the animation not to advance at all.
   private boolean shouldAdvanceFrame(
      long elapsedTime)
   {
      return (elapsedTime >= (MILLISECONDS_PER_SECOND / FRAME_RATE));
   }

   
   // Determines the first frame of the animation.
   public int getFirstFrame()
   {
      int firstFrame = 0;
      
      if (animationDirection == AnimationDirection.REVERSE)
      {
         firstFrame = getEndFrame();
      }
      else
      {
         firstFrame = getStartFrame();         
      }
      
      return (firstFrame);
   }
      
   
   // Determines the next frame of the animation, based on the supplied current animation frame. 
   private int getNextFrame(
      // The current animation frame.
      int currentFrame)
   {
      // Initialize the return value.
      int nextFrame = currentFrame;      
      
      switch (animationType)
      {
         case NONE:
         {
            // No change.
            break;
         }
         
         case NORMAL:
         {
            // Increment/decrement the current frame based on the animation direction.
            nextFrame = currentFrame += animationDirection.getFrameIncrement();      

            if (animationDirection == AnimationDirection.FORWARD)
            {
               nextFrame = (nextFrame > getEndFrame()) ? getEndFrame() : nextFrame;   
            }
            else
            {
               nextFrame = (nextFrame < getEndFrame()) ? getEndFrame() : nextFrame;               
            }
            break;
         }

         case LOOP:
         {
            // Increment/decrement the current frame based on the animation direction.
            nextFrame = currentFrame += animationDirection.getFrameIncrement();      
            
            if (animationDirection == AnimationDirection.FORWARD)
            {
               nextFrame = (nextFrame > getEndFrame()) ? getStartFrame() : nextFrame;   
            }
            else
            {
               nextFrame = (nextFrame < getEndFrame()) ? getStartFrame() : nextFrame;               
            }            
            break;
         }

         case BOUNCE:
         {
            // Increment/decrement the current frame based on the animation direction.
            nextFrame = currentFrame += animationDirection.getFrameIncrement();
            
            if ((nextFrame > getEndFrame()) || (nextFrame < getStartFrame()))
            {
               animationDirection = (animationDirection == AnimationDirection.FORWARD) ? 
                                       AnimationDirection.REVERSE : 
                                       AnimationDirection.FORWARD;
               
               nextFrame = nextFrame + animationDirection.getFrameIncrement();
            }
            break;
         }
      }
      
      return (nextFrame);
   }
   
   
   // Determines if the animation has finished, based on the supplied current frame.
   private boolean isFinished(
      int currentFrame)
   {
      // Does this animation have a logical end?
      boolean isFiniteAnimation = ((animationType == AnimationType.NONE) ||
                                   (animationType == AnimationType.NORMAL));
      
      // Determine the last frame of the animation.
      boolean isFinalFrame =        
         (animationDirection == AnimationDirection.FORWARD) ? 
            (currentFrame == getEndFrame()) :
            (currentFrame == getStartFrame());
               
      return ((isFiniteAnimation == true) && 
              (isFinalFrame == true));
   }
   
   
   private Point getPosition()
   {
      Point position = new Point(0, 0);
      
      if (getParent() instanceof Transformable)
      {
         position = new Point((int)((Transformable)getParent()).getX(), (int)((Transformable)getParent()).getY());
      }
      
      return (position);
   }
   
   
   private double getScale()
   {
      double scale = 1.0;
      
      if (getParent() instanceof Transformable)
      {
         scale = ((Transformable)getParent()).getScale();
      }
      
      return (scale);
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   // A constant specifying the number of milliseconds in a second.
   private static final int MILLISECONDS_PER_SECOND = 1000;  
   
   private final Texture TEXTURE;

   private final AnimationMap ANIMATION_MAP;
   
   private final String ANIMATION_ID;
   
   // The speed (frames per second) at which the Animation should be played.
   private final int FRAME_RATE;
   
   // A value holding the elapsed time since the last animation frame update.
   private long elapsedAnimationTime;
   
   private int currentFrame;
   
   // The direction of animation.
   private AnimationDirection animationDirection;
   
   // An enumeration determining how the Animation should animate.
   private AnimationType animationType;   
}

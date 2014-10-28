package gameEngine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;
import simpleXml.XmlNodeList;

public class Sprite implements Cloneable
{
   // **************************************************************************
   //                                  Typedefs
   // **************************************************************************
   
   public enum Direction
   {
      DIRECTION_UNKNOWN,
      DIRECTION_LEFT,
      DIRECTION_RIGHT,
      DIRECTION_UP,
      DIRECTION_DOWN      
   }

   // **************************************************************************
   //                                  Public Operations
   // **************************************************************************

   //
   // Constructors
   //
   
   // Constructor
   public Sprite(
      // A unique string used to identify a sprite.
      String initSpriteId)
   {
      // Initialize all attributes to their default values.
      initialize();
      
      // Set the sprite id.
      spriteId = initSpriteId;
   }

      
   // Constructor, via XML node.
   public Sprite(
      XmlNode node)
   {
      // Initialize all attributes with values from the XML node.
      initialize(node);
   }
   
   
   // Constructor, via XML node.
   public Sprite(
      String initSpriteId,
      XmlNode node)
   {
      // Initialize all attributes with values from the XML node.
      initialize(node);
      spriteId = initSpriteId;
   }   
   
   
   // Copy constructor.
   public Sprite(
      // A unique string used to identify a sprite.
      String initSpriteId,
      // A template Sprite to use in initializing the Sprite.
      Sprite templateSprite)
   {
      // Initialize all attributes with values from the template Sprite.
      initialize(initSpriteId, templateSprite);
   }
   
   //
   // Save
   //
      
   public void save(
      XmlNode node)
   {
      // Temporary node for appending children.
      XmlNode childNode = null;
      
      // Create the sprite node.
      XmlNode spriteNode = node.appendChild("sprite");
      
      //
      // Identification
      //
      
      // spriteId
      spriteNode.setAttribute("id", spriteId);
      
      // spriteClassSet
      if (spriteClassSet.size() > 0)
      {
         String classString = "";
         int i = 0;
         for (String spriteClass : spriteClassSet)
         {
            classString = classString + spriteClass;
            if (i == (spriteClassSet.size() - 1))
            {
               classString = classString + " ";            
            }
         }
         spriteNode.setAttribute("class", classString);
      }
      
      // parentSpriteId
      if (parentSpriteId != null)
      {
         spriteNode.appendChild("parentSpriteId", parentSpriteId);
      }
      
      // isTemplate
      if (isTemplate == true)
      {
         spriteNode.setAttribute("isTemplate", String.valueOf(isTemplate));
      }
         
      //
      // Display
      //

      // image
      if (image != null)
      {
         spriteNode.appendChild("imageId", image.getImageId());         
      }
      
      // text
      if (text != null)
      {
         text.save(spriteNode);
      }
      
      if (animation != null)
      {
         spriteNode.appendChild("animationId", animation.getAnimationId());
      }
      
      // currentFrame
      if (currentFrame != 0)
      {
         spriteNode.appendChild("currentFrame", String.valueOf(currentFrame));
      }
     
      // scale
      if (scale.equals(new Dimension(100, 100)) == false)
      {
         childNode = spriteNode.appendChild("scale");
         XmlUtils.setDimension(childNode, scale);
      }

      // rotation
      if (rotation != 0)
      {
         spriteNode.appendChild("rotation", String.valueOf(rotation));         
      }
      
      // opacity
      if (opacity != 100)
      {
         spriteNode.appendChild("opacity", String.valueOf(opacity));         
      }
      
      // isVisible
      spriteNode.appendChild("isVisible", String.valueOf(isVisible));
      
      //
      // Selection
      //
      
      if (isSelectable != false)
      {
         spriteNode.appendChild("isSelectable", String.valueOf(isSelectable));         
      }
      
      if (isDraggable != false)
      {
         spriteNode.appendChild("isDraggable", String.valueOf(isDraggable));         
      }
      
      //
      // Location
      //
      
      // position
      childNode = spriteNode.appendChild("position");
      XmlUtils.setPoint2D(childNode, position);
      
      // coordsType
      if (coordsType != CoordinatesType.WORLD)
      {
         spriteNode.appendChild("coordsType", coordsType.toString());         
      }
      
      // zOrder
      if (zOrder != 0)
      {
         spriteNode.appendChild("zOrder", String.valueOf(zOrder));
      }
      
      // facing
      if (facing != Direction.DIRECTION_UNKNOWN)
      {
         spriteNode.appendChild("facing", facing.toString());         
      }
      
      //
      // Motion
      //

      // physics
      if (physics != null)
      {
         physics.save(spriteNode);
      }

      // collision
      if (collision != null)
      {
         collision.save(spriteNode);
      }
      
      // collisionMap
      if (collisionMap.isEmpty() == false)
      {
         // TODO?
      }
      
      //
      // Event handling
      //
      
      // script
      if (script != null)
      {
         spriteNode.appendChild("scriptId", script.scriptId);  
      }

      // eventQueue
      if (eventQueue.isEmpty() == false)
      {
         // TODO?
      }

      //
      // Health/Damage
      //
      
      // health
      if (health != 0)
      {
         spriteNode.appendChild("health", String.valueOf(health));           
      }

      // maxHealth
      if (maxHealth != 0)
      {
         spriteNode.appendChild("maxHealth", String.valueOf(maxHealth));
      }
      
      // damage
      if (damage != 0)
      {
         spriteNode.appendChild("damage", String.valueOf(damage));
      }
 
      //
      // AI
      //
      
      if (ai != null)
      {
         // TODO?
      }
      
      //
      // Events
      //
      
      EventManager.save(spriteNode, spriteId);
      
      //
      // States
      //
      
      StateManager.save(spriteNode, spriteId);
   }

   
   //
   // Identification
   //
   
   public String getSpriteId()
   {
      return (spriteId);
   }
   
   
   public HashSet<String> getSpriteClass()
   {
      return (spriteClassSet);
   }
   
   
   public boolean isSpriteClass(
      String spriteClass)
   {
      return (spriteClassSet.contains(spriteClass) == true);
   }
   
   
   public String getParentId()
   {
      return (parentSpriteId);
   }
   
   
   public void setParentId(
      String initParentSpriteId)
   {
      parentSpriteId = initParentSpriteId;
   }
   
   
   public boolean isTemplate()
   {
      return (isTemplate);
   }

   //
   // Display
   //
   
   // This operation causes the Sprite to be visible, i.e. drawn.
   public void show()
   {
      isVisible = true;
   }
   
   
   // This operation causes the Sprite to be hidden, i.e. not drawn.
   public void hide()
   {
      isVisible = false;
   }
   
   
   // This operation returns a flag indicating if the Sprite is currently 
   // visible, that is, if it will be drawn.
   public boolean isVisible()
   {
      return isVisible;
   }
   
   
   public boolean isSelectable()
   {
      return (isSelectable);
   }
   
   
   public boolean isDraggable()
   {
      return (isDraggable);
   }

   
   public void select()
   {
      logger.log(Level.INFO, String.format("Sprite [%s] selected.", spriteId));
      isSelected = true;
      EventManager.sendEvent(new Event("eventSelected"), spriteId);
   }
   
   
   public void deselect()
   {
      logger.log(Level.INFO, String.format("Sprite [%s] deselected.", spriteId));      
      isSelected = false;
      EventManager.sendEvent(new Event("eventDeselected"), spriteId);
   }
   
   
   public boolean isSelected()
   {
      return (isSelected);
   }
   
   
   public int getImageWidth()
   {
      // Initialize our return value.
      int width = 0;

      if (animation != null)
      {
         width = animation.getWidth();
      }
      else if (image != null)
      {
         width = image.getWidth();
      }
      
      return (width);
   }
   
   
   public int getImageHeight()
   {
      // Initialize our return value.
      int height = 0;

      if (animation != null)
      {
         height = animation.getHeight();
      }
      else if (image != null)
      {
         height = image.getHeight();
      }
      
      return (height);
   }   
   
   
   public int getWidth()
   {
      return ((int)(getImageWidth() * ((double)scale.width / 100.0)));
   }
   
   
   public int getHeight()
   {
      return ((int)(getImageHeight() * ((double)scale.height / 100.0)));
   }
   
   
   public Image getImage()
   {
      return (image);
   }
   
   
   // This operation sets the current animation and current frame for the 
   // Sprite.
   public void setImage(
      // The id of the new image to display.    
      String imageId)
   {
      image = AnimationManager.getImage(imageId);
   }
   
   
   // This operation gets the current animation for the Sprite.
   public String getAnimation()
   {
      String animationId = null;
      
      if (animation != null)
      {
         animationId = animation.getAnimationId();
      }
      
      return (animationId);
   }   

   
   // This operation sets the current animation and current frame for the Sprite.
   public void setAnimation(
      // The id of the new animation to display.    
      String animationId)
   {
      animation = AnimationManager.getAnimation(animationId);
      if (animation != null)
      {
         currentFrame = animation.getFirstFrame();
      }
      
   }  
   
   
   public Text getText()
   {
      return (text);
   }
   
   
   public void setText(
      Text text)
   {
      this.text = text;
   }
   
   
   public int getCurrentFrame()
   {
      return (currentFrame);
   }
   
   
   public boolean setCurrentFrame(
      int newFrame)
   {
      boolean returnStatus = false;
      
      if ((animation != null) &&
          (animation.isValidFrame(newFrame) == true))
      {
         currentFrame = newFrame;
         returnStatus = true;
      }
      
      return (returnStatus);
   }   
   
   
   public Dimension getScale()
   {
      return (scale);
   }   
   
   
   public void setScale(
      Dimension newScale)
   {
      scale.setSize(newScale);
      
   }
   
   //
   // Position
   //
   
   // This operation gets the current position of the Sprite.
   public Point2D getPosition()
   {
      return (position);
   }      
   
   
   // This operation sets the current position of the Sprite.
   // Note:  Unlike moveBy(), this operation performs no collision checking.
   public void setPosition(
      // The new position for this Sprite.   
      Point2D newPosition)
   {
      position.setLocation(
         newPosition.getX(),
         newPosition.getY());
   }

   
   public Point2D getAbsolutePosition()
   {
      Point2D absolutePosition = new Point2D.Double();
      absolutePosition.setLocation(position);
      
      if (coordsType == CoordinatesType.OFFSET)
      {
         Sprite parentSprite = SpriteManager.getSprite(parentSpriteId);
         if (parentSprite == null)
         {
            logger.log(Level.WARNING, 
                  String.format("No parent Sprite specified for Sprite [%s].", 
                                spriteId));            
         }
         else
         {
            absolutePosition.setLocation(
                  (absolutePosition.getX() + parentSprite.getPosition().getX()),            
                  (absolutePosition.getY() + parentSprite.getPosition().getY()));
         }
      }
      
      return (absolutePosition);
   }   
   
   
   public Point2D getCenter()
   {
      Point2D center = new Point2D.Double();
      center.setLocation((getPosition().getX() + (getWidth() / 2.0)),
                         (getPosition().getY() + (getHeight() / 2.0)));
      
      return (center);
   }
   
   
   public void centerOn(
     Point2D center)
   {
      setPosition(
         new Point2D.Double(
            (center.getX() - (getWidth() / 2.0)),
            (center.getY() - (getHeight() / 2.0))));
   }
   
   
   public int getZOrder()
   {
      return (zOrder);
   }

   
   public void setZOrder(
      int initZOrder)
   {
      zOrder = initZOrder;
   }
   
   
   // This operation gets the direction the Sprite is currently facing.
   public Direction getFacing()
   {
      return (facing);
   }
   
   
   // This operation sets the direction the Sprite is currently facing.
   public void setFacing(
      Direction initFacing)
   {
      facing = initFacing;
   }   

   //
   // Motion
   //
   
   // This operation returns the Physics object for the Sprite.
   public Physics getPhysics()
   {
      return (physics);
   }
   
   
   // This operation causes the Sprite to be moved by the specified vector.
   public void move(
      // The vector used to move the Sprite.
      Vector2D translation)
   {
      // Create an empty map of collision results to populate.
      CollisionMap collisionResults = new CollisionMap();
      
      // Move the Sprite, updating collision results as we go.
      moveBy(translation, collisionResults);
      
      // Record collisions/uncollisions and send out the proper events.
      updateCollisions(collisionResults);
   }
   
   
   public void moveTo(
      Point2D point,
      int speed)
   {
      if (ai != null)
      {
         ai.clearWaypoints();
         ai.addWaypoint(point);
         ai.startTraversal(speed);
      }
   }

   
   public Collision getCollision()
   {
      return collision;
   }
   
   
   public void setCollision(
      Collision collision)
   {
      this.collision = collision;
   }
   
   
   public boolean isCollided(
      String collidedSpriteId)
   {
      return (collisionMap.isCollided(collidedSpriteId));
   }
   
   
   public synchronized void queueEvent(
      Event event)
   {
      eventQueue.add(event);
   }

   
   // This operation sets the Script object for this Sprite.
   public void setScript(
      // The id of the new animation to display.    
      String scriptId)
   {
      script = ScriptManager.getScript(scriptId);
   }
   
   
   public void evaluateScript(
      Script.ScriptFunction function,
      Object ... parameters)
   {
      if (script != null)
      {
         script.evaluate(this, function, parameters);   
      }  
      
   }   
   
   
   //
   // Health/Damage
   //
   
   public void setHealth(
      int initHealth)
   {
      if (initHealth < 0)
      {
         health = 0;
      }
      else if ((maxHealth != 0) &&
               (initHealth > maxHealth))
      {
         health = maxHealth;
      }
      else
      {
         health = initHealth;
      }         
   }
   
   
   public int getHealth()
   {
      return (health);
   }   
   
   
   public void setMaxHealth(
      int initMaxHealth)
   {
      maxHealth = (initMaxHealth > 0) ? initMaxHealth : 0;
   }
   
   
   public int getMaxHealth()
   {
      return (maxHealth);
   }   

   
   public void setDamage(
      int initDamage)
   {
      damage = initDamage;
      
   }
      
      
   public int getDamage()
   {
      return (damage);
   }

   
   public void takeDamage(
      String enemySpriteId,
      int inflictedDamage,
      Vector2D damageForce)
   {
      // Decrement the Sprite's health.
      setHealth(getHealth() - inflictedDamage);
      
      // Apply the force of the damage.
      if (physics != null)
      {
         physics.addVelocity(damageForce);
      }
      
      // Send the damage event.
      Event event = new Event("eventDamage");
      event.addPayload("enemy", enemySpriteId);
      EventManager.sendEvent(event, spriteId);
      
      // If the damage is critical, send the death event.
      if (getHealth() == 0)
      {
         event = new Event("eventDead");
         EventManager.sendEvent(event, spriteId);
      }
   }
   
   
   public AI getAi()
   {
      return (ai);
   }
   

   //
   // Game loop
   //
   
   // An operation that performs all updates to the Sprite, including animation,
   // event handling, and AI.
   // (Called from within the main game loop.)
   public void update(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.
      long elapsedTime)
   {
      // Handle all queued events.
      processEventQueue();
      
      // Update the Sprite's animation.
      if (animation != null)
      {
         updateAnimation(elapsedTime);
      }
      
      // Update the Sprite's position according to the Physics object.
      Vector2D translation = new Vector2D(0.0, 0.0);
      if (physics != null)
      {
         translation = physics.update(elapsedTime);
      }
      
      // Update the Sprite's position according to the AI object.
      if (ai != null)
      {
         translation = Vector2D.add(translation, ai.update(elapsedTime));
      }
      
      // Move by the calculated translation.
      move(translation);
   }

   
   // An operation that draws the sprite to the back-buffer.
   // (Called from within the main game loop.)
   public void draw()
   {
      // Do we have the ability to draw this sprite?
      if (isVisible == true)
      {
         // Allow for custom drawing.
         /*
         if (script != null)
         {
            script.evaluate(this,  ScriptFunction.DRAW);
         }
         */
         
         if (animation != null)
         {
            // Draw the sprite's image
            Renderer.drawImage(animation.getImage(), 
                               getSrcRectangle(),
                               getDestRectangle(),
                               coordsType,
                               opacity);         
         }
         else if (image != null)
         {
            // Draw the sprite's image
            Renderer.drawImage(image, 
                               getSrcRectangle(),
                               getDestRectangle(),
                               coordsType,
                               opacity);
            
            // Display the name of the Sprite
            // TODO: Move to Debugger class.
            if (Boolean.valueOf(StateManager.getState("global", "isDebugMode")) == true)
            {
               Renderer.drawText(getSpriteId(), 
                                 new Point((int)getPosition().getX(), (int)getPosition().getY()),
                                 Color.WHITE,
                                 new Font("SansSerif", Font.PLAIN, 12),
                                 CoordinatesType.WORLD,
                                 opacity);
               
               // Display the collisionMap of the Sprite
               int offset = 40;
               for (Map.Entry<String, CollisionResult> entry : collisionMap.entrySet())
               {
                  CollisionResult collisionResult = entry.getValue();
                  
                  Renderer.drawText(collisionResult.collidedSprite.getSpriteId(),
                        new Point(20, offset),
                        Color.WHITE,
                        new Font("SansSerif", Font.PLAIN, 12),
                        CoordinatesType.SCREEN,
                        100);
                  
                  offset += 20.0;
               }
               
               Renderer.drawText(
                     ((isGrounded() == true) ? "GROUNDED" : ""),
                     new Point(20, offset),
                     Color.WHITE,
                     new Font("SansSerif", Font.PLAIN, 12),
                     CoordinatesType.SCREEN,
                     100);
               
            }
         }
         else if (text != null)
         {
            text.draw(new Point((int)getPosition().getX(), (int)getPosition().getY()), opacity);
         }
      }
      
      if ((collision != null) &&
          (Boolean.valueOf(StateManager.getState("global", "isDebugMode")) == true))
      {
         collision.draw();
      }
   }
   
   // **************************************************************************
   //                           Protected Operations
   // **************************************************************************
   
   protected void initialize()
   {
      spriteId = null;
      spriteClassSet = new HashSet<String>();
      parentSpriteId = null;
      isTemplate = false;
      image = null;
      text = null;
      animation = null;
      currentFrame = 0;
      elapsedAnimationTime = 0;
      scale = new Dimension(100, 100);  // percent 
      rotation = 0;  // degrees
      opacity = 100;  // percent
      isVisible = false;
      isSelectable = false;
      isDraggable = false;
      isSelected = false;
      position = new Point2D.Double(0, 0);
      coordsType = CoordinatesType.WORLD;
      zOrder = 0;
      facing = Direction.DIRECTION_UNKNOWN;
      physics = null;
      collision = null;
      collisionMap = new CollisionMap();
      script = null;
      eventQueue = new LockableList<Event>();
      health = 0;
      maxHealth = 0;
      damage = 0;
      ai = null;
   }
   
   // **************************************************************************
   //                           Protected Operations
   // **************************************************************************   
   
   protected void initialize(
      XmlNode node)
   {
      // First, initialize all attributes to their default values.
      initialize();
      
      // spriteId
      spriteId = node.getAttribute("id");
      
      // parentSpriteId
      if (node.hasChild("parentSpriteId") == true)
      {
         parentSpriteId = node.getChild("parentSpriteId").getValue();
      }
      
      // spriteClass
      if (node.hasAttribute("class") == true)
      {
         String[] classStrings = node.getAttribute("class").split(" ");
         
         for (String classString : classStrings)
         {
            spriteClassSet.add(classString);
         }
      }
      
      // isTemplate
      /*
      if (node.hasAttribute("isTemplate") == true)
      {
         isTemplate = Boolean.valueOf(node.getAttribute("isTemplate"));
      }
      */
      
      // image
      if (node.hasChild("imageId") == true)
      {
         setImage(node.getChild("imageId").getValue());
      }
      
      // text
      if (node.hasChild("text") == true)
      {
         text = new Text(node);
      }      
      
      // animation
      if (node.hasChild("animationId") == true)
      {
         setAnimation(node.getChild("animationId").getValue());
      }
      
      // isVisible
      if ((node.hasChild("isVisible") == true) &&
          (node.getChild("isVisible").getBoolValue() == true))
      {
         show();
      }
      
      // isSelectable
      if (node.hasChild("isSelectable") == true)
      {
         isSelectable = node.getChild("isSelectable").getBoolValue();
      }
      
      // isDraggable
      if (node.hasChild("isDraggable") == true)
      {
         isDraggable = node.getChild("isDraggable").getBoolValue();
      }
      
      // scale
      if (node.hasChild("scale") == true)
      {
         scale.setSize(XmlUtils.getDimension(node.getChild("scale")));
      }
      
      // opacity
      if (node.hasChild("opacity") == true)
      {
         opacity = node.getChild("opacity").getIntValue();
      }
      
      // position
      if (node.hasChild("position") == true)
      {
         position.setLocation(XmlUtils.getPoint2D(node.getChild("position")));
      }
      
      // coordsType
      if (node.hasChild("coordsType") == true)
      {
         coordsType = CoordinatesType.valueOf(node.getChild("coordsType").getValue());
      }
      
      // zOrder
      if (node.hasChild("zOrder") == true)
      {
         zOrder = node.getChild("zOrder").getIntValue();
      }      
      
      // facing
      if (node.hasChild("facing") == true)
      {
         facing = Direction.valueOf(node.getChild("facing").getValue());
      }      
      
      // physics
      if (node.hasChild("physics") == true)
      {
         physics = new Physics(node.getChild("physics"));
      }
      
      // collision
      if (node.hasChild("collision") == true)
      {
         collision = new Collision(node.getChild("collision"), this);
      }      
      
      // events
      if (node.hasChild("events") == true)
      {
         // Get the list of eventId child nodes
         XmlNodeList eventIdNodes = node.getChild("events").getChildren("eventId"); 
         
         // Loop through the list of nodes
         for (int i = 0; i < eventIdNodes.getLength(); i++)
         {
           XmlNode eventIdNode = eventIdNodes.item(i);
           EventManager.registerSpriteForEvent(spriteId, eventIdNode.getValue());
         }
      }
      
      // states
      XmlNodeList stateNodes = node.getNodes(".//state");
      for (int i = 0; i < stateNodes.getLength(); i++)
      {
        XmlNode stateNode = stateNodes.item(i);
        StateManager.setState(spriteId, stateNode.getAttribute("id"), stateNode.getValue());
      }
      
      // script
      if (node.hasChild("script") == true)
      {
         setScript(node.getChild("script").getValue());
      }
      
      // health
      if (node.hasChild("health") == true)
      {
         setHealth(node.getChild("health").getIntValue());
      }        
      
      // maxHealth
      if (node.hasAttribute("maxHealth") == true)
      {
         setMaxHealth(node.getChild("maxHealth").getIntValue());
      }      
      
      // damage
      if (node.hasChild("damage") == true)
      {
         setDamage(node.getChild("damage").getIntValue());
      }
      
      // ai
      if (node.hasChild("ai") == true)
      {
         ai = new AI(spriteId);
      }
   }
  
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************   
   
   private void initialize(
      String initSpriteId,
      Sprite templateSprite)
   {
      // Initialize all attributes to their default values.
      initialize();
      
      spriteId = initSpriteId;
      spriteClassSet = new HashSet<String>();
      for (String spriteClass : templateSprite.spriteClassSet)
      {
         spriteClassSet.add(new String(spriteClass));
      }
      
      image = templateSprite.image;
      animation = templateSprite.animation;  // Shallow copy
      currentFrame = templateSprite.currentFrame;
      scale = (Dimension)templateSprite.scale.clone(); 
      rotation = templateSprite.rotation;
      opacity = templateSprite.opacity;
      isVisible = templateSprite.isVisible;
      position = (Point2D.Double)templateSprite.position.clone();
      coordsType = templateSprite.coordsType;
      zOrder = templateSprite.zOrder;
      facing = templateSprite.facing;
      if (templateSprite.physics != null)
      {
         physics = (Physics)templateSprite.physics.clone();
      }
      if (templateSprite.collision != null)
      {
         collision = (Collision)templateSprite.collision.clone(this);
      }
      script = templateSprite.script;  // Shallow copy
      eventQueue = new LockableList<Event>();          
   }
   
   
   private Rectangle getSrcRectangle()
   {
      Rectangle srcRectangle = new Rectangle();
      
      if (animation != null)
      {
         srcRectangle.x = (currentFrame % animation.getNumColumns()) * animation.getWidth();
         srcRectangle.y = (currentFrame / animation.getNumColumns()) * animation.getHeight();
         srcRectangle.width = animation.getWidth();
         srcRectangle.height = animation.getHeight();
      }
      else if (image != null)
      {
         srcRectangle.x = 0;
         srcRectangle.y = 0;
         srcRectangle.width = image.getWidth();
         srcRectangle.height = image.getHeight();
      }      

      return (srcRectangle);
   }

   
   private Rectangle getDestRectangle()
   {
      Rectangle destRectangle = new Rectangle();

      Point2D absolutePosition = getAbsolutePosition();
      
      if (animation != null)
      {
         destRectangle.x = (int)absolutePosition.getX();
         destRectangle.y = (int)absolutePosition.getY();
         destRectangle.width = (int)((double)animation.getWidth() * ((double)scale.width / 100.0));
         destRectangle.height = (int)((double)animation.getHeight() * ((double)scale.height / 100.0));
      }
      else if (image != null)
      {
         destRectangle.x = (int)absolutePosition.getX();
         destRectangle.y = (int)absolutePosition.getY();
         destRectangle.width = (int)((double)image.getWidth() * ((double)scale.width / 100.0));
         destRectangle.height = (int)((double)image.getHeight() * ((double)scale.height / 100.0));
      }
      
      return (destRectangle);
   }   
  
   
   private void updateAnimation(
      long elapsedTime)
   {
      // Remember the current frame.
      int previousFrame = currentFrame;
      
      elapsedAnimationTime += elapsedTime;
      if (animation.shouldAdvanceFrame(elapsedAnimationTime) == true)
      {
         elapsedAnimationTime = 0;
         currentFrame = animation.getNextFrame(currentFrame);
      }
      
      // Determine if our current animation just finished.
      if ((currentFrame != previousFrame) &&
          (animation.isFinished(currentFrame) == true))
      {
         // Send the "animation finished" event.
         Event event = new Event("eventAnimationFinished");
         event.addPayload("animation", animation.getAnimationId());
         EventManager.sendEvent(event, spriteId);
      }
   }
   
   
   private synchronized void processEventQueue()
   {
      // Lock the internal lists against modification.
      eventQueue.lock();   
      
      // Loop through the event queue, front to back.
      Iterator<Event> it = eventQueue.iterator();
      while(it.hasNext() == true)
      {
         Event event = it.next();
               
         handleEvent(event);
         eventQueue.remove(event);
      }
      
      // Unlock the internal lists, flushing any additions/subtractions.
      eventQueue.unlock();      
   }
   
   // TODO: Is this the visibility we want?
   //       What about setting an event handler?
   protected void handleEvent( 
      Event event)
   {
      evaluateScript(Script.ScriptFunction.HANDLE_EVENT, event);

      logger.log(Level.INFO, 
                 String.format("Sprite [%s] handled event [%s].", 
                               spriteId, 
                               event.getEventId()));
   }

   
   // This operation causes the Sprite to be moved by the specified vector.
   public void moveBy(
      // The vector by which the Sprite should be moved.
      Vector2D translation,
      // A map of CollisionResults to populate. 
      CollisionMap collisionResults)
   {
      final boolean GET_SOLIDS = true;
      
      if (translation.getMagnitude() == 0)
      {
         // No action necessary.
      }
      else
      {
         // Check for collisions.
         CollisionMap newCollisionResults = CollisionManager.checkCollision(this, translation);
         
         // Add these new collision results to the overall tally.
         collisionResults.addCollisions(newCollisionResults);
         
         // Start with the first (closest) solid collision we encountered.
         CollisionResult firstCollisionResult = newCollisionResults.getFirstCollision(GET_SOLIDS);
         
         // If we found a solid collision ...
         if (firstCollisionResult != null)
         {
            // Modify the translation based on our collision result.
            translation = getModifiedTranslation(translation, firstCollisionResult);
            
            // Set the position of the Sprite to a known "safe" position based on
            // the results of the collision detection.
            setPosition(Utils.translatePoint(position, translation));
            
            if (physics != null)
            {
               // Allow the physics class to slide, bounce, push, and otherwise react to the collision
               //of this Sprite.
               translation = physics.onCollision(firstCollisionResult);
           
               // Recursively call moveBy() until 
               // a) the Sprite is successfully moved without collision, or
               // b) no movement is deemed possible.
               moveBy(translation, collisionResults);
            }
         }
         else
         {
            // Set the new position.
            setPosition(Utils.translatePoint(position, translation));
            
         }  // // end if (collisionResult.isCollided() == true)
      }  // if (newPosition.equals(position))
   }   
   
   
   private Vector2D getModifiedTranslation(
      Vector2D translation, 
      CollisionResult collisionResult)
   {
      // A constant for correcting jitter caused by random fluctuations in position
      // caused by gravity.
      final double sTRANSLATION_JITTER_BUFFER = 3.0;
      
      // Initialize the return value to the full translation, modified by what's been calculated as
      // the minimum translation vector.
      // (This is the inverse of the penetration vector, plus a buffer vector necessary to push 
      // the Sprite just outside the collision.)
      Vector2D modifiedTranslation = Vector2D.add(translation, collisionResult.getMinimumTranslationVector());
      
      if (modifiedTranslation.getMagnitude() <= sTRANSLATION_JITTER_BUFFER)
      {
         // No change in position.  This helps remove the "jittering" of a Sprite
         // being pulled down by gravity on a immovable surface.
         modifiedTranslation = new Vector2D(0.0, 0.0);
      }
      
      return (modifiedTranslation);
   }

   
   public void updateCollisions(
      CollisionMap collisionResults)
   {
      CollisionMap addedCollisions = new CollisionMap();
      CollisionMap updatedCollisions = new CollisionMap();
      CollisionMap removedCollisions = new CollisionMap();
      
      CollisionResult collisionResult = null;
      String collidedSpriteId = null;
      
      // Remember if we were grounded prior to this update.
      boolean wasGrounded = isGrounded(); 
      
      // First, find all removed collisions.
      for (Map.Entry<String, CollisionResult> entry : collisionMap.entrySet())
      {
         collisionResult = entry.getValue();
         collidedSpriteId = collisionResult.collidedSprite.getSpriteId();
         if (collisionResults.isCollided(collidedSpriteId) == false)
         {
            removedCollisions.addCollision(collisionResult);
         }
      }
      
      // Next, find all added collisions and updated collisions.
      for (Map.Entry<String, CollisionResult> entry : collisionResults.entrySet())
      {
         collisionResult = entry.getValue();
         collidedSpriteId = collisionResult.collidedSprite.getSpriteId();
         if (collisionMap.isCollided(collidedSpriteId) == true)
         {
            updatedCollisions.addCollision(collisionResult);
         }
         else
         {
            addedCollisions.addCollision(collisionResult);
         }
      }
      
      // Remove collisions.
      for (Map.Entry<String, CollisionResult> entry : removedCollisions.entrySet())
      {
         removeCollision(entry.getValue());
      }      
      
      // Add collisions.
      for (Map.Entry<String, CollisionResult> entry : addedCollisions.entrySet())
      {
         collisionResult = entry.getValue();
         addCollision(entry.getValue());
      }
      
      // Update collisions.
      for (Map.Entry<String, CollisionResult> entry : updatedCollisions.entrySet())
      {
         collisionResult = entry.getValue();
         updateCollision(entry.getValue());
      }      
      
      // Look for a change in isGrounded().
      if (wasGrounded != isGrounded())
      {
         Event event = null;
         if (wasGrounded == false)
         {
            event = new Event("eventGrounded");
         }
         else
         {
            event = new Event("eventUngrounded");            
         }
         EventManager.sendEvent(event, getSpriteId());         
      }
      
   }
   

   public void addCollision(
      CollisionResult collisionResult)
   {
      String collidedSpriteId = collisionResult.collidedSprite.getSpriteId();
      
      if (collisionMap.isCollided(collidedSpriteId) == false)
      {
         // Add to our list of current collision.
         collisionMap.addCollision(collisionResult);
         
         // React to the collision by inflicting damage and sending events.
         CollisionManager.onCollision(this, collisionResult.collidedSprite, collisionResult);
      }
   }
   
   
   public void updateCollision(
      CollisionResult collisionResult)
   {
      // Add to our list of current collision.
      // Note: This will overwrite the current collision result.
      collisionMap.addCollision(collisionResult);
   }   
   
   
   public void removeCollision(
         CollisionResult collisionResult)
   {
      String collidedSpriteId = collisionResult.collidedSprite.getSpriteId();
      
      if (collisionMap.isCollided(collidedSpriteId) == true)
      {
         collisionMap.removeCollision(collidedSpriteId);
         
         // React to the collision by sending events.
         CollisionManager.onUncollision(this, collisionResult.collidedSprite);
      }
   }
   
   
   public boolean isGrounded()
   {
      // Initialize the return value.
      boolean isSpriteGrounded = false;

      // Loop through the current map of collision results.
      for (Map.Entry<String, CollisionResult> entry : collisionMap.entrySet())
      {
         if (entry.getValue().isGrounded == true)
         {
            isSpriteGrounded = true;
            break;
         }
      }
      
      return (isSpriteGrounded);
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   // Logging
   private final static Logger logger = Logger.getLogger(Sprite.class.getName());
   
   //
   // Identification
   //
      
   private String spriteId;
   
   private HashSet<String> spriteClassSet;
   
   private String parentSpriteId;
   
   private boolean isTemplate;
      
   //
   // Display
   //

   // The Image used to draw the Sprite.
   // Note: This is only used if no animation is specified.
   private Image image;
   
   // A Text object to be used as the image for the Sprite.
// Note: This is only used if no animation is specified.
   private Text text;
   
   // The current animation being used to draw the Sprite.
   private Animation animation;
   
   // The current frame of the current animation.
   private int currentFrame;
  
   // A value holding the elapsed time since the last animation frame update.
   private long elapsedAnimationTime;
  
   // A class holding the x and y scale of the Sprite, expressed as a 
   // percentage.
   private Dimension scale;

   // A value specifying the rotation of the Sprite, in degrees.
   private int rotation;
   
   // A value specifying the opacity of the Sprite, expressed as a percentage.
   private int opacity;
   
   // A flag, indicating whether the Sprite is visible or not.
   private boolean isVisible;
   
   //
   // Selection
   //
   
   // A flag indicating whether the Sprite can be selected via the mouse.
   private boolean isSelectable;
   
   // A flag indicating whether the Sprite can be dragged via the mouse.
   private boolean isDraggable;
   
   // A flag indicating whether the Sprite is currently selected.
   private boolean isSelected;
   
   //
   // Location
   //
   
   // The location of the top/left corner of the Sprite. 
   private Point2D position;
   
   // The type of coordinates specified by position.
   private CoordinatesType coordsType;
   
   // The z-order of the Sprite, used in ordering the draw list.
   private int zOrder;
   
   // The direction the Sprite is facing.
   private Direction facing;
   
   //
   // Motion
   //
   
   private Physics physics;
   
   private Collision collision;
   
   // The current list of collisions for this Sprite.
   private CollisionMap collisionMap;   
   
   //
   // Event handling
   //
   
   private Script script;

   private LockableList<Event> eventQueue;
   
   // Health/Damage
   
   private int health;
   
   private int maxHealth;
   
   private int damage;
   
   // AI
   
   private AI ai;
}
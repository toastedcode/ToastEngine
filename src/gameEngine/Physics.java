package gameEngine;

import java.awt.geom.Point2D;
import simpleXml.XmlNode;

public class Physics implements Cloneable
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public Physics()
   {
      // Initialize all attributes to their default values.
      initialize();
   }
   
   
   public Physics(
      XmlNode node)
   {
      // Initialize all attributes with values from the XML node.
      initialize(node);
   }
   
   
   public Physics clone()
   {
      Physics clonedPhysics = new Physics();
      
      clonedPhysics.isEnabled = isEnabled;
      clonedPhysics.mass = mass;
      clonedPhysics.velocity = velocity.clone();
      clonedPhysics.acceleration = acceleration.clone();
      clonedPhysics.gravity = gravity.clone();
      clonedPhysics.drag = drag;
      clonedPhysics.friction = friction;
      clonedPhysics.terminalVelocity = terminalVelocity.clone();
      clonedPhysics.elasticity = elasticity;
      
      return (clonedPhysics);
   }
   
   
   public void save(
      XmlNode node)
   {
      // Temporary node for appending children.
      XmlNode childNode = null;
      
      // Create the physics node.
      XmlNode physicsNode = node.appendChild("physics");
      
      // isEnabled
      if (isEnabled == true)
      {
         physicsNode.appendChild("isEnabled", String.valueOf(isEnabled));
      }
      
      // mass
      if (mass != 0)
      {
         physicsNode.appendChild("mass", String.valueOf(mass));         
      }
      
      // velocity
      if (velocity.equals(new Vector2D(0.0, 0.0)) == false)
      {
         childNode = physicsNode.appendChild("velocity");
         XmlUtils.setVector2D(childNode, velocity);
      }
      
      
      // acceleration
      if (acceleration.equals(new Vector2D(0.0, 0.0)) == false)
      {
         childNode = physicsNode.appendChild("acceleration");
         XmlUtils.setVector2D(childNode, acceleration);
      }
      
      // gravity
      if (gravity.equals(new Vector2D(0.0, 0.0)) == false)
      {
         childNode = physicsNode.appendChild("gravity");
         XmlUtils.setVector2D(childNode, gravity);
      }
      
      
      // drag
      if (drag != 0)
      {
         physicsNode.appendChild("drag", String.valueOf(drag));           
      }
      
      // friction
      if (friction != 0)
      {
         physicsNode.appendChild("friction", String.valueOf(friction));           
      }
      
      
      // terminalVelocity
      if (terminalVelocity.equals(new Vector2D(0.0, 0.0)) == false)
      {
         childNode = physicsNode.appendChild("terminalVelocity");
         XmlUtils.setVector2D(childNode, terminalVelocity);
      }  
      
      // elasticity
      if (elasticity != 0)
      {
         physicsNode.appendChild("elasticity", String.valueOf(elasticity));           
      }      
   }
   
   
   public void enable()
   {
      isEnabled = true;
   }
   
   
   public void disable()
   {
      isEnabled = false;
   }   
   
   
   public boolean isEnabled()
   {
      return (isEnabled);
   }   

   
   public void setMass(
      int initMass)
   {
      mass = initMass;
   }
   
   
   public int getMass()
   {
      return (mass);
   }
   
   
   public void setGravity(
      Vector2D initGravity)
   {
      gravity = initGravity;
   }
   
   
   public Vector2D getGravity()
   {
      return (gravity);
   }   
   
   
   public void setAcceleration(
      Vector2D initAcceleration)
   {
      acceleration = initAcceleration;
   }
   
   
   public Vector2D getAcceleration()
   {
      return (acceleration);
   }

   
   public Vector2D getVelocity()
   {
      return (velocity);
   }
   
   
   public void setVelocity(
      Vector2D initVelocity)
   {
      velocity.x = initVelocity.x;
      velocity.y = initVelocity.y;
   }   
   
   
   public void addVelocity(
      Vector2D addedVelocity)
   {
      velocity.x += addedVelocity.x;
      velocity.y += addedVelocity.y;
   }
   
   
   public void setDrag(
      int initDrag)
   {
      drag = initDrag;
   }

   
   public void setFriction(
      int initFriction)
   {
      friction = initFriction;
   }   
   
   
   public double getFriction()
   {
      return (friction);
   }
   
   
   public void setTerminalVelocity(
      Vector2D initTerminalVelocity)
   {
      terminalVelocity.x = initTerminalVelocity.x;
      terminalVelocity.y = initTerminalVelocity.y;
   }      
   
   
   public void setElasticity(
      int initElasticity)
   {
      elasticity = initElasticity;
   }   
   
   
   public double getElasticity()
   {
      return (elasticity);
   }   
   
   
   public Vector2D update(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.
      long elapsedTime)
   {
      Vector2D translation = new Vector2D();
      
      if (isEnabled() == true)
      {
         // Apply gravity.
         velocity.x += gravity.x * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND);  
         velocity.y += gravity.y * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND);
         
         // Apply acceleration.
         velocity.x += acceleration.x * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND);  
         velocity.y += acceleration.y * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND);
         
         // Apply drag.
         velocity.x += (velocity.x * -1.0 * ((double)drag / 100.0)) * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND);  
         
         // Restrict velocity to specified bounds.
         /*
         if (Math.abs(velocity.x) > terminalVelocity.x)
         {
            velocity.x = terminalVelocity.x;
         }
         */
         
         // Determine the translation.
         translation.x = (velocity.x * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND));
         translation.y = (velocity.y * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND));
      }
      
      return (translation);
   }
   
   
   Vector2D onCollision(
      CollisionResult collisionResult)
   {
      Vector2D translationVector = new Vector2D();
      
      if (isEnabled() == true)
      {
         if ((collisionResult.collidedSprite.getPhysics() != null) &&
             (collisionResult.collidedSprite.getPhysics().isEnabled() == true) &&
             (collisionResult.collidedSprite.getPhysics().getMass() < mass))
         {
            translationVector = push(collisionResult);
         }
         else
         {
            // Calculate the translation vector that allows the Sprite to
            // slide along the collision edge.
            //translationVector = slide(collisionResult);
            
            // Calculate the resulting velocity based on the current velocity
            // and the collision results.
            bounce(collisionResult);
         }
      }
      
      return (translationVector);
   }
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private static final int MILLISECONDS_PER_SECOND = 1000;
   
   private boolean isEnabled;
   
   private int mass;
   
   private Vector2D velocity;

   private Vector2D acceleration;
   
   private Vector2D gravity;
   
   private int drag;
   
   private int friction;
   
   private Vector2D terminalVelocity;
   
   private int elasticity;
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   private void initialize()
   {
      isEnabled = true;
      mass  = 0;
      gravity = new Vector2D(0.0, 0.0);
      acceleration = new Vector2D(0.0, 0.0);
      velocity = new Vector2D(0.0, 0.0);
      drag = 0;
      friction = 0;
      terminalVelocity = new Vector2D(0.0, 0.0);
      elasticity = 0;
   }

   
   private void initialize(
      XmlNode node)
   {
      // First, initialize all attributes to their default values.
      initialize();
      
      // isEnabled
      if (node.hasChild("isEnabled") == true)
      {
         isEnabled = node.getChild("isEnabled").getBoolValue();
      }      
      
      // mass
      if (node.hasChild("mass") == true)
      {
         setMass(node.getChild("mass").getIntValue());
      }            
      
      // gravity
      if (node.hasChild("gravity") == true)
      {
         setGravity(XmlUtils.getVector(node.getChild("gravity")));
      }      
      
      // acceleration
      if (node.hasChild("acceleration") == true)
      {
         setAcceleration(XmlUtils.getVector(node.getChild("acceleration")));
      }
      
      // velocity
      if (node.hasChild("velocity") == true)
      {
         setVelocity(XmlUtils.getVector(node.getChild("velocity")));
      }
      
      // drag
      if (node.hasChild("drag") == true)
      {
         setDrag(node.getChild("drag").getIntValue());
      }
      
      // friction
      if (node.hasChild("friction") == true)
      {
         setFriction(node.getChild("friction").getIntValue());
      }      
      
      // terminal velocity
      if (node.hasChild("terminalVelocity") == true)
      {
         setTerminalVelocity(XmlUtils.getVector(node.getChild("terminalVelocity")));         
      }
      
      // elasticity
      if (node.hasChild("elasticity") == true)
      {
         setElasticity(node.getChild("elasticity").getIntValue());
      }
   }
   
   
   private Vector2D push(
      CollisionResult collisionResult)
   {
      Vector2D translationVector = new Vector2D(0.0, 0.0);
      
      // Remember the position of the collided Sprite prior to pushing.
      Point2D oldPosition = collisionResult.collidedSprite.getPosition();
      
      // Attempt to move the collided Sprite.
      collisionResult.collidedSprite.move(collisionResult.penetrationVector);
      
      // Determine the new position of the collided Sprite.
      Point2D newPosition = collisionResult.collidedSprite.getPosition();
      
      // Determine if we successfully moved the collided Sprite.
      if (newPosition.equals(oldPosition) == false)
      {
         translationVector = collisionResult.penetrationVector.clone();
      }
      
      return (translationVector);        
   }   
   
   
   private Vector2D slide(
      CollisionResult collisionResult)
   {
      Vector2D translationVector = new Vector2D();

      // Get a normalized vector parallel to the edge we've collided with.
      Vector2D edgeVector = collisionResult.getCollisionEdgeVector();
      
      // Project the penetration vector onto the edge.
      translationVector = 
         Vector2D.multiply(edgeVector, 
                           collisionResult.penetrationVector.dotProduct(edgeVector));
      
      // Calculate the new velocity by projecting the velocity onto the edge vector.
      velocity = Vector2D.multiply(edgeVector, velocity.dotProduct(edgeVector));
      
      // Apply friction.
      if (collisionResult.collidedSprite.getPhysics() != null)
      {
         double friction = collisionResult.collidedSprite.getPhysics().getFriction();
         velocity = Vector2D.multiply(velocity, (1.0 - (friction / 100.0)));
      }
     
      return (translationVector);      
   }
   
   
   // TODO: Implement bounce.
   private void bounce(
      CollisionResult collisionResult)
   {
      final double MIN_BOUNCE_THRESHOLD = 1.0;
      
      Vector2D reflectionVector = collisionResult.getReflectionVector().getNormalized();
      double reflectionMagnitude = (velocity.getMagnitude() * ((double)getElasticity() / 100.0));
      
      reflectionMagnitude = (reflectionMagnitude < MIN_BOUNCE_THRESHOLD) ? 
                               MIN_BOUNCE_THRESHOLD : 
                               reflectionMagnitude;
      
      // Set the resulting velocity.
      velocity = Vector2D.multiply(reflectionVector, reflectionMagnitude);
   }

}  // end public class Physics





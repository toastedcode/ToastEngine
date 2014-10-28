package gameEngine;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class CollisionResult
{
   // **************************************************************************
   //                          Public Attributes
   // **************************************************************************

   public boolean isCollided;
   
   public Sprite collidedSprite;
   
   public Line2D collisionEdge;
   
   public Point2D collisionPoint;
   
   public Vector2D attackVector;
   
   public Vector2D penetrationVector;
   
   public boolean isGrounded;
  
   // **************************************************************************
   //                          Public Attributes
   // **************************************************************************
   
   public CollisionResult()
   {
      isCollided = false;
      collidedSprite = null;
      collisionEdge = new Line2D.Double();
      collisionPoint = new Point2D.Double();
      attackVector = new Vector2D();
      penetrationVector = new Vector2D();
      isGrounded = false;
   }
   
   
   public boolean isCollided()
   {
      return (isCollided == true);
   }
   
   
   public Vector2D getIncidentVector()
   {
      return (penetrationVector.getNormalized());
   }
   

   public Vector2D getCollisionEdgeVector()
   {
      return ( 
         new Vector2D(
               (collisionEdge.getP2().getX() - collisionEdge.getP1().getX()),
               (collisionEdge.getP2().getY() - collisionEdge.getP1().getY())).getNormalized());   
   }
   
   
   public Vector2D getCollisionNormalVector()
   {
      // Initialize the return value.
      Vector2D collisionNormalVector = new Vector2D();
      
      // Start with the collision edge (unit) vector.
      Vector2D collisionEdgeVector = getCollisionEdgeVector();
      
      // To get a vector orthogonal to this vector, flip and negate.
      collisionNormalVector.x =  collisionEdgeVector.y * -1.0;
      collisionNormalVector.y =  collisionEdgeVector.x;
      
      return (collisionNormalVector);
   }   
   
   
   public Vector2D getMinimumTranslationVector()
   {
      Vector2D minTranslationVector = new Vector2D();
      
      // To start, we'll use the inverse of the penetration vector.
      minTranslationVector = Vector2D.multiply(penetrationVector, -1.0);
      
      // Create a "buffer" vector meant to move the Sprite just outside of
      // a collision.
      Vector2D buffer = Vector2D.multiply(minTranslationVector.getNormalized(), sCOLLISION_BUFFER_MAGNITUDE);

      // Add the buffer to our minimum translation vector.
      minTranslationVector = Vector2D.add(minTranslationVector, buffer);
      
      return (minTranslationVector);
   }
   
   
   /*
   public Vector2D getReflectionVector()
   {
      Vector2D reflectionVector = new Vector2D();
      
      // http://cgmath.blogspot.com/2010/06/reflect.html
      // R = 2 * N * ( DotProduct[ I,N] ) - I
      
      Vector2D incidentVector = getIncidentVector();
      Vector2D normalVector = getCollisionNormalVector();
      
      reflectionVector = Vector2D.multiply(normalVector,
                                           (incidentVector.dotProduct(normalVector) * 5.0));  // Where did this 5 come from?
      reflectionVector = Vector2D.subtract(incidentVector, reflectionVector);
      
      return (reflectionVector);
   }
   */
   
   
   public Vector2D getReflectionVector()
   {
      Vector2D reflectionVector = new Vector2D();
      
      // http://cgmath.blogspot.com/2010/06/reflect.html
      // R = 2 * N * ( DotProduct[ I,N] ) - I
      
      Vector2D normalVector = getCollisionNormalVector();
      
      reflectionVector = Vector2D.multiply(normalVector, penetrationVector.dotProduct(normalVector));
      reflectionVector = Vector2D.multiply(reflectionVector, 2);
      reflectionVector = Vector2D.subtract(reflectionVector, penetrationVector);
      
      return (reflectionVector);
   }

   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************   
   
   // A value used to create a buffer between Sprites following a collision.
   static final double sCOLLISION_BUFFER_MAGNITUDE = 1.0;
}

package gameEngine;

import java.util.HashMap;
import java.util.Map;

// TODO: Rework, overiding put()/remove() and keep sorted by distance.
public class CollisionMap extends HashMap<String, CollisionResult>
{

   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   // Constructor
   public CollisionMap()
   {
   }
   
   
   public void addCollision(
      CollisionResult collisionResult)
   {
      put(collisionResult.collidedSprite.getSpriteId(), collisionResult);
   }
   
   
   public void removeCollision(
      String collidedSpriteId)
   {
      remove(collidedSpriteId);
   }   

   
   public boolean isCollided(
      String spriteId)
   {
      return (containsKey(spriteId));
   }
   
   
   public CollisionResult getCollisionResult(
      String spriteId)
   {
      CollisionResult collisionResult = null;
      if (containsKey(spriteId) == true)
      {
         collisionResult = get(spriteId);
      }
      
      
      return (collisionResult);
   }
   
   
   public CollisionResult getFirstCollision(
      boolean getSolids)
   {
      // Initialize the return value.
      CollisionResult firstCollisionResult = null;
      
      // Verify that we have collisions.
      if (size() > 0)
      {
         // Loop through the rest of the collisions.
         for (Map.Entry<String, CollisionResult> entry : entrySet())
         {
            if ((getSolids == false) ||
                (entry.getValue().collidedSprite.getCollision().isSolid == true))
            {
               if (firstCollisionResult == null)
               {
                  firstCollisionResult = entry.getValue();
               }
               // The first collision is the one with the greatest translation vector.            
               else if (entry.getValue().getMinimumTranslationVector().getMagnitude() > 
                           firstCollisionResult.getMinimumTranslationVector().getMagnitude())
               {
                  firstCollisionResult = entry.getValue();
               }
            }
         }
      }
      
      return (firstCollisionResult);
   }
   
   
   public void addCollisions(
      CollisionMap newCollisions)
   {
      for (Map.Entry<String, CollisionResult> entry : newCollisions.entrySet())
      {
         addCollision(entry.getValue());
      }
      
   }
   
}
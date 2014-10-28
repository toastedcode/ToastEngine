package gameEngine;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CollisionManager
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   // An operation that rebuilds the Sprite quadtree.
   // (Called from within the main game loop.)   
   public static void update(
      // A value representing the elapsed time (in milliseconds) since the last update.         
      long elapsedTime)
   {
      java.awt.Dimension worldDimension = LevelManager.getInstance().getCurrentLevel().getWorldDimensions();
      java.awt.Rectangle worldBounds = new java.awt.Rectangle(0, 0, (int)worldDimension.getWidth(), (int)worldDimension.getHeight());
      
      quadtree = new Quadtree<Sprite>(worldBounds);
      
      SpriteList spriteList = SpriteManager.getSprites();
      for (Sprite sprite : spriteList)
      {
         if ((sprite.getCollision() != null) &&
             (sprite.getCollision().isEnabled == true))
               
         {
            quadtree.insert(sprite,  sprite.getCollision().getBounds());
         }
      }
   }
   
   
   // This operation checks for collisions when the specified Sprite is moved by the specified translation.
   // A map of the resulting collisions is returned.
   // Note: The Sprite will not be moved as a result of this operation.
   public static CollisionMap checkCollision(
      // The Sprite being moved.
      Sprite sprite,
      // A vector describing how the Sprite is to be moved.
      Vector2D translation)
   {
      // Initialize our return value.
      CollisionMap collisionResults = new CollisionMap();
      CollisionResult collisionResult = new CollisionResult();
      
      // Check if collision is enabled for this Sprite.
      if ((sprite.getCollision() != null) &&
          (sprite.getCollision().isEnabled() == true))
      {
         // Loop through all the Sprites in the SpriteManager.
         // TODO: Does this short circuit our concurrent modification logic?
         Iterator<Sprite> iterator = SpriteManager.getIterator();
         while (iterator.hasNext() == true)
         {
            Sprite otherSprite = iterator.next();
            
            // Determine if collision is enabled for the other Sprite.
            if ((otherSprite != sprite) &&
                (otherSprite.getCollision() != null) &&
                (otherSprite.getCollision().isEnabled() == true))
            {
               // Determine if the include/exclude class lists make it possible for these Sprites to collide.
               if (checkClassCollision(sprite, otherSprite) == true)
               {
                  // Check for a collision between two Sprites.
                  collisionResult = checkCollision(sprite, translation, otherSprite);
               }
            }
            
            // Handle detected collisions.
            if (collisionResult.isCollided() == true)
            {
               collisionResults.addCollision(collisionResult);
            }
         }
      }

      return (collisionResults);
   }
   
   
   /*
   // This operation checks for collisions when the specified Sprite is moved by the specified translation.
   // A map of the resulting collisions is returned.
   // Note: The Sprite will not be moved as a result of this operation.
   public static CollisionMap checkCollision(
      // The Sprite being moved.
      Sprite sprite,
      // A vector describing how the Sprite is to be moved.
      Vector2D translation)
   {
      // Initialize our return value.
      CollisionMap collisionResults = new CollisionMap();
      CollisionResult collisionResult = new CollisionResult();
      
      // Check if collision is enabled for this Sprite.
      if ((sprite.getCollision() != null) &&
          (sprite.getCollision().isEnabled() == true))
      {
         // Use the Quadtree to get a optimized list of possible collisions.
         List<Sprite> collisionList = quadtree.retrieve(sprite.getCollision().getBounds());
         
         // Loop through all the potential candidates for collision.
         for (Sprite otherSprite : collisionList)
         {
            // Determine if collision is enabled for the other Sprite.
            if (otherSprite != sprite)
            {
               // Check for a collision between two Sprites.
               collisionResult = checkCollision(sprite, translation, otherSprite);
            }
            
            // Handle detected collisions.
            if (collisionResult.isCollided() == true)
            {
               collisionResults.addCollision(collisionResult);
            }
         }
      }

      return (collisionResults);
   }
   */

   
   // This operation performs the actions that result when two Sprites collide.
   public static void onCollision(
      // The Sprite that moved.
      Sprite sprite1,
      // The Sprite the moving Sprite collided with.
      Sprite sprite2,
      // The details of the collision.
      CollisionResult collisionResult)
   {
      Event event = null;

      // Send an event to the first Sprite.
      event = new Event("eventCollision");
      event.addPayload("collidedSpriteId", sprite2.getSpriteId());
      EventManager.sendEvent(event, sprite1.getSpriteId());
      
      // Send an event to the second Sprite.      
      event = new Event("eventCollision");
      event.addPayload("collidedSpriteId", sprite1.getSpriteId());
      EventManager.sendEvent(event, sprite2.getSpriteId());

      // Calculate damage on each of the Sprites.
      if ((sprite1.getMaxHealth() > 0) &&
          (sprite2.getDamage() > 0))  
      {
         sprite1.takeDamage(sprite2.getSpriteId(), sprite2.getDamage(), new Vector2D(0, 0));
      }
      if ((sprite2.getMaxHealth() > 0) &&
          (sprite1.getDamage() > 0))
      {
         sprite2.takeDamage(sprite2.getSpriteId(), sprite1.getDamage(), new Vector2D(0, 0));
      }
   }
   
   
   // This operation performs the actions that result when two Sprites cease to collide.
   public static void onUncollision(
      Sprite sprite1,
      Sprite sprite2)
   {
      Event event = null;
      
      // Send an event to the first Sprite.
      event = new Event("eventUncollision");
      event.addPayload("collidedSpriteId", sprite2.getSpriteId());
      EventManager.sendEvent(event, sprite1.getSpriteId());
      
      // Send an event to the second Sprite. 
      event = new Event("eventUncollision");
      event.addPayload("collidedSpriteId", sprite1.getSpriteId());
      EventManager.sendEvent(event, sprite2.getSpriteId());
   }
   
   
   public static SpriteList checkIntersection(
      Point2D point)
   {
      SpriteList intersectingSprites = new SpriteList();
      
      // Loop through all the Sprites in the SpriteManager.
      Iterator<Sprite> iterator = SpriteManager.getIterator();
      while (iterator.hasNext() == true)
      {
         Sprite sprite = iterator.next();
         
         if (sprite.getCollision() != null)
         {
            if (sprite.getCollision().getPolygon().contains(point) == true)
            {
               intersectingSprites.add(sprite);
            }
         }
      }
      
      // Sort by ascending z-order so that the top Sprites appear first.
      Collections.sort(intersectingSprites, SpriteManager.Z_ORDER_ASCENDING_COMPARATOR);
      
      return (intersectingSprites);      
   }
   
   
   public static void drawQuadtree()
   {
      if (quadtree != null)
      {
         //quadtree.draw();
      }
   }
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   private static boolean checkClassCollision(
      Sprite spriteA,
      Sprite spriteB)
   {
      boolean hasClassCollision = false;
      
      if ((spriteA.getCollision() != null) &&
          (spriteB.getCollision() != null))
      {
         hasClassCollision = (spriteA.getCollision().collidesWithClassSet(spriteB.getSpriteClass()) &&
                              spriteB.getCollision().collidesWithClassSet(spriteA.getSpriteClass()));
               
      }
      
      return (hasClassCollision);
   }
      
   
   private static CollisionResult checkCollision(
      Sprite spriteA,
      Vector2D translation,
      Sprite spriteB)
   {
      CollisionResult collisionResult = new CollisionResult();
      
      // Get the collision polygons for each Sprite
      Path2D polygonA = spriteA.getCollision().getPolygon();
      Path2D polygonB = spriteB.getCollision().getPolygon();
      
      //if (quickCheckCollision(polygonA, polygonB, translation) == true)
      // TODO: Implement quick collision checking using bounding rectangles.
      if (true)
      {
         collisionChecks++;
         
         // Check for a collision between spriteA and spriteB, when spriteA is moved.
         collisionResult = checkCollision(polygonA, polygonB, translation);
         if (collisionResult.isCollided() == true)
         {
            collisionResult.collidedSprite = spriteB;
         }
         else
         {
            // Check for a collision between spriteA and spriteB, when spriteB is moved.
            // Note:  This is necessary for the case of a big spriteA moving towards a small spriteB.
            collisionResult = checkCollision(polygonB, polygonA, Vector2D.multiply(translation, -1.0));
            if (collisionResult.isCollided() == true)
            {
               collisionResult.collidedSprite = spriteB;
               collisionResult.attackVector = translation.clone();
              
               // We need to alter some of the return results.
               collisionResult.penetrationVector = 
                  Vector2D.multiply(collisionResult.penetrationVector, 
                                    -1.0);
            }
         }
         
         // Set the isGrounded flag if this collision results in spriteA being grounded.
         if ((collisionResult.isCollided() == true) &&
             (isGrounded(spriteA, collisionResult) == true))
         {
            collisionResult.isGrounded = true;
         }
         
      }
      
      return (collisionResult);
   }

   
   // TODO: Doesn't work because getBounds2D() is not precise enough.
   /*
   private static boolean quickCheckCollision(
      Path2D polygonA,
      Path2D polygonB,
      Vector2D velocity)
   {
      // Transform polygonA by the velocity.
      AffineTransform transform = new AffineTransform();
      transform.translate(velocity.x, velocity.y);      
      polygonA.transform(transform);
      
      Rectangle2D rectangleA = polygonA.getBounds2D();
      Rectangle2D rectangleB = polygonB.getBounds2D();
      
      return (rectangleA.intersects(rectangleB) == true);
   }
   */

   
   private static CollisionResult checkCollision(
      Path2D polygonA,
      Path2D polygonB,
      Vector2D translation)
   {
      // Algorithm
      /*
      for (each point in polygonA)
      {
         Line2D rayTrace = new Line2D(point, translate(point, translation));
         for (each edge in polygonB)
         {
            Point2D intersection = intersectLines(edge, rayTrace)
            
            if (intersect != null)
            {
               double distanceToInstersection = Line2D(point, intersect).length();
               if (distanceToIntersection < minDistanceToIntersection)
               {
                  minimumTranslationVector = rayTrace.end() - intersection;
               }
            }
         }
      }
      */
      
      // Initialize the return values.
      CollisionResult collisionResult = new CollisionResult();
     
      // Variables used in iterating through the points of polygon A.
      PathIterator it = null;
      double coords[] = new double[6];
      
      // Variables used in iterating through the edges of polygon B. 
      List<Line2D.Double> edges = getEdges(polygonB);
      //List<Line2D.Double> edges = getFrontFacingEdges(polygonB, relativeVelocity);
      Line2D edge = null;
      
      // Variables used in determining polygon intersection.
      boolean allPointsInsidePolygon = true;
      Point2D startPoint = new Point2D.Double();
      Point2D endPoint = new Point2D.Double();
      Line2D rayTrace = new Line2D.Double();
      Point2D intersection = null;
      double distanceToIntersection = 0;
      double minDistanceToIntersection = java.lang.Double.POSITIVE_INFINITY;
           
      // Loop through the points of polygon A.
      it = polygonA.getPathIterator(IDENTITY_TRANSFORM);
      while (it.isDone() == false)
      {
         // Get the current point.
         int segmentType = it.currentSegment(coords);
         
         // Validate that the polygon was constructed correctly.
         if ((segmentType != PathIterator.SEG_MOVETO) &&
             (segmentType != PathIterator.SEG_LINETO) &&
             (segmentType != PathIterator.SEG_CLOSE))
         {
            logger.log(Level.WARNING, String.format("Improperly formatted polygon detected."));
            break; 
         }
         else
         {
            // Keep track of how many end points are inside the polygon.
            allPointsInsidePolygon &= polygonB.contains(endPoint);
            
            startPoint.setLocation(coords[0], coords[1]);
            endPoint.setLocation((startPoint.getX() + translation.x),
                                 (startPoint.getY() + translation.y));
            rayTrace.setLine(startPoint, endPoint);
            
            // Loop through the edges of polygon B.
            for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++)
            {
               edge = edges.get(edgeIndex);
               
               // Ignore back-facing edges.
               if (isBackFacingEdge(translation, edge) == false)
               {
                  // Check for an intersection of the edge and our ray-trace.
                  intersection = getLineIntersection(edge, rayTrace);
                  
                  // Ignore collisions on the end points of edges.
                  // Note: This keeps us from getting stuck on corners.
                  if ((intersection != null) &&
                      (intersection.equals(edge.getP1()) == false) &&
                      (intersection.equals(edge.getP2()) == false))
                  {
                     collisionResult.isCollided = true;
                     
                     distanceToIntersection = getLineLength(new Line2D.Double(startPoint, intersection));
                     if (distanceToIntersection < minDistanceToIntersection)
                     {
                        minDistanceToIntersection = distanceToIntersection;
                        collisionResult.penetrationVector = 
                           Vector2D.subtract(new Vector2D(endPoint), new Vector2D(intersection));
                        collisionResult.collisionEdge.setLine(edge);
                        collisionResult.collisionPoint.setLocation(intersection);
                     }
                  }
               }
            }
         }
         
         // Next!
         it.next();         
      }
      
      // Handle the case where polygonA is completely inside polygonB.
      // TODO: Test.
      /*
      if ((collisionResult.isCollided == false) &&
          (allPointsInsidePolygon == true))
      {
         collisionResult.isCollided = true;
      }
      */
       
      return (collisionResult);
   }   
   
   
   private static List<Line2D.Double> getEdges(
      Path2D polygon)
   {
      // Initialize the return value.
      List<Line2D.Double> edges = new ArrayList<Line2D.Double>();
      
      // Variables used in iterating through the polygon.
      double coords[] = new double[6];
      Line2D.Double line = new Line2D.Double();
      Point2D pointA = new Point2D.Double();
      Point2D pointB = new Point2D.Double();
      Point2D firstPoint = null;
           
      // Loop through the points of the polygon.
      PathIterator it = polygon.getPathIterator(IDENTITY_TRANSFORM);
      while (it.isDone() == false)
      {
         // Get the current point.
         int segmentType = it.currentSegment(coords);
         
         // Validate that the polygon was constructed correctly.
         if ((segmentType != PathIterator.SEG_MOVETO) &&
             (segmentType != PathIterator.SEG_LINETO) &&
             (segmentType != PathIterator.SEG_CLOSE))
         {
            logger.log(Level.WARNING, String.format("Improperly formatted polygon detected."));
            break; 
         }
         // Store off the first point of the edge.
         else if (firstPoint == null)
         {
            firstPoint = new Point2D.Double(coords[0], coords[1]);
            pointA.setLocation(firstPoint);
         }
         // Complete the edge and store in the list.
         else
         {
            if (segmentType == PathIterator.SEG_CLOSE)
            {
               pointB.setLocation(firstPoint);
            }
            else
            {
               pointB.setLocation(coords[0], coords[1]);
            }
            
            line.setLine(pointA, pointB);
            edges.add(new Line2D.Double(line.getP1(), line.getP2()));
            
            pointA.setLocation(pointB);
         }
         
         // Next!
         it.next();         
      }
      
      return (edges);
   }
   
   
   // http://paulbourke.net/geometry/lineline2d/
   private static Point2D getLineIntersection(
      Line2D lineA,
      Line2D lineB)
   {
      Point2D intersection = null;
      
      double x1 = lineA.getX1();
      double y1 = lineA.getY1();
      double x2 = lineA.getX2();
      double y2 = lineA.getY2();         
      double x3 = lineB.getX1();
      double y3 = lineB.getY1();
      double x4 = lineB.getX2();
      double y4 = lineB.getY2();         
      
      double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
      
      double numeratorA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)); 
      
      double numeratorB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3));
      
      // Check if the lines are parallel.
      if (denominator == 0)
      {
         // Check if the lines are coincidental.
         if ((numeratorA == 0) &&
             (numeratorB == 0))
         {
            // Check if either points in lineA fall inside of lineB.
            if (lineB.contains(lineA.getP1()) == true)
            {
               intersection = (Point2D.Double)lineA.getP1().clone();
               System.out.println("Lines segments are coincidental");
            }
            else if (lineB.contains(lineA.getP1()) == true)
            {
               intersection = (Point2D.Double)lineA.getP2().clone();
               System.out.println("Lines segments are coincidental");
            }
         }
      }
      else
      {
         double r =  numeratorA / denominator;
         
         double s =  numeratorB / denominator;
         
         if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1))
         {
            intersection = new Point2D.Double(
               (lineA.getX1() + (r * (lineA.getX2() - lineA.getX1()))),
               (lineA.getY1() + (r * (lineA.getY2() - lineA.getY1()))));
         }
      }
      
      return (intersection);
   }   
      
   
   private static double getLineLength(
      Line2D line)
   {
      // Pythagorean theorem.
      return (Math.sqrt(Math.pow((line.getX2() - line.getX1()), 2) +
                        Math.pow((line.getY2() - line.getY1()), 2)));
   }
   
   
   private static boolean isBackFacingEdge(
      Vector2D relativeVelocity,
      Line2D edge)
   {
      // Calculate a normal for the edge.
      // n = (y2 - y1), -(x2 - x1) 
      Vector2D edgeNormal = 
         new Vector2D((edge.getP2().getY() - edge.getP1().getY()),
                      (edge.getP2().getX() - edge.getP1().getX()) * -1.0);
                      
      // dotProduct < 0 indicates front facing
      // dotProduct > 0 indicates back facing
      // dotProduct == 0 indicates parallel (consider back facing)
      return (edgeNormal.dotProduct(relativeVelocity) >= 0.0);
   }
   
   
   private static boolean isGrounded(
      Sprite sprite,
      CollisionResult collisionResult)
   {
      // Initialize the return value.
      boolean spriteIsGrounded = false;
      
      final double NEAR_VERTICAL_THRESHOLD = 40.0;    // degrees off of vertical 
   
      if ((sprite.getPhysics() != null) &&
          (sprite.getPhysics().getGravity().getMagnitude() > 0.0) &&
          (collisionResult.isCollided == true))
      {
         boolean alignedWithGravity = 
            (sprite.getPhysics().getGravity().dotProduct(collisionResult.attackVector) >= 0.0);
         

         double angle = Vector2D.angleBetween(sprite.getPhysics().getGravity(),
                                              collisionResult.getCollisionEdgeVector());
         
         boolean nonVerticalEdge = ((angle >= (0.0 + NEAR_VERTICAL_THRESHOLD)) &&
                                    (angle <= (180.0 - NEAR_VERTICAL_THRESHOLD)));
         
         spriteIsGrounded = ((alignedWithGravity == true) &&
                             (nonVerticalEdge == true));
      }   
 
      return (spriteIsGrounded);
   }   
  
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(CollisionManager.class.getName());      
   
   static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();
   
   // A distance (in pixels) to add when calculating the minimum translation vector.
   static final int COLLISION_BUFFER = 1;
   
   static int collisionChecks = 0;
   
   static Quadtree<Sprite> quadtree; 
}

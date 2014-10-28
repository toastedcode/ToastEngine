package gameEngine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// This class encapsulates the logic for simple way-point navigation, path-finding, and Sprite tracking.  
public class AI
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   // Constructor
   public AI(
      String initParentSpriteId)
   {
      initialize();
      parentSpriteId = initParentSpriteId;
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


   // This operation adds a waypoint to the end of the waypoint list.
   public void addWaypoint(
      Point2D point)
   {
      waypoints.add(point);
   }
   
   
   // This operation clears all way-points from the waypoint list.   
   public void clearWaypoints()
   {
      waypoints.clear();
      stopTraversal();
   }
   
   
   // This operation causes the parent Sprite to begin traversing the waypoint list at the specific speed.
   public void startTraversal(
      int initSpeed)
   {
      stopTracking();
      
      if (waypoints.isEmpty() == false)
      {
         currentWaypointIndex = 0;         
         speed = initSpeed;
      }
   }
   
   
   // This operation causes the parent Sprite to stop traversing the waypoint list.   
   public void stopTraversal()
   {
      currentWaypointIndex = -1;         
      speed = 0;
   }   
   
   
   public void trackSprite(
      String initTrackingSpriteId,
      int initSpeed)
   {
      clearWaypoints();
      
      trackingSpriteId = initTrackingSpriteId;
      speed = initSpeed;
   }
   
   
   public void stopTracking()
   {
      trackingSpriteId = null;
      speed = 0;
   }

   
   // An operation that performs all updates to the AI object.
   // (Called from within the main game loop.)
   public Vector2D update(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.
      long elapsedTime)
   {
      // Initialize the return value.
      Vector2D translation = new Vector2D(0.0, 0.0);
      
      if (isEnabled() == true)
      {
         if ((waypoints.isEmpty() == false) &&
             (hasReachedWaypoint() == true))
         {
            // Notify the parent Sprite with an event.
            Event event = new Event("eventAtWaypoint");
            event.addPayload("waypointIndex", currentWaypointIndex);
            EventManager.sendEvent(event, parentSpriteId);
            
            // Set the next waypoint.
            currentWaypointIndex++;
            if (currentWaypointIndex >= waypoints.size())
            {
               currentWaypointIndex = -1;
            }
         }  // end if ((waypoints.isEmpty() == false) ...
         
         // Calculate the translation of the parent Sprite in the specified elapsed time.
         translation = getTranslation(elapsedTime);
         
      }  // end if if (isEnabled() == true)
      
      return (translation);
   }
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   // This operation sets all attributes to their default values.
   private void initialize()
   {
      isEnabled = true;
      waypoints = new ArrayList<Point2D>();
      currentWaypointIndex = -1;
      trackingSpriteId = null;
      speed = 0;
   }

   
   // This operation returns true if the parent Sprite's position is equal (or close enough) to
   // the current waypoint.
   private boolean hasReachedWaypoint()
   {
      final double WAYPOINT_THRESHOLD = 1.0;  // pixels
      
      // Initialize the return value.
      boolean reachedWaypoint = false;
      
      // Validate the current waypoint.
      if ((waypoints.isEmpty() == false) &&
          (currentWaypointIndex >= 0) &&
          (currentWaypointIndex < waypoints.size()))
      {
         // Get the parent Sprite and validate.
         Sprite parentSprite = SpriteManager.getSprite(parentSpriteId);
         if (parentSprite == null)
         {
            logger.log(Level.WARNING, 
                       String.format("Invalid parent Sprite id [%s].  Waypoint traversal cancelled.", parentSpriteId));
            
            // Clear the waypoint list and stop traversal.
            clearWaypoints();
         }
         else
         {
            // Determine if the parent Sprite is "close enough" to the current waypoint.
            reachedWaypoint = 
               (parentSprite.getPosition().distance(waypoints.get(currentWaypointIndex)) < WAYPOINT_THRESHOLD);
         }
      }
    
      return (reachedWaypoint);
   }
   
   
   // This operation calculates a vector representing the amount of translation that the AI will
   // induced in the parent Sprite in the specified amount of time.
   private Vector2D getTranslation(
      // A value representing the elapsed time (in milliseconds) since the last
      // update.
      long elapsedTime)
   {
      // Initialize the return value.
      Vector2D translation = new Vector2D(0.0, 0.0);
      
      // Determine the velocity based on the speed and the current target.
      Vector2D velocity = getVelocity();
      
      // Calculate the translation for this period of time.
      translation.x = (velocity.x * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND));
      translation.y = (velocity.y * ((double)elapsedTime / (double)MILLISECONDS_PER_SECOND));
     
      return (translation);
   }
   
   
   // This operation calculates the current velocity that will be applied to the parent Sprite its AI.
   private Vector2D getVelocity()
   {
      // Initialize the return value.
      Vector2D velocity = new Vector2D(0.0, 0.0);
      
      // Validate the current waypoint.
      if ((waypoints.isEmpty() == false) &&
          (currentWaypointIndex >= 0) &&
          (currentWaypointIndex < waypoints.size()))
      {
         // Get the parent Sprite and validate.
         Sprite parentSprite = SpriteManager.getSprite(parentSpriteId);
         if (parentSprite == null)
         {
            logger.log(Level.WARNING, 
                  String.format("Invalid parent Sprite id [%s].", parentSpriteId));
         }
         else
         {
            velocity = Vector2D.multiply(Vector2D.subtract(new Vector2D(waypoints.get(currentWaypointIndex)),
                                                           new Vector2D(parentSprite.getPosition())).getNormalized(),
                                         (double)speed);
         }
      }
      else if (trackingSpriteId != null)
      {
         // Get the tracking Sprite and validate.
         Sprite trackingSprite = SpriteManager.getSprite(trackingSpriteId);
         if (trackingSprite == null)
         {
            logger.log(Level.WARNING, 
                  String.format("Invalid tracking Sprite id [%s].", trackingSpriteId));
         }
         else
         {
            // Get the parent Sprite and validate.
            Sprite parentSprite = SpriteManager.getSprite(parentSpriteId);
            if (parentSprite == null)
            {
               logger.log(Level.WARNING, 
                     String.format("Invalid parent Sprite id [%s].", parentSpriteId));
            }
            else
            {
               velocity = Vector2D.multiply(Vector2D.subtract(new Vector2D(trackingSprite.getPosition()),
                                                              new Vector2D(parentSprite.getPosition())).getNormalized(),
                                            (double)speed);
            }
         }
      }
      
      return (velocity);      
   }
      
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   // Logging
   private final static Logger logger = Logger.getLogger(AI.class.getName());   
   
   // A constant specifying the number of milliseconds in a second.
   private static final int MILLISECONDS_PER_SECOND = 1000;   
   
   // The parent Sprite that will be affected by the AI calculations.
   private String parentSpriteId;
   
   // A flag indicating if processing of this AI object is enabled.
   private boolean isEnabled;   
   
   // A list of waypoints the AI will navigate.
   private List<Point2D> waypoints;
   
   // The index of the current waypoint.
   private int currentWaypointIndex;
   
   // The id of a Sprite to follow.
   private String trackingSpriteId;
   
   // The speed at which the Sprite should be moved by the AI.
   private int speed;
}


package gameEngine;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Quadtree<T>
{
   private final int NUMBER_OF_QUADRANTS = 4;
   
   private final int MAX_OBJECTS = 1;
   
   private final int MAX_LEVELS = 5;
   
   private final int NORTH_WEST_QUADRANT = 0;
   private final int NORTH_EAST_QUADRANT = 1;
   private final int SOUTH_EAST_QUADRANT = 2;
   private final int SOUTH_WEST_QUADRANT = 3;
   
   private final int NOT_FOUND = -1;
   
   private final java.awt.Color sCOLORS[] = 
   {
      Color.BLUE,
      Color.GREEN,
      Color.RED,
      Color.CYAN,
      Color.MAGENTA,
   };
  
   private int nodeLevel;
   
   private List<QuadtreeObject> objects;
   
   private Rectangle2D nodeBounds;
   
   private Quadtree<T>[] nodes;
   
   private class QuadtreeObject
   {
      private final T OBJECT;
      
      private final Rectangle2D BOUNDS;
      
      QuadtreeObject(T object, Rectangle2D bounds)
      {
         OBJECT = object;
         BOUNDS = bounds;
      }
      
      T getObject()
      {
         return (OBJECT);
      }
      
      Rectangle2D getBounds()
      {
         return (BOUNDS);
      }
   }
   
  
   /*
    * Constructor
    */
   public Quadtree(Rectangle2D nodeBounds)
   {
      this.nodeLevel = 0;
      this.nodeBounds = nodeBounds;
   }
   
   
   /*
    * Constructor
    */
   private Quadtree(int nodeLevel, Rectangle2D nodeBounds)
   {
      this.nodeLevel = nodeLevel;
      this.nodeBounds = nodeBounds;
   }
   
   
   // Instance initialization
   {
      objects = new ArrayList<QuadtreeObject>();
      nodes = new Quadtree[NUMBER_OF_QUADRANTS];
   }
   
   
   /*
    * Clears the Quadtree
    */
   public void clear()
   {
     objects.clear();
   
     for (int i = 0; i < nodes.length; i++)
     {
       if (nodes[i] != null)
       {
         nodes[i].clear();
         nodes[i] = null;
       }
     }
   }
   
   
   /*
    * Return all objects that could collide with the given object
    */
   public List<T> retrieve(Rectangle2D bounds)
   {
      List<T> retrievedObjects = new ArrayList<T>();
      retrieve(bounds, retrievedObjects);
      
      return (retrievedObjects);
   }
   
   
   public void draw()
   {
      Renderer.drawShape(nodeBounds, sCOLORS[this.nodeLevel], 100);
      
      if (isLeaf() == false)
      {
         for (Quadtree<T> node : nodes)
         {
            node.draw();
         }
      }
   }
   
   
   private void retrieve(
      Rectangle2D bounds,
      List<T> retrievedObjects)
   {
      int index = getContainingNode(bounds);
      
      if ((index != NOT_FOUND) && 
          (isLeaf() == false))
      {
         nodes[index].retrieve(bounds, retrievedObjects);
      }
     
      for (QuadtreeObject object : objects)
      {
         retrievedObjects.add(object.getObject());
      }
   }
    
    
   /*
    * Splits the node into 4 sub-nodes
    */
   private void split()
   {
     int subWidth = (int)(nodeBounds.getWidth() / 2);
     int subHeight = (int)(nodeBounds.getHeight() / 2);
     int x = (int)nodeBounds.getX();
     int y = (int)nodeBounds.getY();

     // Create child nodes.
     nodes[NORTH_WEST_QUADRANT] = new Quadtree<T>((nodeLevel + 1), new Rectangle(x, y, subWidth, subHeight));
     nodes[NORTH_EAST_QUADRANT] = new Quadtree<T>((nodeLevel + 1), new Rectangle((x + subWidth), y, subWidth, subHeight));
     nodes[SOUTH_EAST_QUADRANT] = new Quadtree<T>((nodeLevel + 1), new Rectangle((x + subWidth), (y + subHeight), subWidth, subHeight));
     nodes[SOUTH_WEST_QUADRANT] = new Quadtree<T>((nodeLevel + 1), new Rectangle(x, (y + subHeight), subWidth, subHeight));
     
     // Distribute contents of the parent into the child nodes.
     Iterator<QuadtreeObject> it = objects.iterator();
     while (it.hasNext())
     {
        QuadtreeObject object = it.next();
        int index = getContainingNode(object.getBounds());
        
        if (index != NOT_FOUND)
        {
           nodes[index].insert(object);
           it.remove();
        }
     }
   }
   
   
   /*
    * Determine which node the object belongs to. NOT_FOUND means
    * object cannot completely fit within a child node and is part
    * of the parent node
    */
   private int getContainingNode(Rectangle2D bounds)
   {
      int index = NOT_FOUND;
      
      double verticalMidpoint = nodeBounds.getX() + (nodeBounds.getWidth() / 2);
      double horizontalMidpoint = nodeBounds.getY() + (nodeBounds.getHeight() / 2);
    
      // Object can completely fit within the top quadrants
      boolean inTopQuadrant = ((bounds.getY() < horizontalMidpoint) && 
                               (bounds.getY() + bounds.getHeight() < horizontalMidpoint));
     
      // Object can completely fit within the bottom quadrants
      boolean inBottomQuadrant = (bounds.getY() > horizontalMidpoint);
    
      // Object can completely fit within the left quadrants
      if ((bounds.getX() < verticalMidpoint) && 
          (bounds.getX() + bounds.getWidth() < verticalMidpoint))
      {
         if (inTopQuadrant == true)
         {
            index = NORTH_WEST_QUADRANT;
         }
         else if (inBottomQuadrant == true)
         {
            index = SOUTH_WEST_QUADRANT;
         }
      }
      // Object can completely fit within the right quadrants
      else if (bounds.getX() > verticalMidpoint)
      {
         if (inTopQuadrant)
         {
            index = NORTH_EAST_QUADRANT;
         }
         else if (inBottomQuadrant)
         {
            index = SOUTH_EAST_QUADRANT;
         }
      }
    
      return index;
   }
   
   
   public void insert(T object, Rectangle2D bounds)
   {
      insert(new QuadtreeObject(object, bounds));
   }

   
   /*
    * Insert the object into the quadtree. If the node
    * exceeds the capacity, it will split and add all
    * objects to their corresponding nodes.
    */
   private void insert(QuadtreeObject object)
   {
      if (isLeaf() == false)
      {
         int index = getContainingNode(object.getBounds());
    
         if (index != NOT_FOUND)
         {
            nodes[index].insert(object);
         }
         else
         {
            objects.add(object);            
         }
      }
      else
      {
         objects.add(object);
      }
    
      if ((isLeaf() == true) &&
          (objects.size() > MAX_OBJECTS) && 
          (nodeLevel < MAX_LEVELS))
      {
         split(); 
      }
   }
   
    private boolean isLeaf()
    {
       return (nodes[0] == null);
    }
}

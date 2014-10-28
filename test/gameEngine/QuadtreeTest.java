package gameEngine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.util.List;

import org.junit.Test;

public class QuadtreeTest
{
   private static final Rectangle SCREEN_BOUNDS = new Rectangle(0, 0, 100, 100);
   private static final Rectangle NORTH_WEST_QUADRANT = new Rectangle(0, 0, 50, 50);
   private static final Rectangle NORTH_EAST_QUADRANT = new Rectangle(50, 0, 50, 50);
   
   @Test
   public void testInsert()
   {
      Quadtree<Integer> quadtree = new Quadtree<Integer>(SCREEN_BOUNDS);
      
      quadtree.insert(1,  new Rectangle(0, 0, 10, 10));
      quadtree.insert(2,  new Rectangle(0, 0, 10, 10));
      quadtree.insert(3,  new Rectangle(90, 0, 10, 10));
      quadtree.insert(4,  new Rectangle(90, 0, 10, 10));
      quadtree.insert(5,  new Rectangle(90, 90, 10, 10));
      quadtree.insert(6,  new Rectangle(90, 90, 10, 10));
      quadtree.insert(7,  new Rectangle(0, 90, 10, 10));
      quadtree.insert(8,  new Rectangle(0, 90, 10, 10));
      quadtree.insert(9,  new Rectangle(45, 45, 10, 10));
      quadtree.insert(10,  new Rectangle(45, 45, 10, 10));
      quadtree.insert(11,  new Rectangle(10, 10, 10, 10));
      
      List<Integer> possibleCollisions = quadtree.retrieve(NORTH_WEST_QUADRANT);
      
      assertTrue(quadtree.retrieve(NORTH_WEST_QUADRANT).contains(1));
      assertFalse(quadtree.retrieve(NORTH_EAST_QUADRANT).contains(1));
   }
}

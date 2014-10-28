package gameEngine;

import java.util.Collections;
import java.util.Comparator;

public class SpriteList extends LockableList<Sprite>
{

   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   // Constructor
   public SpriteList()
   {
      super();
      comparator = null;
   }
   
   
   // Sets the class used to sort Sprites that are added to this list.
   public void setComparator(
      Comparator<Sprite> initComparator)
   {
      comparator = initComparator;
   }

   
   // Overide the add() operation to keep this list of Sprites ordered (descending) by z-order.
   public boolean add(
      Sprite sprite)
   {
      boolean returnStatus = super.add(sprite);
      
      if ((isLocked() == false) &&
          (comparator != null))
           
      {
         Collections.sort(this, comparator);         
      }
      
      return (returnStatus);
   }
  
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************

   private Comparator<Sprite> comparator;
}
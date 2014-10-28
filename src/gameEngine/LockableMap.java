package gameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LockableMap<K, E> extends HashMap<K, E>
{

   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   // Constructor
   public LockableMap()
   {
      super();
      additionList = new ArrayList<Pair<K, E>>();
      subtractionList = new ArrayList<Object>();
      lockDepth = 0;
   }
   
   
   public void lock()
   {
      lockDepth++;
   }
   
   
   public void unlock()
   {
      if (isLocked() == true)
      {
         lockDepth--;
         
         // React to a call to clear() that occurred while we were locked.
         if (shouldClear == true)
         {
            super.clear();
            shouldClear = false;
         }         

         // Process any additions or subtractions that occurred while we were locked.
         flushAdditionList();
         flushSubtractionList();
      }
   }
  
   
   public boolean isLocked()
   {
      return (lockDepth > 0);
   }
  
   
   public E put(
      K key,
      E value)
   {
      E returnStatus = null;
      
      if (isLocked() == true)
      {
         additionList.add(new Pair(key, value));
         returnStatus = value;
      }
      else
      {
         return super.put(key, value);
      }
      
      return (returnStatus);
   }
   
   
   public E remove(
      Object key)
   {
      E returnStatus = null;
      
      if (containsKey(key) == true)
      {
         if (isLocked() == true)
         {
            subtractionList.add(key);
         }
         else
         {
            super.remove(key);
         }
      }
      
      return (returnStatus);
   }
   
   
   public void clear()
   {
      if (isLocked() == true)
      {
         shouldClear = true;
      }
      else
      {
         super.clear();
      }

      // Clear any pending additions/subtractions.
      additionList.clear();
      subtractionList.clear();
   }
   
   
   public Set<Map.Entry<K, E>> entrySet()
   {
      return (super.entrySet());
   }
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************   
   
   private void flushAdditionList()
   {
      if (isLocked() == false)
      {
         for (Pair<K, E> pair : additionList)
         {
            put(pair.getFirst(), pair.getSecond());
         }
         
         additionList.clear();         
      }
   }   
   
   
   private void flushSubtractionList()
   {
      if (isLocked() == false)
      {
         for (Object key : subtractionList)
         {
            remove(key);
         }
         
         subtractionList.clear();         
      }
      
   }      
  
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   // A list of objects to add into the list.
   // Note: This is necessary so that we can modify the list while iterating.
   private List<Pair<K, E>> additionList;
   
   // A list of objects to remove from into the list.
   // Note: This is necessary so that we can modify the list while iterating.
   private List<Object> subtractionList;
   
   // A flag indicating if the map should be cleared, once it has been safely unlocked.
   // Note: This is necessary so we can clear the list while iterating.
   private boolean shouldClear;      
   
   // A counter indicating how many times the map has been recursively locked.
   // The list is considered "ulocked" when the counter is 0.
   private int lockDepth;
}
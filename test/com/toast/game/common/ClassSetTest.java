package com.toast.game.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClassSetTest
{
   @Test
   public void testConstructor_Default()
   {
      ClassSet classSet = new ClassSet();
      assertTrue(classSet.size() == 0);
   }
   
   
   @Test
   public void testConstructor_String()
   {
      String classString = "enemy mushroom";
      ClassSet classSet = new ClassSet(classString);
      
      assertFalse(classSet.contains("enemy mushroom"));
      assertTrue(classSet.contains("enemy"));
      assertTrue(classSet.contains("mushroom"));
      assertTrue(classSet.size() == 2);
   }   
}

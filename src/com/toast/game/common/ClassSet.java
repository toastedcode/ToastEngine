package com.toast.game.common;

import java.util.HashSet;

@SuppressWarnings("serial")
public class ClassSet extends HashSet<String>
{
   public ClassSet()
   {
   }

   
   public ClassSet(String classString)
   {
      String[] classStrings = classString.split(" ");
      
      for (String string : classStrings)
      {
         add(string);
      }      
   }
}

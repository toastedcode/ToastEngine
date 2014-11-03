package com.toast.game.engine.component;

import java.util.List;
import com.toast.game.common.ClassSet;

public class Component
{
   protected Component()
   {
      ID = null;
      CLASS_SET = null;
      PARENT = null;
   }

   
   protected Component(String id)
   {
      ID = id;
      CLASS_SET = null;
      PARENT = null;
   }
   
   
   protected Component(
      String id,
      ClassSet classSet)
   {
      ID = id;
      CLASS_SET = classSet;
      PARENT = null;
   }
   
   
   protected Component(
      String id,
      ClassSet classSet,
      Component parent)
   {
      ID = id;
      CLASS_SET = classSet;
      PARENT = parent;
   }

   
   public String getId()
   {
      return (ID);
   }

   
   public Component getParent()
   {
      return (PARENT);
   }

   
   public Component getChild(
      String id)
   {
      Component foundChild = null;

      for(Component child: children)
      {
         if (child.getId().equals(id) == true)
         {
            foundChild = child;
         }
      }

      return (foundChild);
   }

   
   public ClassSet getClassSet()
   {
      return ((ClassSet)CLASS_SET.clone());
   }

   
   public boolean add(
      Component child)
   {
      boolean returnStatus = false;

      if (validateChild(child) == true)
      {
         if (children.contains(child) == false)
         {
            children.add(child);
         }

         returnStatus = true;
      }

      return (returnStatus);
   }

   
   public void remove(
      Component child)
   {
      children.remove(child);
   }

   
   protected boolean validateChild(
      Component child)
   {
      return (true);
   }


   public boolean isEnabled()
   {
      return (isEnabled);
   }


   public void setEnabled(
         boolean isEnabled)
   {
      this.isEnabled = isEnabled;
   }

   protected final String ID;

   protected final ClassSet CLASS_SET;

   protected final Component PARENT;

   protected List<Component> children;

   protected boolean isEnabled;
}

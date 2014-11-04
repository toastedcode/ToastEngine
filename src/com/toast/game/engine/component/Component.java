package com.toast.game.engine.component;

import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.toast.game.common.ClassSet;
import com.toast.game.engine.property.Property;
import com.toast.game.engine.property.Transform;
import com.toast.xml.XmlNode;

public class Component
{
   public static Component createComponent(
      XmlNode node) throws ComponentCreationException
   {
      Component component = null;
      
      try
      {
         String classString = "com.toast.game.engine.component.Component";
         if (node.hasAttribute("code") == true)
         {
            classString = node.getAttribute("code");
         }
         
         component = (Component)Class.forName(classString).getConstructor(XmlNode.class).newInstance(node);
      }
      catch (NullPointerException | InvocationTargetException | IllegalAccessException | InstantiationException | 
             NoSuchMethodException | ClassNotFoundException e)
      {
         throw (new ComponentCreationException());
      }
      
      return (component);
   }
   
   
   public Component(
      XmlNode node)
   {
      ID = node.getAttribute("id");
      if (node.hasAttribute("class") == true)
      {
         classSet = new ClassSet(node.getAttribute("class"));
      }
      parent = null;
   }

   
   public Component(String id)
   {
      ID = id;
      classSet = new ClassSet();
      parent = null;
   }

   
   public String getId()
   {
      return (ID);
   }

   
   public Component getParent()
   {
      return (parent);
   }
   
   
   public void setParent(Component parent)
   {
      this.parent = parent;
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
      return ((ClassSet)classSet.clone());
   }
   
   
   public boolean isClass(String classString)
   {
      return (classSet.contains(classString));
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
   
   
   public Point2D.Double getPosition()
   {
      Point2D.Double position = new Point2D.Double(0, 0);
      
      // Start with the parent's position.
      if (parent != null)
      {
         position = parent.getPosition();
      }
      
      // Transform.
      Transform transform = getTransform();
      if (transform != null)
      {
         position.setLocation((position.getX() + transform.getPosition().getX()),
                              (position.getY() + transform.getPosition().getY()));
      }
      
      return (position);
   }
   
   
   public double getScale()
   {
      double scale = 1.0;
      
      // Start with the parent's scale.
      if (parent != null)
      {
         scale *= parent.getScale();
      }
      
      // Transform.
      Transform transform = getTransform();
      if (transform != null)
      {
         scale *= transform.getScale();
      }
      
      return (scale);
   }
   
   
   private Transform getTransform()
   {
      Transform transform = null;
      
      for (Property property : properties)
      {
         if (property instanceof Transform)
         {
            transform = (Transform)property;
            break;
         }
      }
      
      return (transform);
   }

   protected final String ID;

   protected ClassSet classSet;

   protected Component parent;

   protected List<Component> children;
   
   protected List<Property> properties;

   protected boolean isEnabled;
}

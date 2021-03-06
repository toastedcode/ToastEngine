package com.toast.game.engine.component;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import com.toast.game.common.ClassSet;
import com.toast.game.engine.Event;
import com.toast.game.engine.Renderer;
import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.interfaces.Updatable;
import com.toast.game.engine.property.EventHandler;
import com.toast.game.engine.property.Property;
import com.toast.game.engine.property.Transform;
import com.toast.xml.XmlNode;
import com.toast.xml.exception.XmlFormatException;

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
            classString = node.getAttribute("code").getValue();
         }
         
         component = (Component)Class.forName(classString).getConstructor(XmlNode.class).newInstance(node);
      }
      catch (NullPointerException | InvocationTargetException | IllegalAccessException | InstantiationException | 
             NoSuchMethodException | ClassNotFoundException | XmlFormatException e)
      {
         throw (new ComponentCreationException());
      }
      
      return (component);
   }
   
   
   public Component(
      XmlNode node) throws XmlFormatException
   {
      ID = node.getAttribute("id").getValue();
      if (node.hasAttribute("class") == true)
      {
         classSet = new ClassSet(node.getAttribute("class").getValue());
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

   
   public boolean add(
      Property property)
   {
      boolean returnStatus = true;
      
      properties.put(property.getId(), property);

      return (returnStatus);
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

   
   public void update(long elapsedTime)
   {
      // Update this Component's updatable properties.
      for (Property property : properties.values())
      {
         if (property instanceof Updatable)
         {
            ((Updatable)property).update(elapsedTime);
         }
      }
      
      // Update all of the Component's children.
      for (Component child : children)
      {
         child.update(elapsedTime);
      }        
   }
   
   
   public void draw()
   {
      // Get the transformation matrix.
      AffineTransform affineTransform = new AffineTransform();
      int layerIndex = 0;
      Transform transform = getTransform();
      if (transform != null)
      {
         affineTransform = transform.getTransform();
         layerIndex = transform.getZOrder();
      }
      
      // Draw this Component's drawable properties.
      for (Property property : properties.values())
      {
         if (property instanceof Drawable)
         {
            Renderer.draw((Drawable)property, affineTransform, layerIndex);
         }
      }
      
      // Draw all of the Component's children.
      for (Component child : children)
      {
         child.draw();
      }      
   }
   
   
   public void queueEvent(Event event)
   {
      // Queue this event for all of this Component's EventHandler properties.
      for (Property property : properties.values())
      {
         if (property instanceof EventHandler)
         {
            ((EventHandler)property).queueEvent(event);
         }
      }      
   }
   
   
   public void move(
      double deltaX,
      double deltaY)
   {
      Transform transform = getTransform();
      if (transform != null)
      {
         Point2D.Double position = transform.getPosition();
         getTransform().setPosition(position.getX() + deltaX, position.getY() + deltaY);
      }
   }
   
   
   public void moveTo(
      double x,
      double y)
   {
      Transform transform = getTransform();
      if (transform != null)
      {
         Point2D.Double position = transform.getPosition();
         double deltaX = x - position.getX();
         double deltaY = y - position.getY();
         
         move(deltaX, deltaY);
      }      
   }
   
   
   private Transform getTransform()
   {
      return ((Transform)properties.get("transform"));
   }

   protected final String ID;

   protected ClassSet classSet;

   protected Component parent;

   protected ArrayList<Component> children = new ArrayList<>();
   
   protected HashMap<String, Property> properties = new HashMap<>();

   protected boolean isEnabled;
}

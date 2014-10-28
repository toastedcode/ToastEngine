package com.toast.game.engine.actor;

import com.toast.game.common.ClassSet;
import com.toast.game.engine.component.Component;
import com.toast.game.engine.interfaces.Updatable;
import com.toast.game.engine.property.Property;

public class Actor extends Component implements Updatable
{
   public Actor(
      String id,
      ClassSet classSet)
   {
      super(id);
      CLASS_SET = classSet;
   }
   
   
   @Override
   protected boolean validateChild(Component child)
   {
      return ((child instanceof Component) ||
              (child instanceof Property));
   }


   @Override
   public void update(long elapsedTime)
   {
      for (Component child : children)
      {
         // Update children.
         if (child instanceof Updatable)
         {
            ((Updatable)child).update(elapsedTime);
         }
      }
   }
   
   public ClassSet getClassSet()
   {
      return ((ClassSet)CLASS_SET.clone());
   }
   
   private final ClassSet CLASS_SET;
}

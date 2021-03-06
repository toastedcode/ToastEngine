package com.toast.game.engine.component;

import com.toast.game.engine.interfaces.Updatable;
import com.toast.xml.XmlNode;
import com.toast.xml.exception.XmlFormatException;

public class Actor extends Component implements Updatable
{
   public Actor(String id)
   {
      super(id);
   }
   
   
   public Actor(XmlNode node) throws XmlFormatException
   {
      super(node);
   }   
   
   
   @Override
   protected boolean validateChild(Component child)
   {
      return ((child instanceof Component));
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
}

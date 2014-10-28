package com.toast.game.engine.property;

import com.toast.game.engine.component.Component;

public class Property extends Component
{
   public Property()
   {
      super();
   }
   
   
   public Property(
      String id) 
   {
      super(id);
   }
   
   
   @Override
   protected boolean validateChild(Component child)
   {
      return (child instanceof Property);
   }
}

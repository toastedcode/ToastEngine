package com.toast.game.engine.property;

public class State extends Property
{
   public State(String id)
   {
      super(id);
   }
   
   
   public Object getValue()
   {
      return (value);
   }

   
   public void setValue(Object value)
   {
      this.value = value;
   }
   
   
   public Object getDefaultValue()
   {
      return (defaultValue);
   }

   
   public void setDefaultValue(Object defaultValue)
   {
      this.defaultValue = defaultValue;
   }
  
   
   public void reset()
   {
      value = defaultValue;
   }
   
   private Object value;
   
   private Object defaultValue;
}

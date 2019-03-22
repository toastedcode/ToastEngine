package com.toast.game.engine.property;

import com.toast.xml.XmlNode;
import com.toast.xml.exception.XmlFormatException;

public class Property
{
   public Property(String id)
   {
      ID = id;
   }

   
   public Property(XmlNode node) throws XmlFormatException
   {
      ID = node.getAttribute("id").getValue();
   }    

   
   public String getId()
   {
      return (ID);
   }
   
   private final String ID;
}

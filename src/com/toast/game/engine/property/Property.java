package com.toast.game.engine.property;

import com.toast.xml.XmlNode;

public class Property
{
   public Property(String id)
   {
      ID = id;
   }

   
   public Property(XmlNode node)
   {
      ID = node.getAttribute("id");
   }    

   
   public String getId()
   {
      return (ID);
   }
   
   private final String ID;
}

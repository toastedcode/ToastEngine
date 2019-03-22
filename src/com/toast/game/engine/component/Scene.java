package com.toast.game.engine.component;

import com.toast.xml.XmlNode;
import com.toast.xml.exception.XmlFormatException;

public class Scene extends Component
{
   public Scene(XmlNode node) throws XmlFormatException
   {
      super(node);
   }
   
   
   public Scene(String id)
   {
      super(id);
   }
}

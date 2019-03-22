package com.toast.game.engine.components;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.toast.game.engine.component.Component;
import com.toast.game.engine.component.ComponentCreationException;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.xml.XmlDocument;
import com.toast.xml.XmlNode;
import com.toast.xml.XmlNodeList;
import com.toast.xml.exception.XPathExpressionException;
import com.toast.xml.exception.XmlFormatException;
import com.toast.xml.exception.XmlParseException;

public class ComponentTest
{
   @Test
   public void TestCreateComponent() throws ComponentCreationException, ResourceCreationException, IOException, XmlParseException, XPathExpressionException, XmlFormatException
   {
      XmlDocument document = new XmlDocument();
      document.load(Resource.getResourcePath() + "\\components\\sprites.xml");
      XmlNodeList nodes = document.getRootNode().getNodes("*");
      
      for (int i = 0; i < nodes.size(); i++)
      {
         XmlNode node = nodes.get(i);
         
         String id = node.getAttribute("id").getValue();
                  
         Component component = Component.createComponent(node);
         assertTrue(component != null);
         assertTrue(component.getId().equals(id));
      }
   }
}
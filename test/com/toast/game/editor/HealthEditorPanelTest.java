package com.toast.game.editor;

import java.awt.BorderLayout;
import java.io.File;
import java.net.URL;

import com.toast.game.engine.property.Health;
import com.toast.game.engine.property.Image;
import com.toast.game.engine.property.State;
import com.toast.game.engine.property.Transform;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.game.engine.resource.Texture;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HealthEditorPanelTest
{
   public static void main(final String args[])
   {
      JFrame frame = new JFrame("Test");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(300, 300);
      
      Health health = new Health();
      health.setMaxHealth(100);
      health.setHealth(10);
      
      Transform transform = new Transform();
      
      State state = new State("damage");
      state.setDefaultValue(10);
      state.setValue(10);
      
      Image image = null;
      try
      {
         Texture texture = (Texture)Resource.createResource(getResourcePath() + "\\images\\jason.png");
         image = new Image("jason", texture);
      }
      catch (ResourceCreationException e)
      {
         System.out.format("Failed to load image.\n");
         image = null;
      }

      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
      
      centerPanel.add(new HealthEditor(health));
      centerPanel.add(new TransformEditor(transform));
      centerPanel.add(new StateEditor(state));
      if (image != null)
      {
         centerPanel.add(new ImageEditor(image));
      }

      frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
      
      frame.setVisible(true);
   }
   
   
   private static String getResourcePath() throws ResourceCreationException
   {
      String path = null;
      
      URL url = Resource.class.getResource("/resources");
      if (url != null)
      {
         path = url.getFile();
         path = path.replace("/", File.separator);
      }
      else
      {
         path = null;
         throw (new ResourceCreationException());
      }
      
      return (path); 
   }   
}

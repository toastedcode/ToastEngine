package com.toast.game.engine;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.toast.game.engine.Renderer;
import com.toast.game.engine.component.Component;
import com.toast.game.engine.property.Image;
import com.toast.game.engine.property.Transform;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.game.engine.resource.Texture;

public class RendererTest
{
   public static void main(final String args[])
   {
      JFrame frame = new JFrame("Test");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(300, 300);

      Image image = null;
      Component component = null;
      try
      {
         Texture texture = (Texture)Resource.createResource(Resource.getResourcePath() + "\\images\\jason.png");
         image = new Image("jasonHead", texture);
         
         Transform transform = new Transform();
         transform.setPosition(50, 50);
         transform.setScale(0.5);
                  
         component = new Component("jason");
         component.add(image);
         component.add(transform);
         
         Renderer.initialize(300, 300, 1);
         component.draw();
      }
      catch (ResourceCreationException e)
      {
         System.out.format("Failed to load image.\n");
         image = null;
         component = null;
      }

      @SuppressWarnings("serial")
      JPanel centerPanel = new JPanel()
      {
         @Override
         public void paint(Graphics graphics)
         {
            Renderer.paint(graphics,  this);
         }
      };

      frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
      
      frame.setVisible(true);
   }
}

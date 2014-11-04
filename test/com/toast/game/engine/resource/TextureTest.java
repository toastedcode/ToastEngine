package com.toast.game.engine.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.junit.Before;
import org.junit.Test;

import com.toast.game.engine.resource.Texture;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;

public class TextureTest
{
   @Before
   public void freeResources()
   {
      Resource.freeResources();
   }
   
   
   @Test
   public void testGetId() throws ResourceCreationException
   {
      Texture image = (Texture)Resource.createResource(Resource.getResourcePath() + "\\images\\jason.png");
      assertTrue(image.getId().equals("jason.png"));
   }
   
   
   @Test
   public void testGetBufferedImage() throws ResourceCreationException
   {
      Texture image = (Texture)Resource.createResource(Resource.getResourcePath() + "\\images\\jason.png");
      assertTrue(image.getBufferedImage() != null);
   }
   
   
   @Test
   public void testExists() throws ResourceCreationException
   {
      assertFalse(Resource.exists("jason.png"));
      
      Resource.createResource(Resource.getResourcePath() + "\\images\\jason.png");
         
      assertTrue(Resource.exists("jason.png"));
   }
   
   
   @Test
   public void testGetResource() throws ResourceCreationException
   {
      Resource.createResource(Resource.getResourcePath() + "\\images\\jason.png");
      
      Texture image = (Texture)Resource.getResource("jason.png");
         
      assertTrue(image != null);
      assertTrue(image.getId().equals("jason.png"));
   }
   
   
   public static void main(final String args[])
   {
      try
      {
         JFrame frame = new JFrame("Test");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(300, 300);
         
         final Texture image = (Texture)Resource.createResource(Resource.getResourcePath() + "\\images\\jason.png");
   
         @SuppressWarnings("serial")
         JPanel centerPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (image != null)
                {
                   g.drawImage(image.getBufferedImage(), 0, 0, null);
                }
            }         
         };
            
         centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
         
         frame.getContentPane().add(new JLabel("North"), BorderLayout.NORTH);
         frame.getContentPane().add(new JLabel("South"), BorderLayout.SOUTH);
         frame.getContentPane().add(new JLabel("East"), BorderLayout.EAST);
         frame.getContentPane().add(new JLabel("West"), BorderLayout.WEST);
         frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
         
         frame.setVisible(true);
      }
      catch (ResourceCreationException e)
      {
         
      }
   }
}

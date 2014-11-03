package com.toast.game.engine.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.junit.Before;
import org.junit.Test;

public class SfxTest
{
   @Before
   public void freeResources()
   {
      Resource.freeResources();
   }
   
   
   @Test
   public void testIsLoaded() throws ResourceCreationException
   {
      Sfx sfx = new Sfx("bakedPotato.wav");
      
      assertFalse(sfx.isLoaded() == true);
      
      sfx.load("/resources/sfx/bakedPotato.wav");
      
      assertTrue(sfx.isLoaded() == true);
   }
   
   
   @Test
   public void testGetId() throws ResourceCreationException
   {
      Sfx sfx = (Sfx)Resource.createResource("/resources/sfx/bakedPotato.wav");
      assertTrue(sfx.getId().equals("bakedPotato.wav"));
   }
   
   
   @Test
   public void testGetClip() throws ResourceCreationException, LineUnavailableException
   {
      Sfx sfx = (Sfx)Resource.createResource("/resources/sfx/bakedPotato.wav");
      assertTrue(sfx.getClip() != null);
   }
   
   
   @Test
   public void testExists() throws ResourceCreationException
   {
      assertFalse(Resource.exists("bakedPotato.wav"));
      
      Resource.createResource("/resources/sfx/bakedPotato.wav");
         
      assertTrue(Resource.exists("bakedPotato.wav"));
   }
   
   
   @Test
   public void testGetResource() throws ResourceCreationException
   {
      Resource.createResource("/resources/sfx/bakedPotato.wav");
      
      Sfx sfx = (Sfx)Resource.getResource("bakedPotato.wav");
         
      assertTrue(sfx != null);
      assertTrue(sfx.isLoaded());
      assertTrue(sfx.getId().equals("bakedPotato.wav"));
   }
   
   
   public static void main(final String args[])
   {
      try
      {
         JFrame frame = new JFrame("Test");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(300, 300);
         
         Sfx sfx = (Sfx)Resource.createResource("/resources/sfx/carl.wav");
         final Clip clip = sfx.getClip();
   
         JButton button = new JButton("Play");
         button.addActionListener(new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent event)
            {
               JButton button = (JButton)event.getSource();
               boolean isPlaying = button.getText().equals("Stop");
               
               if (isPlaying == false)
               {
                  clip.start();
                  button.setText("Stop");
               }
               else
               {
                  clip.stop();
                  clip.setFramePosition(0);
                  button.setText("Play");
               }
            }
         });
         
         frame.getContentPane().add(button, BorderLayout.CENTER);
         
         frame.setVisible(true);
      }
      catch (ResourceCreationException | LineUnavailableException e)
      {
         System.out.format("Failed to play sfx.\n");
      }
   }   
}

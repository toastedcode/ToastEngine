package com.toast.game.engine.resource;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.net.URL;

import javax.sound.sampled.LineUnavailableException;

import org.junit.Before;
import org.junit.Test;

import com.toast.game.engine.resource.AnimationMap;
import com.toast.game.engine.resource.Texture;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;

public class ResourceTest
{
   @Before
   public void freeResources()
   {
      Resource.freeResources();
   }
   
   @Test
   public void testCreateResources() throws ResourceCreationException
   {
      Resource.createResources(getResourcePath());
      
      assertTrue(Resource.getResource("jason.png") != null);
      assertTrue(Resource.getResource("boxy.anim") != null);
      assertTrue(Resource.getResource("test.bsh") != null);
      assertTrue(Resource.getResource("frenchFry.wav") != null);
   }
   
   
   @Test
   public void testCreateImage() throws ResourceCreationException
   {
      Texture texture = (Texture)Resource.createResource(getResourcePath() + "\\images\\jason.png");
      
      assertTrue(texture != null);
      assertTrue(texture.getId().equals("jason.png"));
      assertTrue(texture.isLoaded() == true);
      assertTrue(texture.getBufferedImage() != null);
      assertTrue(Resource.getResource("jason.png").equals(texture));
   }
   
   
   @Test
   public void testCreateAnimationMap() throws ResourceCreationException
   {
      AnimationMap animationMap = (AnimationMap)Resource.createResource(getResourcePath() + "\\animations\\boxy.anim");
      
      assertTrue(animationMap != null);
      assertTrue(animationMap.getId().equals("boxy.anim"));
      assertTrue(animationMap.isLoaded() == true);
      assertTrue(Resource.getResource("boxy.anim").equals(animationMap));
      
      AnimationMap.Frame frame = animationMap.getFrame("walk", 0);
      assertTrue(frame != null);
      assertTrue(frame.getPosition().equals(new Point(0, 0)));
      assertTrue(frame.getDimension().equals(new Dimension(70, 56)));
   }
   
   
   @Test
   public void testCreateScript() throws ResourceCreationException
   {
      Script script = (Script)Resource.createResource(getResourcePath() + "\\scripts\\test.bsh");
      
      assertTrue(script != null);
      assertTrue(script.getId().equals("test.bsh"));
      assertTrue(script.isLoaded() == true);
      assertTrue(script.getInterpreter() != null);
      assertTrue(Resource.getResource("test.bsh").equals(script));
   }
   
   
   @Test
   public void testCreateSfx() throws ResourceCreationException, LineUnavailableException
   {
      Sfx sfx = (Sfx)Resource.createResource(getResourcePath() + "\\sfx\\frenchFry.wav");
      
      assertTrue(sfx != null);
      assertTrue(sfx.getId().equals("frenchFry.wav"));
      assertTrue(sfx.isLoaded() == true);
      assertTrue(sfx.getClip() != null);
      assertTrue(Resource.getResource("frenchFry.wav").equals(sfx));
   }
   
   
   private String getResourcePath() throws ResourceCreationException
   {
      String path = null;
      
      URL url = getClass().getResource("/resources");
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

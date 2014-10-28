package com.toast.game.engine.resource;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;

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
   public void testCreateImage() throws ResourceCreationException
   {
      Texture texture = (Texture)Resource.createResource("/resources/images/jason.png");
      
      assertTrue(texture != null);
      assertTrue(texture.getId().equals("jason.png"));
      assertTrue(texture.isLoaded() == true);
      assertTrue(texture.getBufferedImage() != null);
      assertTrue(Resource.getResource("jason.png").equals(texture));
   }
   
   
   @Test
   public void testCreateAnimationMap() throws ResourceCreationException
   {
      AnimationMap animationMap = (AnimationMap)Resource.createResource("/resources/animations/boxy.anim");
      
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
      Script script = (Script)Resource.createResource("/resources/scripts/test.bsh");
      
      assertTrue(script != null);
      assertTrue(script.getId().equals("test.bsh"));
      assertTrue(script.isLoaded() == true);
      assertTrue(script.getInterpreter() != null);
      assertTrue(Resource.getResource("test.bsh").equals(script));
   }
}

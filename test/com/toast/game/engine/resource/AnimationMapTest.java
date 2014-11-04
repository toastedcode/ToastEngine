package com.toast.game.engine.resource;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.Test;

import com.toast.game.engine.resource.AnimationMap;

public class AnimationMapTest
{
   @Test
   public void testConstructor()
   {
      String id = "test";
      AnimationMap animationMap = new AnimationMap(id);
      
      assertTrue(animationMap.getId().equals("test"));
   }
   
   
   @Test
   public void testLoad() throws ResourceCreationException
   {
      AnimationMap animationMap = new AnimationMap("boxy");
      animationMap.load(Resource.getResourcePath() + "\\animations\\boxy.anim");
   }
   
   
   @Test
   public void testGetFrame() throws ResourceCreationException
   {
      AnimationMap animationMap = new  AnimationMap("test");
      
      AnimationMap.Frame frame = animationMap.getFrame("walk", 0);
      assertTrue(frame == null);
      
      animationMap.load(Resource.getResourcePath() + "\\animations\\boxy.anim");
      
      frame = animationMap.getFrame("walk", 0);
      assertTrue(frame != null);
      assertTrue(frame.getPosition().equals(new Point(0, 0)));
      assertTrue(frame.getDimension().equals(new Dimension(70, 56)));
      
      frame = animationMap.getFrame("walk", 5);
      assertTrue(frame == null);
   }
}

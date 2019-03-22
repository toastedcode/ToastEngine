package com.toast.game.engine;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.toast.game.engine.component.Component;
import com.toast.game.engine.property.Animation;
import com.toast.game.engine.property.Image;
import com.toast.game.engine.property.Physics;
import com.toast.game.engine.property.Transform;
import com.toast.game.engine.property.Animation.AnimationDirection;
import com.toast.game.engine.property.Animation.AnimationType;
import com.toast.game.engine.resource.AnimationMap;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.game.engine.resource.Texture;

public class GameTest
{
   public static void main(final String args[])
   {
      JFrame frame = new JFrame("Test");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(300, 300);
      
      Renderer.initialize(300, 300, 1);
      Game game = new Game("game");

      Image image = null;
      Component component = null;
      try
      {
         Texture texture = (Texture)Resource.createResource(Resource.getResourcePath() + "\\images\\jason.png");
         image = new Image("jasonHead", texture);
         
         Transform transform = new Transform();
         transform.setPosition(50, 50);
         transform.setScale(0.5);
         
         Physics physics = new Physics();
         //physics..setVelocity(5, 0);
                  
         component = new Component("jason");
         component.add(image);
         component.add(transform);
         game.add(component);
         
         texture = (Texture)Resource.createResource(Resource.getResourcePath() + "\\images\\boxy.png");
         AnimationMap animationMap = (AnimationMap)Resource.createResource(Resource.getResourcePath() + "\\animations\\boxy.anim");
         Animation walk = new Animation("boxy.walk", texture, animationMap, "walk", 1);
         walk.start(AnimationType.LOOP, AnimationDirection.FORWARD);
         
         component = new Component("boxy");
         component.add(walk);
         component.add(transform);
         game.add(component);
      }
      catch (ResourceCreationException e)
      {
         System.out.format("Failed to load resources.\n");
         image = null;
         component = null;
      }

      frame.getContentPane().add(game.getGamePanel(), BorderLayout.CENTER);
      
      frame.setVisible(true);
   }
}

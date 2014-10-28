package com.toast.game.engine.actor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.toast.game.engine.property.Animation;
import com.toast.game.engine.property.Animation.AnimationDirection;
import com.toast.game.engine.property.Animation.AnimationType;
import com.toast.game.engine.property.Display;
import com.toast.game.engine.property.Transform;
import com.toast.game.engine.resource.AnimationMap;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.game.engine.resource.Texture;

public class SpriteTest
{
   public static void main(final String args[])
   {
      try
      {
         JFrame frame = new JFrame("Test");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(300, 300);
         
         Texture image = (Texture)Resource.createResource("/resources/images/boxy.png");
         AnimationMap animationMap = (AnimationMap)Resource.createResource("/resources/animations/boxy.anim");

         Animation idle = new Animation("boxy.idle", image, animationMap, "idle", 1);
         
         Animation walk = new Animation("boxy.walk", image, animationMap, "walk", 1);
         walk.start(AnimationType.LOOP, AnimationDirection.FORWARD);
      
         Display display = new Display("display");
         display.addChild(idle);
         display.addChild(walk);
         display.setDrawable(walk);
         
         Transform transform = new Transform();
         transform.setPosition(new Point2D.Double(50, 50));
         transform.setScale(1.5);
         
         final Sprite boxy = new Sprite("boxy", null);
         boxy.addChild(display);
         boxy.addChild(idle);
         boxy.addChild(transform);
         
         @SuppressWarnings("serial")
         final JPanel centerPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (boxy != null)
                {
                   boxy.draw(g, new Point(0, 0), 1.0);
                }
            }         
         };
            
         centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
         
         Timer timer = new Timer(100, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               boxy.update(100);
               centerPanel.repaint();
            }
         });
         timer.start();         

         frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
         
         frame.setVisible(true);
      }
      catch (ResourceCreationException e)
      {
         
      }
   } 
}

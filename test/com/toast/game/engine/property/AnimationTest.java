package com.toast.game.engine.property;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.toast.game.engine.property.Animation.AnimationDirection;
import com.toast.game.engine.property.Animation.AnimationType;
import com.toast.game.engine.resource.AnimationMap;
import com.toast.game.engine.resource.Texture;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;

public class AnimationTest
{
   public static void main(final String args[])
   {
      try
      {
         JFrame frame = new JFrame("Test");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(300, 300);
         
         Texture image = (Texture)Resource.createResource(Resource.getResourcePath() + "\\images\\boxy.png");
         AnimationMap animationMap = (AnimationMap)Resource.createResource(Resource.getResourcePath() + "\\animations\\boxy.anim");

         final Animation idle = new Animation("boxy.idle", image, animationMap, "idle", 1);
         
         final Animation walk = new Animation("boxy.walk", image, animationMap, "walk", 1);
         walk.start(AnimationType.LOOP, AnimationDirection.FORWARD);
         
  
         @SuppressWarnings("serial")
         final JPanel centerPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if ((idle != null) &&
                    (walk != null))
                {
                   idle.draw(g, new Point(100, 50), 1.0);
                   walk.draw(g, new Point(100, 150), 1.0);
                }
            }         
         };
            
         centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
         
         Timer timer = new Timer(100, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               idle.update(100);
               walk.update(100);
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

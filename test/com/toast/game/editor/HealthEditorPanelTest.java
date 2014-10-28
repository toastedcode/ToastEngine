package com.toast.game.editor;

import java.awt.BorderLayout;

import com.toast.game.engine.property.Health;
import com.toast.game.engine.property.Transform;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
     
      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
      
      centerPanel.add(new HealthEditor(health));
      centerPanel.add(new TransformEditor(transform));
      
      frame.getContentPane().add(new JLabel("North"), BorderLayout.NORTH);
      frame.getContentPane().add(new JLabel("South"), BorderLayout.SOUTH);
      frame.getContentPane().add(new JLabel("East"), BorderLayout.EAST);
      frame.getContentPane().add(new JLabel("West"), BorderLayout.WEST);
      frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
      
      frame.setVisible(true);
   }

}

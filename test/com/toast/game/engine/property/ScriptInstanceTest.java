package com.toast.game.engine.property;

import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.game.engine.resource.Script;

public class ScriptInstanceTest
{
   public static void main(final String args[])
   {
      try
      {
         JFrame frame = new JFrame("Test");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(300, 300);
         
         Script script = new Script("test.bsh");
         script.load(Resource.getResourcePath() + "\\scripts\\test.bsh");
         assertTrue(script.isLoaded() == true);
         
         final ScriptInstance scriptInstance = new ScriptInstance("script", script);
  
         @SuppressWarnings("serial")
         final JPanel centerPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                
                scriptInstance.draw(g);
            }         
         };

         frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
         
         frame.setVisible(true);
      }
      catch (ResourceCreationException e)
      {
         
      }
   }  
}

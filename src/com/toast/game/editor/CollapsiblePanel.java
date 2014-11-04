package com.toast.game.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class CollapsiblePanel extends JPanel
{
   public CollapsiblePanel(
      String title)
   {
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      
      JPanel titlePanel = createTitlePanel(title);
      add(titlePanel);
      
      this.setBorder(new LineBorder(Color.BLACK));
   }
   
   
   public void setContent(
      Container content)
   {
      if (this.content != null)
      {
         remove(this.content);
      }
      
      this.content = content;
      add(this.content);
   }
   
   
   private JPanel createTitlePanel(
      String titleString)
   {
      JPanel panel = new JPanel();
      panel.add(new JLabel(titleString));
      
      panel.addMouseListener(new MouseListener()
      {
         @Override
         public void mouseClicked(MouseEvent e){}

         @Override
         public void mouseEntered(MouseEvent e){}

         @Override
         public void mouseExited(MouseEvent e){}

         @Override
         public void mousePressed(MouseEvent e)
         {
            content.setVisible(!content.isVisible());
         }

         @Override
         public void mouseReleased(MouseEvent e){}
      });
      
      return (panel);
   }
   
   private Container content = null;
}

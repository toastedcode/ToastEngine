package com.toast.game.editor;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toast.game.engine.property.Health;

@SuppressWarnings("serial")
public class HealthEditor extends CollapsiblePanel
{
   public HealthEditor(
      Health health)
   {
      super("Health");
      
      this.health = health;
      
      setContent(getContentPanel());
   }
   
   
   public boolean validateHealth(
      String stringValue)
   {
      boolean isValid = false;
      
      int intValue;
      try
      {
         intValue = Integer.valueOf(stringValue);
         
         isValid = ((intValue >= health.MIN_HEALTH) &&
                    (intValue <= health.getMaxHealth()));
      }
      catch (Exception e)
      {
         // Not an integer!
      }
      
      return (isValid);
   }
   
   
   public boolean validateMaxHealth(
      String stringValue)
   {
      boolean isValid = false;
      
      int intValue;
      try
      {
         intValue = Integer.valueOf(stringValue);
         
         isValid = (intValue >= health.MIN_HEALTH);
      }
      catch (Exception e)
      {
         // Not an integer!
      }
      
      return (isValid);
   }
   
   private JPanel getContentPanel()
   {
      JPanel contentPanel = new JPanel();
      
      contentPanel.add(getHealthPanel());
      contentPanel.add(getMaxHealthPanel());
      
      return (contentPanel);
   }
   
   
   JPanel getHealthPanel()
   {
      JPanel panel = new JPanel();
      
      final JTextField input = new JTextField();
      input.setColumns(4);
      input.setText(Integer.toString(health.getHealth()));
      input.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent event)
         {
            onUpdate(input.getText());
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            onUpdate(input.getText()); 
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            onUpdate(input.getText());
         }
         
         void onUpdate(
            String text)
         {
            if (validateHealth(text) == true)
            {
               health.setHealth(Integer.valueOf(text));
               input.setForeground(Color.BLACK);
            }
            else
            {
               input.setForeground(Color.RED);
            }
         }
      });
      
      panel.add(new JLabel("Health"));
      panel.add(input);
      
      return (panel);
   }
   
   
   private JPanel getMaxHealthPanel()
   {
      JPanel panel = new JPanel();
      
      final JTextField input = new JTextField();
      input.setColumns(4);
      input.setText(Integer.toString(health.getMaxHealth()));
      input.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent event)
         {
            onUpdate(input.getText());
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            onUpdate(input.getText()); 
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            onUpdate(input.getText());
         }
         
         void onUpdate(
            String text)
         {
            if (validateMaxHealth(text) == true)
            {
               health.setMaxHealth(Integer.valueOf(text));
               input.setForeground(Color.BLACK);
            }
            else
            {
               input.setForeground(Color.RED);
            }
         }
      });
      
      panel.add(new JLabel("Max health"));
      panel.add(input);
      
      return (panel);
   }
   
   private Health health;
}

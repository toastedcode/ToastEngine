package com.toast.game.editor;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toast.game.engine.property.Health;

@SuppressWarnings("serial")
public class HealthEditor extends JPanel
{
   public HealthEditor(
      Health health)
   {
      this.health = health;
      createPanel();
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
   
   private JTextField healthInputField;
   private JTextField maxHealthInputField;
   
   private void createPanel()
   {
      createHealthInput();
      createMaxHealthInput();
   }
   
   
   void createHealthInput()
   {
      healthInputField = new JTextField();
      healthInputField.setColumns(4);
      healthInputField.setText(Integer.toString(health.getHealth()));
      healthInputField.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent event)
         {
            onUpdate(healthInputField.getText());
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            onUpdate(healthInputField.getText()); 
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            onUpdate(healthInputField.getText());
         }
         
         void onUpdate(
            String text)
         {
            if (validateHealth(text) == true)
            {
               health.setHealth(Integer.valueOf(text));
               healthInputField.setForeground(Color.BLACK);
            }
            else
            {
               healthInputField.setForeground(Color.RED);
            }
         }
      });
      
      add(new JLabel("Health"));
      add(healthInputField);      
   }
   
   
   void createMaxHealthInput()
   {
      maxHealthInputField = new JTextField();
      maxHealthInputField.setColumns(4);
      maxHealthInputField.setText(Integer.toString(health.getMaxHealth()));
      maxHealthInputField.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent event)
         {
            onUpdate(maxHealthInputField.getText());
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            onUpdate(maxHealthInputField.getText()); 
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            onUpdate(maxHealthInputField.getText());
         }
         
         void onUpdate(
            String text)
         {
            if (validateMaxHealth(text) == true)
            {
               health.setMaxHealth(Integer.valueOf(text));
               maxHealthInputField.setForeground(Color.BLACK);
            }
            else
            {
               maxHealthInputField.setForeground(Color.RED);
            }
         }
      });
      
      add(new JLabel("Max health"));
      add(maxHealthInputField);          
   }
   
   private Health health;
}

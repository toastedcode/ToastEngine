package com.toast.game.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toast.game.common.CoordinatesType;
import com.toast.game.engine.property.Transform;

@SuppressWarnings("serial")
public class TransformEditor extends JPanel
{
   public TransformEditor(
      Transform transform)
   {
      this.transform = transform;
      createPanel();
   }
   
   
   private void createPanel()
   {
      createPositionInput();
      createCoordsTypeInput();
      createScaleInput();
   }
   
   JTextField positionXInput;
   JTextField positionYInput;
   
   void createPositionInput()
   {
      positionXInput = new JTextField();
      positionXInput.setColumns(4);
      positionXInput.setText(Double.toString(transform.getPosition().getX()));
      positionXInput.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent event)
         {
            onUpdate(positionXInput.getText());
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            onUpdate(positionXInput.getText());         
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            onUpdate(positionXInput.getText());
         }
         
         void onUpdate(
            String text)
         {
            if (validatePosition(text) == true)
            {
               transform.setPosition(Double.valueOf(text), transform.getPosition().getY());
               positionXInput.setForeground(Color.BLACK);
            }
            else
            {
               positionXInput.setForeground(Color.RED);
            }            
         }
      });
      
      add(new JLabel("X"));
      add(positionXInput);
      
      positionYInput = new JTextField();
      positionYInput.setColumns(4);
      positionYInput.setText(Double.toString(transform.getPosition().getY()));
      positionYInput.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent event)
         {
            onUpdate(positionYInput.getText());
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            onUpdate(positionYInput.getText());         
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            onUpdate(positionYInput.getText());
         }
         
         void onUpdate(
            String text)
         {
            if (validatePosition(text) == true)
            {
               transform.setPosition(transform.getPosition().getX(), Double.valueOf(text));
               positionYInput.setForeground(Color.BLACK);
            }
            else
            {
               positionYInput.setForeground(Color.RED);
            }            
         }
      });
      
      add(new JLabel("Y"));
      add(positionYInput);    
   }
   
   
   void createCoordsTypeInput()
   {
      Vector<String> strings = new Vector<String>();
      for (CoordinatesType coordsType : CoordinatesType.values())
      {
         strings.add(coordsType.toString());
      }
      
      JComboBox<CoordinatesType> coordsTypeInput = new JComboBox<CoordinatesType>(CoordinatesType.values());
      coordsTypeInput.setSelectedItem(transform.getCoordsType());
      coordsTypeInput.addActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent event)
         {
            transform.setCoordsType((CoordinatesType)(((JComboBox<?>)(event.getSource())).getSelectedItem()));
         }
      });
      
      add(coordsTypeInput);
   }
   
   
   void createScaleInput()
   {
      
   }
   
   public boolean validatePosition(
      String stringValue)
   {
      boolean isValid = false;
      
      double doubleValue;
      
      try
      {
         doubleValue = Double.valueOf(stringValue);
         
         isValid = (doubleValue > 0);
      }
      catch (Exception e)
      {
         // Not a double!
      }
      
      return (isValid);
   }
   
   private Transform transform;
}

package com.toast.game.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toast.game.common.CoordinatesType;
import com.toast.game.engine.property.Transform;

@SuppressWarnings("serial")
public class TransformEditor extends CollapsiblePanel
{
   public TransformEditor(
      Transform transform)
   {
      super("Transform");
      
      this.transform = transform;
      
      final JPanel contentPanel = createContentPanel();
      
      setContent(contentPanel);
   }
   
   
   private JPanel createContentPanel()
   {
      JPanel contentPanel = new JPanel();
      contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
      
      // Position
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
      createPositionInput(panel);
      createCoordsTypeInput(panel);
      contentPanel.add(panel);

      // Z-order
      createZOrderInput(contentPanel);
      
      // Scale
      createScaleInput(contentPanel);
      
      return (contentPanel);
   }

   
   void createPositionInput(
      Container container)         
   {
      final JTextField positionXInput = new JTextField();
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
      
      final JTextField positionYInput = new JTextField();
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
      
      JPanel panel = new JPanel();
      panel.add(new JLabel("X"));
      panel.add(positionXInput);
      panel.add(new JLabel("Y"));
      panel.add(positionYInput);
      
      container.add(panel);
   }
   
   
   private void createCoordsTypeInput(
      Container container)
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
      
      JPanel panel = new JPanel();
      panel.add(coordsTypeInput);
      
      container.add(panel);
   }
   
   
   private void createZOrderInput(
      Container container)
   {
      final JTextField input = new JTextField();
      input.setColumns(4);
      input.setText(Integer.toString(transform.getZOrder()));
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
            if (validateZOrder(text) == true)
            {
               transform.setZOrder(transform.getZOrder());
               input.setForeground(Color.BLACK);
            }
            else
            {
               input.setForeground(Color.RED);
            }            
         }
      });
      
      JPanel panel = new JPanel();
      panel.add(new JLabel("Z Order"));
      panel.add(input);
      
      container.add(panel);
   }
   
   
   private void createScaleInput(
      Container container)
   {
      final JTextField input = new JTextField();
      input.setColumns(4);
      input.setText(Double.toString(transform.getScale()));
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
            if (validateScale(text) == true)
            {
               transform.setZOrder(transform.getZOrder());
               input.setForeground(Color.BLACK);
            }
            else
            {
               input.setForeground(Color.RED);
            }            
         }
      });
      
      JPanel panel = new JPanel();
      panel.add(new JLabel("Scale"));
      panel.add(input);
      
      container.add(panel);      
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
   
   
   public boolean validateZOrder(
      String stringValue)
   {
      boolean isValid = false;
      
      int intValue;
      
      try
      {
         intValue = Integer.valueOf(stringValue);         
         isValid = ((intValue > 0) &&
                    (intValue <= 100));
      }
      catch (Exception e)
      {
         // Not an int!
      }
      
      return (isValid);
   }   
   
   
   public boolean validateScale(
      String stringValue)
   {
      boolean isValid = false;
      
      double doubleValue;
      
      try
      {
         doubleValue = Double.valueOf(stringValue);         
         isValid = ((doubleValue >= 0) &&
                    (doubleValue <= 10.0));
      }
      catch (Exception e)
      {
         // Not a double!
      }
      
      return (isValid);
   }  
   
   private Transform transform;
}

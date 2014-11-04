package com.toast.game.editor;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toast.game.engine.property.State;

@SuppressWarnings("serial")
public class StateEditor extends CollapsiblePanel
{
   public StateEditor(
      State state)
   {
      super("State: " + state.getId());
      
      this.state = state;
      
      final JPanel contentPanel = createContentPanel();
      
      setContent(contentPanel);
   }
   
   
   private JPanel createContentPanel()
   {
      JPanel contentPanel = new JPanel();
      
      final JTextField valueInput = new JTextField();
      valueInput.setColumns(4);
      valueInput.setText(state.getValue().toString());
      valueInput.getDocument().addDocumentListener(new DocumentListener()
      {
         @Override
         public void changedUpdate(DocumentEvent event)
         {
            onUpdate(valueInput.getText());
         }

         @Override
         public void insertUpdate(DocumentEvent arg0)
         {
            onUpdate(valueInput.getText());         
         }

         @Override
         public void removeUpdate(DocumentEvent arg0)
         {
            onUpdate(valueInput.getText());
         }
         
         void onUpdate(
            String text)
         {
            //if (validatePosition(text) == true)
            {
               valueInput.setForeground(Color.BLACK);
            }
            /*
            else
            {
               valueInput.setForeground(Color.RED);
            }
            */            
         }
      });
      
      contentPanel.add(new JLabel("Value"));
      contentPanel.add(valueInput);
      
      return (contentPanel);
   }
   
   State state;
}

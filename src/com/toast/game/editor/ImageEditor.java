package com.toast.game.editor;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import com.toast.game.engine.property.Image;

@SuppressWarnings("serial")
public class ImageEditor extends CollapsiblePanel
{
   public ImageEditor(Image image)
   {
      super("Image: " + image.getId());
      
      this.image = image;
      
      final JPanel contentPanel = createContentPanel();
      
      setContent(contentPanel);      
   }
   
   private JPanel createContentPanel()
   {
      JPanel contentPanel = new JPanel(){
         @Override
         public void paint(Graphics g)
         {
            double scale = 1.0;
            Point position = new Point(0, 0);

            int width = getWidth();
            int height = getHeight();
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            
            if (imageWidth >= imageHeight)
            {
               // Scale to fill up height.
               scale = (double)height / (double)imageHeight;
               position.setLocation(((double)width / 2.0) - (((double)imageWidth * scale) / 2.0), position.getY());
            }
            else
            {
               // Scale to fill up width.
               scale = (double)width / (double)imageWidth;
               position.setLocation(position.getX(), ((double)height / 2.0) - (((double)imageHeight * scale) / 2.0));
            }
            
            super.paint(g);
            image.draw(g, position, scale);
         }         
      };
      
      return (contentPanel);
   }
   
   private Image image;
}

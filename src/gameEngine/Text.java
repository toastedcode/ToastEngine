package gameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.lang.reflect.Field;
import simpleXml.XmlNode;

public class Text
{
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   // Default constructor.
   public Text()
   {
      initialize();
   }   

   
   public Text(
      String initText,
      Font initFont,
      Color initColor,
      int initParagraphWidth)
   {
      // Set all attributes to default values.
      initialize();
      
      setText(initText);
      setFont(initFont);
      setColor(initColor);
      setParagraphWidth(initParagraphWidth);
   }
   
   
   // Constructor, via XML node.
   public Text(
      XmlNode node)
   {
      // Initialize all attributes with values from the XML node.
      initialize(node);
   }
   
   public void save(
      XmlNode node)
   {
      // Create the text node.
      XmlNode textNode = node.appendChild("text");
      
      // text
      textNode.appendChild("text", text);
      
      // font
      textNode.appendChild("fontName", font.getFontName());
      textNode.appendChild("fontStyle", String.valueOf(font.getStyle()));
      textNode.appendChild("fontSize", String.valueOf(font.getSize()));
      
      // color
      textNode.appendChild("color", color.toString());
      
      // paragraphWidth
      if (paragraphWidth != 0)
      {
         textNode.appendChild("paragraphWidth", String.valueOf(paragraphWidth));         
      }
   }   
  
   public void setText(
      String initTextString)
   {
      text = initTextString;
   }
   
   
   public String getText()
   {
      return (text);
   }
   
   
   public void setFont(
      Font initFont)
   {
      font = initFont;
   }   
   
   
   public Font getFont()
   {
      return (font);
   }
   
   
   public void setColor(
      Color initColor)
   {
      color = initColor;
   }
   
   
   public Color getColor()
   {
      return (color);
   }
   
   
   public void setParagraphWidth(
      int initParagraphWidth)
   {
      paragraphWidth = initParagraphWidth;
   }
   
   
   public int getParagraphWidth()
   {
      return (paragraphWidth);
   }
   
   
   public void draw(
      Point position,
      int opacity)
   {
      Renderer.drawText(
         text, 
         position,
         color,
         font,
         CoordinatesType.WORLD,
         opacity);
   }
      
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************
   
   private void initialize()
   {
      text = "";
      font = new Font("SansSerif", Font.PLAIN, 12);
      color = Color.WHITE;
      paragraphWidth = 0;      
   }
   
   
   private void initialize(
      XmlNode node)
   {
      // Initialize all attributes with values from the XML node.
      initialize();
      
      // textString
      if (node.hasChild("textString") == true)
      {
         text = node.getChild("textString").getValue();
      }
      
      // font
      // TODO: Make this a little more safe.
      if (node.hasChild("font") == true)
      {
         XmlNode fontNode = node.getChild("font");
         
         String fontName = fontNode.getChild("fontName").getValue();
         int fontStyle = fontNode.getChild("fontStyle").getIntValue();
         int fontSize = fontNode.getChild("fontSize").getIntValue();
         font = new Font(fontName, fontStyle, fontSize);
      }
      
      // color
      // TODO: Make this a little more safe.
      if (node.hasChild("color") == true)
      {
         try
         {
            Field field = 
               Class.forName("java.awt.Color").getField(node.getChild("color").getValue().toLowerCase());
            color = (Color)field.get(null);
         }
         catch (Exception e)
         {
            
         }
      }
      
      // paragraphWidth
      if (node.hasChild("paragraphWidth") == true)
      {
         paragraphWidth = node.getChild("paragraphWidth").getIntValue();
      }      
   }   
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private String text;
   
   private Font font;
   
   private Color color;
   
   private int paragraphWidth;
}


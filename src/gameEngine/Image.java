package gameEngine;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import simpleXml.XmlNode;

public class Image
{
   // **************************************************************************
   //                          Public operations
   // **************************************************************************
   
   // Constructor
   public Image(
      XmlNode node)
   {
      initialize(node);
   }
   
   
   public void save(
      XmlNode node)
   {
      // Create the resource node.
      XmlNode imageNode = node.appendChild("resource");
      
      // type
      imageNode.setAttribute("type", "image");
      
      // imageId
      imageNode.setAttribute("id", imageId);
      
      // src
      imageNode.appendChild("src", srcFilename);
   }   
   
   // This operation retrieves the id for an Image.
   public String getImageId()
   {
      return (imageId);
   }
   
   
   // This operation retrieves the source filename for the Image.
   public String getSrc()
   {
      return (srcFilename);
   }
   
   
   // This operation returns the actual buffered image.
   public BufferedImage getBufferedImage()
   {
      return (bufferedImage);
   }
   
   
   public int getWidth()
   {
      return (bufferedImage.getWidth());  
   }
   
   
   public int getHeight()
   {
      return (bufferedImage.getHeight());      
   }
   
   // **************************************************************************
   //                          Private operations
   // **************************************************************************
   
   public void initialize()
   {
      imageId = null;
      srcFilename = null;
      bufferedImage = null;
   }
   
   
   public void initialize(
      XmlNode node)
   {
      // First, initialize all attributes to their default values.
      initialize();
      
      // Extract the Image id.
      imageId = node.getAttribute("id");
      
      // An valid image node requires a "src" node.
      if (node.hasChild("src") == false)
      {
         logger.log(Level.WARNING, "No image source was specified.");         
      }
      else
      {
         // Extract the src file name.
         srcFilename = node.getChild("src").getValue();
         
         // Load the image.
         bufferedImage = ResourceManager.loadImage(srcFilename);
         
         // Check for success.
         if (bufferedImage == null)
         {
            logger.log(Level.WARNING,
                       String.format("Failed to load image from file [%s].", srcFilename)); 
            
         }
      }
   }
      
   // **************************************************************************
   //                          Private attributes
   // **************************************************************************
   
   // Logging
   private final static Logger logger = Logger.getLogger(Animation.class.getName());
   
   // A unique identifier for an Image.
   String imageId;
   
   // The source file from which this Image was loaded.
   String srcFilename;
   
   // The BufferedImage object storing the pixel data for this Image.
   BufferedImage bufferedImage;
}

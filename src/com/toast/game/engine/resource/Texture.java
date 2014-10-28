package com.toast.game.engine.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture extends Resource
{
   public Texture(
      String id)
   {
      super(id);
   }
   
   
   @Override
   public void load(String filename) throws ResourceCreationException
   {
      try
      {
         bufferedImage = ImageIO.read(getClass().getResource(filename));
         isLoaded = true;
      }
      catch (IOException e)
      {
         isLoaded = false;
         bufferedImage = null;
         throw(new ResourceCreationException());
      }
   }
   
   
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
     
   private BufferedImage bufferedImage = null;
}

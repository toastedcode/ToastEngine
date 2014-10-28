package gameEngine;

import bsh.Interpreter;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ResourceManager
{
   // **************************************************************************
   //                          Public Attributes
   // **************************************************************************
   
   public enum FileType
   {
      FILE_TYPE_UNKNOWN,
      FILE_TYPE_XML,
      FILE_TYPE_BSH,
      FILE_TYPE_PNG,
      FILE_TYPE_CSV,
      FILE_TYPE_MAP,
      FILE_TYPE_AI,
   }   
   
   // **************************************************************************
   //                          Public Operations
   // **************************************************************************
   
   public static void initialize()
   {
      fileTypeMap = new HashMap<String, FileType>();
   }   
 
   // ***************************** Images *************************************
   //
   
   public static BufferedImage loadImage(
      String fileName)
   {
      BufferedImage image = null;
      
      URL url = getUrl(fileName);
      if (url == null)
      {
         logger.log(Level.SEVERE, 
                    String.format("Failed to get URL for file name [%s].", 
                                  fileName));           
      }
      else
      {
         // Load the image from the specified URL.
         try
         {
            image = ImageIO.read(url);
         }
         catch (Exception e)
         {
            logger.log(Level.WARNING, 
                       String.format("Exception!  Failed to load image from URL [%s].", url.toString())); 
         }
      }
      
      return (image);
   }
   
   
   // ***************************** Sfx *************************************
   //
   
   /*
   public static Clip loadSfx(
      String fileName)
   {
      Clip clip = null;
      
      URL url = getUrl(fileName);
      if (url == null)
      {
         logger.log(Level.SEVERE, 
                    String.format("Failed to get URL for file name [%s].", 
                                  fileName));           
      }
      else
      {
         // Load the sfx from the specified URL.
         try
         {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
            clip.open(inputStream);
         }
         catch (Exception e)
         {
            logger.log(Level.SEVERE, 
                  String.format("Exception! %s.", 
                                e.toString())); 
            clip = null;            
         }
      }
      
      return (clip);
   }
   */
   
   public static AudioInputStream loadSfx(
         String fileName) throws UnsupportedAudioFileException, IOException
      {
         AudioInputStream audioInputStream = null;
         
         URL url = getUrl(fileName);
         if (url == null)
         {
            logger.log(Level.SEVERE, 
                       String.format("Failed to get URL for file name [%s].", 
                                     fileName));           
         }
         else
         {
            // Load the sfx from the specified URL.
            audioInputStream = AudioSystem.getAudioInputStream(url);
         }
         
         return (audioInputStream);
      }
  
   
   // ***************************** Scripts ************************************
   //
   
   public static Interpreter loadInterpreter(
      String filename)
   {
      // Initialize the return value.
      Interpreter interpreter = new Interpreter();
      
      URL url = getUrl(filename);
      if (url == null)
      {
         logger.log(Level.SEVERE, 
                    String.format("Failed to get URL for file name [%s].", 
                                  filename));           
      }
      else
      {
         try
         {
            interpreter.eval(new BufferedReader(new InputStreamReader(url.openStream())));
         }
         catch (Exception e)
         {
            logger.log(Level.SEVERE, 
                       String.format("Exception! %s.", 
                                     e.toString())); 
            interpreter = null;
         }
      }
      
      return (interpreter);
   }

   // ***************************** General I/O ********************************
   //
   
   public static String getWorkingPath()
   {
      return (System.getProperty("user.dir"));  
   }
   
   public static List<String> loadTextFile(
      String fileName)
   {
      List<String> lines = new ArrayList<String>();
      
      BufferedReader reader = null;
      
      URL url = getUrl(fileName);
      if (url == null)
      {
         logger.log(Level.SEVERE, 
                    String.format("Failed to get URL for file name [%s].", 
                                  fileName));           
      }
      else
      {
         try 
         {
            String currentLine;

            reader = new BufferedReader(new InputStreamReader(url.openStream()));
    
            // Read the first line of the file.
            currentLine = reader.readLine();
            
            while (currentLine != null)
            {
               // Add the line to our array.
               lines.add(currentLine);
               
               // Read the next line.
               currentLine = reader.readLine();               
            }
         } 
         catch (Exception e)
         {
            logger.log(Level.SEVERE, String.format("Exception!  Failed to read from file [%s].", fileName));
            logger.log(Level.SEVERE, String.format("Details: %s", e.toString()));
         }
      }
      
      return (lines);
   }   
   
   
   public static FileType getFileType(
      String filename)
   {
      // Initialize the return value.
      FileType fileType = FileType.FILE_TYPE_UNKNOWN;
      
      // Initialize our mapping of extensions to file types.
      if (fileTypeMap.isEmpty() == true)
      {
         fileTypeMap.put(".xml", FileType.FILE_TYPE_XML);
         fileTypeMap.put(".bsh", FileType.FILE_TYPE_BSH);
         fileTypeMap.put(".png", FileType.FILE_TYPE_PNG);
         fileTypeMap.put(".csv", FileType.FILE_TYPE_CSV);
         fileTypeMap.put(".map", FileType.FILE_TYPE_MAP);
         fileTypeMap.put(".ai", FileType.FILE_TYPE_AI);
      }
      
      if (filename != null)
      {
         // Extract the file extension.
         int startPos = filename.lastIndexOf('.');
         if ((startPos > 0) &&
             (startPos < (filename.length() - 1)))
         {
           String extension = filename.substring(startPos).toLowerCase();
           
           // Map to a file type, if possible.
           if (fileTypeMap.containsKey(extension) == true)
           {
              fileType = fileTypeMap.get(extension);
           }
         }
      }
      
      return (fileType);
   }
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   private static URL getUrl(
      String fileName)
   {
      URL url = null;
      
      try
      {
         url = ResourceManager.class.getResource(fileName);
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, String.format("Exception!  Failed to get URL for file name [%s].", fileName));         
      }
      
      return url;
   }
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = Logger.getLogger(ResourceManager.class.getName());
   
   private static Map<String, FileType> fileTypeMap;
}

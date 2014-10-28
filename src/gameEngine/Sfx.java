package gameEngine;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import simpleXml.XmlNode;

public class Sfx
{
   // **************************************************************************
   //                          Public attributes
   // **************************************************************************
   
   public static final int LOOP_CONTINUOUSLY = Clip.LOOP_CONTINUOUSLY;
   
   // **************************************************************************
   //                          Public operations
   // **************************************************************************
   
   // Constructor
   public Sfx(
      XmlNode node)
   {
      initialize(node);
   }
   
   
   public void save(
      XmlNode node)
   {
      // Create the resource node.
      XmlNode sfxNode = node.appendChild("resource");
      
      // type
      sfxNode.setAttribute("type", "sfx");
      
      // sfxId
      sfxNode.setAttribute("id", sfxId);
      
      // src
      sfxNode.appendChild("src", srcFilename);
   }      
   
   
   // This operation retrieves the id for a Sfx.
   public String getSfxId()
   {
      return (sfxId);
   }
   
   
   // This operation causes the Sfx to be played.
   public Clip play()
   {
      Clip clip = null;
      
      try
      {
         clip = getClip();
         clip.start();
      } 
      catch (LineUnavailableException e)
      {
         logger.log(Level.WARNING,
               String.format("Failed to play sfx [%s].", sfxId)); 
      }
      
      return (clip);
   }
   
   
   // This operation causes the Sfx to be looped.
   public Clip play(
      // The number of times to loop the Sfx.
      // Specify Clip.LOOP_CONTINUOUSLY for an infinite loop.
      int count)
   {
      Clip clip = null;
      
      try
      {
         clip = getClip();
         clip.loop(count);
      } 
      catch (LineUnavailableException e)
      {
         logger.log(Level.WARNING,
               String.format("Failed to play sfx [%s].", sfxId)); 
      }
      
      return (clip);
   }
   
   // **************************************************************************
   //                          Private operations
   // **************************************************************************
   
   private void initialize()
   {
      sfxId = null;
      srcFilename = null;
      af = null;
      size = 0;
      audio = null;
      info = null;
   }
   
   
   private void initialize(
      XmlNode node)
   {
      // First, initialize all attributes to their default values.
      initialize();
      
      // Extract the Sfx id.
      sfxId = node.getAttribute("id");
      
      // An valid image node requires a "src" node.
      if (node.hasChild("src") == false)
      {
         logger.log(Level.WARNING, "No sfx source was specified.");         
      }
      else
      {
         // Extract the src file name.
         srcFilename = node.getChild("src").getValue();
         
         // Load the sfx.
         AudioInputStream audioInputStream;
         try
         {
            audioInputStream = ResourceManager.loadSfx(srcFilename);
            
            af = audioInputStream.getFormat();
            size = (int)(af.getFrameSize() * audioInputStream.getFrameLength());
            audio = new byte[size];
            info = new DataLine.Info(Clip.class, af, size);
            audioInputStream.read(audio, 0, size);
            
         } 
         catch (UnsupportedAudioFileException | IOException e)
         {
            logger.log(Level.WARNING,
                       String.format("Failed to load sfx from file [%s].", srcFilename)); 
         }
      }
   }
   
   
   private Clip getClip() throws LineUnavailableException
   {
      Clip clip = (Clip)AudioSystem.getLine(info);
      clip.open(af, audio, 0, size);
     
      return (clip);
   }
      
   // **************************************************************************
   //                          Private attributes
   // **************************************************************************
   
   // Logging
   private final static Logger logger = Logger.getLogger(Sfx.class.getName());
   
   // A unique identifier for a Sfx.
   String sfxId;
   
   // The source file from which this Sfx was loaded.
   String srcFilename;   
   
   // The audio format of the Sfx.
   AudioFormat af;
   
   // The size (in bytes) of the Sfx.
   int size;
   
   // The raw bytes of the Sfx.
   byte[] audio;
   
   // Info about the Sfx data, used in retrieving a line from the mixer.
   DataLine.Info info;
}
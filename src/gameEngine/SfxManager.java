package gameEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;
import simpleXml.XmlNode;

public class SfxManager implements LineListener
{
   // **************************************************************************
   //                             Public Operations
   // **************************************************************************
   
   public static SfxManager getInstance()
   {
      if (instance == null)
      {
         instance = new SfxManager();
      }
      
      return (instance);
   }

   
   public Sfx createSfx(
      XmlNode node)
   {
      Sfx sfx = new Sfx(node);
      
      String sfxId = sfx.getSfxId();
      if (sfxId == null)
      {
         logger.log(Level.WARNING, String.format("No id specified for Sfx."));
         sfx = null;
      }
      else if (sfxMap.containsKey(sfxId) == true)
      {
         logger.log(Level.WARNING, String.format("Duplicate Sfx id [%s] found.", sfxId)); 
         sfx = null;
      }
      else
      {
         sfxMap.put(sfxId, sfx);         
      }
      
      return (sfx);
   }     

   
   public Sfx getSfx(
      String sfxId)
   {
      Sfx sfx = null;
      
      if (sfxMap.containsKey(sfxId) == false)
      {
         logger.log(Level.WARNING, 
                    String.format("Sfx [%s] cannot be found.", sfxId));
      }
      else
      {
         sfx = sfxMap.get(sfxId);
      }
      
      return (sfx);
   }
  
   
   // This operation causes the Sfx to be played.
   public int playSfx(
      String sfxId)
   {
      int clipId = 0;
      Sfx sfx = getSfx(sfxId);
      
      if (sfx != null)
      {
         Clip clip = sfx.play();
         
         if (clip != null)
         {
            clipId = currentClipId++;
            clipMap.put(clipId, clip);
            
            clip.addLineListener(this);            
         }
      }
      
      return (clipId);
   }
   
   
   // This operation causes the Sfx to be looped.
   public int playSfx(
      String sfxId,
      // The number of times to loop the Sfx.
      // Specify Clip.LOOP_CONTINUOUSLY for an infinite loop.
      int count)
   {
      int clipId = 0;
      Sfx sfx = getSfx(sfxId);
      
      if (sfx != null)
      {
         Clip clip = sfx.play(count);
         
         if (clip != null)
         {
            clipId = currentClipId++;
            clipMap.put(clipId, clip);
            
            clip.addLineListener(this);            
         }
      }
      
      return (clipId);
   }
   
   
   // This operation pauses the playing of the sound clip.
   public void pauseSfx(int clipId)
   {
      if (clipMap.containsKey(clipId) == true)
      {
         Clip clip = clipMap.get(clipId);
         clip.stop();
      }
   }
   
   
   // This operation pauses the playing of the sound clip.
   public void resumeSfx(int clipId)
   {
      if (clipMap.containsKey(clipId) == true)
      {
         Clip clip = clipMap.get(clipId);
         clip.start();
      }
   }
   
   
   // This operation stops the clip and resets it to the beginning.
   public void stopSfx(int clipId)
   {
      if (clipMap.containsKey(clipId) == true)
      {
         Clip clip = clipMap.get(clipId);
         clip.stop();
      }
   }
   
   
   public void stopAllSfx()
   {
      for (Clip clip : clipMap.values())
      {
         clip.stop();
      }
   }
   
   
   public void freeSfx()
   {
      sfxMap.clear();
   }
   
   
   public void save(
      XmlNode node)
   {
      // Loop through the Sfx map and save each entry.
      for (Map.Entry<String, Sfx> entry : sfxMap.entrySet())
      {
         entry.getValue().save(node);
      }
   }      
   
   // **************************************************************************
   //                           LineListener operations   
   
   @Override
   public void update(LineEvent event)
   {
      if (event.getType() == Type.START)
      {
         System.out.format("Start playing\n");         
      }
      else if (event.getType() == Type.STOP)
      {
         System.out.format("Done playing\n");
         
         Clip clip = (Clip)event.getLine();

         clip.close();
         clipMap.remove(clip);
      }
   }   
   
   // **************************************************************************
   //                           Private Operations
   // **************************************************************************
   
   // Constructor
   private SfxManager()
   {
      initialize();
   }
   
   
   // This operation creates the internal structures used in storing Animation.
   private void initialize()
   {
   }   
   
   // **************************************************************************
   //                           Private Attributes
   // **************************************************************************
   
   private final static Logger logger = 
      Logger.getLogger(AnimationManager.class.getName());
   
   // An instance of the SfxManager class, used in implementing the Singleton pattern.
   private static SfxManager instance;
   
   // A map of Sfx objects, used to quickly retrieve a Sfx its id.
   private Map<String, Sfx> sfxMap = new HashMap<>();
   
   private Map<Integer, Clip> clipMap = new HashMap<>();
      
   private int currentClipId = 1;
}
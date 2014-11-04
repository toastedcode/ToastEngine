package com.toast.game.engine.resource;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sfx extends Resource
{
   public Sfx(String id)
   {
      super(id);
      
      isLoaded = false;
      audioFormat = null;
      size = 0;
      audio = null;
      info = null;
   }
   
   @Override
   public void load(String filename) throws ResourceCreationException
   {
      try
      {
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));  
         
         audioFormat = audioInputStream.getFormat();
         size = (int)(audioFormat.getFrameSize() * audioInputStream.getFrameLength());
         audio = new byte[size];
         info = new DataLine.Info(Clip.class, audioFormat, size);
         audioInputStream.read(audio, 0, size);
         
         isLoaded = true;
      }
      catch (IOException | UnsupportedAudioFileException e)
      {
         audioFormat = null;
         size = 0;
         audio = null;
         info = null;
         
         isLoaded = false;
         throw(new ResourceCreationException());
      }
   }
   
   
   public Clip getClip() throws LineUnavailableException
   {
      Clip clip = (Clip)AudioSystem.getLine(info);
      clip.open(audioFormat, audio, 0, size);
     
      return (clip);
   }
   
   
   // The audio format of the Sfx.
   private AudioFormat audioFormat;
   
   // The size (in bytes) of the Sfx.
   private int size;
   
   // The raw bytes of the Sfx.
   private byte[] audio;
   
   // Info about the Sfx data, used in retrieving a line from the mixer.
   private DataLine.Info info;
}

package gameEngine;

import bsh.EvalError;
import bsh.Interpreter;

import java.lang.Exception;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import simpleXml.XmlNode;

public class Script
{
   // **************************************************************************
   //                             Enumerations
   // **************************************************************************
   
   enum ScriptFunction
   {
      INIT,
      HANDLE_EVENT,
      DESTROY,
      DRAW
   }
   
   // **************************************************************************
   //                           Public Operations
   // **************************************************************************
   
   
   public Script(
      String initScriptId,
      String initSrcFilename)
   {
      // Initialize all attributes to their default values.
      initialize();
      
      scriptId = initScriptId;
      srcFilename = initSrcFilename;
      interpreter = ResourceManager.loadInterpreter(srcFilename);
   }
   
   
   public Script(
      XmlNode node)
   {
      initialize(node);         
   }
   
   
   public void save(
      XmlNode node)
   {
      // Create the resource node.
      XmlNode scriptNode = node.appendChild("resource");
      
      // type
      scriptNode.setAttribute("type", "script");
      
      // scriptId
      scriptNode.setAttribute("id", scriptId);
      
      // src
      scriptNode.appendChild("src", srcFilename);
   }   
   
   
   public String getScriptId()
   {
      return (scriptId);
   }
   
   
   public boolean evaluate(
      Sprite sprite,
      ScriptFunction function,
      Object ... parameters)
   {
      boolean returnStatus = true;
      
      try
      {
         interpreter.set("thisSprite", sprite);
         interpreter.set("spriteId", sprite.getSpriteId());
      
         switch (function)
         {
            case INIT:
            {
               interpreter.eval("initialize()");            
               break;
            }
            
            case DESTROY:
            {
               interpreter.eval("destroy()");
               break;
            }
            
            case HANDLE_EVENT:
            {
               Event event = (Event)parameters[0];
               
               if (event.getEventId().equals("eventCALL"))
               {
                  interpreter.set("parameters",  event.getPayload());
                  interpreter.eval(event.getPayload("function") + "(parameters)");
               }
               else
               {
                  interpreter.set("event", event);
                  interpreter.eval("handleEvent(event)");
               }
               break;
            }
            
            case DRAW:
            {
               interpreter.set("thisSprite", sprite);
               interpreter.set("spriteId", sprite.getSpriteId());
               interpreter.set("renderer", Renderer.getInstance());
               
               interpreter.eval("draw(renderer)");
               break;
            }
            
            default:
            {
               logger.log(Level.WARNING, String.format("Unsupported function [%s].", function.toString()));
               returnStatus = false;
               break;
            }         
         }
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, String.format("Exception! %s.", e.toString()));
         returnStatus = false;
      }
      
      return (returnStatus);
   }
   
   
   // This operation calls the initialize() operation defined in the Script.
   public boolean initialize(
      Sprite sprite)      
   {
      boolean returnStatus = true;
      
      try
      {
         interpreter.set("thisSprite", sprite);
         interpreter.set("spriteId", sprite.getSpriteId());
         interpreter.eval("initialize()");
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, 
               String.format("Exception! %s.", 
                             e.toString()));          
         
         returnStatus = false;
      }
      
      if (returnStatus == false)
      {
         logger.log(Level.SEVERE, 
                    String.format("Failed to evaluate initialize() operation for Script [%s].", 
                                  scriptId));
      }
      
      return (returnStatus);
   }
   
   // **************************************************************************
   //                          Private Operations
   // **************************************************************************   
   
   private void initialize()
   {
      scriptId = null;
      interpreter = new Interpreter();
   }
   
   
   private void initialize(
      XmlNode node)      
   {
      // Initialize all attributes to their default values.
      initialize();
      
      // scriptId
      scriptId = node.getAttribute("id");
      
      // interpreter
      if (node.hasChild("src") == true)
      {
         srcFilename = node.getChild("src").getValue();
         interpreter = ResourceManager.loadInterpreter(srcFilename);
      }
   }
   
   
   public boolean loadScript(
      String fileName)
   {
      boolean returnStatus = true;
      
      try
      {
         interpreter.source(fileName);
      }
      catch (Exception e)
      {
         logger.log(Level.SEVERE, 
                    String.format("Exception! %s.", 
                                  e.toString())); 
         returnStatus = false;
      }
      
      if (returnStatus == false)
      {
         logger.log(Level.SEVERE,         
            String.format("Failed to load script [%s] from file [%s].", 
                          scriptId,
                          fileName)); 
      }
      
      return (returnStatus);
   }
  
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private final static Logger logger = 
      Logger.getLogger(Script.class.getName());   
   
   // A unique identifier for the Script.
   String scriptId;
   
   // The source file from which this Script was loaded.
   String srcFilename;
   
   Interpreter interpreter;
}

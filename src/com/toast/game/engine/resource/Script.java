package com.toast.game.engine.resource;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Script extends Resource
{
   // **************************************************************************
   //                             Enumerations
   // **************************************************************************
   
   public enum ScriptFunction
   {
      INIT("initialize"),
      UPDATE("update"),
      HANDLE_EVENT("handleEvent"),
      DESTROY("destroy"),
      DRAW("draw");
      
      private ScriptFunction(String callString)
      {
         CALL_STRING = callString;
      }
      
      public String getCallString()
      {
         return (CALL_STRING);
      }
      
      private final String CALL_STRING;
   }
   
   // **************************************************************************
   //                           Public Operations
   // **************************************************************************
      
   public Script(
      String id)
   {
      super(id);
   }
   
   
   public Interpreter getInterpreter()
   {
      return (interpreter);
   }
    
   // **************************************************************************
   //                                  Resource
   
   @Override
   public void load(String filename) throws ResourceCreationException
   {
      interpreter = new Interpreter();
      
      try
      {
         URL url = getClass().getResource(filename);
         //interpreter.eval(new BufferedReader(new InputStreamReader(url.openStream())));
         interpreter.source(filename);
         isLoaded = true;
      }
      catch (IOException | EvalError e)
      {
         isLoaded = false;
         interpreter = null;
         
         throw(new ResourceCreationException());
      }
   }  
   
   // **************************************************************************
   //                          Private Attributes
   // **************************************************************************
   
   private Interpreter interpreter;
}

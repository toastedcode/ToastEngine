package com.toast.game.engine.resource;

import bsh.EvalError;
import bsh.Interpreter;

import java.io.IOException;

public class Script extends Resource
{
   // **************************************************************************
   //                             Enumerations
   // **************************************************************************
   
   public enum Function
   {
      INIT("initialize"),
      UPDATE("update"),
      HANDLE_EVENT("handleEvent"),
      DESTROY("destroy"),
      DRAW("draw");
      
      private Function(String callString)
      {
         CALL_STRING = callString;
      }
      
      public String getCallString()
      {
         return (CALL_STRING);
      }
      
      private final String CALL_STRING;
   }
   
   
   public static class Variable
   {
      public Variable(
         String name,
         Object value)
      {
         NAME = name;
         VALUE = value;
      }
      
      public String getName()
      {
         return (NAME);
      }
      
      public Object getValue()
      {
         return (VALUE);
      }
      
      private final String NAME;
      
      private final Object VALUE;
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
         // Load the script into the interpreter.
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

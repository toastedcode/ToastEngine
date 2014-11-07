package com.toast.game.engine.property;

import bsh.EvalError;
import bsh.Interpreter;
import java.awt.Graphics;

import com.toast.game.engine.interfaces.Drawable;
import com.toast.game.engine.interfaces.Updatable;
import com.toast.game.engine.resource.Script;

public class ScriptInstance extends Property implements Drawable, Updatable
{
   public ScriptInstance(
      String id,
      Script script)
   {
      super(id);
      SCRIPT = script;
   }
   
   
   public void evaluate(
      Script.Function function,
      Script.Variable ... variables)
   {
      try
      {
         Interpreter interpreter = SCRIPT.getInterpreter();
         
         // Get the call string.
         String callString = function.getCallString() + "()";
         
         // Set parameters.
         for (Script.Variable variable : variables)
         {
            interpreter.set(variable.getName(),  variable.getValue());
         }
         
         // Evaluate.
         interpreter.eval(callString);
      }
      catch (EvalError e)
      {
      }
   }
   
   
   @Override
   public void draw(Graphics graphics)
   {
      evaluate(Script.Function.DRAW, new Script.Variable("graphics", graphics));
   }

   
   @Override
   public int getWidth()
   {
      // TODO
      return (0);
   }

   
   @Override
   public int getHeight()
   {
      // TODO
      return (0);
   }
   
   
   @Override
   public boolean isVisible()
   {
      // TODO Auto-generated method stub
      return false;
   }   
   
   
   @Override
   public void update(long elapsedTime)
   {
      evaluate(Script.Function.UPDATE, new Script.Variable("elapsedTime",  elapsedTime));
   }
   
   Script SCRIPT;
}

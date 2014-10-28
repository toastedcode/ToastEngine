package com.toast.game.engine.components;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.toast.game.engine.property.ScriptInstance;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.game.engine.resource.Script;

public class ScriptTest
{
   @Test
   public void testEvaluate() throws ResourceCreationException
   {
      Script script = new Script("test.bsh");
      script.load("/resources/scripts/test.bsh");
      assertTrue(script.isLoaded() == true);
      
      ScriptInstance scriptInstance = new ScriptInstance("script", script);
      
      scriptInstance.evaluate(Script.ScriptFunction.INIT);
      
      scriptInstance.evaluate(Script.ScriptFunction.DESTROY);
      
      scriptInstance.evaluate(Script.ScriptFunction.DRAW);
      
      scriptInstance.evaluate(Script.ScriptFunction.DRAW);
      
      scriptInstance.evaluate(Script.ScriptFunction.UPDATE);
   }
}

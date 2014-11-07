package com.toast.game.engine.resource;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.toast.game.engine.property.ScriptInstance;
import com.toast.game.engine.resource.Resource;
import com.toast.game.engine.resource.ResourceCreationException;
import com.toast.game.engine.resource.Script;

public class ScriptTest
{
   @Test
   public void testEvaluate() throws ResourceCreationException
   {
      Script script = new Script("test.bsh");
      script.load(Resource.getResourcePath() + "\\scripts\\test.bsh");
      assertTrue(script.isLoaded() == true);
      
      ScriptInstance scriptInstance = new ScriptInstance("script", script);
      
      scriptInstance.evaluate(Script.Function.INIT);
      
      scriptInstance.evaluate(Script.Function.DESTROY);
      
      scriptInstance.evaluate(Script.Function.DRAW);
      
      scriptInstance.evaluate(Script.Function.DRAW);
      
      scriptInstance.evaluate(Script.Function.UPDATE);
   }
}

package com.toast.game.engine.property;

import java.util.Iterator;

import com.toast.game.common.LockableList;
import com.toast.game.engine.Event;
import com.toast.game.engine.interfaces.Updatable;
import com.toast.game.engine.resource.Script;

public class EventHandler extends Property implements Updatable
{
   // *************************************************************************
   //                            Private Operations
   // *************************************************************************
   
   public EventHandler()
   {
      super("eventHandler");
   }
   
   
   public void setScript(ScriptInstance script)
   {
      this.script = script;
   }
   
   
   public void queueEvent(Event event)
   {
      eventQueue.add(event);
   }
   
   
   @Override
   public void update(long elapsedTime)
   {
      eventQueue.lock();
      
      // Loop through the event queue, front to back.
      Iterator<Event> it = eventQueue.iterator();
      while(it.hasNext() == true)
      {
         Event event = it.next();
               
         handleEvent(event);
         eventQueue.remove(event);
      }
      
      eventQueue.unlock();
   }
   
   // *************************************************************************
   //                            Protected Operations
   // *************************************************************************
   
   protected void handleEvent(Event event)
   {
      if (script != null)
      {
         script.evaluate(Script.Function.HANDLE_EVENT, new Script.Variable("event", event));
      }
   }
   
   // *************************************************************************
   //                            Private Variables
   // *************************************************************************
   
   private LockableList<Event> eventQueue = new LockableList<>();
   
   private ScriptInstance script;
}

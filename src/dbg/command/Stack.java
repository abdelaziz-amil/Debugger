package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;

public class Stack implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      List<StackFrame> frames = thread.frames();

      if (frames.isEmpty()) {
        debugger.log("Pile d'appels vide.");
      } else {
        debugger.log("Pile d'appels :");
        for (int i = 0; i < frames.size(); i++) {
          StackFrame frame = frames.get(i);
          debugger.log("#" + i + " " + frame.location().declaringType().name() +
                  "." + frame.location().method().name() +
                  " (" + frame.location().sourceName() + ":" + frame.location().lineNumber() + ")");
        }
      }

    } catch (IncompatibleThreadStateException e) {
      debugger.log("Impossible de récupérer la pile d'appels : " + e.getMessage());
    } catch (AbsentInformationException e) {
      debugger.log("Impossible de récupérer les informations de la pile d'appels.");
    }
    return false;

  }
}

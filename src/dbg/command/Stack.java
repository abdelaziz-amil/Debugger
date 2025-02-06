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
        System.out.println("Pile d'appels vide.");
      } else {
        System.out.println("Pile d'appels :");
        for (int i = 0; i < frames.size(); i++) {
          StackFrame frame = frames.get(i);
          System.out.println("#" + i + " " + frame.location().declaringType().name() +
                  "." + frame.location().method().name() +
                  " (" + frame.location().sourceName() + ":" + frame.location().lineNumber() + ")");
        }
      }

    } catch (IncompatibleThreadStateException e) {
      System.out.println("Impossible de récupérer la pile d'appels : " + e.getMessage());
    } catch (AbsentInformationException e) {
      System.out.println("Impossible de récupérer les informations de la pile d'appels.");
    }
    return false;

  }
}

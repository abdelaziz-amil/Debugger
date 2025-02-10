package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class Methode implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      StackFrame frame = thread.frame(0);
      Method method = frame.location().method();

      debugger.log("Méthode en cours d'exécution : " + method.name());

    } catch (IncompatibleThreadStateException e) {
      debugger.log("Erreur lors de la récupération de la méthode : " + e.getMessage());
    }

    return false;

  }
}

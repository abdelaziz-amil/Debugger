package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;

public class Argument implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      StackFrame frame = thread.frame(0);

      List<Value> arguments = frame.getArgumentValues();

      if (arguments.isEmpty()) {
        debugger.log("Aucun argument pour la méthode.");
      } else {
        debugger.log("Arguments de la méthode :");
        for (Value value : arguments) {
          debugger.log(value.type().name() + " → " + value);
        }
      }

    } catch (IncompatibleThreadStateException e) {
      debugger.log("Erreur lors de la récupération des arguments : " + e.getMessage());
    }
    return false;
  }
}

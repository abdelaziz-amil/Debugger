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
        System.out.println("Aucun argument pour la méthode.");
      } else {
        System.out.println("Arguments de la méthode :");
        for (Value value : arguments) {
          System.out.println(value.type().name() + " → " + value);
        }
      }

    } catch (IncompatibleThreadStateException e) {
      System.out.println("Erreur lors de la récupération des arguments : " + e.getMessage());
    }
    return false;
  }
}

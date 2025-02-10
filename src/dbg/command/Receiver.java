package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class Receiver implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      StackFrame frame = thread.frame(0);
      ObjectReference receiver = frame.thisObject();

      if (receiver != null) {
        debugger.log("Receveur courant (this) : " + receiver.referenceType().name());
      } else {
        debugger.log("Aucun receveur (la méthode est statique).");
      }
    } catch (IncompatibleThreadStateException e) {
      debugger.log("Erreur lors de la récupération du receveur : " + e.getMessage());
    }
    return false;
  }
}

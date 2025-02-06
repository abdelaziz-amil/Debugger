package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class Receiver implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      StackFrame frame = thread.frame(0); // Récupère la frame en cours
      ObjectReference receiver = frame.thisObject(); // Récupère `this`

      if (receiver != null) {
        System.out.println("Receveur courant (this) : " + receiver.referenceType().name());
      } else {
        System.out.println("Aucun receveur (la méthode est statique).");
      }
    } catch (IncompatibleThreadStateException e) {
      System.out.println("Erreur lors de la récupération du receveur : " + e.getMessage());
    }
    return false;
  }
}

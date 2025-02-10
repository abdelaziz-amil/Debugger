package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;

public class Sender implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      List<StackFrame> frames = thread.frames();

      if (frames.size() < 2) {
        debugger.log("Aucun appelant trouvé (la méthode en cours est au sommet de la pile).");
      } else {
        StackFrame senderFrame = frames.get(1);
        ObjectReference sender = senderFrame.thisObject();

        if (sender != null) {
          debugger.log("Objet appelant : " + sender.referenceType().name());
        } else {
          debugger.log("L'appelant est une méthode statique.");
        }
      }

    } catch (IncompatibleThreadStateException e) {
      debugger.log("Erreur lors de la récupération de l'appelant : " + e.getMessage());
    }
    return false;

  }
}

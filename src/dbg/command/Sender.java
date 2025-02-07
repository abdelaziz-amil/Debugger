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
        System.out.println("Aucun appelant trouvé (la méthode en cours est au sommet de la pile).");
      } else {
        StackFrame senderFrame = frames.get(1);
        ObjectReference sender = senderFrame.thisObject();

        if (sender != null) {
          System.out.println("Objet appelant : " + sender.referenceType().name());
        } else {
          System.out.println("L'appelant est une méthode statique.");
        }
      }

    } catch (IncompatibleThreadStateException e) {
      System.out.println("Erreur lors de la récupération de l'appelant : " + e.getMessage());
    }
    return false;

  }
}

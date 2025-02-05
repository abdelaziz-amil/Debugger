package dbg.command;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class Frame implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      if (thread.frameCount() > 0) {
        StackFrame frame = thread.frame(0);

        System.out.println("Frame courante : " + frame.location());
      } else {
        System.out.println("Aucune frame disponible.");
      }
    } catch (IncompatibleThreadStateException e) {
      System.out.println("Impossible de récupérer la frame courante : " + e.getMessage());
    }
    return false;
  }
}

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

        debugger.log("Frame courante : " + frame.location());
      } else {
        debugger.log("Aucune frame disponible.");
      }
    } catch (IncompatibleThreadStateException e) {
      debugger.log("Impossible de récupérer la frame courante : " + e.getMessage());
    }
    return false;
  }
}

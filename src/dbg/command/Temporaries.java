package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;

public class Temporaries implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      if (thread.frameCount() > 0) {
        StackFrame frame = thread.frame(0);
        List<LocalVariable> variables = frame.visibleVariables();

        if (variables.isEmpty()) {
          debugger.log("Aucune variable locale disponible.");
        } else {
          debugger.log("Variables locales :");
          for (LocalVariable var : variables) {
            Value value = frame.getValue(var);
            debugger.log(var.name() + " → " + (value != null ? value.toString() : "null"));
          }
        }

      } else {
        debugger.log("Aucune frame disponible.");
      }
    } catch (AbsentInformationException e) {
      debugger.log("Impossible de récupérer les variables locales : " + e.getMessage());
    } catch (IncompatibleThreadStateException e) {
      debugger.log("Impossible de récupérer la frame courante : " + e.getMessage());
    }
return false;
  }
}

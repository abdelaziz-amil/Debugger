package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;
import java.util.Map;

public class ReceiverVariable implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      StackFrame frame = thread.frame(0);
      ObjectReference receiver = frame.thisObject();

      if (receiver != null) {
        debugger.log("Variables d'instance de " + receiver.referenceType().name() + " :");

        List<Field> fields = receiver.referenceType().allFields();
        Map<Field, Value> values = receiver.getValues(fields);

        for (Map.Entry<Field, Value> entry : values.entrySet()) {
          debugger.log(entry.getKey().name() + " → " + entry.getValue());
        }
      } else {
        debugger.log("Aucune variable d'instance (la méthode est statique).");
      }


    } catch (IncompatibleThreadStateException e) {
      debugger.log("Erreur lors de la récupération des variables d'instance : " + e.getMessage());
    }
    return false;

  }
}

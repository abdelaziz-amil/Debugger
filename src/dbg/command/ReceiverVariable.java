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
        System.out.println("Variables d'instance de " + receiver.referenceType().name() + " :");

        List<Field> fields = receiver.referenceType().allFields();
        Map<Field, Value> values = receiver.getValues(fields);

        for (Map.Entry<Field, Value> entry : values.entrySet()) {
          System.out.println(entry.getKey().name() + " → " + entry.getValue());
        }
      } else {
        System.out.println("Aucune variable d'instance (la méthode est statique).");
      }


    } catch (IncompatibleThreadStateException e) {
      System.out.println("Erreur lors de la récupération des variables d'instance : " + e.getMessage());
    }
    return false;

  }
}

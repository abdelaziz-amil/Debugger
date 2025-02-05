package dbg.command;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import dbg.ScriptableDebugger;

public class Step implements DebuggerCommand {
  @Override
  public void execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {

    // Crée un StepRequest qui entre dans les méthodes
    StepRequest stepRequest = vm.eventRequestManager().createStepRequest(
            event.thread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO
    );
    stepRequest.enable();
    System.out.println("Step: Exécution de la prochaine instruction.");
  }
}

package dbg.command;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import dbg.ScriptableDebugger;

public class StepOver implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    // Crée un StepRequest pour sauter la ligne actuelle
    StepRequest stepRequest = vm.eventRequestManager().createStepRequest(
            event.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER
    );
    stepRequest.enable();
    System.out.println("Step Over: Exécution de la ligne courante.");
    return true;
  }
}

package dbg.command;

import com.sun.jdi.Location;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;
import dbg.ScriptableDebugger;

public class Step implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      Location location = event.location();
      debugger.recordStep(location);  // 🔥 Enregistre le step

      StepRequest stepRequest = vm.eventRequestManager()
              .createStepRequest(event.thread(), StepRequest.STEP_LINE, StepRequest.STEP_INTO);
      stepRequest.enable();
      debugger.log("Step: Exécution de la prochaine instruction.");
      return true;
    } catch (Exception e) {
      debugger.log("Erreur lors du step : " + e.getMessage());
      return false;
    }
  }
}

package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;
import dbg.ScriptableDebugger;

import java.util.List;

public class BreakPoints implements DebuggerCommand {
  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) throws AbsentInformationException {
    List<BreakpointRequest> breakpoints = vm.eventRequestManager().breakpointRequests();

    if (breakpoints.isEmpty()) {
      debugger.log("Aucun point d'arrêt actif.");
    } else {
      debugger.log("Points d'arrêt actifs :");
      for (BreakpointRequest bp : breakpoints) {
        Location loc = bp.location();
        debugger.log("- " + loc.sourceName() + ":" + loc.lineNumber());
      }
    }
    return false;

  }
}

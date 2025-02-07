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
      System.out.println("Aucun point d'arrêt actif.");
    } else {
      System.out.println("Points d'arrêt actifs :");
      for (BreakpointRequest bp : breakpoints) {
        Location loc = bp.location();
        System.out.println("- " + loc.sourceName() + ":" + loc.lineNumber());
      }
    }
    return false;

  }
}

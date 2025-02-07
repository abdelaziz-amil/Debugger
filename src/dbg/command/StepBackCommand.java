package dbg.command;

import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;
import dbg.ScriptableDebugger;
import dbg.history.ExecutionHistory;

import java.util.List;

public class StepBackCommand implements DebuggerCommand {

  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    if (debugger.getPc() < 1) {
      System.out.println("Impossible de revenir en arrière : pas assez d'historique.");
      return false;
    }

    int previousLine = debugger.decrementPc(1).lineNumber();


    if (previousLine == -1) {
      System.out.println("Erreur : pas de ligne précédente trouvée.");
      return false;
    }

    System.out.println("Step-back: Retour à la ligne " + previousLine);
    debugger.restartAndReplay(previousLine);
    return true;
  }

}

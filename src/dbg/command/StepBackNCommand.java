package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class StepBackNCommand implements DebuggerCommand {

  private int steps = 1;

  public void setSteps(int steps) {
    this.steps = steps;
  }

  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) throws AbsentInformationException {
    if (debugger.getPc() < steps) {
      System.out.println("Impossible de revenir en arrière : pas assez d'historique.");
      return false;
    }

    int previousLine = debugger.decrementPc(steps).lineNumber();


    if (previousLine == -1) {
      System.out.println("Erreur : pas de ligne précédente trouvée.");
      return false;
    }

    System.out.println("Step-back: Retour à la ligne " + previousLine);
    debugger.restartAndReplay(previousLine);
    return true;  }
}

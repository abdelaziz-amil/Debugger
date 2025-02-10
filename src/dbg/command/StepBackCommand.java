package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class StepBackCommand implements DebuggerCommand {

  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) throws AbsentInformationException {
    if (debugger.getPc() < 1) {
      System.out.println("Impossible de revenir en arrière : pas assez d'historique.");
      return false;
    }

    Location previousLocation = debugger.decrementPc(1);
    int previousLine = previousLocation.lineNumber();
    String previousClass = previousLocation.declaringType().name();


    if (previousLine == -1) {
      System.out.println("Erreur : pas de ligne précédente trouvée.");
      return false;
    }

    System.out.println("Step-back: Retour à la ligne " + previousLine);
    debugger.restartAndReplay(previousLine, previousClass);
    return true;
  }

}

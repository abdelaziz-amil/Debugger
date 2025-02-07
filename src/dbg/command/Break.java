package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;
import dbg.ScriptableDebugger;


public class Break implements DebuggerCommand {
  private String fileName;
  private int lineNumber;

  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {

      for (ReferenceType refType : vm.allClasses()) {
        if (refType.name().endsWith(fileName)) {
          for (Location location : refType.locationsOfLine(lineNumber)) {
            BreakpointRequest breakpointRequest = vm.eventRequestManager().createBreakpointRequest(location);
            breakpointRequest.enable();
            System.out.println("Point d'arrêt installé à " + location);
          }
          return false;
        }
      }
      System.out.println("Fichier " + fileName + " non trouvé.");
    } catch (AbsentInformationException e) {
      System.out.println("Erreur lors de la création du point d'arrêt : " + e.getMessage());
    }
    return false;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }
}

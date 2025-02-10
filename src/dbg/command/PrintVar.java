package dbg.command;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.List;

public class PrintVar implements DebuggerCommand {

  private String varName;

  public void setVarName(String varName) {
    this.varName = varName;
  }

  @Override
  public boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    try {
      ThreadReference thread = event.thread();
      StackFrame frame = thread.frame(0);

      List<LocalVariable> variables = frame.visibleVariables();

      for (LocalVariable var : variables) {
        if (var.name().equals(varName)) {
          Value value = frame.getValue(var);
          debugger.log(varName + " → " + value);
          return false;
        }
      }

      debugger.log("Variable '" + varName + "' non trouvée dans la frame.");

    } catch (AbsentInformationException e) {
      debugger.log("Erreur : Les informations de débogage ne sont pas disponibles (compiler avec -g).");
    } catch (IncompatibleThreadStateException e) {
      debugger.log("Erreur : Impossible d'accéder à la stack frame.");
    }
    return false;

  }
}

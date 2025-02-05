package dbg.command;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
  private final Map<String, DebuggerCommand> commands = new HashMap<>();

  public CommandManager() {
    commands.put("step", new Step());
    commands.put("so", new StepOver());
    commands.put("continue", new Continue());
    commands.put("print", new PrintVar());
    commands.put("argument", new Argument());
    commands.put("break", new Break());
    commands.put("bbmc", new BreakBeforeMethodeCall());
    commands.put("frame", new Frame());
    commands.put("tmp", new Temporaries());
  }

  public boolean executeCommand(String command, ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {
    DebuggerCommand cmd = commands.get(command.toLowerCase().trim());
    if (cmd != null) {
      return cmd.execute(debugger, vm, event);
    } else {
      System.out.println("Commande inconnue: " + command);
      return false;
    }
  }
}

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
    commands.put("frame", new Frame());
    commands.put("tmp", new Temporaries());
    commands.put("stack", new Stack());
    commands.put("receiver", new Receiver());
    commands.put("sender", new Sender());
    commands.put("rv", new ReceiverVariable());
    commands.put("method", new Methode());
    commands.put("argument", new Argument());
    commands.put("pv", new PrintVar());
    commands.put("break", new Break());
    commands.put("breakpoint", new BreakPoints());
    commands.put("bo", new BreakOnce());
    commands.put("boc", new BreakOnCount());
    commands.put("bbmc", new BreakBeforeMethodeCall());
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

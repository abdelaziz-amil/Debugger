package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

import java.util.HashMap;
import java.util.List;
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
    commands.put("step-back", new StepBackCommand());
    commands.put("sbn", new StepBackNCommand());
    //commands.put("bo", new BreakOnce());
    //commands.put("boc", new BreakOnCount());
    //commands.put("bbmc", new BreakBeforeMethodeCall());
  }

  public boolean executeCommand(String command, ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) throws AbsentInformationException {
    String[] inputs  = command.split(" ");
    if (inputs.length > 1) {
      command = inputs[0];
      if (command.equals("pv")) {
        PrintVar pv = (PrintVar) commands.get("pv");
        pv.setVarName(inputs[1]);
      } else if (command.equals("break")) {
        Break b = (Break) commands.get("break");
        b.setFileName(inputs[1]);
        b.setLineNumber(Integer.parseInt(inputs[2]));
      } else if (command.equals("sbn")) {
        StepBackNCommand sb = (StepBackNCommand) commands.get("sbn");
        sb.setSteps(Integer.parseInt(inputs[1]));
      }
    }
    DebuggerCommand cmd = commands.get(command.toLowerCase().trim());
    if (cmd != null) {
      return cmd.execute(debugger, vm, event);
    } else {
      System.out.println("Commande inconnue: " + command);
      return false;
    }
  }
}

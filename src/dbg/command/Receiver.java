package dbg.command;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class Receiver implements DebuggerCommand {
  @Override
  public void execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {

  }
}

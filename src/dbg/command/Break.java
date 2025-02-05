package dbg.command;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public class Break implements DebuggerCommand {
  @Override
  public void execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) {

  }
}

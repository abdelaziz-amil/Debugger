package dbg.command;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import dbg.ScriptableDebugger;

public interface DebuggerCommand {
  boolean execute(ScriptableDebugger debugger, VirtualMachine vm, LocatableEvent event) throws AbsentInformationException;
}

package dbg.history;

import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;

import java.util.List;

public class ExecutionState {
  private final List<StackFrame> stackFrames;

  public ExecutionState(ThreadReference thread) throws Exception {
    this.stackFrames = thread.frames(); // Capture l'état de la pile
  }

  public List<StackFrame> getStackFrames() {
    return stackFrames;
  }
}

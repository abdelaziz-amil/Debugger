package dbg.history;

import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExecutionHistory {
  private int currentIndex = -1;

  private Stack<List<StackFrame>> history = new Stack<>();

  public void saveState(List<StackFrame> stackFrames) {
    history.push(stackFrames);
  }

  public List<StackFrame> getPreviousState() {
    if (history.size() > 1) {
      history.pop(); // On enlève l'état actuel
      return history.peek(); // On retourne l'état précédent
    }
    return null;
  }

  public List<StackFrame> getPreviousState(int steps) {
    currentIndex = Math.max(0, currentIndex - steps);
    return history.get(currentIndex);
  }
}


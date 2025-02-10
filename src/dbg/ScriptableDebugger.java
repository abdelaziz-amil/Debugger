package dbg;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import dbg.command.CommandManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScriptableDebugger {

    private Class debugClass;
    private VirtualMachine vm;
    private final CommandManager commandManager = new CommandManager();
    private final String userMessage = "Veuillez taper une commande valide pour continuer le stepping : ";
    private int pc = 0;
    private final List<Location> executionHistory = new ArrayList<>();

    public void recordStep(Location location) {
        pc++;
        executionHistory.add(location);
    }

    public Location decrementPc(int numberOfSteps) {
        if (pc >= numberOfSteps) {
            for (int tmp = pc; tmp - numberOfSteps + 1 < pc; pc--){
                executionHistory.remove(executionHistory.size() - 1);
            }
            pc--;
            return executionHistory.remove(executionHistory.size() - 1);
        }
        return null;
    }

    public int getPc() {
        return pc;
    }

    public VirtualMachine connectAndLaunchVM() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue(debugClass.getName());
        return launchingConnector.launch(arguments);
    }

    public void attachTo(Class debuggeeClass) {
        this.debugClass = debuggeeClass;
        try {
            vm = connectAndLaunchVM();
            enableClassPrepareRequest(vm);
            startDebugger();
        } catch (IOException | IllegalConnectorArgumentsException | VMStartException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableClassPrepareRequest(VirtualMachine vm) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(debugClass.getName());
        classPrepareRequest.enable();
    }

    public void setBreakPoint(Location location) {
        BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
        bpReq.enable();
    }

    public void setBreakPoint(String className, int lineNumber) throws AbsentInformationException {
        for (ReferenceType targetClass : vm.allClasses()) {
            if (targetClass.name().equals(className)) {
                Location location = targetClass.locationsOfLine(lineNumber).get(0);
                BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
                bpReq.enable();
            }
        }
    }

    private void waitForUserInput(LocatableEvent event) {
        vm.eventRequestManager().deleteEventRequests(vm.eventRequestManager().stepRequests());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(userMessage);
        try {
            String input = reader.readLine();
            boolean isValidCommand = commandManager.executeCommand(input, this, vm, event);
            while (!isValidCommand) {
                System.out.println(userMessage);
                input = reader.readLine();
                isValidCommand = commandManager.executeCommand(input, this, vm, event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AbsentInformationException e) {
          throw new RuntimeException(e);
        }
    }

    public void startDebugger() throws VMDisconnectedException, InterruptedException, AbsentInformationException, IncompatibleThreadStateException {
        EventSet eventSet;
        while ((eventSet = vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                System.out.println(event.toString());

                if (event instanceof ClassPrepareEvent) {
                    setBreakPoint(debugClass.getName(), 6);
                    setBreakPoint(debugClass.getName(), 10);
                }

                if (event instanceof LocatableEvent) {
                    waitForUserInput((LocatableEvent) event);
                }

                if (event instanceof VMDisconnectEvent) {
                    System.out.println("End of program");
                    InputStreamReader reader = new InputStreamReader(vm.process().getInputStream());
                    OutputStreamWriter writer = new OutputStreamWriter(System.out);
                    try {
                        reader.transferTo(writer);
                        writer.flush();
                    } catch (IOException e) {
                        System.out.println("Target VM input stream reading error.");
                    }
                }
            }
            vm.resume();
        }
    }
    public void restartAndReplay(int targetLine, String targetClassName) {
        try {
            vm.exit(0);

            vm = connectAndLaunchVM();
            enableClassPrepareRequest(vm);

            startReplay(targetLine, targetClassName);

        } catch (Exception e) {
            System.out.println("Erreur lors du redémarrage de la VM : " + e.getMessage());
        }
    }

    private void startReplay(int targetLine, String targetClassName) throws InterruptedException, AbsentInformationException {
        EventSet eventSet;
        while ((eventSet = vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                if (event instanceof ClassPrepareEvent) {
                    setBreakPoint(debugClass.getName(), targetLine);
                }

                if (event instanceof BreakpointEvent) {
                    System.out.println("✅ Revenu à la ligne " + targetLine);
                    waitForUserInput((LocatableEvent) event);
                    return;
                }
            }
            vm.resume();
        }
    }

}
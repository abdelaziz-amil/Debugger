package dbg;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import dbg.command.CommandManager;

import javax.swing.*;
import java.awt.*;
import java.awt.EventQueue;
import java.io.IOException;
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
    private Event currentEvent;

    // UI Components
    private JFrame frame;
    private JTextArea logArea;

    public ScriptableDebugger() {
        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("Scriptable Debugger");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JButton stepButton, stepOverButton, continueButton, frameButton,
                temporariesButton, stackButton, receiverButton, senderButton,
                receiverVariableButton, methodButton, argumentButton, printVarButton,
                breakButton, breakpointButton, stepBackButton;

        stepButton = new JButton("Step");
        stepOverButton = new JButton("Step Over");
        continueButton = new JButton("Continue");
        frameButton = new JButton("Frame");
        temporariesButton = new JButton("Temporaries");
        stackButton = new JButton("Stack");
        receiverButton = new JButton("Receiver");
        senderButton = new JButton("Sender");
        receiverVariableButton = new JButton("Receiver Variable");
        methodButton = new JButton("Method");
        argumentButton = new JButton("Argument");
        printVarButton = new JButton("Print");
        stepBackButton = new JButton("Step Back");
        breakButton = new JButton("Break");
        breakpointButton = new JButton("Breakpoint");

        stepButton.addActionListener(e -> executeCommand("step"));
        stepOverButton.addActionListener(e -> executeCommand("so"));
        continueButton.addActionListener(e -> executeCommand("continue"));
        frameButton.addActionListener(e -> executeCommand("frame"));
        temporariesButton.addActionListener(e -> executeCommand("tmp"));
        stackButton.addActionListener(e -> executeCommand("stack"));
        receiverButton.addActionListener(e -> executeCommand("receiver"));
        senderButton.addActionListener(e -> executeCommand("sender"));
        receiverVariableButton.addActionListener(e -> executeCommand("rv"));
        methodButton.addActionListener(e -> executeCommand("method"));
        argumentButton.addActionListener(e -> executeCommand("argument"));
        printVarButton.addActionListener(e -> executeCommand("print"));
        stepBackButton.addActionListener(e -> executeCommand("step-back"));
        breakButton.addActionListener(e -> executeCommand("break"));
        breakpointButton.addActionListener(e -> executeCommand("breakpoint"));
        // Boutons de commande
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 5));
        buttonPanel.add(stepButton);
        buttonPanel.add(stepOverButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(frameButton);
        buttonPanel.add(temporariesButton);
        buttonPanel.add(stackButton);
        buttonPanel.add(receiverButton);
        buttonPanel.add(senderButton);
        buttonPanel.add(receiverVariableButton);
        buttonPanel.add(methodButton);
        buttonPanel.add(argumentButton);
        buttonPanel.add(printVarButton);
        buttonPanel.add(breakpointButton);
        buttonPanel.add(stepBackButton);
        buttonPanel.add(breakButton);
        buttonPanel.add(breakpointButton);


        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void executeCommand(String command) {
        if (!command.isEmpty() && currentEvent != null) {
            try {
                boolean success = commandManager.executeCommand(command, this, vm, (LocatableEvent) currentEvent);
                if (success) {
                    currentEvent = null; // Débloquer l'attente de commande
                } else {
                }
            } catch (Exception e) {
                log("Erreur lors de l'exécution de la commande : " + e.getMessage());
            }
        }
    }

    public void log(String message) {
        logArea.append(message + "\n");
    }


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
            log(e.toString());
        } catch (VMDisconnectedException e) {
            log("Virtual Machine is disconnected: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log("Erreur lors de l'attachement : " + e.getMessage());
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
        SwingUtilities.invokeLater(() -> {
            log("Cliquer sur une commande pour continuer :");
        });

        while (currentEvent == event) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void startDebugger() {
        new Thread(() -> {
            try {
                EventSet eventSet;
                while ((eventSet = vm.eventQueue().remove()) != null) {
                    for (Event event : eventSet) {
                        log("Événement reçu : " + event.toString());

                        if (event instanceof ClassPrepareEvent) {
                            setBreakPoint(debugClass.getName(), 6);
                            setBreakPoint(debugClass.getName(), 10);
                        }

                        if (event instanceof LocatableEvent) {
                            currentEvent = event;
                            log("En attente d'une commande...");

                            waitForUserInput((LocatableEvent) currentEvent);
                        }

                        if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
                            log("Fin du programme");
                            return;
                        }
                    }

                    if (vm != null && vm.process() != null) {
                        vm.resume();
                    }
                }
            } catch (Exception e) {
                log("Erreur dans le débogueur : " + (e.getMessage() != null ? e.getMessage() : "Exception inconnue"));
                e.printStackTrace();
            }
        }).start();
    }

    public void restartAndReplay(int targetLine) {
        try {
            log("Redémarrage de la VM pour revenir à la ligne " + targetLine + "...");
            List<Location> previousBreakpoints = new ArrayList<>();
            for (BreakpointRequest br : vm.eventRequestManager().breakpointRequests()) {
                previousBreakpoints.add(br.location());
            }

            Thread.sleep(500);

            vm.exit(0);
            vm = connectAndLaunchVM();
            enableClassPrepareRequest(vm);

            startReplay(targetLine);

            restoreBreakpoints(previousBreakpoints);

        } catch (Exception e) {
            log("Erreur lors du redémarrage de la VM : " + e.getMessage());
        }
    }

    private void restoreBreakpoints(List<Location> previousBreakpoints) {
        vm.eventRequestManager().deleteAllBreakpoints();
        for (Location location : previousBreakpoints) {
            try {
                setBreakPoint(location.declaringType().name(), location.lineNumber());
            } catch (Exception e) {
                log("Impossible de restaurer le breakpoint à la ligne " + location.lineNumber());
            }
        }
    }


    private void startReplay(int targetLine) throws InterruptedException, AbsentInformationException {
        log("Attente du chargement de la classe pour revenir à la ligne " + targetLine + "...");

        while (true) {
            EventSet eventSet = vm.eventQueue().remove();
            for (Event event : eventSet) {
                log("Événement reçu : " + event.toString());

                if (event instanceof ClassPrepareEvent) {
                    log("Classe chargée, définition du breakpoint à la ligne " + targetLine);
                    setBreakPoint(debugClass.getName(), targetLine);
                }

                if (event instanceof LocatableEvent) {
                    log("Revenu à la ligne " + targetLine);
                    currentEvent = event;
                    return;
                }
            }
            vm.resume();
        }
    }

}
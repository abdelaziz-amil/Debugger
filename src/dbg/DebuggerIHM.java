package dbg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DebuggerIHM extends JFrame implements DebuggerListener {
  public static final String STEP = "step";
  public static final String STEP_BACK = "step-back";
  public static final String CONTINUE = "continue";
  public static final String BREAK = "break";
  private JTextArea outputArea;
  private JTextField inputField;
  private JButton stepButton, stepOverButton, continueButton, frameButton,
          temporariesButton, stackButton, receiverButton, senderButton,
          receiverVariableButton, methodButton, argumentButton, printVarButton,
          breakButton, breakpointButton, stepBackButton;
  public ScriptableDebugger debugger = new ScriptableDebugger();

  public DebuggerIHM() {
    setTitle("Debugger Interface");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initializeComponents();
    setVisible(true);
    debugger.attachTo(JDISimpleDebuggee.class);
  }

  private void initializeComponents() {
    // Layout principal
    setLayout(new BorderLayout());

    // Zone de sortie
    outputArea = new JTextArea();
    outputArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(outputArea);
    add(scrollPane, BorderLayout.CENTER);

    // Zone d'entrée et boutons
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());

    inputField = new JTextField();
    inputPanel.add(inputField, BorderLayout.CENTER);

    JButton sendButton = new JButton("Envoyer");
    inputPanel.add(sendButton, BorderLayout.EAST);

    add(inputPanel, BorderLayout.SOUTH);

    // Boutons de commande
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(4, 4));

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

    add(buttonPanel, BorderLayout.NORTH);

    // Gestion des actions
    sendButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String command = inputField.getText();
        inputField.setText("");
        appendOutput("Commande : " + command);
      }
    });

    stepButton.addActionListener(e -> appendOutput("Commande : " + STEP));
    stepBackButton.addActionListener(e -> appendOutput("Commande : " + STEP_BACK));
    continueButton.addActionListener(e -> appendOutput("Commande : " + CONTINUE));
    breakButton.addActionListener(e -> appendOutput("Commande : " + BREAK));
  }

  public void appendOutput(String text) {
    outputArea.append(text + "\n");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(DebuggerIHM::new);
  }

  @Override
  public void onDebugEvent(String message) {
    SwingUtilities.invokeLater(() -> outputArea.append(message + "\n"));
  }
}

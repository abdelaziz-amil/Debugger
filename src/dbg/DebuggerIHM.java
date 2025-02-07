package dbg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DebuggerIHM extends JFrame{
  public static final String STEP = "step";
  public static final String STEP_BACK = "step-back";
  public static final String CONTINUE = "continue";
  public static final String BREAK = "break";
  private JTextArea outputArea;
  private JTextField inputField;
  private JButton stepButton, stepBackButton, continueButton, breakButton;

  public DebuggerIHM() {
    setTitle("Debugger Interface");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initializeComponents();
    setVisible(true);
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
    buttonPanel.setLayout(new GridLayout(1, 4));

    stepButton = new JButton("Step");
    stepBackButton = new JButton("Step Back");
    continueButton = new JButton("Continue");
    breakButton = new JButton("Break");

    buttonPanel.add(stepButton);
    buttonPanel.add(stepBackButton);
    buttonPanel.add(continueButton);
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
}

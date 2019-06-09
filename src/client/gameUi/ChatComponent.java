package client.gameUi;

import client.Client;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Base64;

public class ChatComponent extends JPanel{
  private Client CLIENT;
  //https://www.javatpoint.com/java-jtextpane
  private static double SCALING;
  private static int HEIGHT, WIDTH;
  private static int PARENT_WIDTH, PARENT_HEIGHT;

  private SimpleAttributeSet regular = new SimpleAttributeSet();
  private SimpleAttributeSet friendly = new SimpleAttributeSet();
  private SimpleAttributeSet enemy = new SimpleAttributeSet();

  private JTextField textField = new JTextField();
  private JScrollPane scrollPane;
  Document doc;

  private int mode = 1; // Default is to everyone
  private String[] modeString = {"To Team: ", "To Everyone: ", "To Player: "};

  public ChatComponent(double SCALING, int width, int height, Client client){
    SCALING = SCALING;
    WIDTH = width;
    HEIGHT = height;
    this.CLIENT = client;

    // Setting up styles
    StyleConstants.setFontFamily(regular, "Arial");
    StyleConstants.setFontSize(regular, (int)(10 * SCALING));
    friendly.addAttributes(regular);
    StyleConstants.setBold(friendly, true);
    StyleConstants.setForeground(friendly, Color.decode("#66bb6a"));
    enemy.addAttributes(regular);
    StyleConstants.setBold(enemy, true);
    StyleConstants.setForeground(enemy,Color.decode("#ff7043"));
    StyleConstants.setForeground(regular, Color.decode("#f5f5f5"));

    // Styling panel
    this.setLayout(new BorderLayout());
    this.setBackground(new Color(255,255,255,20 ));

    // Adding components
    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setBackground(new Color(0, 0, 0, 0));
    doc = textPane.getStyledDocument();
    try{
      doc.insertString(doc.getLength(), "Game Chat\n", regular);
    } catch (Exception e){
      e.printStackTrace();
    }
    JPanel textPanel = new JPanel(new BorderLayout());
    textPanel.setBackground(new Color(0,0,0,0));
    textPanel.add(textPane, BorderLayout.SOUTH);
    scrollPane = new JScrollPane(textPanel);
    scrollPane.setBackground(new Color(0,0,0,0));
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        e.getAdjustable().setValue(e.getAdjustable().getMaximum());
      }
    });
    this.add(scrollPane, BorderLayout.CENTER);
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
    textField.setBackground(new Color(0,0,0,0));
    textField.setFont(new Font("Arial", Font.PLAIN, (int) (10 * SCALING)));
    textField.setForeground(Color.white);
    //this.setFocusTraversalKeysEnabled(false);
    textField.addActionListener(new ActionListener(){ // Do when enter key is pressed
      public void actionPerformed(ActionEvent e){
        //TODO: add support for dm and teams
        if(!textField.getText().isEmpty()){
          System.out.println("Sending: " + textField.getText());
          byte[] encodedBytes = Base64.getEncoder().encode(textField.getText().getBytes());
          CLIENT.sendMessage(new String(encodedBytes), mode);
          textField.setText("");
          client.requestFocus(); // Change focus back to game
        }
      }
    });
    bottomPanel.add(textField);
    bottomPanel.setPreferredSize(new Dimension(width, height/10));
    this.add(bottomPanel, BorderLayout.SOUTH);
    this.setVisible(true);
  }

  public void toggleMode(){
    System.out.println("toggle mode");
    mode ++;
    if(mode > 2){
      mode = 0;
    }
    if(mode > 0){
      try {
        doc.insertString(doc.getLength(), modeString[mode - 1], regular);
      } catch (Exception e){
        e.printStackTrace();
      }
    }

  }

  public int getMode(){
    return mode;
  }


  public void messageIn(String userName, String message, boolean sameTeam){
    // Format and insert messages
    byte[] decodedBytes = Base64.getDecoder().decode(message.getBytes());
    message = new String(decodedBytes);

    try {
      if (sameTeam) { // Friendly
        doc.insertString(doc.getLength(), userName + " ", friendly);
      } else {
        doc.insertString(doc.getLength(), userName + " ", enemy);
      }
      doc.insertString(doc.getLength(), message + "\n", regular);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void requestFocus(){
    textField.requestFocus();
  }

}

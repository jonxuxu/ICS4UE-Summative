package client.gameUi;

import client.Client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatComponent extends JPanel{
  private Client CLIENT;
  //https://www.javatpoint.com/java-jtextpane
  private static double SCALING;
  private static int HEIGHT, WIDTH;
  private static int PARENT_WIDTH, PARENT_HEIGHT;

  SimpleAttributeSet regular = new SimpleAttributeSet();
  SimpleAttributeSet friendly = new SimpleAttributeSet();
  SimpleAttributeSet enemy = new SimpleAttributeSet();

  private JTextField textField = new JTextField();
  Document doc;


  public ChatComponent(double scaling, int width, int height, Client client){
    SCALING = scaling;
    WIDTH = width;
    HEIGHT = height;
    this.CLIENT = client;

    // Setting up styles
    StyleConstants.setFontFamily(regular, "Arial");
    StyleConstants.setFontSize(regular, (int)(10 * SCALING));
    friendly.addAttributes(regular);
    StyleConstants.setBold(friendly, true);
    StyleConstants.setForeground(friendly, Color.green);
    enemy.addAttributes(regular);
    StyleConstants.setBold(enemy, true);
    StyleConstants.setForeground(enemy, Color.red);
    StyleConstants.setForeground(regular, Color.white);

    // Styling panel
    this.setLayout(new BorderLayout());
    this.setBackground(new Color(255,255,255,20 ));

    // Adding components
    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setBackground(new Color(0, 0, 0, 0));
    doc = textPane.getStyledDocument();
    try{
      doc.insertString(doc.getLength(), "HELLO", friendly);
    } catch (Exception e){
      e.printStackTrace();
    }
    JScrollPane chatPane = new JScrollPane(textPane);
    chatPane.setBackground(new Color(0,0,0,0));
    chatPane.setBorder(null);
    this.add(chatPane, BorderLayout.CENTER);
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
    textField.setBackground(new Color(0,0,0,0));
    textField.setFont(new Font("Arial", Font.PLAIN, (int) (10 * SCALING)));
    textField.setForeground(Color.white);
    //this.setFocusTraversalKeysEnabled(false);
    textField.addActionListener(new ActionListener() // Do when enter key is pressed
    {
      public void actionPerformed(ActionEvent e)
      {
        //TODO: add support for dm and teams
        client.requestFocus();
        //CLIENT.sendMessage(textField.getText(), 1);
        textField.setText("");
      }
    });
    bottomPanel.add(textField);
    bottomPanel.setPreferredSize(new Dimension(width, height/10));
    this.add(bottomPanel, BorderLayout.SOUTH);
    this.setVisible(true);
  }

  public void draw(Graphics g) {
  }


  public void messageIn(String player, String message, int team) throws BadLocationException {
    if(team == 0){ // Friendly
      doc.insertString(doc.getLength(), player, friendly);
    } else if (team == 1){
      doc.insertString(doc.getLength(), player, enemy);
    }
    doc.insertString(doc.getLength(), message + "\n", regular);
  }

  public void requestFocus(){
    textField.requestFocus();
  }

}

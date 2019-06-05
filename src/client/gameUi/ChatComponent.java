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

  private JButton send = new JButton("Send");
  private JTextField textField = new JTextField();
  Document doc;


  public ChatComponent(double scaling, int width, int height, Client client){
    SCALING = scaling;
    WIDTH = width;
    HEIGHT = height;
    this.CLIENT = client;

    // Setting up styles
    StyleConstants.setFontFamily(regular, "Arial");
    StyleConstants.setFontSize(regular, (int)(8 * SCALING));
    friendly.addAttributes(regular);
    StyleConstants.setBold(friendly, true);
    StyleConstants.setForeground(friendly, Color.green);
    enemy.addAttributes(regular);
    StyleConstants.setBold(enemy, true);
    StyleConstants.setForeground(enemy, Color.red);
    StyleConstants.setForeground(regular, Color.white);

    // Styling panel
    this.setLayout(new BorderLayout());
    this.setBackground(new Color(0,0,0,0));

    // Adding components
    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setBackground(new Color(0, 0, 0, 128));
    doc = textPane.getStyledDocument();
    try{
      doc.insertString(doc.getLength(), "HELLO", friendly);
    } catch (Exception e){
      e.printStackTrace();
    }
    JScrollPane chatPane = new JScrollPane(textPane);
    this.add(chatPane, BorderLayout.CENTER);

    send.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if(!textField.getText().equals("")){ // If text field ins't empty
          CLIENT.sendMessage(textField.getText());
          textField.setText("");
        }
      }
    });
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
    bottomPanel.add(textField);
    bottomPanel.add(send);
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

}

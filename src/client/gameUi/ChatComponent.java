package client.gameUi;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class ChatComponent extends JPanel{
  //https://www.javatpoint.com/java-jtextpane
  private static double SCALING = 300;
  private static int HEIGHT, WIDTH;
  private static int PARENT_WIDTH, PARENT_HEIGHT;

  SimpleAttributeSet regular = new SimpleAttributeSet();
  SimpleAttributeSet friendly = new SimpleAttributeSet();
  SimpleAttributeSet enemy = new SimpleAttributeSet();

  private JTextPane textPane;
  private JButton send = new JButton("Send");

  public ChatComponent(double scaling, int bigX, int bigY){
    this.add(new JButton("customer"));
    SCALING = scaling;
    WIDTH = scale(bigY/2);
    HEIGHT = scale(bigX/6);
    PARENT_WIDTH = bigX;
    PARENT_HEIGHT = bigY;

    // Setting up styles
    StyleConstants.setBold(friendly, true);
    StyleConstants.setForeground(friendly, Color.green);
    StyleConstants.setBold(enemy, true);
    StyleConstants.setForeground(enemy, Color.red);

    this.setLayout(new BorderLayout());
    this.setBackground(new Color(0,0,0,0));

    textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setBackground(new Color(0, 0, 0, 128));
    textPane.setText("HELLo world");

    JScrollPane chatPane = new JScrollPane(textPane);
    this.add(chatPane, BorderLayout.CENTER);
    this.add(send, BorderLayout.SOUTH);
    this.setVisible(true);
  }

  public void draw(Graphics g) {
  }

  public int scale(int unscaled){
    return((int)(unscaled*SCALING));
  }

  public void messageIn(String player, String message, int team) throws BadLocationException {
    Document doc = textPane.getStyledDocument();
    if(team == 0){ // Friendly
      doc.insertString(doc.getLength(), player, friendly);
    } else if (team == 1){
      doc.insertString(doc.getLength(), player, enemy);
    }
    doc.insertString(doc.getLength(), message + "\n", regular);
  }

}

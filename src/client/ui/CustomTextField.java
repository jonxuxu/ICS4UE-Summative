package client.ui;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class CustomTextField extends JTextField {
  private Color foregroundColor = new Color(1f, 1f, 1f, 1f);
  private Color backgroundColor = new Color(1f, 1f, 1f, 0f);

  public CustomTextField(int row) {
    super(row);
    Font BUTTON_FONT = new Font("Cambria Math", Font.PLAIN, (int) (24 ));
    this.setFont(BUTTON_FONT);
    this.setBorder(BorderFactory.createLineBorder(Color.white,3));
    this.setForeground(foregroundColor);
    this.setBackground(backgroundColor);
    this.setOpaque(false);
  }
}
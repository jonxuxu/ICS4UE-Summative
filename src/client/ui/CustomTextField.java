package client.ui;

import javax.swing.*;
import java.awt.*;

public class CustomTextField extends JTextField {
  private Color foregroundColor = new Color(1f, 1f, 1f, 1f);
  private Color backgroundColor = new Color(1f, 1f, 1f, 0f);

  public CustomTextField(int row, double scaling) {
    super(row);
    Font BUTTON_FONT = new Font("Cambria Math", Font.PLAIN, (int) (12 * scaling));
    this.setFont(BUTTON_FONT);
    this.setBorder(BorderFactory.createLineBorder(Color.white, (int) (1.5 * scaling)));
    this.setForeground(foregroundColor);
    this.setBackground(backgroundColor);
    this.setOpaque(false);
  }
}
package client.ui;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
/**
 * CustomButton.java
 * This is the class for text in the game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public class CustomTextField extends JTextField {
  private Color foregroundColor = new Color(1f, 1f, 1f, 1f);
  private Color backgroundColor = new Color(1f, 1f, 1f, 0f);

  /**
   * Sets the text field size
   * @param row: represents the row number
   */
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
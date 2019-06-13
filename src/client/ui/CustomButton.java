package client.ui;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
/**
 * CustomButton.java
 * This is the class for buttons in the game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public class CustomButton extends JButton {
  private Color foregroundColor = new Color(1f, 1f, 1f, 1f);
  private Color backgroundColor = new Color(0f, 0f, 0f, 0f);
  private Color rolloverColor = new Color(1f, 1f, 1f, 0.1f);
  private Color pressedColor = new Color(1f, 1f, 1f, 0.2f);
  private CustomButton thisButton;

  /**
   * Sets up a custom button for use
   * @param text Text to be inserted in the button
   */
  public CustomButton(String text) {
    super(text);
    super.setContentAreaFilled(false);
    thisButton = this;
    this.setFont(new Font("Cambria Math",Font.PLAIN, (int) (24)));
    this.setBorder(BorderFactory.createLineBorder(Color.white, (int) (4 )));
    this.setForeground(foregroundColor);
    this.setBackground(backgroundColor);
    this.setFocusPainted(false);
  }

    /**
     * Paints the create panel on the screen
     * @param g used to draw the panel
     */
  @Override
  protected void paintComponent(Graphics g) {
    if (getModel().isPressed()) {
      g.setColor(pressedColor);
    } else if (getModel().isRollover()) {
      g.setColor(rolloverColor);
    } else {
      g.setColor(getBackground());
    }
    g.fillRect(0, 0, getWidth(), getHeight());
    super.paintComponent(g);
  }
}
package client.ui;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
  private Color foregroundColor = new Color(1f, 1f, 1f, 1f);
  private Color backgroundColor = new Color(0f, 0f, 0f, 0f);
  private Color rolloverColor = new Color(1f, 1f, 1f, 0.1f);
  private Color pressedColor = new Color(1f, 1f, 1f, 0.2f);
  private CustomButton thisButton;

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
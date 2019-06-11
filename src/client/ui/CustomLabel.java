package client.ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CustomLabel extends JLabel {

  public CustomLabel(String text, String font, Color color){
    super(text);
    switch(font){
      case "title": this.setFont(new Font("Cambria Math", Font.PLAIN, (int) (24)));
      case "header": this.setFont(new Font("Cambria Math", Font.PLAIN, (int) (20)));
    }
    this.setForeground(color);
  }
}

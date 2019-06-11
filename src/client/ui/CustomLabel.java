package client.ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CustomLabel extends JLabel {

  public CustomLabel(String text, String font, double scaling, Color color){
    super(text);
    switch(font){
      case "title": this.setFont(new Font("Cambria Math", Font.PLAIN, (int) (12 * scaling)));
      case "header": this.setFont(new Font("Cambria Math", Font.PLAIN, (int) (10 * scaling)));
    }
    this.setForeground(color);
  }
}

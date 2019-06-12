package client.ui;

import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JLabel {

  public CustomLabel(String text, String font, Color color){
    super(text);
    switch(font){
      case "title":
        this.setFont(new Font("Cambria Math", Font.PLAIN, 30));
        System.out.println("titlecust");
      case "header":
        this.setFont(new Font("Cambria Math", Font.PLAIN, 20));
    }
    this.setForeground(color);
  }
}

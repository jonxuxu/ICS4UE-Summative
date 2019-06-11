package client.gameUi;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ThemedPanel extends JPanel {
  private static Border border1, border2, border3, border4, compound;

  public ThemedPanel(){
    // Borders
    border1 = BorderFactory.createLineBorder(new Color(72, 60, 32), 4);
    border2 = BorderFactory.createLineBorder(new Color(141, 130, 103), 3);
    border3 = BorderFactory.createLineBorder(new Color(95, 87, 69), 4);
    border4 = BorderFactory.createLineBorder(new Color(50, 46, 41), 2);
    compound = BorderFactory.createCompoundBorder(border1, border2);
    compound = BorderFactory.createCompoundBorder(compound, border3);
    compound = BorderFactory.createCompoundBorder(compound, border4);
    this.setBorder(compound);
  }
}

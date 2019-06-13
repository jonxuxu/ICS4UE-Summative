package client.gameUi;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;

public class ThemedPanel extends JPanel {
  private static Border border1, border2, border3, border4, compound;

  /**
   * ThemedPanel.java
   * This is responsible for drawing the themed panel
   *
   * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
   * @version 1.0
   * @since 2019-06-02
   */
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

package client.gameUi;

import client.ui.CustomLabel;

import javax.swing.*;
import java.awt.*;

public class PauseComponent extends ThemedPanel {

   public PauseComponent(double SCALING, int width, int height) {
      super();

      // UI Components
      this.setLayout(new BorderLayout());
      this.setBackground(new Color(33, 35, 37));
      CustomLabel title = new CustomLabel("Pause", "title", SCALING, Color.WHITE);
      this.add(title, BorderLayout.NORTH);
      this.add(new JButton("Cust me"), BorderLayout.SOUTH);
      //this.add(new JButton("CUst"));

      this.setVisible(false);
   }

}
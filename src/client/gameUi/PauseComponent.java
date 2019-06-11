package client.gameUi;

import client.ui.CustomLabel;

import javax.swing.*;
import java.awt.*;

public class PauseComponent extends ThemedPanel {

   public PauseComponent(double SCALING) {
      super();

      // UI Components
      this.setLayout(new BorderLayout());
      //this.setBackground(new Color(33, 35, 37));
      this.setBackground(Color.green);
      CustomLabel title = new CustomLabel("Pause", "title", SCALING, Color.WHITE);
      this.add(title, BorderLayout.NORTH);
      this.add(new JButton("Cust me"), BorderLayout.SOUTH);
      //this.add(new JButton("CUst"));

      this.setVisible(false);

   }

}
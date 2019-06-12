package client.gameUi;

import client.ui.CustomLabel;

import javax.swing.*;
import java.awt.*;

public class PauseComponent extends ThemedPanel {

   public PauseComponent(int width, int height) {
      super();
      // UI Components
      this.setLayout(new BorderLayout());
      this.setBackground(new Color(33, 35, 37));
      CustomLabel title = new CustomLabel("Pause", "title", Color.WHITE);
      this.add(title, BorderLayout.NORTH);
      this.add(new JButton("Cust me"), BorderLayout.SOUTH);

      //this.setVisible(false);

   }

}
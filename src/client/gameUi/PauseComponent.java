package client.gameUi;

import client.Client;
import client.ui.CustomLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseComponent extends ThemedPanel {
   public Client client;
   public PauseComponent thisComponent;

   public PauseComponent(int width, int height, Client client) {
      super();
      thisComponent = this;
      this.client = client;
      // UI Components
      this.setLayout(new BorderLayout());
      this.setBackground(new Color(33, 35, 37));
      /*
      JLabel title = new JLabel("Pause");
      title.setForeground(Color.WHITE);
      title.setFont(new Font("Cambria Math", Font.PLAIN, 30));
      */
      CustomLabel title = new CustomLabel("Pause", "title", Color.WHITE);
      title.setHorizontalAlignment(JLabel.CENTER);
      this.add(title, BorderLayout.NORTH);
      JButton back = new JButton("Return to game");
      back.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            client.requestFocus();
            thisComponent.setVisible(false);
         }
      });
      this.add(back, BorderLayout.SOUTH);

      this.setVisible(false);

   }

}
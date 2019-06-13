package client.ui;

import client.Client;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * LoginPanel.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public class LoginPanel extends MenuPanel {
   private Graphics2D g2;

   private final int MAX_X = super.getWidth();
   private final int MAX_Y = super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private CustomTextField nameField = new CustomTextField(3);
   private CustomButton testButton = new CustomButton("Test");

   public LoginPanel() {
      //Basic username field
      //sendName = true;
      nameField.addActionListener((ActionEvent e) -> {
         CLIENT.testName(nameField.getText());
      });
      nameField.setFont(super.getFont("main"));
      System.out.println(MAX_X / 2);
      nameField.setBounds(MAX_X / 2 - 100, MAX_Y / 5, 200, (MAIN_FONT.getSize() + 20));
      this.add(nameField);

      testButton.addActionListener((ActionEvent e) -> {
         CLIENT.testingBegin();
      });

      testButton.setBounds(MAX_X / 2 - 100, MAX_Y * 2 / 5, 200, (MAIN_FONT.getSize() + 20));
      this.add(testButton);
      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(20, 20, 20));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      this.setFocusable(true);
   }

   @Override
   public void paintComponent(Graphics g) {
      g2 = (Graphics2D) g;
      g2.setFont(MAIN_FONT);
      super.paintComponent(g);
      //Begin drawing
      g2.setColor(Color.WHITE);
      g2.setFont(HEADER_FONT);
      g2.drawString("Login", (MAX_X - g2.getFontMetrics().stringWidth("Login")) / 2, MAX_Y / 6);
      g2.setFont(MAIN_FONT);
      int tempConnectionState = CLIENT.getConnectionState();
      if (tempConnectionState == 0) {
         g2.drawString("Connecting...", (MAX_X - g2.getFontMetrics().stringWidth("Connecting...")) / 2, MAX_Y * 5 / 16);
      } else if (tempConnectionState == 1) {
         g2.drawString("Connected", (MAX_X - g2.getFontMetrics().stringWidth("Connected")) / 2, MAX_Y * 5 / 16);
      } else {
         g2.drawString("Unable to Connect", (MAX_X - g2.getFontMetrics().stringWidth("Unable to Connect")) / 2, MAX_Y * 5 / 16);
      }
      //Write error
      writeError(g2, MAX_X / 2, MAX_Y * 3 / 8);
   }
}
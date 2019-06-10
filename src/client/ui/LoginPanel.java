package client.ui;

import client.Client;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * LoginPanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class LoginPanel extends MenuPanel {
   private Graphics2D g2;
   private final double SCALING = super.getScaling();
   private final int MAX_X = super.getWidth();
   private final int MAX_Y = super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private CustomTextField nameField = new CustomTextField(3, SCALING);
   private CustomButton testButton = new CustomButton("Test", SCALING);

   public LoginPanel() {
      //Basic username field
      //sendName = true;
      nameField.addActionListener((ActionEvent e) -> {
         CLIENT.testName(nameField.getText());
      });
      nameField.setFont(super.getFont("main"));
      nameField.setBounds(MAX_X / 2 - (int) (45 * SCALING), MAX_Y / 5, (int) (90 * SCALING), (int) (19 * SCALING));
      this.add(nameField);

      testButton.addActionListener((ActionEvent e) -> {
         CLIENT.testingBegin();
      });

      testButton.setBounds(MAX_X / 2 - (int) (45 * SCALING), MAX_Y * 2 / 5, (int) (90 * SCALING), (int) (19 * SCALING));
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
      g2.drawString("Login", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Login")) / 2.0), (int) (MAX_Y / 5.0 - 5 * SCALING));
      g2.setFont(MAIN_FONT);
      int tempConnectionState = CLIENT.getConnectionState();
      if (tempConnectionState == 0) {
         g2.drawString("Connecting...", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Connecting...")) / 2.0), (int) (MAX_Y * 5 / 16.0));
      } else if (tempConnectionState == 1) {
         g2.drawString("Connected", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Connected")) / 2.0), (int) (MAX_Y * 5 / 16.0));
      } else {
         g2.drawString("Unable to Connect", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Unable to Connect")) / 2.0), (int) (MAX_Y * 5 / 16.0));
      }
      //Write error
      writeError(g2, MAX_X / 2, (int) (MAX_Y * 3 / 8.0));
   }
}
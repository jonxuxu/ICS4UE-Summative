package client.ui;

import client.Client;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JoinPanel.java
 * This is the panel for joining a game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public class JoinPanel extends MenuPanel { //State =4
   private Graphics2D g2;

   private final int MAX_X = super.getWidth();
   private final int MAX_Y = super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private final CustomTextField gameNameField = new CustomTextField(3);
   private final CustomTextField gamePasswordField = new CustomTextField(3);
   private final CustomButton backButton = new CustomButton("Back");
   private final CustomButton confirmButton = new CustomButton("Confirm Game");

   /**
    * Sets up the join screen
    */
   public JoinPanel() {
      //Setting up the size
      this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
      //Basic create and join server buttons
      //Game name
      gameNameField.addActionListener((ActionEvent e) -> {
         CLIENT.testGame(gameNameField.getText(), gamePasswordField.getText());
      });
      gameNameField.setFont(MAIN_FONT);
      gameNameField.setBounds(MAX_X / 2 - 100, MAX_Y * 3 / 10, 200, MAIN_FONT.getSize() + 20);
      this.add(gameNameField);
      //Game password
      gamePasswordField.addActionListener((ActionEvent e) -> {
         CLIENT.testGame(gameNameField.getText(), gamePasswordField.getText());
      });
      gamePasswordField.setFont(MAIN_FONT);
      gamePasswordField.setBounds(MAX_X / 2 - 100, MAX_Y * 2 / 5, 200, MAIN_FONT.getSize() + 20);
      this.add(gamePasswordField);
      //Confirm button
      confirmButton.addActionListener((ActionEvent e) -> {
         CLIENT.testGame(gameNameField.getText(), gamePasswordField.getText());
      });
      confirmButton.setBounds(MAX_X / 2 - 100, MAX_Y / 2, 200, MAIN_FONT.getSize() + 20);
      this.add(confirmButton);
      //Back button
      backButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNextPanel(2);
      });
      backButton.setBounds(MAX_X / 2 - 100, MAX_Y * 7 / 10, 200, MAIN_FONT.getSize() + 20);
      this.add(backButton);

      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(20, 20, 20));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      this.setFocusable(true);
   }

   /**
    * Paints the create panel on the screen
    *
    * @param g used to draw the panel
    */
   @Override
   public void paintComponent(Graphics g) {
      g2 = (Graphics2D) g;
      super.paintComponent(g);
      //Background
      drawBackground(g2);
      g2.setColor(Color.WHITE);
      g2.setFont(HEADER_FONT);
      g2.drawString("Join Server", (MAX_X - g2.getFontMetrics().stringWidth("Join Server")) / 2, (MAX_Y / 5));
      //Server name
      g2.setFont(MAIN_FONT);
      g2.drawString("Server Name", (MAX_X - g2.getFontMetrics().stringWidth("Server Name")) / 2, (MAX_Y * 3 / 10 - MAIN_FONT.getSize() + 20));
      //Server password
      g2.drawString("Server Password", (MAX_X - g2.getFontMetrics().stringWidth("Server Password")) / 2, (MAX_Y * 2 / 5 - MAIN_FONT.getSize() + 20));
      //Write error
      writeError(g2, MAX_X / 2, MAX_Y * 3 / 5);
      //Draws particles
      drawAllParticles(g2);
   }
}
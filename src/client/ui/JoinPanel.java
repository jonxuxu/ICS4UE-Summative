package client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JoinPanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class JoinPanel extends GeneralPanel { //State =4
   private Graphics2D g2;
   private final double SCALING = super.getScaling();
   private final int MAX_X = super.getWidth();
   private final int MAX_Y = super.getHeight();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private CustomTextField gameNameField = new CustomTextField(3, SCALING);
   private CustomTextField gamePasswordField = new CustomTextField(3, SCALING);
   private CustomButton backButton = new CustomButton("Back", SCALING);
   private CustomButton confirmButton = new CustomButton("Confirm Game", SCALING);

   public JoinPanel() {
      //Setting up the size
      this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
      //Basic create and join server buttons
      gameNameField.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      gameNameField.setFont(MAIN_FONT);
      gameNameField.setBounds(MAX_X / 2 - (int) (45 * SCALING), MAX_Y * 3 / 10, (int) (90 * SCALING), (int) (19 * SCALING));
      this.add(gameNameField);
      gamePasswordField.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      gamePasswordField.setFont(MAIN_FONT);
      gamePasswordField.setBounds(MAX_X / 2 - (int) (45 * SCALING), MAX_Y * 2 / 5, (int) (90 * SCALING), (int) (19 * SCALING));
      this.add(gamePasswordField);
      confirmButton.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      confirmButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), MAX_Y / 2, (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(confirmButton);
      backButton.addActionListener((ActionEvent e) -> {
         newState = 2;
      });
      backButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), MAX_Y * 7 / 10, (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(backButton);
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
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setFont(MAIN_FONT);
      super.paintComponent(g);
      //Background
      drawBackground(g2);
      g2.setColor(Color.WHITE);
      g2.setFont(HEADER_FONT);
      g2.drawString("Join Server", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Join Server")) / 2.0), (MAX_Y / 5));
      //Server name
      g2.setFont(MAIN_FONT);
      g2.drawString("Server Name", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Server Name")) / 2.0), (MAX_Y * 3 / 10 - g2.getFontMetrics().getHeight()));
      //Server password
      g2.drawString("Server Password", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Server Password")) / 2.0), (MAX_Y * 2 / 5 - g2.getFontMetrics().getHeight()));
      //Draws particles
      drawAllParticles(g2);
   }
}

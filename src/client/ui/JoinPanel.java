package client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
   private double scaling = super.getScaling();
   private int width = super.getWidth();
   private int height = super.getHeight();

   private CustomTextField gameNameField = new CustomTextField(3, scaling);
   private CustomTextField gamePasswordField = new CustomTextField(3, scaling);
   private CustomButton backButton = new CustomButton("Back", scaling);
   private CustomButton confirmButton = new CustomButton("Confirm Game", scaling);

   public JoinPanel() {
      //Setting up the size
      this.setPreferredSize(new Dimension(width, height));
      //Basic create and join server buttons
      gameNameField.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      gameNameField.setFont(super.getFont("main"));
      gameNameField.setBounds(width / 2 - (int) (45 * scaling), height * 3 / 10, (int) (90 * scaling), (int) (19 * scaling));
      this.add(gameNameField);
      gamePasswordField.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      gamePasswordField.setFont(super.getFont("main"));
      gamePasswordField.setBounds(width / 2 - (int) (45 * scaling), height * 2 / 5, (int) (90 * scaling), (int) (19 * scaling));
      this.add(gamePasswordField);
      confirmButton.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      confirmButton.setBounds(width / 2 - (int) (65 * scaling), height / 2, (int) (130 * scaling), (int) (19 * scaling));
      this.add(confirmButton);
      backButton.addActionListener((ActionEvent e) -> {
         newState = 2;
      });
      backButton.setBounds(width / 2 - (int) (65 * scaling), height * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
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
      g2.setFont(super.getFont("main"));
      super.paintComponent(g);
      //Background
      g2.drawImage(LOADED_TITLE_SCREEN, width - (int) (1800 * introScaling), height - (int) (1198 * introScaling), null);
      g2.setColor(Color.WHITE);
      g2.setFont(super.getFont("header"));
      g2.drawString("Join Server", (int) ((width - g2.getFontMetrics().stringWidth("Join Server")) / 2.0), (height / 5));
      //Server name
      g2.setFont(super.getFont("main"));
      g2.drawString("Server Name", (int) ((width - g2.getFontMetrics().stringWidth("Server Name")) / 2.0), (height * 3 / 10 - g2.getFontMetrics().getHeight()));
      //Server password
      g2.drawString("Server Password", (int) ((width - g2.getFontMetrics().stringWidth("Server Password")) / 2.0), (height * 2 / 5 - g2.getFontMetrics().getHeight()));
      //Draws particles
      drawAllParticles(g2);
   }
}

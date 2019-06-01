package client.ui;
import client.Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

/**
 * CreatePanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class CreatePanel extends GeneralPanel { //State =3
   private Graphics2D g2;
   private final CustomTextField gameNameField, gamePasswordField;
   private final CustomButton backButton, confirmButton;
   private double scaling = super.getScaling();
   private int width= super.getWidth();
   private int height= super.getWidth();


   public CreatePanel() {
      gameNameField = new CustomTextField(3, scaling);
      gamePasswordField = new CustomTextField(3, scaling);
      backButton = new CustomButton("Back", scaling);
      confirmButton = new CustomButton("Confirm Game", scaling);
      
      //Setting up the size
      this.setPreferredSize(new Dimension(super.getWidth(), super.getHeight()));
      //Basic create and join server buttons
      gameNameField.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      gameNameField.setFont(super.getFont("main"));
      gameNameField.setBounds(super.getWidth() / 2 - (int) (45 * scaling), super.getHeight() * 3 / 10, (int) (90 * scaling), (int) (19 * scaling));
      this.add(gameNameField);
      gamePasswordField.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      gamePasswordField.setFont(super.getFont("main"));
      gamePasswordField.setBounds(super.getWidth() / 2 - (int) (45 * scaling), super.getHeight() * 2 / 5, (int) (90 * scaling), (int) (19 * scaling));
      this.add(gamePasswordField);
      confirmButton.addActionListener((ActionEvent e) -> {
         if (!testGame) {
            attemptedGameName = gameNameField.getText();
            attemptedGamePassword = gamePasswordField.getText();
            testGame = true;
         }
      });
      confirmButton.setBounds(super.getWidth() / 2 - (int) (65 * scaling), super.getHeight() / 2, (int) (130 * scaling), (int) (19 * scaling));
      this.add(confirmButton);
      backButton.addActionListener((ActionEvent e) -> {
         newState = 2;
      });
      backButton.setBounds(super.getWidth() / 2 - (int) (65 * scaling), super.getHeight() * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
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
      super.paintComponent(g);
      //Background
      g2.drawImage(LOADED_TITLE_SCREEN, super.getWidth() - (int) (1800 * introScaling), super.getHeight() - (int) (1198 * introScaling), null);
      g2.setColor(Color.WHITE);
      g2.setFont(super.getFont("header"));
      g2.drawString("Create Server", (int) ((super.getWidth() - g2.getFontMetrics().stringWidth("Create Server")) / 2.0), (super.getHeight() / 5));
      //Server name
      g2.setFont(super.getFont("main"));
      g2.drawString("Server Name", (int) ((super.getWidth() - g2.getFontMetrics().stringWidth("Server Name")) / 2.0), (super.getHeight() * 3 / 10 - g2.getFontMetrics().getHeight()));
      //Server password
      g2.drawString("Server Password", (int) ((super.getWidth() - g2.getFontMetrics().stringWidth("Server Password")) / 2.0), (super.getHeight() * 2 / 5 - g2.getFontMetrics().getHeight()));
      //Draws particles
      drawAllParticles(g2);
   }
}
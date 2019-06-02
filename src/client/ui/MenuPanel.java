package client.ui;

import client.Client;
import client.particle.AshParticle;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * MenuPanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class MenuPanel extends GeneralPanel {//State=2
   private Graphics2D g2;
   private final double SCALING = super.getScaling();
   private final int MAX_X= super.getWidth();
   private final int MAX_Y= super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");

   private CustomButton createButton = new CustomButton("Create Game", SCALING);
   private CustomButton joinButton = new CustomButton("Join Game", SCALING);
   private CustomButton instructionButton = new CustomButton("Instructions", SCALING);
   private CustomButton logoutButton = new CustomButton("Logout", SCALING);

   private double introAlpha = 1;

   public MenuPanel() {
      //Setting up the size
      this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
      //Basic create and join server buttons
      createButton.addActionListener((ActionEvent e) -> {
       CLIENT.setNewState(3);
      });
      createButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), (int) (MAX_Y * 8.0 / 20.0), (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(createButton);

      joinButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNewState(4);
      });
      joinButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), (int) (MAX_Y * 10.0 / 20.0), (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(joinButton);
      instructionButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNewState(5);
      });
      instructionButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), (int) (MAX_Y * 12.0 / 20.0), (int) (130 * SCALING), (int) (19 * SCALING));

      this.add(instructionButton);
      logoutButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNewState(0);
         CLIENT.logout();
      });
      logoutButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), (int) (MAX_Y * 14.0 / 20.0), (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(logoutButton);

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
      //Adds particles
      //Background
      drawBackground(g2);
      //Title
      drawTitle(g2);
      if (introAlpha != 0) {
         introAlpha -= 0.05;
         g2.setColor(new Color(0f, 0f, 0f, (float) (introAlpha)));
         g2.fillRect(0, 0, MAX_X, MAX_Y);
         if (introAlpha < 0.05) {
            introAlpha = 0;
         }
      }
      if (Math.random() < 0.2) {
         GeneralPanel.getParticles().add(new AshParticle(Math.random() * MAX_X + MAX_X / 20, 0, (int) ((Math.random() * 3 + 3) * SCALING), MAX_Y));
      }
      drawAllParticles(g2);
   }
}
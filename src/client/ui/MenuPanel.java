package client.ui;

import client.Client;
import client.particle.AshParticle;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
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
   private double scaling = super.getScaling();
   private int width= super.getWidth();
   private int height= super.getWidth();
   private CustomButton createButton = new CustomButton("Create Game", scaling);
   private CustomButton joinButton = new CustomButton("Join Game", scaling);
   private CustomButton instructionButton = new CustomButton("Instructions", scaling);
   private CustomButton logoutButton = new CustomButton("Logout", scaling);

   private double introAlpha = 1;

   public MenuPanel() {
      //Setting up the size
      this.setPreferredSize(new Dimension(super.getWidth(), super.getHeight()));
      //Basic create and join server buttons
      createButton.addActionListener((ActionEvent e) -> {
         newState = 3;
      });
      createButton.setBounds(super.getWidth() / 2 - (int) (65 * scaling), (int) (super.getHeight() * 8.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));
      this.add(createButton);

      joinButton.addActionListener((ActionEvent e) -> {
         newState = 4;
      });
      joinButton.setBounds(super.getWidth() / 2 - (int) (65 * scaling), (int) (super.getHeight() * 10.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));
      this.add(joinButton);
      instructionButton.addActionListener((ActionEvent e) -> {
         newState = 5;//I added this later so I didn't want to move everything around
      });
      instructionButton.setBounds(super.getWidth() / 2 - (int) (65 * scaling), (int) (super.getHeight() * 12.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));

      this.add(instructionButton);
      logoutButton.addActionListener((ActionEvent e) -> {
         newState = 0;
         logout = true;
      });
      logoutButton.setBounds(super.getWidth() / 2 - (int) (65 * scaling), (int) (super.getHeight() * 14.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));
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
      g2.drawImage(LOADED_TITLE_SCREEN, super.getWidth() - (int) (1800 * introScaling), super.getHeight() - (int) (1198 * introScaling), null);
      //Title
      g2.drawImage(LOADED_TITLE, (int) ((super.getWidth() - (super.getHeight() / 4.0 * 1316 / 625)) / 2.0), (int) (super.getHeight() / 10.0), null);
      if (introAlpha != 0) {
         introAlpha -= 0.05;
         g2.setColor(new Color(0f, 0f, 0f, (float) (introAlpha)));
         g2.fillRect(0, 0, super.getWidth(), super.getHeight());
         if (introAlpha < 0.05) {
            introAlpha = 0;
         }
      }
      if (Math.random() < 0.2) {
         particles.add(new AshParticle(Math.random() * super.getWidth() + super.getWidth() / 20, 0, (int) ((Math.random() * 3 + 3) * scaling), super.getHeight()));
      }
      drawAllParticles(g2);
   }
}
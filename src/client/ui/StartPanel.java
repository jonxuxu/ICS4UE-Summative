package client.ui;

import client.Client;
import client.particle.AshParticle;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * StartPanel.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public class StartPanel extends MenuPanel {//State=2
   private Graphics2D g2;

   private final int MAX_X= super.getWidth();
   private final int MAX_Y= super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private int stringSize;

   private CustomButton createButton = new CustomButton("Create Game");
   private CustomButton joinButton = new CustomButton("Join Game");
   private CustomButton instructionButton = new CustomButton("Instructions");
   private CustomButton logoutButton = new CustomButton("Logout");

   private double introAlpha = 1;

   public StartPanel() {
      //Setting up the size
      this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
      //Basic create and join server buttons
      createButton.addActionListener((ActionEvent e) -> {
       CLIENT.setNextPanel(3);
       System.out.println("3");
      });
      createButton.setBounds(1, 1, 1, 1);
      this.add(createButton);

      joinButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNextPanel(4);
         System.out.println("4");
      });
      joinButton.setBounds(1, 1, 1, 1);
      this.add(joinButton);
      instructionButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNextPanel(5);
         System.out.println("5");
      });
      instructionButton.setBounds(1, 1, 1, 1);

      this.add(instructionButton);
      logoutButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNextPanel(0);
         System.out.println("0");
         CLIENT.logout();
      });
      logoutButton.setBounds(1, 1, 1, 1);
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
      g2.setFont(MAIN_FONT);

      //Set buttons to proper size and location
      stringSize = g2.getFontMetrics().stringWidth("     Instructions     ");
      createButton.setBounds((MAX_X - stringSize) / 2, MAX_Y * 4 / 10, stringSize, MAIN_FONT.getSize() + 20);
      joinButton.setBounds((MAX_X - stringSize) / 2, MAX_Y / 2, stringSize, MAIN_FONT.getSize() + 20);
      instructionButton.setBounds((MAX_X - stringSize) / 2, MAX_Y * 6 / 10, stringSize, MAIN_FONT.getSize() + 20);
      logoutButton.setBounds((MAX_X - stringSize) / 2, MAX_Y * 7 / 10, stringSize, MAIN_FONT.getSize() + 20);

      super.paintComponent(g);

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
      //Adds particles
      if (Math.random() < 0.2) {
         MenuPanel.getParticles().add(new AshParticle(Math.random() * MAX_X + MAX_X / 20, 0, (int) ((Math.random() * 3 + 3) ), MAX_Y));
      }
      drawAllParticles(g2);
   }
}
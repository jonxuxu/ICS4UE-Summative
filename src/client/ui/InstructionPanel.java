package client.ui;

import client.Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * InstructionPanel.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public class InstructionPanel extends MenuPanel { //State=5
   private Graphics2D g2;
   private final int MAX_X= super.getWidth();
   private final int MAX_Y= super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private CustomButton backButton = new CustomButton("Back");

   //Instructions pages
   private BufferedImage[] pages = new BufferedImage[3];


   public InstructionPanel() {
      //Setting up buttons
      backButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNextPanel(2);
       //  CLIENT.setAction(2);
       //  CLIENT.leaveGame();
      });
      backButton.setBounds(MAX_X / 2 - 100, MAX_Y * 7 / 10, 200, MAIN_FONT.getSize() + 20);
      this.add(backButton);

      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(150, 150, 150));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      this.setFocusable(true);
   }

   @Override
   public void paintComponent(Graphics g) {
      g2 = (Graphics2D) g;
      super.paintComponent(g);
      //Background
      drawBackground(g2);
      drawAllParticles(g2);
      g2.setColor(Color.WHITE);
      g2.setFont(HEADER_FONT);
      g2.drawString("Instructions", (MAX_X - g2.getFontMetrics().stringWidth("Instructions")) / 2, (MAX_Y / 5));
   }
}

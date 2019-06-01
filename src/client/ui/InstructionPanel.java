package client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

/**
 * InstructionPanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class InstructionPanel extends GeneralPanel { //State=5
   private Graphics2D g2;
   private final double SCALING = super.getScaling();
   private final int MAX_X= super.getWidth();
   private final int MAX_Y= super.getHeight();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private CustomButton backButton = new CustomButton("Back", SCALING);


   public InstructionPanel() {
      //Setting up buttons
      backButton.addActionListener((ActionEvent e) -> {
         newState = 2;
         leaveGame = true;
      });
      backButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), MAX_Y * 7 / 10, (int) (130 * SCALING), (int) (19 * SCALING));
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
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setFont(MAIN_FONT);
      super.paintComponent(g);
      //Background
      drawBackground(g2);
      drawAllParticles(g2);
   }
}

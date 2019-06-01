package client.ui;

import client.Client;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
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
   private double scaling = super.getScaling();
   private int width= super.getWidth();
   private int height= super.getWidth();
   private CustomButton backButton = new CustomButton("Back", scaling);


   public InstructionPanel() {
      //Setting up buttons
      backButton.addActionListener((ActionEvent e) -> {
         newState = 2;
         leaveGame = true;
      });
      backButton.setBounds(super.getWidth() / 2 - (int) (65 * scaling), super.getY() * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
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
      g2.setFont(super.getFont("main"));
      super.paintComponent(g);
      //Background
      g2.drawImage(LOADED_TITLE_SCREEN, width - (int) (1800 * introScaling), height - (int) (1198 * introScaling), null);
      drawAllParticles(g2);
   }
}

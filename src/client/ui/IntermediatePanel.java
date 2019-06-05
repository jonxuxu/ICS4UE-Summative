package client.ui;


import client.Client;
import client.gameUi.ChatComponent;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * IntermediatePanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class IntermediatePanel extends JLayeredPane { //State=7 (intermediate)=
   private final double SCALING;
   private int MAX_X, MAX_Y;
   private Client.GamePanel gamePanel;
   private ChatComponent chat;

   public IntermediatePanel(int MAX_X, int MAX_Y, double SCALING, Client CLIENT) {
      this.SCALING = SCALING;
      this.MAX_X = MAX_X;
      this.MAX_Y = MAX_Y;
      //Scaling is a factor which reduces the MAX_X/MAX_Y so that it eventually fits
      //Setting up the size
      this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(0, 0, 0));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      gamePanel= CLIENT.new GamePanel();
   }
   public void initializeSize(int DESIRED_X, int DESIRED_Y) {
      gamePanel.setBounds((int) ((MAX_X - (DESIRED_X * SCALING)) / 2), (int) ((MAX_Y - (DESIRED_Y * SCALING)) / 2), (int) (DESIRED_X * SCALING), (int) (DESIRED_Y * SCALING));

      chat = new ChatComponent(SCALING, DESIRED_X, DESIRED_Y);
      chat.setBounds(0, (int)(SCALING*DESIRED_Y/4*3 - 20), (int)(SCALING*DESIRED_X/6), (int)(SCALING*DESIRED_Y/4));

      this.add(gamePanel, 1);
      this.add(chat, 2);
   }

   public void repaintReal() {
      gamePanel.repaint();
   }
}

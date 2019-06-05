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
      gamePanel.setBounds(0, 0, MAX_X, MAX_Y);

      chat = new ChatComponent(SCALING,  MAX_X/6, MAX_Y/4, CLIENT);
      chat.setBounds(0, MAX_Y/4*3, MAX_X/4, MAX_Y/4);

      this.add(gamePanel, new Integer(1));
      this.add(chat, new Integer(2));
   }

   public void repaintReal() {
      gamePanel.repaint();
   }
}

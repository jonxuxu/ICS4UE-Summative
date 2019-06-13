package client.ui;


import client.Client;
import client.gameUi.ChatComponent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * IntermediatePanel.java
 * This is the centred panel
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public class IntermediatePanel extends JLayeredPane { //State=7 (intermediate)=
   private int MAX_X, MAX_Y;
   private Client.GamePanel gamePanel;
   private ChatComponent chat;
   private Client CLIENT;

   /**
    * This sets up the centred panel
    * @param MAX_X middle of x coordinates
    * @param MAX_Y middle of y coordinates
    * @param CLIENT client that the panel shows to
    */
   public IntermediatePanel(int MAX_X, int MAX_Y, Client CLIENT) {
      this.MAX_X = MAX_X;
      this.MAX_Y = MAX_Y;
      this.CLIENT = CLIENT;
      //Scaling is a factor which reduces the MAX_X/MAX_Y so that it eventually fits
      //Setting up the size
      this.setSize(new Dimension(MAX_X, MAX_Y));
      //Basic visuals
      this.setBackground(new Color(0, 0, 0));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      gamePanel= CLIENT.new GamePanel();
      System.out.println(MAX_X);
      gamePanel.setBounds(0, 0, this.getWidth(),this.getHeight());
      System.out.println(this.getWidth()+" "+MAX_X);
      chat = new ChatComponent(  MAX_X/6, MAX_Y/4, CLIENT);
      chat.setBounds(0, MAX_Y*3/4, MAX_X/4, MAX_Y/4);

      this.add(gamePanel, new Integer(1));
      this.add(chat, new Integer(2));
      this.setDoubleBuffered(true);
   }

   /**
    * Requests the focus of chat
    */
   public void toggleMode() {
      chat.requestFocus();
      /*
      chat.toggleMode();
      if(chat.getMode() > 0){ // Focus on chat
         chat.requestFocus();
         //System.out.println("Chat");
      } else {
         CLIENT.requestFocus();
      }*/
   }

   /**
    * Sends message to user
    * @param player Name of player
    * @param message Message from player
    * @param friendly Whether message was sent from team or enemy
    */
   public void messageIn(String player, String message, boolean friendly) {
      chat.messageIn(player, message, friendly);
   }

   /**
    * Repaints the gamepanel
    */
   public void repaintReal() {
      gamePanel.repaint();
   }

   /**
    * Hides the chat on call
    */
   public void hideChat(){
      chat.setVisible(false);
   }
}

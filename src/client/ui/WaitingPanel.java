package client.ui;

import client.Client;
import client.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WaitingPanel extends MenuPanel { //State=6
   private Graphics2D g2;

   private final int MAX_X = super.getWidth();
   private final int MAX_Y = super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");
   private int stringSize;

   private boolean buttonAdd = true;
   private boolean buttonRemove = true;
   private CustomButton readyGameButton = new CustomButton("Begin game");
   private CustomButton backButton = new CustomButton("Back");
   private CustomButton teamOneButton = new CustomButton("West");
   private CustomButton teamTwoButton = new CustomButton("East");

   //Class pedestals
   private CustomButton SAFE_MARKSMAN_PEDESTAL = new CustomButton("Archer");
   private CustomButton GHOST_PEDESTAL = new CustomButton("Ghost");
   private CustomButton SUMMONER_PEDESTAL = new CustomButton("Summoner");
   private CustomButton JUGGERNAUT_PEDESTAL = new CustomButton("Juggernaut");
   private CustomButton TIME_MAGE_PEDESTAL = new CustomButton("Time Mage");
   private CustomButton MOBILE_SUPPORT_PEDESTAL = new CustomButton("Mobile Support");

   //Character visuals
   private BufferedImage[] idleCharacters = new BufferedImage[6];
   private boolean selected = false;
   private int boxMultiplier;

   //Mouse
   private int[] mouseState = CLIENT.getMouseState();
   private boolean justPressed = false;


   private ArrayList<User> onlineList;


   public WaitingPanel() {
      // Initializing buttons
      readyGameButton.setBounds(1, 1, 1, 1);
      readyGameButton.addActionListener((ActionEvent e) -> {
         CLIENT.ready();
      });

      teamOneButton.addActionListener((ActionEvent e) -> {
         CLIENT.setTeam(0);
      });
      teamOneButton.setBounds(1, 1, 1, 1);
      this.add(teamOneButton);

      teamTwoButton.addActionListener((ActionEvent e) -> {
         CLIENT.setTeam(1);
      });
      teamTwoButton.setBounds(1, 1, 1, 1);
      this.add(teamTwoButton);

      backButton.addActionListener((ActionEvent e) -> {
         selected = false;
         CLIENT.setNextPanel(2);
         CLIENT.leaveGame();
      });
      backButton.setBounds(1, 1, 1, 1);
      this.add(backButton);

      //Setting up the classes
      SAFE_MARKSMAN_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Archer");
         selected = true;
         boxMultiplier = 0;
      });
      SAFE_MARKSMAN_PEDESTAL.setBounds(1, 1, 1, 1);
      this.add(SAFE_MARKSMAN_PEDESTAL);

      GHOST_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Ghost");
         selected = true;
         boxMultiplier = 1;
      });
      GHOST_PEDESTAL.setBounds(1, 1, 1, 1);
      this.add(GHOST_PEDESTAL);

      TIME_MAGE_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("TimeMage");
         selected = true;
         boxMultiplier = 2;
      });
      TIME_MAGE_PEDESTAL.setBounds(1, 1, 1, 1);
      this.add(TIME_MAGE_PEDESTAL);

      JUGGERNAUT_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Juggernaut");
         selected = true;
         boxMultiplier = 3;
      });
      JUGGERNAUT_PEDESTAL.setBounds(1, 1, 1, 1);
      this.add(JUGGERNAUT_PEDESTAL);

      SUMMONER_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Summoner");
         selected = true;
         boxMultiplier = 4;
      });
      SUMMONER_PEDESTAL.setBounds(1, 1, 1, 1);
      this.add(SUMMONER_PEDESTAL);

      MOBILE_SUPPORT_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("MobileSupport");
         selected = true;
         boxMultiplier = 5;
      });
      MOBILE_SUPPORT_PEDESTAL.setBounds(1, 1, 1, 1);
      this.add(MOBILE_SUPPORT_PEDESTAL);

      //Import character idle frames
      try {
         idleCharacters[0] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/archer/archer.png"));
         idleCharacters[1] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/ghost/ghost.png"));
         idleCharacters[2] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/timemage/timemage.png"));
         idleCharacters[3] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/juggernaut/juggernaut.png"));
         idleCharacters[4] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/summoner/summoner.png"));
         idleCharacters[5] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/orb/orb.png"));
      } catch (IOException e) {
         System.out.println("Unable to find an image");
      }

      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(70, 70, 70));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      this.setFocusable(true);

      //Test if this works
      onlineList = CLIENT.getOnlineList();
   }

   @Override
   public void paintComponent(Graphics g) {
      g2 = (Graphics2D) g;
      g2.setFont(MAIN_FONT);

      FontMetrics metrics = g2.getFontMetrics();

      //Change buttons to fit size
      //Start button
      stringSize = metrics.stringWidth("   Begin game   ");
      readyGameButton.setBounds((MAX_X - stringSize) / 2, MAX_Y * 4 / 5, stringSize, MAIN_FONT.getSize() + 20);

      //Team buttons
      stringSize = metrics.stringWidth("    West    ");
      teamOneButton.setBounds(MAX_X / 2 - (stringSize + 200), MAX_Y * 7 / 10, stringSize, MAIN_FONT.getSize() + 20);
      teamTwoButton.setBounds(MAX_X / 2 + 200, MAX_Y * 7 / 10, stringSize, MAIN_FONT.getSize() + 20);

      //Back button
      stringSize = metrics.stringWidth("   Back   ");
      backButton.setBounds((MAX_X - stringSize) / 2, MAX_Y * 9 / 10, stringSize, MAIN_FONT.getSize() + 20);

      //Character buttons
      stringSize = metrics.stringWidth("   Mobile Support   ");
      SAFE_MARKSMAN_PEDESTAL.setBounds(MAX_X / 2 - (stringSize * 3 + 75), MAX_Y * 3 / 5, stringSize, MAIN_FONT.getSize() + 20);
      GHOST_PEDESTAL.setBounds(MAX_X / 2 - (stringSize * 2 + 45), MAX_Y * 3 / 5, stringSize, MAIN_FONT.getSize() + 20);
      TIME_MAGE_PEDESTAL.setBounds(MAX_X / 2 - (stringSize + 15), MAX_Y * 3 / 5, stringSize, MAIN_FONT.getSize() + 20);
      JUGGERNAUT_PEDESTAL.setBounds(MAX_X / 2 + (15), MAX_Y * 3 / 5, stringSize, MAIN_FONT.getSize() + 20);
      SUMMONER_PEDESTAL.setBounds(MAX_X / 2 + (stringSize + 45), MAX_Y * 3 / 5, stringSize, MAIN_FONT.getSize() + 20);
      MOBILE_SUPPORT_PEDESTAL.setBounds(MAX_X / 2 + (stringSize * 2 + 75), MAX_Y * 3 / 5, stringSize, MAIN_FONT.getSize() + 20);

      super.paintComponent(g);
      //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
      //if host==true, then display the ready button
      //Background
      drawBackground(g2);
      g2.setColor(Color.white);
      if ((CLIENT.getHost()) && (buttonAdd)) {
         this.add(readyGameButton);
         buttonAdd = false;
      }


      //Display all characters:
      for (int i = 0; i < 6; i++){
         g2.drawImage(idleCharacters[i], MAX_X / 2 - (stringSize * 3 + 75) + (stringSize + 30) * i, MAX_Y * 11 / 30, stringSize, stringSize, null);
      }

      //Draw square around selected character
      Stroke oldStroke = g2.getStroke(); //Store old stroke width
      g2.setStroke(new BasicStroke(5)); //Set new stroke width
      if (selected) {
         g2.drawRect(MAX_X / 2 - (stringSize * 3 + 80) + (stringSize + 30) * boxMultiplier, MAX_Y * 11 / 30 - 5, stringSize + 10, stringSize + 10);
      }
      g2.setStroke(oldStroke); //Reset stroke width

      //Display all players under respective team
      StringBuilder players = new StringBuilder("Players: ");
      //Shifts for each line
      int shift1 = 1;
      int shift2 = 1;
      stringSize = metrics.stringWidth("   West   ");
      String tempString; // Variable for username being drawn

      for (int i = 0; i < onlineList.size(); i++) {
         players.append(onlineList.get(i).getUsername() + ", ");
         if (onlineList.get(i).getTeam() != -1) {
            tempString = onlineList.get(i).getUsername(); //Store username
            if (onlineList.get(i).getTeam() == 0) {
               g2.drawString(tempString, (MAX_X - stringSize - metrics.stringWidth(tempString)) / 2 - 205, MAX_Y * 7 / 10 + 20 + metrics.getHeight() * (shift1 + 1));
               shift1++;
            } else if (onlineList.get(i).getTeam() == 1) {
               g2.drawString(tempString, (MAX_X + stringSize - metrics.stringWidth(tempString)) / 2 + 205, MAX_Y * 7 / 10 + 20 + metrics.getHeight() * (shift2 + 1));
               shift2++;
            }
         }
      }

      //Show room info
      if (!onlineList.isEmpty()) {
         g2.drawString(players.toString().substring(0, players.toString().lastIndexOf(",")), (int) (2), (int) 15);
      }
      g2.drawString("Server name: " + CLIENT.getGameName(), (2), (15) + metrics.getHeight());
      g2.drawString("Server password: " + CLIENT.getGamePassword(), (2), (15) + 2 * metrics.getHeight());
      if (CLIENT.getLoading()) {
         if (buttonRemove) {
            this.remove(readyGameButton);
            buttonRemove = false;
         }
         g2.drawString("Loading...", (MAX_X - metrics.stringWidth("Loading...")) / 2, (MAX_Y * 13 / 16));
      }
      writeError(g2, MAX_X / 2, (int) (MAX_Y * 11.0 / 16.0));
      drawAllParticles(g2);
   }
}

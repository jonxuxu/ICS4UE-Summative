package client.ui;

import client.Client;
import client.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class WaitingPanel extends MenuPanel { //State=6
   private Graphics2D g2;
   private final double SCALING = super.getScaling();
   private final int MAX_X = super.getWidth();
   private final int MAX_Y = super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private boolean buttonAdd = true;
   private boolean buttonRemove = true;
   private CustomButton readyGameButton = new CustomButton("Begin game", SCALING);
   private CustomButton backButton = new CustomButton("Back", SCALING);
   private CustomButton teamOneButton = new CustomButton("Team one", SCALING);
   private CustomButton teamTwoButton = new CustomButton("Team two", SCALING);

   //Class pedestals
   private CustomButton SAFE_MARKSMAN_PEDESTAL= new CustomButton("Archer", SCALING);
   private CustomButton GHOST_PEDESTAL= new CustomButton("Ghost", SCALING);
   private CustomButton SUMMONER_PEDESTAL= new CustomButton("Summoner", SCALING);
   private CustomButton JUGGERNAUT_PEDESTAL= new CustomButton("Juggernaut", SCALING);
   private CustomButton TIME_MAGE_PEDESTAL= new CustomButton("Time Mage", SCALING);
   private CustomButton MOBILE_SUPPORT_PEDESTAL= new CustomButton("Mobile Support", SCALING);


   //Mouse
   private int[] mouseState = CLIENT.getMouseState();
   private boolean justPressed=false;


   private ArrayList<User> onlineList;


   public WaitingPanel() {
      // Initializing buttons
      readyGameButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), MAX_Y * 8 / 10, (int) (130 * SCALING), (int) (19 * SCALING));
      readyGameButton.addActionListener((ActionEvent e) -> {
         CLIENT.ready();
      });

      teamOneButton.addActionListener((ActionEvent e) -> {
         CLIENT.setTeam(0);
      });
      teamOneButton.setBounds(MAX_X / 2 - (int) ((65 + 200) * SCALING), MAX_Y * 7 / 10, (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(teamOneButton);

      teamTwoButton.addActionListener((ActionEvent e) -> {
         CLIENT.setTeam(1);
      });
      teamTwoButton.setBounds(MAX_X / 2 - (int) ((65 - 200) * SCALING), MAX_Y * 7 / 10, (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(teamTwoButton);

      backButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNextPanel(2);
         CLIENT.leaveGame();
      });
      backButton.setBounds(MAX_X / 2 - (int) (65 * SCALING), MAX_Y * 9 / 10, (int) (130 * SCALING), (int) (19 * SCALING));
      this.add(backButton);

      //TODO: Make into buttons
      //Setting up the classes
      SAFE_MARKSMAN_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Archer");
      });
      SAFE_MARKSMAN_PEDESTAL.setBounds(MAX_X / 2 - (int) (290 * SCALING), MAX_Y * 3 / 5, (int) (100 * SCALING), (int) (15 * SCALING));
      this.add(SAFE_MARKSMAN_PEDESTAL);

      GHOST_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Ghost");
      });
      GHOST_PEDESTAL.setBounds(MAX_X / 2 - (int) (170 * SCALING), MAX_Y * 3 / 5, (int) (100 * SCALING), (int) (15 * SCALING));
      this.add(GHOST_PEDESTAL);

      TIME_MAGE_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("TimeMage");
      });
      TIME_MAGE_PEDESTAL.setBounds(MAX_X / 2 - (int) (50 * SCALING), MAX_Y * 3 /5, (int) (100 * SCALING), (int) (15 * SCALING));
      this.add(TIME_MAGE_PEDESTAL);

      JUGGERNAUT_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Juggernaut");
      });
      JUGGERNAUT_PEDESTAL.setBounds(MAX_X / 2 + (int) (70 * SCALING), MAX_Y * 3 / 5, (int) (100 * SCALING), (int) (15 * SCALING));
      this.add(JUGGERNAUT_PEDESTAL);

      SUMMONER_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("Summoner");
      });
      SUMMONER_PEDESTAL.setBounds(MAX_X / 2 + (int) (190 * SCALING), MAX_Y * 3 / 5, (int) (100 * SCALING), (int) (15 * SCALING));
      this.add(SUMMONER_PEDESTAL);

      MOBILE_SUPPORT_PEDESTAL.addActionListener((ActionEvent e) -> {
         CLIENT.setClassName("MobileSupport");
      });
      MOBILE_SUPPORT_PEDESTAL.setBounds(MAX_X / 2 + (int) (310 * SCALING), MAX_Y * 3 / 5, (int) (100 * SCALING), (int) (15 * SCALING));
      this.add(MOBILE_SUPPORT_PEDESTAL);

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


      //Display classes:


      //Display all players
      StringBuilder players = new StringBuilder("Players: ");
      int shift1 = 0;
      int shift2 = 0;
      for (int i = 0; i < onlineList.size(); i++) {
         players.append(onlineList.get(i).getUsername() + ", ");
         if (onlineList.get(i).getTeam() != -1) {
            if (onlineList.get(i).getTeam() == 0) {
               g2.drawString(onlineList.get(i).getUsername(), MAX_X / 2 - (int) ((65 + 200) * SCALING), (int) (19 * SCALING) + MAX_Y * 7 / 10 + metrics.getHeight() * (shift1 + 1));
               shift1++;
            } else if (onlineList.get(i).getTeam() == 1) {
               g2.drawString(onlineList.get(i).getUsername(), MAX_X / 2 - (int) ((65 - 200) * SCALING), (int) (19 * SCALING) + MAX_Y * 7 / 10 + metrics.getHeight() * (shift2 + 1));
               shift2++;
            }
         }
      }
      if (!onlineList.isEmpty()) {
         g2.drawString(players.toString().substring(0, players.toString().lastIndexOf(",")), (int) (2 * SCALING), (int) (10 * SCALING));
      }
      g2.drawString("Server name: "+CLIENT.getGameName(), (int) (2 * SCALING), (int) (10 * SCALING)+metrics.getHeight());
      g2.drawString("Server password: "+CLIENT.getGamePassword(), (int) (2 * SCALING), (int) (10 * SCALING)+2*metrics.getHeight());
      if (CLIENT.getLoading()) {
         if (buttonRemove) {
            this.remove(readyGameButton);
            buttonRemove = false;
         }
         g2.drawString("LOADING", (int) ((MAX_X - metrics.stringWidth("LOADING")) / 2.0), (int) (MAX_Y * 9.0 / 16.0));
      }
      writeError(g2, MAX_X / 2, (int) (MAX_Y * 11.0 / 16.0));
      drawAllParticles(g2);
   }
}

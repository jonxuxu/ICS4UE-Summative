package client.ui;

import client.Client;
import client.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class WaitingPanel extends GeneralPanel { //State=6
  private Graphics2D g2;
  private final double scaling = super.getScaling();
  private final int MAX_X= super.getWidth();
  private final int MAX_Y= super.getHeight();
  private final Client CLIENT = super.getClient();
  private final Font MAIN_FONT = super.getFont("main");
  private final Font HEADER_FONT = super.getFont("header");

  private boolean buttonAdd = true;
  private boolean buttonRemove = true;
  private CustomButton readyGameButton = new CustomButton("Begin game", scaling);
  private CustomButton backButton = new CustomButton("Back", scaling);
  private CustomButton teamOneButton = new CustomButton("Team one", scaling);
  private CustomButton teamTwoButton = new CustomButton("Team two", scaling);

  private ArrayList<User> onlineList;


  public WaitingPanel() {
    // Initializing buttons
    readyGameButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 4 / 10, (int) (130 * scaling), (int) (19 * scaling));
    readyGameButton.addActionListener((ActionEvent e) -> {
      CLIENT.ready();
    });

    teamOneButton.addActionListener((ActionEvent e) -> {
      CLIENT.setTeam(1);
    });
    teamOneButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 3 / 10, (int) (130 * scaling), (int) (19 * scaling));
    this.add(teamOneButton);

    teamTwoButton.addActionListener((ActionEvent e) -> {
      CLIENT.setTeam(2);
    });
    teamTwoButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y / 2, (int) (130 * scaling), (int) (19 * scaling));
    this.add(teamTwoButton);

    backButton.addActionListener((ActionEvent e) -> {
      System.out.println("wdwd");
      CLIENT.setNextPanel(2);
      CLIENT.leaveGame();
    });
    backButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
    this.add(backButton);


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
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
    StringBuilder players = new StringBuilder("Players: ");
    for (int i = 0; i < onlineList.size(); i++) {
      players.append(onlineList.get(i).getUsername() + ", ");
    }
    g2.drawString(players.toString(), (int) (2 * scaling), (int) (10 * scaling));
    if (CLIENT.getLoading()) {
      if (buttonRemove) {
        this.remove(readyGameButton);
        buttonRemove = false;
      }
      g2.drawString("LOADING", (int) ((MAX_X - metrics.stringWidth("LOADING")) / 2.0), MAX_Y / 2);
    }
    drawAllParticles(g2);
  }
}

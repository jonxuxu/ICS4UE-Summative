package client.ui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WaitingPanel extends GeneralPanel { //State=6
  private Graphics2D g2;
  private final double scaling = super.getScaling();
  private final int MAX_X= super.getWidth();
  private final int MAX_Y= super.getHeight();
  private final Font MAIN_FONT = super.getFont("main");
  private final Font HEADER_FONT = super.getFont("header");

  private boolean buttonAdd = true;
  private boolean buttonRemove = true;
  private CustomButton readyGameButton = new CustomButton("Begin game", scaling);
  private CustomButton backButton = new CustomButton("Back", scaling);
  private CustomButton teamOneButton = new CustomButton("Team one", scaling);
  private CustomButton teamTwoButton = new CustomButton("Team two", scaling);


  public WaitingPanel() {
    // Initializing buttons
    readyGameButton.setBounds(MAX_X / 2 - (int) (65 * scaling), height * 4 / 10, (int) (130 * scaling), (int) (19 * scaling));
    readyGameButton.addActionListener((ActionEvent e) -> {
      game.setReady(true);
    });

    teamOneButton.addActionListener((ActionEvent e) -> {
      myTeam = 1;
      teamChosen = true;
    });
    teamOneButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 3 / 10, (int) (130 * scaling), (int) (19 * scaling));
    this.add(teamOneButton);

    teamTwoButton.addActionListener((ActionEvent e) -> {
      myTeam = 2;
      teamChosen = true;
    });
    teamTwoButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y / 2, (int) (130 * scaling), (int) (19 * scaling));
    this.add(teamTwoButton);

    backButton.addActionListener((ActionEvent e) -> {
      newState = 2;
      leaveGame = true;
    });
    backButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
    this.add(backButton);


    //Basic visuals
    this.setDoubleBuffered(true);
    this.setBackground(new Color(70, 70, 70));
    this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
    this.setVisible(true);
    this.setFocusable(true);
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
    if ((host) && (buttonAdd)) {
      this.add(readyGameButton);
      buttonAdd = false;
    }
    StringBuilder players = new StringBuilder("Players: ");
    for (int i = 0; i < onlineList.size(); i++) {
      players.append(onlineList.get(i).getUsername() + ", ");
    }
    g2.drawString(players.toString(), (int) (2 * scaling), (int) (10 * scaling));
    if (loading) {
      if (buttonRemove) {
        this.remove(readyGameButton);
        buttonRemove = false;
      }
      g2.drawString("LOADING", (int) ((MAX_X - metrics.stringWidth("LOADING")) / 2.0), MAX_Y / 2);
    }
    drawAllParticles(g2);
  }
}

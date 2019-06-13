package client;

import java.awt.Graphics2D;
import java.awt.Color;

class GhostE extends Status{
  private int x,y;
  private static int RADIUS = 25;
  GhostE(int x, int y){
    this.x = x;
    this.y = y;
  }
  
  public void draw(Graphics2D g2, int playerX, int playerY, int index){
    g2.setColor(Color.GRAY);
    g2.fillOval(x + getXyAdjust()[0] - RADIUS, y + getXyAdjust()[1] - RADIUS, RADIUS * 2, RADIUS * 2);
  }
}
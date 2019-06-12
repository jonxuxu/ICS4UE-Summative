package client;

import java.awt.Graphics2D;
import java.awt.Color;

class GhostE extends Status{
  private int x,y;
  private static int RADIUS = 25;
  private static int[] xyAdjust;
  GhostE(int x, int y){
    this.x = x;
    this.y = y;
  }
  
  public void draw(Graphics2D g2, int playerX, int playerY, int index){
    g2.setColor(Color.GRAY);
    g2.fillOval(x + xyAdjust[0] - RADIUS, y + xyAdjust[1] - RADIUS, RADIUS * 2, RADIUS * 2);
  }
}
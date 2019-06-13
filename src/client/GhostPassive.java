package client;

import java.awt.Graphics2D;
import java.awt.Color;

class GhostPassive extends Status{
  private static int LENGTH = 10;
  private int stacks;
  GhostPassive(int stacks){
    this.stacks = stacks;
  }
  
  public void draw(Graphics2D g2, int playerX, int playerY, int index){
    int red = 200 - stacks;
    int green = 100-stacks;
    int blue = 200 - stacks;
    Color color = new Color((int)Math.max(0, red),(int)Math.max(0, green),(int)Math.max(0, blue));
    g2.setColor(color);
    g2.fillRect(playerX + getXyAdjust()[0] - getPlayerLength()/2 + index*LENGTH, playerY + getXyAdjust()[1] - getPlayerLength()/2 - LENGTH, LENGTH, LENGTH);
  }
}
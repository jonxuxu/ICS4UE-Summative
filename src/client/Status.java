package client;

import java.awt.Graphics2D;

/**
 * Status.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-21
 */

public abstract class Status {
  private static int[] xyAdjust;
  private static int LENGTH = 10;
  private static int PLAYER_LENGTH = 120;
  public abstract void draw(Graphics2D g2, int playerX, int playerY, int index);
  
  public static void setXyAdjust(int[] xyAdjust1) {
    xyAdjust = xyAdjust1;
  }
  public static int[] getXyAdjust(){
    return xyAdjust;
  }
  public static void setLength(int length) {
    LENGTH = length;
  }
  public static int getLength(){
    return LENGTH;
  }
  public static void setPlayerLength(int playerLength) {
    PLAYER_LENGTH = playerLength;
  }
  public static int getPlayerLength(){
    return PLAYER_LENGTH;
  }
}

package client;

import java.awt.Graphics2D;

/**
 * Status.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-21
 */

public abstract class Status {
  private static int[] xyAdjust;
  public abstract void draw(Graphics2D g2, int playerX, int playerY, int index);
  public static void setXyAdjust(int[] xyAdjust1) {
    xyAdjust = xyAdjust1;
  }
  public static int[] getXyAdjust(){
    return xyAdjust;
  }
}

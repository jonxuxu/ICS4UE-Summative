package client;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Projectile.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-30
 */

public class Projectile {
   private int ID;
   private int x;
   private int y;
   private static int[] xyAdjust;

   Projectile(int ID, int x, int y) {
      this.ID = ID;
      this.x = x;
      this.y = y;
   }

   public void draw(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      g2.fillRect(x+xyAdjust[0], y+xyAdjust[1], 10, 10);
   }

   public static void setXyAdjust(int[] xyAdjust1) {
      xyAdjust = xyAdjust1;
   }
}

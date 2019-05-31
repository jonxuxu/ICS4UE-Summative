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
   int ID;
   int x;
   int y;

   Projectile(int ID, int x, int y) {
      this.ID = ID;
      this.x = x;
      this.y = y;
   }

   public void draw(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      g2.fillRect(x, y, 10, 10);
   }
}

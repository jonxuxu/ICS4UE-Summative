package client;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * AOE.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class AOE {
   int ID;
   int x;
   int y;
   int radius;

   AOE(int ID, int x, int y, int radius) {
      this.ID = ID;
      this.x = x;
      this.y = y;
      this.radius=radius;
   }

   public void draw(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      g2.fillOval(x-radius, y-radius, radius*2, radius*2);
   }
}
package client;

import client.particle.AshParticle;
import client.particle.FireParticle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

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
   private int x, y;
   private static int[] xyAdjust;
   private ArrayList<FireParticle> particles = new ArrayList<FireParticle>();

   Projectile(int ID, int x, int y) {
      this.ID = ID;
      this.x = x;
      this.y = y;
   }

   public void draw(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      g2.fillRect(x+xyAdjust[0], y+xyAdjust[1], 10, 10);
      particles.add(new FireParticle(x+xyAdjust[0], y+xyAdjust[1], (int) ((Math.random() * 5 + 5))));

      //Draws particles
      for (int i = 0; i < particles.size(); i++) {
         try {
            if (particles.get(i).update()) {
               particles.remove(i);
            } else {
               particles.get(i).render(g2);
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public static void setXyAdjust(int[] xyAdjust1) {
      xyAdjust = xyAdjust1;
   }
}

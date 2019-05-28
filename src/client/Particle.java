package client;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {

   private double x;
   private double y;
   private double dx;
   private double dy;
   private int size;
   private int life;
   private Color color;
   private final boolean GRAVITY = true;
   private int lifeinit;

   public Particle(double x, double y, double dx, double dy, int size, int life, Color c) {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
      this.size = size;
      this.life = life;
      this.lifeinit = life;
      this.color = c;
   }

   public boolean update() {
      x += dx;
      y += dy;

      if (GRAVITY) {
         y -= 1.0 / 2.0 * (-9.81) * Math.pow(lifeinit - life, 0.25);
      }
      life--;
      if (life <= 0)
         return true;
      return false;
   }

   public void render(Graphics2D g) {
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.setColor(color);
      g2d.fillRect((int) (x - (size / 2)), (int) (y - (size / 2)), size, size);

      g2d.dispose();
   }
}
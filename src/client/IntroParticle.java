package client;

import java.awt.Color;
import java.awt.Graphics2D;

public class IntroParticle extends Particle{
   private double dx, dy;
   private int lifeInit;

   public IntroParticle(double x, double y, double dx, double dy, int size, int life, Color c) {
      super(x, y, size, life, c);
      this.dx = dx;
      this.dy = dy;
      this.lifeInit = life;
   }

   public boolean update() {
      return super.update(dx, dy - 1.0 / 2.0 * (-9.81) * Math.pow(lifeInit - super.getInfo()[2], 0.25));
   }


}
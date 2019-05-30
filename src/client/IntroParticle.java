package client;

import java.awt.Color;
import java.awt.Graphics2D;

public class IntroParticle extends Particle{
   private double dx, dy;
   private int lifeInit;
   private static Color fireColours[] = {new Color(255, 249, 202), new Color(209, 106, 4), new Color(227, 238, 35), new Color(254, 69, 0), new Color(234, 185, 79)};


   public IntroParticle(double x, double y, double dx, double dy, int size, int life) {
      super(x, y, size, life);
      int randomNum = (int) (Math.random() * fireColours.length);
      super.setColor(fireColours[randomNum]);

      this.dx = dx;
      this.dy = dy;
      this.lifeInit = life;
   }

   public boolean update() {
      return super.update(dx, dy - 1.0 / 2.0 * (-9.81) * Math.pow(lifeInit - super.getInfo()[2], 0.25));
   }


}
package client.particle;

import java.awt.Color;

/**
 * IntroParticle.java
 * This is a particle class used during during the intro
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public class IntroParticle extends Particle {
   private double dx, dy;
   private int lifeInit;
   private static Color fireColours[] = {new Color(255, 249, 202), new Color(209, 106, 4), new Color(227, 238, 35), new Color(254, 69, 0), new Color(234, 185, 79)};

   /**
    * Class Constructor
    * @param x x-location
    * @param y y-location
    * @param dx change in x
    * @param dy change in y
    * @param size size of particle
    * @param life duration of particle
    */
   public IntroParticle(double x, double y, double dx, double dy, int size, int life) {
      super(x, y, size, life);
      int randomNum = (int) (Math.random() * fireColours.length);
      super.setColor(fireColours[randomNum]);

      this.dx = dx;
      this.dy = dy;
      this.lifeInit = life;
   }

   /**
    * method to update the particle and erase if time is done
    * @return the state of the update
    */
   public boolean update() {
      return super.update(dx, dy - 1.0 / 2.0 * (-9.81) * Math.pow(lifeInit - super.getInfo()[2], 0.25));
   }


}
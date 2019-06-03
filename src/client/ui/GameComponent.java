package client.ui;

import java.awt.Graphics2D;

/**
 * GameComponent.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-06-02
 */

public abstract class GameComponent {
   private static double SCALING;
   private static int DESIRED_X, DESIRED_Y;

   public abstract void draw(Graphics2D g2);

   public static void initializeSize(double SCALING1, int DESIRED_X1, int DESIRED_Y1) {
      SCALING = SCALING1;
      DESIRED_X = DESIRED_X1;
      DESIRED_Y = DESIRED_Y1;
   }

   public double getSCALING() {
      return SCALING;
   }

   public int getMAX_X() {
      return scale(DESIRED_X);
   }

   public int getMAX_Y() {
      return scale(DESIRED_Y);
   }
   public int scale(int unscaled){
      return((int)(unscaled*SCALING));
   }
}

package client.gameUi;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
   private static Map<String, Font> fonts = new HashMap<String, Font>();

   public abstract void draw(Graphics2D g2);

   public static void initializeSize(double SCALING1, int DESIRED_X1, int DESIRED_Y1) {
      SCALING = SCALING1;
      DESIRED_X = DESIRED_X1;
      DESIRED_Y = DESIRED_Y1;
      // Setting fonts
      fonts.put("main", new Font("Cambria Math", Font.PLAIN, (int) (12 * SCALING)));
      fonts.put("regular", new Font("Cambria Math", Font.PLAIN, (int) (5 * SCALING)));
      fonts.put("header", new Font("Akura Popo", Font.PLAIN, (int) (25 * SCALING)));
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

   public Font getFont(String fontName) {
      return fonts.get(fontName);
   }
}

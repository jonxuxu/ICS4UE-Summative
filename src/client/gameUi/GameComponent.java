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
   private static int MAX_X, MAX_Y;
   private static Map<String, Font> fonts = new HashMap<String, Font>();


   public abstract void draw(Graphics2D g2);

   public static void initializeSize(double SCALING1, int MAX_X1, int MAX_Y1) {
      SCALING = SCALING1;
      MAX_X = MAX_X1;
      MAX_Y = MAX_Y1;
      // Setting fonts
      fonts.put("main", new Font("Cambria Math", Font.PLAIN, (int) (12 * SCALING)));
      fonts.put("regular", new Font("Cambria Math", Font.PLAIN, (int) (5 * SCALING)));
      fonts.put("stats", new Font("Cambria Math", Font.PLAIN, (int) (9 * SCALING)));
      fonts.put("header", new Font("Akura Popo", Font.PLAIN, (int) (25 * SCALING)));
   }

   public double getSCALING() {
      return SCALING;
   }

   public int getMAX_X() {
      return MAX_X;
   }

   public int getMAX_Y() {
      return MAX_Y;
   }
   public int scale(int unscaled){
      return((int)(unscaled*SCALING));
   }

   public Font getFont(String fontName) {
      return fonts.get(fontName);
   }
}

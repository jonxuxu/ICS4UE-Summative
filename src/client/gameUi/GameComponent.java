package client.gameUi;


import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

/**
 * GameComponent.java
 * This is a general super class for all the components in the game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-02
 */

public abstract class GameComponent {
   private static int MAX_X, MAX_Y;
   private static Map<String, Font> fonts = new HashMap<String, Font>();

   /**
    * Method to draw all the game components
    * @param g2 Graphics2D
    */
   public void draw(Graphics2D g2){};


   /**
    * Initializes fonts and screen dimensions
    * @param MAX_X1 int, the max x dimension
    * @param MAX_Y1 int, the max y dimenison
    */
   public static void initializeSize(int MAX_X1, int MAX_Y1) {
      MAX_X = MAX_X1;
      MAX_Y = MAX_Y1;
      // Setting fonts
      fonts.put("main", new Font("Cambria Math", Font.PLAIN, (int) (24 )));
      fonts.put("regular", new Font("Cambria Math", Font.PLAIN, (int) (10 )));
      fonts.put("stats", new Font("Cambria Math", Font.PLAIN, (int) (18 )));
      fonts.put("header", new Font("Akura Popo", Font.PLAIN, (int) (50 )));
   }

   /**
    * Gets the max_X
    * @return MAX_X, which refers to the maximum X
    */
   public int getMAX_X() {
      return MAX_X;
   }

   /**
    * Gets the max_y
    * @return MAX_Y, which refers to the maximum Y
    */
   public int getMAX_Y() {
      return MAX_Y;
   }

   /**
    * Returns the font necessary
    * @param fontName String, which refers to the font in question
    * @return The font itself
    */
   public Font getFont(String fontName) {
      return fonts.get(fontName);
   }
}

package client.gameUi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * DebugComponent.java
 * This is responsible for drawing debug info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-02
 */

public class DebugComponent extends GameComponent {
   private final int MAX_X = super.getMAX_X();
   private final int MAX_Y = super.getMAX_Y();
   private final Font FONT = super.getFont("main");
   private boolean visible = false;

   // Debug info
   private int fps;
   private int[] mouse;
   private String[] mouseMessage = {"standby", "click"};
   private char keyPress;
   private double mb = 1024 * 1024;
   private double usedMem, maxMem;
   private int[] currentXy;

   /**
    * Draws the debug
    * @param g2 Graphics2D, used to draw the debug component
    */
   public void draw(Graphics2D g2) {
      if (visible) {
         g2.setColor(new Color(0, 0, 0, 128));
         g2.fillRect(0, 0, MAX_X, MAX_Y);
         g2.setColor(Color.white);
         g2.setFont(FONT);
         g2.drawString("DEBUG MODE", 0, (int) (20));
         g2.drawString("Fps: " + fps, 0, (int) (40));
         g2.drawString("Mouse: " + mouse[0] + "x " + mouse[1] + "y " + mouseMessage[mouse[2]], 0, (int) (60));
         g2.drawString("Keyboard: " + keyPress, 0, (int) (80));
         g2.drawString("Memory: " + String.format("%.2f", usedMem) + "mb out of " + maxMem + "mb   " + String.format("%.2f", usedMem / maxMem * 100) + "%", 0, (int) (100));
         g2.drawString("X: " + currentXy[0] + " Y:" + currentXy[1], 0, (int) (120));

      }
   }

   /**
    * Toggles the visiblity
    */
   public void toggle() {
      visible = !visible;
   }

   /**
    * Updates the debug
    * @param fps int, the frames per second
    * @param mouseState int[], returns the state of the mouse
    * @param keyPress char, the key that was last pressed
    * @param usedMem double, the used memory
    * @param totalMem double, the total memory
    * @param currentXy int[], the xy
    */
   public void update(int fps, int[] mouseState, char keyPress, double usedMem, double totalMem, int[] currentXy) {
      this.fps = fps;
      this.mouse = mouseState;
      this.keyPress = keyPress;
      this.usedMem = usedMem / mb;
      this.maxMem = totalMem / mb;
      this.currentXy = currentXy;
   }
}

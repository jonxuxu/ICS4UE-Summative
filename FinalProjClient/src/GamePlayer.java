import java.awt.Graphics2D;

/**
 * GamePlayer.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public class GamePlayer extends Player {
   //Constants
   private int ID;
   private int[] xy = {300, 300};
   private int[] centerXy = new int[2];
   private double scaling;

   GamePlayer(String username) {
      super(username);
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public void addXy(double angle) {
      xy[0] += 3 * Math.cos(angle);
      xy[1] += 3 * Math.sin(angle);
   }

   public void setCenterXy(int[] centerXy) {
      this.centerXy[0] = centerXy[0];
      this.centerXy[1] = centerXy[1];
   }

   public int[] getXy() {
      return (xy);
   }

   public void setXy(int[] xy) {
      this.xy = xy;
   }

   public void draw(Graphics2D g2, int[] midXy) {
      g2.fillRect(centerXy[0] + (int) (scaling * (xy[0] - midXy[0]))-(int) (100 * scaling)/2, centerXy[1] + (int) (scaling * (xy[1] - midXy[1]))-(int) (100 * scaling)/2, (int) (100 * scaling), (int) (100 * scaling));
   }

   public void setScaling(double scaling) {
      this.scaling = scaling;
   }
}

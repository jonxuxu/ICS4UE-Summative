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
   private int[] xy= {300,300};
   GamePlayer (String username){
      super (username);
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public void addXy(double angle){
      xy[0]+=3*Math.cos(angle);
      xy[1]+=3*Math.sin(angle);
   }

   public int[] getXy(){
      return(xy);
   }

   public void setXy(int[] xy) {
      this.xy = xy;
   }

   public void draw (Graphics2D g2){
      g2.fillRect(xy[0],xy[1],200,200);
   }

}

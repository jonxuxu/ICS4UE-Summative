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
      if (angle!=-1) {
         xy[0] += 5 * Math.cos(angle);
         xy[1] += 5 * Math.sin(angle);
      }
   }

   public int[] getXy(){
      return(xy);
   }
}

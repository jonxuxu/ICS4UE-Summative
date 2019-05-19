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
   private double[] xy= {300,300};//set as a double intentionally so that adding diagonally will be better
   private int[] finalXy= new int [2];
   GamePlayer (String username){
      super (username);
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public void addXy(double angle){
      if (angle!=-1) {
         xy[0] += 7.5 * Math.cos(angle);
         xy[1] += 7.5 * Math.sin(angle);
      }
   }

   public int[] getXy(){
      finalXy[0]=(int)(xy[0]);
      finalXy[1]=(int)(xy[1]);
      return(finalXy);
   }
}

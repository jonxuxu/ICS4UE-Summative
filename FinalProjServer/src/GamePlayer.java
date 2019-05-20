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
   private GeneralClassServer thisClass = new TestClassServer();//Temporary, normally it should be determined in the constructor
   private boolean spell1;

   GamePlayer(String username) {
      super(username);
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

   public int[] getXy() {
      return (xy);
   }

   public void setXy(int[] xy) {
      this.xy = xy;
   }

   public void setSpell1(boolean spell1) {
      this.spell1=spell1;
   }

   public boolean getSpell1() {
      return spell1;
   }


   public GeneralClassServer getThisClass(){
      return(thisClass);
   }
}

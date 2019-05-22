import java.awt.Graphics2D;
import java.util.ArrayList;

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
   private GeneralClass thisClass = new TestClass();//Temporary, normally it should be determined in the constructor
   private int desiredSpell;
   private int [] spellPercent = {100, 100, 100};
   private ArrayList<Status> allStatus = new ArrayList<Status>();
   private int gold = 0;
   private boolean artifact;
   private int level = 0;

   GamePlayer(String username) {
      super(username);
   }

   public void setID(int ID) {
      this.ID = ID;
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
      thisClass.drawReal(g2, centerXy[0] + (int) (scaling * (xy[0] - midXy[0])) - (int) (100 * scaling) / 2, centerXy[1] + (int) (scaling * (xy[1] - midXy[1])) - (int) (100 * scaling) / 2, (int) (100 * scaling), (int) (100 * scaling), desiredSpell);
      if (desiredSpell!=-1) {
         desiredSpell=-1;
      }
   }

   public void setScaling(double scaling) {
      this.scaling = scaling;
   }

   public void setSpell(int spellIndex) {
      if (spellIndex!=-1) {
         this.desiredSpell = spellIndex;
      }
   }

   public void setSpellPercent(int spellPercent, int spellIndex) {
      this.spellPercent[spellIndex] = spellPercent;
   }

   public double getSpellPercent(int spellIndex) {
      return spellPercent[spellIndex] / 100.0;
   }


   public GeneralClass getThisClass() {
      return (thisClass);
   }

   public void setGold(int gold) {
      this.gold = gold;
   }

   public void addStatus(int statusInt) {
      allStatus.clear(); //very inefficient, possibly change?
      allStatus.add(new Status(statusInt));
   }

   public void setArtifact(boolean artifact) {
      this.artifact = artifact;
   }
   public void setLevel(int level){
      this.level=level;
   }

   public int getGold() {
      return gold;
   }

   public int getLevel() {
      return level;
   }
}

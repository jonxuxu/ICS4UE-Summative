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
   private boolean spells[] = new boolean[3];
   private boolean artifact = false;
   private Status status = null;
   private int gold = 0;
   private int level = 0;


   GamePlayer(String username) {
      super(username);
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public void addXy(double angle) {
      if (angle != -1) {
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

   public void setSpell(boolean spell, int spellIndex) {
      spells[spellIndex] = spell;
   }

   public boolean getSpell(int spellIndex) {
      return spells[spellIndex];
   }

   public String getFullOutput(int spellTick) {
      String outputString = "";
      outputString += xy[0] + "," + xy[1] + ",";//Coords
      outputString += thisClass.getHealth() + "," + thisClass.getMaxHealth() + "," + thisClass.getAttack() + "," + thisClass.getMobility() + "," + thisClass.getRange() + ",";//Stats
      outputString += artifact + "," + status + "," + gold + "," + level + ",";//General
      outputString += spells[0] + "," + thisClass.getSpellPercent(spellTick, 0) + "," + spells[1] + "," + thisClass.getSpellPercent(spellTick, 1) + "," + spells[2] + "," + thisClass.getSpellPercent(spellTick, 2);//Spells
      return outputString;
   }

   public GeneralClass getThisClass() {
      return (thisClass);
   }
}

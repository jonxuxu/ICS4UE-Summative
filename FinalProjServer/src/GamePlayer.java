import java.util.ArrayList;

/**
 * GamePlayer.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public abstract class GamePlayer extends Player {
   //Constants
   private int ID;
   private int[] xy = {300, 300};
   private int[] centerXy = new int[2];
   private double scaling;//Temporary, normally it should be determined in the constructor
   private boolean spells[] = new boolean[3];
   private boolean artifact = false;
   private ArrayList<Status> allStatus = new ArrayList<Status>();
   private int gold = 0;
   private int level = 0;
   private int desiredSpell;
   private int maxHealth;
   private int health;
   private int attack;
   private int mobility;
   private int range;


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
      desiredSpell = -1;//In case nothing affects it
      for (int i = 0; i < 3; i++) {
         if (spells[i]) {
            desiredSpell = i;
         }
      }
      outputString += xy[0] + "," + xy[1] + ",";//Coords
      outputString += health + "," + maxHealth + "," + attack + "," + mobility + "," + range + ",";//Stats
      outputString += artifact + "," + gold + "," + level + ",";//General
      outputString += desiredSpell + "," + getSpellPercent(spellTick, 0) + "," + getSpellPercent(spellTick, 1) + "," + getSpellPercent(spellTick, 2);//Spells
      for (int i = 0; i < allStatus.size(); i++) {
         outputString += "," + allStatus.get(i); //Status exclusive
      }
      outputString += " ";
      return outputString;
   }
   public abstract boolean testSpell(int spellTick, int spellIndex);
   public abstract int getSpellPercent(int spellTick, int spellIndex);
   public int getAttack() {
      return attack;
   }

   public int getMaxHealth() {
      return maxHealth;
   }

   public int getRange() {
      return range;
   }

   public int getHealth() {
      return health;
   }

   public int getMobility() {
      return mobility;
   }

   public void setAttack(int attack) {
      this.attack = attack;
   }

   public void setHealth(int health) {
      this.health = health;
   }

   public void setMaxHealth(int maxHealth) {
      this.maxHealth = maxHealth;
   }

   public void setMobility(int mobility) {
      this.mobility = mobility;
   }

   public void setRange(int range) {
      this.range = range;
   }
}

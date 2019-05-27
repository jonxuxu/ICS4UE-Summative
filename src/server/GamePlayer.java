package server;

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
   private double[] xy = {300, 300};
   private int[] centerXy = new int[2];
   private double scaling;//Temporary, normally it should be determined in the constructor
   private boolean spells[] = new boolean[3];
   private boolean artifact = false;
   private boolean damaged = false;
   private ArrayList<Status> allStatus = new ArrayList<Status>();
   private int gold = 0;
   private int desiredSpell;
   private int maxHealth;
   private int health;
   private int attack;
   private int mobility;
   private int range;
   private int spriteID;


   GamePlayer(String username) {
      super(username);
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public double[] getXy() {
      return (xy);
   }

   public void addXy(double xDisp, double yDisp) {
      this.xy[0] += xDisp;
      this.xy[1] += yDisp;
   }

   public void setSpell(boolean spell, int spellIndex) {
      spells[spellIndex] = spell;
   }

   public boolean getSpell(int spellIndex) {
      return spells[spellIndex];
   }

   public String getMainOutput(int spellTick) {
      StringBuilder outputString = new StringBuilder();
      outputString.append((int) (xy[0]) + "," + (int) (xy[1]) + ",");//Coords
      outputString.append(health + "," + maxHealth + "," + attack + "," + mobility + "," + range + ",");//Stats
      outputString.append(artifact + "," + gold + ",");//General
      outputString.append(spriteID + ",");//Sprite
      outputString.append(getSpellPercent(spellTick, 0) + "," + getSpellPercent(spellTick, 1) + "," + getSpellPercent(spellTick, 2) + ",");//Spells
      outputString.append(damaged + "," + allStatus.size());
      for (int i = 0; i < allStatus.size(); i++) {
         outputString.append("," + allStatus.get(i)); //Status exclusive
      }
      return outputString.toString();
   }

   public String getOtherOutput() {
      StringBuilder outputString = new StringBuilder();
      outputString.append((int) (xy[0]) + "," + (int) (xy[1]) + ",");//Coords
      outputString.append(health + "," + maxHealth + ",");//stats
      outputString.append(artifact + ",");//General
      outputString.append(spriteID + ",");//Sprite
      outputString.append(damaged + "," + allStatus.size());
      for (int i = 0; i < allStatus.size(); i++) {
         outputString.append("," + allStatus.get(i)); //Status exclusive
      }
      return outputString.toString();
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

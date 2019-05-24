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

public abstract class GamePlayer extends Player {
   //Constants
   private int ID;
   private int[] xy = {300, 300};
   private int[] centerXy = new int[2];
   private double scaling;
   private int desiredSpell;
   private int [] spellPercent = {100, 100, 100};
   private ArrayList<Status> allStatus = new ArrayList<Status>();
   private int gold = 0;
   private boolean artifact;
   private int level = 0;
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
      drawReal(g2, centerXy[0] + (int) (scaling * (xy[0] - midXy[0])) - (int) (100 * scaling) / 2, centerXy[1] + (int) (scaling * (xy[1] - midXy[1])) - (int) (100 * scaling) / 2, (int) (100 * scaling), (int) (100 * scaling), desiredSpell);
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
   public abstract void spellAnimation(Graphics2D g2, int x, int y, int width, int height, int spellIndex);
   //public abstract void spell2(Graphics2D g2, int x, int y, int width, int height);
   //  public abstract void spell3(Graphics2D g2, int x, int y, int width, int height);
   public abstract void move(Graphics2D g2, int x, int y, int width, int height);
   public abstract void drawReal(Graphics2D g2, int x, int y, int width, int height, int spellIndex) ;
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

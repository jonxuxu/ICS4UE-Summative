import java.awt.Graphics2D;

/**
 * GeneralClass.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public abstract class GeneralClass {
   private int maxHealth;
   private int health;
   private int attack;
   private int mobility;
   private int range;
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

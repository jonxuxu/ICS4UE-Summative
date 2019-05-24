/**
 * TestClass.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public class TestClass extends GamePlayer {
   private int[] spellCooldowns = {500,500,500};
   private int[] lastSpellTicks = {-10000,-10000,-10000};//So that they can be used immediately

   TestClass(String username) {
      super(username);
      setMaxHealth(100);
      setHealth(100);
      setAttack(100);
      setMobility(10);
      setRange(10);
   }

   public boolean testSpell(int spellTick, int spellIndex) {
      if (spellTick - lastSpellTicks[spellIndex] > spellCooldowns[spellIndex]) {
         lastSpellTicks[spellIndex] = spellTick;
         return true;
      } else {
         return false;
      }
   }

   public int getSpellPercent(int spellTick, int spellIndex) {
      if (spellTick - lastSpellTicks[spellIndex] > spellCooldowns[spellIndex]) {
         return (100);
      } else {
         return ((int) ((100.0 * (spellTick - lastSpellTicks[spellIndex]) / spellCooldowns[spellIndex])));
      }
   }
}

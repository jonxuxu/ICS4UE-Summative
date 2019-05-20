/**
 * TestClassServer.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public class TestClassServer extends GeneralClassServer {
   private int maxHealth = 100;
   private int attack = 10;
   private int mobility = 10;
   private int range = 10;
   private int lastSpellTick=-10000;//So that it can be used immediately
   private int spell1Cooldown=500;

   public boolean testSpell1(int spellTick) {
      if (spellTick-lastSpellTick>spell1Cooldown){
         lastSpellTick=spellTick;
         return true;
      }else{
         return false;
      }
   }

   public int getSpell1Percent(int spellTick){
      if (spellTick-lastSpellTick>spell1Cooldown){
         return(100);
      }else{
         return ((int)((100.0*(spellTick-lastSpellTick)/spell1Cooldown)));
      }
   }
}

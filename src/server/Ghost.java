package server;
import java.util.ArrayList;

/**
 * Ghost.java
 *
 * This is the class handling the Ghost class, an extension of the Player class
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */

public class Ghost extends Player {
   private int[] spellCooldowns = {100, 100, 100};
   private int[] spellTimers = {0, 0, 0};
   private int[] passiveTimers;
   private static int PASSIVE_COOLDOWN = 100;
   private static int PASSIVE_RANGE = 300;
   private static int Q_BASE_DAMAGE = 100;
   private static int Q_DAMAGE_PER_STACK = 10;
   private static int Q_SPEED = 5;
   private static int Q_RANGE = 200;
   private static int Q_DURATION = Q_RANGE / Q_SPEED;
   private boolean inE = false;
   private static int SPACE_DURATION = 200;

   private ArrayList<Player> qBlacklist = new ArrayList<Player>();

   /**
    * Another constructor for class Ghost
    * @param username the username of the player on the server
    * @param teamNumber the team which the player is on
    */
   Ghost(String username, int teamNumber) {
      super(username, teamNumber);
      setMaxHealth(300);
      setHealth(300);
      setAttack(30);
      setMobility(5);
      setRange(75);
      setAutoAttackCooldown(20);
      setFlareCooldown(100);
      setMelee(true);
   }

   /**
    * Handles the casting of the 3 different spells
    * @param spellIndex the spell which to cast, where 0 is q, 1 is e, and 2 is space
    * @return a boolean for whether the spell is cast or not
    */
   @Override
   public boolean castSpell(int spellIndex) {
      if (!getStunned()) {
         if (spellTimers[spellIndex] <= 0) {
            spellTimers[spellIndex] = spellCooldowns[spellIndex];
            if (spellIndex == 0) { //Q
               launch(getMouseX(), getMouseY(), Q_SPEED, Q_RANGE);
               addAOE(new GhostQAOE(getX(), getY(), Q_DURATION));
               qBlacklist.clear();
            } else if (spellIndex == 1) {//E
               addStatus(new GhostE(getX(), getY(), getMouseX(), getMouseY()));
               inE = true;
            } else {//Space
               addStatus(new Uncollidable(SPACE_DURATION));
               addStatus(new Invisible(SPACE_DURATION));
            }
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
   
   /**
    * Gets the cooldown remaining, as a percent, of the chosen spell
    * @param spellIndex the spell which to check, where 0 is q, 1 is e, and 2 is space
    * @return the percent away from the spell being off cooldown
    */
   @Override
   public int getSpellPercent(int spellIndex) {
      return ((int)((1.0*(spellCooldowns[spellIndex] - spellTimers[spellIndex]) / spellCooldowns[spellIndex]*100)));
   }

   /**
    * Updates the character and all the autoattacks, aoes, statuses, and shields each frame
    */
   @Override
   public void update() {
      //ARTIFACT ADDED HERE
      if ((checkOnArtifact())) {
         setHasArtifact(true);
      }
      if (getHasArtifact()) {
         if (checkOnBaseArtifact()) {
            getArtifacts(getTeam()).setWinner(true);
         }
      }

      if (passiveTimers == null) {
         passiveTimers = new int[getEnemiesSize()];
      }
      for (int i = 0; i < 3; i++) {
         if (spellTimers[i] > 0) {
            spellTimers[i]--;
         }
      }
      if (inE) {
         spellTimers[1] = spellCooldowns[1];
      }
      for (int i = 0; i < passiveTimers.length; i++) {
         if (passiveTimers[i] > 0) {
            passiveTimers[i]--;
         }
      }
      updateBasicTimers();
      //Passive
      for (int i = 0; i < getEnemiesSize(); i++) {
         if (passiveTimers[i] <= 0) {
            if (Math.sqrt(Math.pow(getEnemy(i).getX() - getX(), 2) + Math.pow(getEnemy(i).getY() - getY(), 2)) < PASSIVE_RANGE) {
               getEnemy(i).addStatus(new GhostPassive());
               for (int j = 0; j < getEnemy(i).getStatusesSize(); j++){
                 if(getEnemy(i).getStatus(j) instanceof GhostPassive){
                   (getEnemy(i).getStatus(j)).refresh();
                 }
               }
               passiveTimers[i] = PASSIVE_COOLDOWN;
            }
         }
      }

      //Update Projectiles
      for (int i = getProjectilesSize() - 1; i >= 0; i--) {
         getProjectile(i).advance();
         Projectile removed = null;
         if ((getProjectile(i).getRemainingDuration() <= 0) || hitObstacle(getProjectile(i))) {
            removed = removeProjectile(i);
            if (removed instanceof FlareProjectile) {
               addAOE(new FlareAOE(removed.getX(), removed.getY()));
            }
         } else {
            //Insert Collision with Terrain
            if (getProjectile(i) instanceof AutoProjectile) {
               for (int j = 0; j < getEnemiesSize(); j++) {
                  if (getProjectile(i).collides(getEnemy(j))) {
                     getEnemy(j).damage(getAttack());
                     removed = removeProjectile(i);
                  }
               }
            }
         }
      }

      //Update AOEs
      for (int i = getAOESize() - 1; i >= 0; i--) {
         getAOE(i).advance();
         AOE removed = null;
         if (getAOE(i).getRemainingDuration() <= 0) {
            removed = removeAOE(i);
         } else {
            if (getAOE(i) instanceof FlareAOE) {
               for (int j = 0; j < getEnemiesSize(); j++) {
                  if (getAOE(i).collides(getEnemy(j))) {
                     getEnemy(j).addStatus(new Illuminated(500));
                  }
               }
            } else if (getAOE(i) instanceof AutoAOE) {
               for (int j = 0; j < getEnemiesSize(); j++) {
                  if (getAOE(i).collides(getEnemy(j))) {
                     getEnemy(j).damage(getAttack());
                  }
               }
            } else if (getAOE(i) instanceof GhostQAOE) {
               ((GhostQAOE) getAOE(i)).setX(getX());
               ((GhostQAOE) getAOE(i)).setY(getY());
               for (int j = 0; j < getEnemiesSize(); j++) {
                  for (int k = 0; k < qBlacklist.size(); k++) {
                     if (!qBlacklist.isEmpty()) {
                        if (getEnemy(j) != qBlacklist.get(k)) {
                           if (getAOE(i).collides(getEnemy(j))) {
                              int numStacks = 0;
                              for (int m = 0; m < getEnemy(j).getStatusesSize(); m++) {
                                 if (getEnemy(j).getStatus(m) instanceof GhostPassive) {
                                    numStacks++;
                                 }
                              }
                              getEnemy(j).damage(Q_BASE_DAMAGE + Q_DAMAGE_PER_STACK * numStacks);
                              qBlacklist.add(getEnemy(j));
                           }
                        }
                     } else {
                        if (getAOE(i).collides(getEnemy(j))) {
                           int numStacks = 0;
                           for (int m = 0; m < getEnemy(j).getStatusesSize(); m++) {
                              if (getEnemy(j).getStatus(m) instanceof GhostPassive) {
                                 numStacks++;
                              }
                           }
                           getEnemy(j).damage(Q_BASE_DAMAGE + Q_DAMAGE_PER_STACK * numStacks);
                           qBlacklist.add(getEnemy(j));
                        }
                     }
                  }
               }
            }
         }
      }

      updateStatuses();

      for (int i = getShieldsSize() - 1; i >= 0; i--) {
         getShield(i).advance();
         Shield removed = null;
         if (getShield(i).getRemainingDuration() <= 0) {
            removed = removeShield(i);
         }
      }
   }

   /**
    * Applies damage to self, updates health and shield as needed
    * @param damage the amount of damage to inflict
    */
   @Override
   public void damage(int damage) {
      damage = (int) (damage * (1 - getDamageReduction()));
      if (getShieldsSize() == 0) {
         setHealth(getHealth() - damage);
      } else {
         if (getShield(0).getStrength() - damage > 0) {
            getShield(0).damage(damage);
         } else {
            setHealth(getHealth() - (damage - getShield(0).getStrength()));
            removeShield(0);
         }
      }
      if (inE) {
         inE = false;
         for (int i = getStatusesSize()-1; i >= 0; i--) {
            if (getStatus(i) instanceof GhostE) {
               setX(((GhostE) getStatus(i)).getX());
               setY(((GhostE) getStatus(i)).getY());
               removeStatus(i);
            }
         }
      }
      if (getHealth() <= 0) {
         addStatus(new Dead());
      }
   }
}

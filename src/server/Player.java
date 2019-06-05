package server;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * User.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public abstract class Player extends User implements CanIntersect {
   //Constants
   private int ID;
   private double[] xy = {300, 300};
   private int[] centerXy = new int[2];
   private double scaling;//Temporary, normally it should be determined in the constructor
   private boolean spells[] = new boolean[3];
   private boolean artifact = false;
   private boolean damaged = false;
   private int gold = 0;
   private int maxHealth;
   private int health;
   private int attack;
   private int mobility;
   private int mobilityBoost = 0;
   //private int maxMobility;
   private int range;
   private int spriteID;
   private int mouseX;
   private int mouseY;

   //May 25//
   private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
   private ArrayList<AOE> aoes = new ArrayList<AOE>();
   private ArrayList<Status> statuses = new ArrayList<Status>();
   private ArrayList<Class> buffBlacklist = new ArrayList<Class>();
   private ArrayList<Shield> shields = new ArrayList<Shield>();
   private ArrayList<Player> allies = new ArrayList<Player>();
   private ArrayList<Player> enemies = new ArrayList<Player>();
   private int autoSpeed = 10;//REE
   private int autoRange;//REE
   private int teamNumber = 9;//Which means that it is invalid
   private Rectangle hitbox = new Rectangle(((int) (xy[0])), ((int) (xy[1])), 50, 50);
   private boolean illuminated = false;
   private boolean stunned = false;
   private boolean invisible = false;
   //Movement
   private int positionIndex;
   private boolean walking;
   //Lighting
   private double flashlightAngle;
   private double damageReduction;

   Player(String username) {
      super(username);
   }


   public void setMouse(int mouseX, int mouseY) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public double[] getXy() {
      return (xy);
   }

   public void addXy(double xDisp, double yDisp) {
      if (!stunned) {
         xy[0] += xDisp;
         xy[1] += yDisp;
      }
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
      outputString.append(health + "," + maxHealth + "," + attack + "," + (mobility + mobilityBoost) + "," + range + ",");//Stats
      outputString.append(artifact + "," + gold + ",");//General
      outputString.append(spriteID + ",");//Sprite
      outputString.append(getSpellPercent(0) + "," + getSpellPercent(1) + "," + getSpellPercent(2) + ",");//Spells
      /* STATUSES NOT WORKING!!! UNCOMMMENT WHEN SUPPORT FOR STATUSES IS ADDED
      outputString.append(damaged + "," + statuses.size());
      for (int i = 0; i < statuses.size(); i++) {
         outputString.append("," + statuses.get(i)); //Status exclusive
      }*/

      outputString.append(damaged + "," + 0);//Temporary "fix"

      return outputString.toString();
   }

   public String getOtherOutput() {
      StringBuilder outputString = new StringBuilder();
      outputString.append((int) (xy[0]) + "," + (int) (xy[1]) + ",");//Coords
      outputString.append(health + "," + maxHealth + ",");//stats
      outputString.append(artifact + ",");//General
      outputString.append(spriteID + ",");//Sprite
      /* STATUSES NOT WORKING!!! UNCOMMMENT WHEN SUPPORT FOR STATUSES IS ADDED
      outputString.append(damaged + "," + statuses.size());
      for (int i = 0; i < statuses.size(); i++) {
         outputString.append("," + statuses.get(i)); //Status exclusive
      }*/

      outputString.append(damaged + "," + 0);//Temporary "fix"
      return outputString.toString();
   }

   //May 25
   public void sendInfo(Player[] players) {
      for (Player player : players) {
         if (player.getTeam() == teamNumber) {
            System.out.println("Ally");
            allies.add(player);
         } else {
            System.out.println("Enemy");
            enemies.add(player);
         }
      }
   }

   public void setFlashlightAngle(double flashlightAngle) {
      this.flashlightAngle = flashlightAngle;
   }

   public double getFlashlightAngle() {
      return (flashlightAngle);
   }


   public void autoAttack() {
      if (!stunned) {
         projectiles.add(new AutoProjectile(((int) (xy[0])), ((int) (xy[1])), mouseX, mouseY, autoSpeed, range));
         //Check for this
      }
   }

   public void flare() {
      if (!stunned) {
         projectiles.add(new FlareProjectile(((int) (xy[0])), ((int) (xy[1])), mouseX, mouseY));
      }
   }

   public void launch(int targetX, int targetY, int speed, int range) {
      double theta = Math.atan2(targetY - xy[1], targetX - xy[0]);
      double dx = speed * Math.cos(theta);
      double dy = speed * Math.sin(theta);
      int totalTime = (int) Math.round(range * 1.0 / speed);
      statuses.add(new Launched(dx, dy, totalTime));
      statuses.add(new Stun(totalTime));
   }

   public abstract void update();

   public void damage(int damage) {//Watch out this is overridden sometimes
      damage = (int) (damage * (1 - damageReduction));
      if (shields.isEmpty()) {
         health -= damage;
      } else {
         if (shields.get(0).getStrength() - damage > 0) {
            shields.get(0).damage(damage);
         } else {
            health -= damage - shields.get(0).getStrength();
            shields.remove(0);
         }
      }
   }


   public void setTeam(int teamNumber) {
      this.teamNumber = teamNumber;
   }

   public int getTeam() {
      return (teamNumber);
   }

   //Movement
   public void setWalking(boolean walking) {
      this.walking = walking;
   }

   public void setPositionIndex(int positionIndex) {
      this.positionIndex = positionIndex;
   }

   public boolean getWalking() {
      return (walking);
   }

   public int getPositionIndex() {
      return (positionIndex);
   }

   public void updateStatuses() {
      mobilityBoost = 0;
      buffBlacklist.clear();
      illuminated = false;
      stunned = false;
      invisible = false;
      damageReduction = 0;
      for (int i = statuses.size() - 1; i >= 0; i--) {
         statuses.get(i).advance();
         Status removed = null;
         if (statuses.get(i).getRemainingDuration() <= 0) {
            removed = statuses.remove(i);
            if (removed instanceof TimeMageQ) {
               addAOE(new TimeMageQAOE(((TimeMageQ) removed).getX(), ((TimeMageQ) removed).getY(), ((TimeMageQ) removed).getTargetX(), ((TimeMageQ) removed).getTargetY()));
            } else if (removed instanceof TimeMageE) {
               setX(((TimeMageE) removed).getX());
               setY(((TimeMageE) removed).getY());
            }
         } else {
            applyStatus(statuses.get(i));
         }
      }
   }




   public void applyStatus(Status status) {
      boolean blacklisted = false;
      if ((status instanceof Illuminated) && (!invisible)) {
         illuminated = true;
      } else if (status instanceof MSBuff) {
         for (int i = 0; i < buffBlacklist.size(); i++) {
            if (status.getClass().equals(buffBlacklist.get(i))) {
               blacklisted = true;
            }
         }
         if (!blacklisted) {
            mobilityBoost += ((MSBuff) (status)).getStrength();
            buffBlacklist.add(status.getClass());
         }
      } else if (status instanceof Stun) {
         stunned = true;
      } else if (status instanceof Launched) {
         xy[0] += ((Launched) (status)).getDX();
         xy[1] += ((Launched) (status)).getDY();
      } else if (status instanceof Invisible) {
         invisible = true;
         illuminated = false;
      } else if (status instanceof Stun) {
         stunned = true;
      } else if (status instanceof Launched) {
         xy[0] += ((Launched) (status)).getDX();
         xy[1] += ((Launched) (status)).getDY();
      } else if (status instanceof Invisible) {
         invisible = true;
         illuminated = false;
      } else if (status instanceof GhostE) {
         ((GhostE) status).setProjectedX(xy[0]);
         ((GhostE) status).setProjectedY(xy[1]);
      } else if (status instanceof ReduceDamage) {
         if (((ReduceDamage) status).getDamageReduction() > damageReduction) {
            damageReduction = ((ReduceDamage) status).getDamageReduction();
         }
      }
   }

   public Area getHitbox() {
      hitbox.setLocation(((int) (xy[0])), ((int) (xy[1])));
      return new Area(hitbox);
   }

   public boolean contains(int x, int y) {
      return hitbox.contains(x, y);
   }

   public int getX() {
      return ((int) (xy[0]));
   }

   public int getY() {
      return ((int) (xy[1]));
   }

   public void setX(int x) {
      xy[0] = x;
   }

   public void setY(int y) {
      xy[1] = y;
   }

   public int getMouseX() {
      return mouseX;
   }

   public int getMouseY() {
      return mouseY;
   }

   public boolean getStunned() {
      return stunned;
   }

   public boolean getIlluminated() {
      return illuminated;
   }

   public void setIlluminated(boolean illuminated) {
      this.illuminated = illuminated;
   }

   public Projectile getProjectile(int i) {
      return projectiles.get(i);
   }

   public ArrayList<Projectile> getAllProjectiles() {
      return projectiles;
   }

   public void addProjectile(Projectile projectile) {
      projectiles.add(projectile);
   }

   public Projectile removeProjectile(int i) {
      return projectiles.remove(i);
   }

   public int getProjectilesSize() {
      return projectiles.size();
   }

   public AOE getAOE(int i) {
      return aoes.get(i);
   }

   public ArrayList<AOE> getAllAOES() {
      return aoes;
   }

   public void addAOE(AOE aoe) {
      aoes.add(aoe);
   }

   public AOE removeAOE(int i) {
      return aoes.remove(i);
   }

   public int getAOESize() {
      return aoes.size();
   }

   public Status getStatus(int i) {
      return statuses.get(i);
   }

   public void addStatus(Status status) {
      statuses.add(status);
   }

   public Status removeStatus(int i) {
      return statuses.remove(i);
   }

   public int getStatusesSize() {
      return statuses.size();
   }

   public Shield getShield(int i) {
      return shields.get(i);
   }

   public void addShield(Shield shield) {
      shields.add(shield);
   }

   public Shield removeShield(int i) {
      return shields.remove(i);
   }

   public int getShieldsSize() {
      return shields.size();
   }

   public Player getEnemy(int i) {
      return enemies.get(i);
   }

   public int getEnemiesSize() {
      return enemies.size();
   }

   public Player getAlly(int i) {
      return allies.get(i);
   }

   public int getAlliesSize() {
      return allies.size();
   }

   public abstract boolean castSpell(int spellIndex);

   public abstract int getSpellPercent(int spellIndex);

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
      return mobility + mobilityBoost;
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

   public void setRange(int range) {
      this.range = range;
   }

   public void setMobility(int mobility) {
      this.mobility = mobility;
   }

   public double getDamageReduction() {
      return damageReduction;
   }
}

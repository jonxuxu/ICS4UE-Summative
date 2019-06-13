package server;


import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 * Player.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-04-24
 */

public abstract class Player extends User implements CanIntersect {
   //Constants
   private int ID;
   private double[] xy = {300, 300};
   private int spawnX = 300;
   private int spawnY = 300;
   private boolean spells[] = new boolean[3];
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
   private double mouseAngle;
   private boolean melee;

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
   private int autoAttackCooldown;
   private int autoAttackTimer;
   private int flareCooldown;
   private int flareTimer;
   private int teamNumber = 9;//Which means that it is invalid
   private static int WIDTH = 30;
   private static int HEIGHT = 60;
   private Rectangle hitbox = new Rectangle(((int) (xy[0]) - WIDTH / 2), ((int) (xy[1]) - HEIGHT / 2), WIDTH, HEIGHT);
   private boolean illuminated = false;
   private boolean stunned = false;
   private boolean invisible = false;
   private boolean uncollidable = false;
   private double damageReduction;
   private String selectedClass;

   //Movement
   private int positionIndex;
   private boolean walking;

   //Lighting
   private boolean flashlightOn;
   private Polygon flashlightBeam = new Polygon();
   private int FLASHLIGHT_RADIUS = 600;
   private Polygon test = new Polygon();
   private static CustomPolygon[] obstacles;
   private CustomPolygon lightingHitbox;
   private int xCo;
   private int yCo;
   private int cVal;

   //Artifact
   private boolean hasArtifact;

   //Player references
   private static Player[] players;
   private static int playerNum;

   //Player artifact num
   private static Artifact[] artifacts = new Artifact[2];

   Player(String username) {
      super(username);
   }

   Player(String username, int teamNumber) {
      super(username);
      this.teamNumber = teamNumber;
   }


   public void setSelectedClass(String selectedClass) {
      this.selectedClass = selectedClass;
   }

   public String getSelectedClass() {
      return (selectedClass);
   }


   public static void updateHitbox() {
      for (int i = 0; i < players.length; i++) {
         if (players[i] != null) {
            players[i].setCenter();
         } else {
            if (obstacles[i] != null) {
               obstacles[i] = null;
            }
         }
      }
   }


   public void setCenter() {
      int[] xP = {(int) (xy[0]) - WIDTH / 2, (int) (xy[0]) + WIDTH / 2, (int) (xy[0]) + WIDTH / 2, (int) (xy[0]) - WIDTH / 2};
      int[] yP = {(int) (xy[1]) - HEIGHT / 2, (int) (xy[1]) - HEIGHT / 2, (int) (xy[1]) + HEIGHT / 2, (int) (xy[1]) + HEIGHT / 2};
      lightingHitbox.setCenter(xP, yP);
   }

   public void setLightingHitbox(int i) {
      int[] xP = {(int) (xy[0]) - WIDTH / 2, (int) (xy[0]) + WIDTH / 2, (int) (xy[0]) + WIDTH / 2, (int) (xy[0]) - WIDTH / 2};
      int[] yP = {(int) (xy[1]) - HEIGHT / 2, (int) (xy[1]) - HEIGHT / 2, (int) (xy[1]) + HEIGHT / 2, (int) (xy[1]) + HEIGHT / 2};
      lightingHitbox = new CustomPolygon(xP, yP, 4);
      obstacles[i] = lightingHitbox;
      //REPLACE WITH getHitboxRectangle
   }

   public static void setConstantHitboxes(int playerNum, ArrayList<Obstacle> obstacles) {
      Player.obstacles = new CustomPolygon[playerNum + obstacles.size()];//For each shape, add another
      for (int i = 0; i < obstacles.size(); i++) {
         Polygon thisPolygon = obstacles.get(i).boundingBox;
         int[] xP = new int[thisPolygon.npoints];
         int[] yP = new int[thisPolygon.npoints];
         for (int j = 0; j < thisPolygon.npoints; j++) {
            xP[j] = thisPolygon.xpoints[j]/2 + 7500;
            yP[j] = thisPolygon.ypoints[j]/2 + 5000;
         }
         Player.obstacles[i + playerNum] = (new CustomPolygon(xP, yP, thisPolygon.npoints));
      }
   }

   public static CustomPolygon[] getObstacles() {
      return (obstacles);
   }

   public void setMouse(int mouseX, int mouseY) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
   }

   public void setMouseAngle(double mouseAngle) {
      this.mouseAngle = mouseAngle;
   }

   public double getMouseAngle() {
      return this.mouseAngle;
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

   public String getMainOutput() {
      StringBuilder outputString = new StringBuilder();
      outputString.append((int) (xy[0]) + "," + (int) (xy[1]) + ",");//Coords
      outputString.append(health + "," + maxHealth + "," + attack + "," + (mobility + mobilityBoost) + "," + range + ",");//Stats
      outputString.append(hasArtifact + "," + gold + ",");//General
      outputString.append(getSpellPercent(0) + "," + getSpellPercent(1) + "," + getSpellPercent(2) + ",");//Spells
      outputString.append(damaged + ",");
      outputString.append(illuminated + "," + 0);//Temporary "fix"
      /* STATUSES NOT WORKING!!! UNCOMMMENT WHEN SUPPORT FOR STATUSES IS ADDED
      outputString.append(damaged + "," + statuses.size());
      for (int i = 0; i < statuses.size(); i++) {
         outputString.append("," + statuses.get(i)); //Status exclusive
      }*/


      return outputString.toString();
   }

   public String getOtherOutput() {
      StringBuilder outputString = new StringBuilder();
      outputString.append((int) (xy[0]) + "," + (int) (xy[1]) + ",");//Coords
      outputString.append(health + "," + maxHealth + ",");//stats
      outputString.append(hasArtifact + ",");//General
      outputString.append(damaged + ",");
      outputString.append(illuminated + "," + 0);//Temporary "fix"


      /* STATUSES NOT WORKING!!! UNCOMMMENT WHEN SUPPORT FOR STATUSES IS ADDED
      outputString.append(damaged + "," + statuses.size());
      for (int i = 0; i < statuses.size(); i++) {
         outputString.append("," + statuses.get(i)); //Status exclusive
      }*/

      return outputString.toString();
   }

   //May 25
   public void sendInfo() {
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

   public static void setPlayerReference(Player[] players1, int playerNum1) {
      players = players1;
      playerNum = playerNum1;
   }

   public boolean checkOnArtifact() {
      if ((!(getArtifacts(1 - getTeam()).getPickedUp())) && (getArtifacts(1 - getTeam()).getBoundingBox().contains(getX(), getY()))) {
         getArtifacts(1 - getTeam()).setPickedUp(true);
         return true;
      } else {
         return false;
      }
   }

   public boolean checkOnBaseArtifact() {
      if ((!(getArtifacts(getTeam()).getPickedUp())) && (getArtifacts(getTeam()).getBoundingBox().contains(getX(), getY()))) {
         getArtifacts(getTeam()).setPickedUp(true);
         return true;
      } else {
         return false;
      }
   }

   public void calculateFlashlightPolygon(double flashlightAngle) {
      int[] xy = {(int) (this.xy[0]), (int) (this.xy[1])};
      flashlightBeam.reset();
      flashlightBeam.addPoint(xy[0], xy[1]);
      int shapeIndex = -2;
      int intersectionIndex = -2;
      int[] savedPoint = new int[2];
      int[] prevPoint = new int[2];
      int newShapeIndex = -1;
      int newIntersectionIndex = -1;
      int points = 1;
      boolean hit;
      double tempFlashlightAngle = flashlightAngle;
      int FLASHLIGHT_SPREAD = 44;
      tempFlashlightAngle -= 0.01 * FLASHLIGHT_SPREAD / 2;
      for (double k = 0; k < FLASHLIGHT_SPREAD; k++) {//If you want to change this, change the 29 below
         hit = false;
         tempFlashlightAngle += 0.01;
         setPlayerVector(xy, xy[0] + (int) (FLASHLIGHT_RADIUS * Math.cos(tempFlashlightAngle)), xy[1] + (int) (FLASHLIGHT_RADIUS * Math.sin(tempFlashlightAngle)));
         int smallestDist = FLASHLIGHT_RADIUS * FLASHLIGHT_RADIUS;
         for (int i = 0; i < obstacles.length; i++) {
            if (!obstacles[i].equals(lightingHitbox)) {
               obstacles[i].setPlayerScalar(xCo, yCo, cVal);
               obstacles[i].setPlayerVector(Math.cos(tempFlashlightAngle), Math.sin(tempFlashlightAngle));
               obstacles[i].setVectorMagnitude(Math.abs(tempFlashlightAngle - flashlightAngle));
               if (obstacles[i].intersect(xy)) {
                  if ((distance(obstacles[i].getIntersect(), xy) < smallestDist)) {
                     smallestDist = distance(obstacles[i].getIntersect(), xy);
                     newShapeIndex = i;
                     newIntersectionIndex = obstacles[i].getIntersectionIndex();
                     savedPoint[0] = obstacles[i].getIntersect()[0];
                     savedPoint[1] = obstacles[i].getIntersect()[1];
                     hit = true;
                  }
               }
            }
         }
         if (!hit) {
            newShapeIndex = -1;
            newIntersectionIndex = -1;
         } else {
            if (newShapeIndex < playerNum) {
               players[newShapeIndex].addStatus(new Illuminated(2));
            }
         }
         if ((shapeIndex != newShapeIndex) || (intersectionIndex != newIntersectionIndex) || (k == 0) || (k == FLASHLIGHT_SPREAD - 1)) {
            points++;
            shapeIndex = newShapeIndex;
            intersectionIndex = newIntersectionIndex;
            if (!((prevPoint[0] == 0) && (prevPoint[1] == 0))) {
               flashlightBeam.addPoint((prevPoint[0]), (prevPoint[1]));
            }
            if (shapeIndex != -1) {
               flashlightBeam.addPoint((savedPoint[0]), (savedPoint[1]));
            } else {
               flashlightBeam.addPoint((int) ((xy[0] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.cos(tempFlashlightAngle))), (int) ((xy[1] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.sin(tempFlashlightAngle))));
            }
         }
         if (hit) {
            prevPoint[0] = savedPoint[0];
            prevPoint[1] = savedPoint[1];
         } else {
            prevPoint[0] = (int) ((xy[0] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.cos(tempFlashlightAngle)));
            prevPoint[1] = (int) ((xy[1] + FLASHLIGHT_RADIUS / Math.cos(flashlightAngle - tempFlashlightAngle) * Math.sin(tempFlashlightAngle)));
         }
      }
      if (points < 3) {
         flashlightBeam.addPoint((int) ((xy[0] + FLASHLIGHT_RADIUS * Math.cos(tempFlashlightAngle + 0.1))), (int) ((xy[1] + FLASHLIGHT_RADIUS * Math.sin(tempFlashlightAngle + 0.1))));
      }
   }

   public int distance(int[] firstXy, int[] secondXy) {
      return (((firstXy[0] - secondXy[0]) * (firstXy[0] - secondXy[0])) + ((firstXy[1] - secondXy[1]) * (firstXy[1] - secondXy[1])));
   }

   public void setPlayerVector(int[] initalXy, int finalX, int finalY) { //player is initial, final is mouse
      xCo = initalXy[1] - finalY;
      yCo = finalX - initalXy[0];
      cVal = finalY * initalXy[0] - finalX * initalXy[1];
   }

   public boolean getFlashlightOn() {
      return (flashlightOn);
   }

   public void setFlashlightOn(boolean flashlightOn) {
      this.flashlightOn = flashlightOn;
   }

   public int getFlashlightPointNum() {
      return (flashlightBeam.npoints);
   }

   public int[] getFlashlightPointX() {
      return (flashlightBeam.xpoints);
   }

   public int[] getFlashlightPointY() {
      return (flashlightBeam.ypoints);
   }

   public void autoAttack() {
      if (!stunned && (autoAttackTimer <= 0)) {
         if (!melee) {
            projectiles.add(new AutoProjectile(((int) (xy[0])), ((int) (xy[1])), mouseX, mouseY, autoSpeed, range));
            autoAttackTimer = autoAttackCooldown;
         } else {
            addAOE(new AutoAOE(((int) (xy[0])), ((int) (xy[1])), mouseX, mouseY, range));
            autoAttackTimer = autoAttackCooldown;
         }
      }
   }

   public void flare() {
      if (!stunned && (flareTimer <= 0)) {
         projectiles.add(new FlareProjectile(((int) (xy[0])), ((int) (xy[1])), mouseX, mouseY));
         flareTimer = flareCooldown;
      }
   }

   public void launch(int targetX, int targetY, int speed, int range) {
      double theta = Math.atan2(targetY - xy[1], targetX - xy[0]);
      double dx = speed * Math.cos(theta);
      double dy = speed * Math.sin(theta);
      int totalTime = (int) Math.round(range * 1.0 / speed);
      statuses.add(new Launched(dx, dy, totalTime));
      statuses.add(new Stun(totalTime, -1));
   }

   public void moveTo(int targetX, int targetY, int speed) {
      double range = Math.sqrt(Math.pow(targetX - xy[0], 2) + Math.pow(targetY - xy[1], 2));
      double theta = Math.atan2(targetY - xy[1], targetX - xy[0]);
      double dx = speed * Math.cos(theta);
      double dy = speed * Math.sin(theta);
      int totalTime = (int) Math.round(range * 1.0 / speed);
      statuses.add(new Launched(dx, dy, totalTime));
      statuses.add(new Stun(totalTime, -1));
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
      if (health <= 0) {
         addStatus(new Dead());
      }
   }

   public void setHasArtifact(boolean hasArtifact) {
      this.hasArtifact = hasArtifact;
   }

   public boolean getHasArtifact() {
      return hasArtifact;
   }

   public static void setArtifacts(Region region, int artifactNum) {
      artifacts[artifactNum] = new Artifact((region.getMidXy()[0]/2 + 7500), (region.getMidXy()[1]/2 + 5000), artifactNum);
   }

   public static Artifact getArtifacts(int teamN) {
      return (artifacts[teamN]);
   }

   public void setSpawn(Region region) {
      //Random radius and random angle
      double radius = 100 + Math.random() * 25;
      double angle = Math.random() * Math.PI;
      //Set spawn according to random numbers
      spawnX = (region.getMidXy()[0]/2 + 7500) + (int) (radius * Math.cos(angle));
      spawnY = (region.getMidXy()[1]/2 + 5000) + (int) (radius * Math.sin(angle));
      xy[0]=spawnX;
      xy[1] = spawnY;
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


   /*
   public int getAutoAttackTimer(){
     return autoAttackTimer;
   }*/
   public void setAutoAttackCooldown(int cooldown) {
      autoAttackCooldown = cooldown;
   }

   public void updateBasicTimers() {
      if (autoAttackTimer > 0) {
         autoAttackTimer--;
      }
      if (flareTimer > 0) {
         flareTimer--;
      }
   }

   /*
   public int getFlareTimer(){
     return flareTimer;
   }*/
   public void setFlareCooldown(int cooldown) {
      flareCooldown = cooldown;
   }

   public void updateStatuses() {
      mobilityBoost = 0;
      buffBlacklist.clear();
      illuminated = false;
      stunned = false;
      invisible = false;
      uncollidable = false;
      walking = false;
      damageReduction = 0;
      if (hasArtifact) {
         statuses.add(new Illuminated(2));
      }
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
            } else if (removed instanceof Dead) {
               setX(spawnX);
               setY(spawnY);
               setHealth(maxHealth);
               //Add here
               getArtifacts(getTeam()).setPickedUp(false);
               setHasArtifact(false);
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
      } else if (status instanceof Uncollidable){
        uncollidable = true;
      }else if (status instanceof GhostE) {
         ((GhostE) status).setProjectedX(xy[0]);
         ((GhostE) status).setProjectedY(xy[1]);
      } else if (status instanceof ReduceDamage) {
         if (((ReduceDamage) status).getDamageReduction() > damageReduction) {
            damageReduction = ((ReduceDamage) status).getDamageReduction();
         }
      }
   }

   public Area getHitbox() {
      hitbox.setLocation(((int) (xy[0] - WIDTH / 2)), ((int) (xy[1] - HEIGHT / 2)));
      if (!uncollidable){
        return new Area(hitbox);
      } else {
        return new Area();
      }
   }

   public Rectangle getHitboxRectangle() {
      hitbox.setLocation(((int) (xy[0] - WIDTH / 2)), ((int) (xy[1] - HEIGHT / 2)));
      return hitbox;
   }

   public Rectangle getAdjustedHitboxRectangle(double x, double y) {
      hitbox.setLocation(((int) (xy[0] - WIDTH / 2 + x)), ((int) (xy[1] - HEIGHT / 2 + y)));
      return hitbox;
   }

   public boolean hitObstacle(Projectile projectile) {
      for (int k = 0; k < obstacles.length; k++) {
         if (projectile.collides(obstacles[k])) {
            return true;
         }
      }
      return false;
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
      if (i<projectiles.size()) {
         return projectiles.get(i);
      } else {
         return null;
      }
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

   public ArrayList<Status> getAllStatuses() {
      return statuses;
   }

   public Status getStatus(int i) {
      if (i < statuses.size()) {
         return statuses.get(i);
      } else {
         return null;
      }
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

   public void setMelee(boolean melee) {
      this.melee = melee;
   }
}

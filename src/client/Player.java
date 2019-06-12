package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 * User.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public abstract class Player extends User {
   //Constants
   private int ID;
   private int[] xy = {300, 300};
   private int[] centerXy = new int[2];

   private int desiredSpell = -1;
   private int[] spellPercent = {100, 100, 100};
   private ArrayList<Status> statuses = new ArrayList<Status>();
   private int gold = 0;
   private boolean artifact;
   private int level = 0;
   private int maxHealth;
   private int health;
   private int attack;
   private int mobility = 20;
   private int range;
   private boolean damaged;
   private int spriteID;
   private double flashlightAngle;
   private boolean flashlightOn;
   private double ROOT2O2 = 0.70710678118;
   private Polygon flashlightBeam = new Polygon();
   private boolean translated;
   
   private boolean illuminated;
   
   private boolean dead;
   private boolean invisible;
   private boolean uncollidable;


   Player(String username) {
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
   
   public int getX(){
     return xy[0];
   }
   public int getY(){
     return xy[1];
   }

   public void setXy(int x, int y) {
      this.xy[0] = x;
      this.xy[1] = y;
   }

   public void draw(Graphics2D g2, int[] midXy) {
      drawReal(g2, centerXy[0] + (int) ((xy[0] - midXy[0])) - (int) (120 / 2), centerXy[1] + (int) ((xy[1] - midXy[1])) - (int) (120) / 2, (int) (120), (int) (120), desiredSpell);
      if (desiredSpell != -1) {
         desiredSpell = -1;
      }
   }

  /* public void drawFlashlight(Graphics2D g2, int [] xyAdjust) {
      if (flashlightOn) {
         g2.setColor(new Color (1f,1f,0.4f,0.1f));
         if (!translated) {
            flashlightBeam.translate(xyAdjust[0], xyAdjust[1]);
            translated = true;
         }
         g2.fillPolygon(flashlightBeam);
      }
   }*/
   public void translateFlashlight(int[]xyAdjust){
      if (!translated) {
         flashlightBeam.translate(xyAdjust[0], xyAdjust[1]);
         translated = true;
      }
   }

   public double[] getDisp(int angleOfMovement) {
      double[] displacements = new double[2];
      if (angleOfMovement == 0) {
         displacements[0] = mobility;
         displacements[1] = 0;
      } else if (Math.abs(angleOfMovement) == 1) {
         displacements[0] = ROOT2O2 * mobility;
         displacements[1] = ROOT2O2 * mobility;
      } else if (Math.abs(angleOfMovement) == 2) {
         displacements[0] = 0;
         displacements[1] = mobility;
      } else if (Math.abs(angleOfMovement) == 3) {
         displacements[0] = -ROOT2O2 * mobility ;
         displacements[1] = ROOT2O2 * mobility;
      } else {
         displacements[0] = -mobility ;
         displacements[1] = 0;
      }
      if (angleOfMovement < 0) {
         displacements[1] = -displacements[1];
      }
      return (displacements);
   }

   public void setSpell(int spellIndex) {
      if (spellIndex != -1) {
         this.desiredSpell = spellIndex;
      }
   }


   public void setFlashlightOn(boolean flashlightOn) {
      flashlightBeam.reset();
      this.flashlightOn = flashlightOn;
      translated = false;
   }

   public boolean getFlashlightOn() {
      return flashlightOn;
   }

   public Polygon getFlashlightBeam(){
      return flashlightBeam;
   }

   public void setFlashlightPoint(int x, int y) {
      flashlightBeam.addPoint((int) (x), (int) (y ));
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

   //Statuses
   public ArrayList<Status> getStatuses(){
     return statuses;
   }
   public void addStatus(Status status) {
     statuses.add(status);
   }
   public void clearStatuses(){
     statuses.clear();
     dead = false;
     //illuminated = false;
     invisible = false;
     uncollidable = false;
   }
   
   public boolean isDead(){
     return dead;
   }
   public boolean isInvisible(){
     return invisible;
   }
   public boolean isUncollidable(){
     return uncollidable;
   }
   
   public void setDead(boolean state){
     dead = state;
   }
   public void setInvisible(boolean state){
     invisible = state;
   }
   public void setUncollidable(boolean state){
     uncollidable = state;
   }

   public void setArtifact(boolean artifact) {
      this.artifact = artifact;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getGold() {
      return gold;
   }

   public int getLevel() {
      return level;
   }

   public boolean getIlluminated() {
      return illuminated;
   }

   public void setIlluminated(boolean illuminated) {
      this.illuminated = illuminated;
   }

   public abstract void spellAnimation(Graphics2D g2, int x, int y, int width, int height, int spellIndex);

   //public abstract void spell2(Graphics2D g2, int x, int y, int width, int height);
   //  public abstract void spell3(Graphics2D g2, int x, int y, int width, int height);
   public abstract void move(Graphics2D g2, int x, int y, int width, int height);

   public abstract void drawReal(Graphics2D g2, int x, int y, int width, int height, int spellIndex);

   public abstract void setMovementIndex(int positionIndex, boolean moving);

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

   public void setSpriteID(int spriteID) {
      this.spriteID = spriteID;
   }

   public void setDamaged(boolean damaged) {
      this.damaged = damaged;
   }
}

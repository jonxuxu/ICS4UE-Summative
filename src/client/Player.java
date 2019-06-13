package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.awt.AlphaComposite;

/**
 * Player.java
 * This is the player superclass for each player of the game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-04-24
 */

public abstract class Player extends User {
   //Constants
   private int ID;
   private int[] xy = {300, 300};
   private int[] centerXy = new int[2];
   private static int PLAYER_LENGTH = 120;


   private int desiredSpell = -1;
   private int[] spellPercent = {100, 100, 100};
   private ArrayList<Status> statuses = new ArrayList<Status>();
   private int gold = 0;
   private boolean artifact;
   private int level = 0;
   private int health, maxHealth;
   private int attack, range;
   private int mobility = 20;
   private boolean damaged;
   private int spriteID;
   private double flashlightAngle;
   private boolean flashlightOn;
   private double ROOT2O2 = 0.70710678118;
   private Polygon flashlightBeam = new Polygon();
   private boolean translated;
   private CustomMouseAdapter myMouse;

   private boolean illuminated;

   private boolean dead;
   private boolean invisible;
   private boolean uncollidable;


   Player(String username, CustomMouseAdapter myMouse) {
      super(username);
      this.myMouse = myMouse;

   }

   public boolean getMouse() {
       if (myMouse.getState(2) == 1) {
           return true;
       } else {
           return false;
       }
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
   
   public int getPlayerLength(){
     return PLAYER_LENGTH;
   }

   public void draw(Graphics2D g2, Player mainPlayer) {
     int[] playerXy = mainPlayer.getXy();
     if (invisible){
       float alpha = (float)0.5; //draw half transparent
       g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
     }
     if(damaged){
       g2.setXORMode(new Color(255, 0, 0, 0));
     }
     drawReal(g2, centerXy[0] + (int) ((xy[0] - playerXy[0])) - (int) (PLAYER_LENGTH / 2), centerXy[1] + (int) ((xy[1] - playerXy[1])) - (int) (PLAYER_LENGTH) / 2, (int) (PLAYER_LENGTH), (int) (PLAYER_LENGTH), desiredSpell);
     if(damaged){
       g2.setPaintMode();
     }
     if(invisible){
       g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)1));
     }

     if (desiredSpell != -1) {
       desiredSpell = -1;
     }
     // Draws status effects
        
     // Draws health bars
     g2.setColor(Color.white);
     g2.fillRect(centerXy[0] + xy[0] - playerXy[0] - 76, centerXy[1] + xy[1] - playerXy[1] - 71, 152, 8);
     g2.setColor(Color.black);
     g2.fillRect(centerXy[0] + xy[0] - playerXy[0] - 75, centerXy[1] + xy[1] - playerXy[1] - 70, 150, 6);
     g2.setColor(Color.red);
     if (maxHealth!=0) {
        g2.fillRect(centerXy[0] + xy[0] - playerXy[0] - 75, centerXy[1] + xy[1] - playerXy[1] - 70, 150 * health / maxHealth, 6);
     }
     // Draws name
     if(getTeam() == mainPlayer.getTeam()){
       g2.setColor(Color.green);
     } else {
       g2.setColor(Color.red);
     }
     g2.drawString(getUsername(), centerXy[0] + xy[0] - playerXy[0] - g2.getFontMetrics().stringWidth(getUsername())/2, centerXy[1] + xy[1] - playerXy[1] - 80);
     
     if (dead){
       float alpha = (float)0.5; //draw half transparent
       g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
       g2.setColor(Color.BLACK);
       g2.fillRect(0,0,1600,900);
       g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)1));
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

   public int getSpellPercent(int spellIndex) {
      return spellPercent[spellIndex];
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

   public boolean getArtifact() {
      return artifact;
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

   public abstract void setAttackIndex(int attackIndex, boolean attacking);

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

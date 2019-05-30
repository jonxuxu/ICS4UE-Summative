package server;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 * User.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public abstract class Player extends User implements CanIntersect{
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
   private int mouseX;
   private int mouseY;

   //May 25//
   private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
   private ArrayList<AOE> aoes = new ArrayList<AOE>();
   private ArrayList<Status> statuses = new ArrayList<Status>();
   private ArrayList<Shield> shields = new ArrayList<Shield>();
   private int team;
   private ArrayList<Player> allies = new ArrayList<Player>();
   private ArrayList<Player> enemies = new ArrayList<Player>();
   private int x,y = 300;
   private int autoSpeed =1;//TEMP, ASK KAMRON
   private int autoRange =1;
   private Rectangle hitbox = new Rectangle(x,y,50,50);
   private boolean illuminated = false;
   private boolean stunned = false;

   Player(String username) {
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
      outputString.append(getSpellPercent(0) + "," + getSpellPercent(1) + "," + getSpellPercent(2) + ",");//Spells
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
   //May 25
   public void sendInfo(Player[] gamePlayers){
      for (int i = 0; i < gamePlayers.length; i++){
         if (gamePlayers[i].getTeam() == this.team){
            allies.add(gamePlayers[i]);
         } else {
            enemies.add(gamePlayers[i]);
         }
      }
   }
   public void autoAttack(int mouseX, int mouseY){
      if (!stunned){
         projectiles.add(new AutoProjectile(x,y,mouseX,mouseY,autoSpeed,autoRange));
      }
   }
   public void flare(int mouseX, int mouseY){
      if (!stunned){
         projectiles.add(new FlareProjectile(x,y,mouseX,mouseY));
      }
   }
   public void move(double angle){
      if (!stunned){
         x += mobility * Math.cos(angle);
         y += mobility * Math.sin(angle);
      }
   }
   
   public abstract void update();

   public void damage(int damage){
      if (shields.isEmpty()){
         health -= damage;
      } else {
         if (shields.get(0).getStrength() - damage > 0){
            shields.get(0).damage(damage);
         } else {
            health -= damage - shields.get(0).getStrength();
            shields.remove(0);
         }
      }
   }

   public void updateStatuses(){
      for (int i = statuses.size()-1; i >= 0; i--){
         illuminated = false;
         statuses.get(i).advance();
         Status removed = null;
         if (statuses.get(i).getRemainingDuration() <= 0){
            removed = statuses.get(i);
         } else {
            if (statuses.get(i) instanceof Illuminated){
               illuminated = true;//NOTE REE ILLUMINATED ALWAYS TRUE
            } else if (statuses.get(i) instanceof MSBuff){
               mobility += ((MSBuff)(statuses.get(i))).getStrength();
            } else if (statuses.get(i) instanceof Stun){
               stunned = true;
            }
         }
      }
   }

   public Area getHitbox(){
      hitbox.setLocation(x,y);
      return new Area(hitbox);
   }

   public int getTeam(){
      return team;
   }
   public void setTeam(int team){
      this.team = team;
   }
   public int getX(){
      return x;
   }
   public int getY(){
      return y;
   }
   public void setX(int x){
      this.x = x;
   }
   public void setY(int y){
      this.y = y;
   }
   public int getMouseX(){
      return mouseX;
   }
   public int getMouseY(){
      return mouseY;
   }

   public boolean getStunned(){
      return stunned;
   }
   public boolean getIlluminated(){
      return illuminated;
   }
  /*
   public void setIlluminated(boolean illuminated){
   this.illuminated = illuminated;
   }*/

   public Projectile getProjectile(int i){
      return projectiles.get(i);
   }
   public void addProjectile(Projectile projectile){
      projectiles.add(projectile);
   }
   public Projectile removeProjectile(int i){
      return projectiles.remove(i);
   }
   public int getProjectilesSize(){
      return projectiles.size();
   }

   public AOE getAOE(int i){
      return aoes.get(i);
   }
   public void addAOE(AOE aoe){
      aoes.add(aoe);
   }
   public AOE removeAOE(int i){
      return aoes.remove(i);
   }
   public int getAOESize(){
      return aoes.size();
   }

   public Status getStatus(int i){
      return statuses.get(i);
   }
   public void addStatus(Status status){
      statuses.add(status);
   }
   public Status removeStatus(int i){
      return statuses.remove(i);
   }
   public int getStatusesSize(){
      return statuses.size();
   }

   public Shield getShield(int i){
      return shields.get(i);
   }
   public void addShield(Shield shield){
      shields.add(shield);
   }
   public Shield removeShield(int i){
      return shields.remove(i);
   }
   public int getShieldsSize(){
      return shields.size();
   }

   public Player getEnemy(int i){
      return enemies.get(i);
   }
   public int getEnemiesSize(){
      return enemies.size();
   }

   public Player getAlly(int i){
      return allies.get(i);
   }
   public int getAlliesSize(){
      return allies.size();
   }
   //////////////////////////////////////////////////////////////////////////
   public abstract boolean testSpell(int spellIndex);

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

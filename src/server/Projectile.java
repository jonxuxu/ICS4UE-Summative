package server;

import java.awt.Rectangle;

/**
 * Projectile.java
 * This is the projectile class for projectiles of the game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-04-24
 */
class Projectile implements HasID{
   private int spawnX, spawnY;
   private int targetX, targetY;
   private int speed;
   private int range;
   private double theta;
   private double dx;
   private double dy;
   private int lifetime;
   private double x, y;
   private int ID;
   private Rectangle hitbox;
   private int totalTime;

   /**
    * Class Constructor
    * @param spawnX
    * @param spawnY
    * @param targetX
    * @param targetY
    * @param speed
    * @param range
    * @param ID
    */
   Projectile(int spawnX, int spawnY, int targetX, int targetY, int speed, int range, int ID) {
      this.spawnX = spawnX;
      this.spawnY = spawnY;
      this.targetX = targetX;
      this.targetY = targetY;
      this.speed = speed;
      this.range = range;
      x = spawnX;
      y = spawnY;
      hitbox = new Rectangle((int)x,(int)y,10,10);//10 is abritrary
      this.ID=ID;

      //This could cause problems if trajectory is later updated
      //Also, since y is down, the angle might be messed up

      theta = Math.atan2((targetY - spawnY), (targetX - spawnX));
      dx = speed * Math.cos(theta);
      dy = speed * Math.sin(theta);
          totalTime = (int)Math.round(range*1.0/speed);
   }

   /**
    * method to move the projectile
    */
   public void advance() {
      lifetime++;
      x += dx;
      y += dy;
      hitbox.setLocation((int)x, (int)y);
   }

   /**
    * Getter for the remaining life of the projectile
    * @return int of the time left
    */
   public int getRemainingDuration() {
      return totalTime - lifetime;
   }

   /**
    * check method to see if the projectile has collided
    * @param object
    * @return
    */
   public boolean collides(CanIntersect object) {
      return object.getHitbox().intersects(hitbox);
   }

   /**
    * Getter for the x position
    * @return
    */
   public int getX(){
     return (int)x;
   }

   /**
    * Setter for the y position
    * @return
    */
   public int getY(){
     return (int)y;
   }

   /**
    * Getter for the ID
    * @return
    */
   public int getID() {
      return (ID);
   }

   /**
    * Setter for the ID
    * @param ID
    */
   public void setID(int ID) {
    this.ID = ID;
  }

}
package server;

import java.awt.Rectangle;

class Projectile implements HasID{
   private int spawnX, spawnY;
   private int targetX, targetY;
   private int speed;
   private int range;
   private double theta;
   private double dx;
   private double dy;
   private int lifetime;
   private int x, y;
   private int ID;
   private Rectangle hitbox;

   Projectile(int spawnX, int spawnY, int targetX, int targetY, int speed, int range, int ID) {
      this.spawnX = spawnX;
      this.spawnY = spawnY;
      this.targetX = targetX;
      this.targetY = targetY;
      this.speed = speed;
      this.range = range;
      x = spawnX;
      y = spawnY;
      hitbox = new Rectangle(x, y, 10, 10);//10 is arbitrary
      this.ID=ID;

      //This could cause problems if trajectory is later updated
      //Also, since y is down, the angle might be messed up

      theta = Math.atan2((targetY - spawnY), (targetX - spawnX));
      dx = speed * Math.cos(theta);
      dy = speed * Math.sin(theta);
   }

   public void advance() {
      lifetime++;
      x += dx;
      y += dy;
      hitbox.setLocation(x, y);
   }

   public int getRemainingDuration() {
      return range / speed - lifetime;
   }

   public boolean collides(CanIntersect object) {
      return object.getHitbox().intersects(hitbox);
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public int getID() {
      return (ID);
   }

}
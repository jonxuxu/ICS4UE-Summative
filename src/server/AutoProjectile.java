package server;

/**
 * AutoProjectile.java
 *
 * A class containing the data for the AutoProjectile, an extension of the regular projectile
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-30
 * @version 1.0
 */

class AutoProjectile extends Projectile {
   private static int ID = 1;

   /**
    * Constructor for the object, sending all the values up to the Projectile superclass
    *
    * @param spawnX the x-value of the projectile's origin position
    * @param spawnY the y-value of the projectile's origin position
    * @param targetX the x-value of the projectile's target position
    * @param targetY the y-value of the projectile's target position
    * @param speed the speed of the projectile
    * @param range the range of the projectile
    */

   AutoProjectile(int spawnX, int spawnY, int targetX, int targetY, int speed, int range) {
      super(spawnX, spawnY, targetX, targetY, speed, range, ID);
   }
}
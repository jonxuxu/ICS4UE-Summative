package server;

/**
 * FlareProjectile.java
 *
 * A class dealing with the specific case of the flare projectile, containing constants for it
 */
class FlareProjectile extends Projectile{
  // Constanst for flare projectile case
  private static int SPEED = 10;//TEMP
  private static int RANGE = 500;//TEMP
  private static int ID = 0;

  /**
   * Basic constructor, sending all local constants up to the Projectile superclass
   *
   * @param spawnX the x-value of the projectile's origin position
   * @param spawnY the y-value of the projectile's origin position
   * @param targetX the x-value of the projectile's target position
   * @param targetY the y-value of the projectile's target position
   */

  FlareProjectile(int spawnX, int spawnY, int targetX, int targetY){
    super(spawnX, spawnY, targetX, targetY, SPEED, RANGE,ID);
  }
}
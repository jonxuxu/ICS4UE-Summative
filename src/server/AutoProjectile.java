package server;

class AutoProjectile extends Projectile {
   private static int ID = 1;

   AutoProjectile(int spawnX, int spawnY, int targetX, int targetY, int speed, int range) {
      super(spawnX, spawnY, targetX, targetY, speed, range, ID);
   }
}
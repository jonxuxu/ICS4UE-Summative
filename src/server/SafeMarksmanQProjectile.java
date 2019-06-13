package server;
class SafeMarksmanQProjectile extends Projectile{
  private static int SPEED = 10;
  private static int RANGE = 300;
  private static int ID = 2;
  SafeMarksmanQProjectile(int spawnX, int spawnY, int targetX, int targetY){
    super(spawnX, spawnY, targetX, targetY, SPEED, RANGE,ID);
  }
}
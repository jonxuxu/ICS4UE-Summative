package server;
class SafeMarksmanQProjectile extends Projectile{
  private static int SPEED = 50;
  private static int RANGE = 300;
  SafeMarksmanQProjectile(int spawnX, int spawnY, int targetX, int targetY){
    super(spawnX, spawnY, targetX, targetY, SPEED, RANGE);
    setID(2);
  }
}
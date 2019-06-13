package server;
class FlareProjectile extends Projectile{
  private static int SPEED = 50;//TEMP
  private static int RANGE = 450;//TEMP
  private static int ID = 0;
  FlareProjectile(int spawnX, int spawnY, int targetX, int targetY){
    super(spawnX, spawnY, targetX, targetY, SPEED, RANGE,ID);
  }
}
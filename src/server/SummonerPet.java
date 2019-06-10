package server;
class SummonerPet extends AOE{
  private static int DURATION = 1000;//IDK
  private static int RADIUS = 50;
  private static int ID = 12;
  private static int SPEED = 50;
  double dx,dy;
  int moveTimer;
  int moveTicks;
  int attackTimer;
  int originalX, originalY;
  SummonerPet(int x, int y){
    super(x,y,DURATION,RADIUS, ID);
    originalX = x;
    originalY = y;
  }
  
  public void moveTo(int targetX, int targetY){
     double range = Math.sqrt(Math.pow(targetX-getX(),2) + Math.pow(targetY-getY(),2));
     double theta = Math.atan2(targetY - getY(), targetX - getX());
     dx = SPEED * Math.cos(theta);
     dy = SPEED * Math.sin(theta);
     moveTimer = (int) Math.round(range * 1.0 / SPEED);
     moveTicks = 0;
     originalX = getX();
     originalY = getY();
   }
  
  @Override
  public void advance() {
    super.advance();
    if (moveTimer > 0){
      moveTimer--;
      moveTicks++;
      setX((int)(originalX + dx * moveTicks));
      setY((int)(originalY + dy * moveTicks));
    }
    if (attackTimer > 0){
      attackTimer--;
    }
  }
  
  public int getAttackTimer(){
    return attackTimer;
  }
  public void setAttackTimer(int timer){
    attackTimer = timer;
  }
}
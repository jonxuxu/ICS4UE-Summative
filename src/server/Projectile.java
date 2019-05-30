package server;
import java.awt.Rectangle;
class Projectile{
  private int spawnX, spawnY;
  private int targetX, targetY;
  private int speed;
  private int range;
  private double theta;
  private double dx;
  private double dy;
  private int lifetime;
  private int x,y;
  private Rectangle hitbox;
  
  Projectile(int spawnX, int spawnY, int targetX, int targetY, int speed, int range){
    this.spawnX = spawnX;
    this.spawnY = spawnY;
    this.targetX = targetX;
    this.targetY = targetY;
    this.speed = speed;
    System.out.println(speed);
    this.range = range;
    x = spawnX;
    y = spawnY;
    hitbox = new Rectangle(x,y,10,10);//10 is abritrary
    
    //This could cause problems if tragectory is later updated
    //Also, since y is down, the angle might be messed up
    theta = Math.atan((targetY-spawnY)*1.0/(targetX-spawnX));
    dx = speed * Math.cos(theta);
    dy = speed * Math.sin(theta);
  }
  public void advance(){
    lifetime++;
    x+=dx;
    y+=dy;
    hitbox.setLocation(x, y);
  }
  public int getRemainingDuration(){
    return range/speed-lifetime;
  }
  public boolean collides(CanIntersect object){
    return object.getHitbox().intersects(hitbox);
  }
  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
}
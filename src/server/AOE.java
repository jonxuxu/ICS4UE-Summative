package server;

import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;
class AOE{
  int x,y;
  int duration;
  int radius;
  int lifetime;
  Ellipse2D hitbox;
  AOE(int x, int y, int duration, int radius){
    this.x = x;
    this.y = y;
    this.duration = duration;
    this.radius = radius;
    hitbox = new Ellipse2D.Double(x,y,radius*2,radius*2);
  }
  
  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
  
  public void advance(){
    lifetime++;
  }
  
  public int getRemainingDuration(){
    return duration-lifetime;
  }
  
  public boolean collides(CanIntersect object){
    Area collisionArea = new Area(object.getHitbox());
    collisionArea.intersect(new Area(hitbox));
    return !(collisionArea.isEmpty());
  }
}
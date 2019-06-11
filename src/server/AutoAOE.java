package server;
import java.awt.Polygon;
import java.awt.geom.Area;
class AutoAOE extends AOE{
  
  private int spawnX, spawnY, targetX, targetY;
  private static int DURATION = 2;
  //private static int WIDTH = 100;
  //private static int HEIGHT = 500;
  private static int ID = 14;
  private Polygon hitbox;
  private int[][] points = new int[2][4];
  AutoAOE(int spawnX, int spawnY, int targetX, int targetY, int range){
    super(spawnX,spawnY,DURATION,range,ID);//These things don't matter except for duration
    this.spawnX = spawnX;
    this.spawnY = spawnY;
    this.targetX = targetX;
    this.targetY = targetY;
    double theta = Math.atan2((targetY-spawnY),(targetX-spawnX));
    double endX = (spawnX + range * Math.cos(theta));
    double endY = (spawnY + range * Math.sin(theta));
    points[0][0] = (int)(spawnX+range/2.0*Math.sin(theta));
    points[1][0] = (int)(spawnY-range/2.0*Math.cos(theta));
    
    points[0][1] = (int)(spawnX-range/2.0*Math.sin(theta));
    points[1][1] = (int)(spawnY+range/2.0*Math.cos(theta));
    
    points[0][3] = (int)(endX+range/2.0*Math.sin(theta));
    points[1][3] = (int)(endY-range/2.0*Math.cos(theta));
    
    points[0][2] = (int)(endX-range/2.0*Math.sin(theta));
    points[1][2] = (int)(endY+range/2.0*Math.cos(theta));
    
    hitbox = new Polygon(points[0],points[1],4);
  }
  
  @Override
  public boolean collides(CanIntersect object){
    Area collisionArea = new Area(object.getHitbox());
    collisionArea.intersect(new Area(hitbox));
    return !(collisionArea.isEmpty());
  }
  
  public int[][] getPoints(){
    return points;
  }
}
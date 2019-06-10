package server;
import java.awt.Polygon;
import java.awt.geom.Area;
class TimeMageQAOE extends AOE{
  
  private int spawnX, spawnY, targetX, targetY;
  private static int DURATION = 100;
  private static int WIDTH = 100;
  private static int HEIGHT = 500;
  private static int ID = 4;
  private int[][] points = new int[2][4];
  private Polygon hitbox;
  TimeMageQAOE(int spawnX, int spawnY, int targetX, int targetY){
    super(spawnX,spawnY,DURATION,0,ID);//These things don't matter except for duration
    this.spawnX = spawnX;
    this.spawnY = spawnY;
    this.targetX = targetX;
    this.targetY = targetY;
    double theta = Math.atan2((targetY-spawnY),(targetX-spawnX));
    //double endX = targetX - (HEIGHT-Math.sqrt(Math.pow(targetX-spawnX,2)+Math.pow(targetY-spawnY,2))) * Math.cos(theta);
    //double endY = targetY - (HEIGHT-Math.sqrt(Math.pow(targetX-spawnX,2)+Math.pow(targetY-spawnY,2))) * Math.sin(theta);
    double endX = (spawnX + HEIGHT * Math.cos(theta));
    double endY = (spawnY + HEIGHT * Math.sin(theta));
    points[0][0] = (int)(spawnX+WIDTH/2.0*Math.sin(theta));
    points[1][0] = (int)(spawnY-WIDTH/2.0*Math.cos(theta));
    
    points[0][1] = (int)(spawnX-WIDTH/2.0*Math.sin(theta));
    points[1][1] = (int)(spawnY+WIDTH/2.0*Math.cos(theta));
    
    points[0][3] = (int)(endX+WIDTH/2.0*Math.sin(theta));
    points[1][3] = (int)(endY-WIDTH/2.0*Math.cos(theta));
    
    points[0][2] = (int)(endX-WIDTH/2.0*Math.sin(theta));
    points[1][2] = (int)(endY+WIDTH/2.0*Math.cos(theta));
    
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
package server;
import java.awt.Polygon;
import java.awt.geom.Area;
class TimeMageQAOE extends AOE{
  
  private int spawnX, spawnY, targetX, targetY;
  private static int DURATION = 100;
  private static int WIDTH = 100;
  private static int HEIGHT = 500;
  private static int ID = 4;
  private Polygon hitbox;
  TimeMageQAOE(int spawnX, int spawnY, int targetX, int targetY){
    super(spawnX,spawnY,DURATION,0,ID);//These things don't matter except for duration
    this.spawnX = spawnX;
    this.spawnY = spawnY;
    this.targetX = targetX;
    this.targetY = targetY;
    double theta = Math.atan((targetY-spawnY)*1.0/(targetX-spawnX));
    double endX = targetX - (HEIGHT-Math.sqrt(Math.pow(targetX-spawnX,2)+Math.pow(targetY-spawnY,2))) * Math.cos(theta);
    double endY = targetY - (HEIGHT-Math.sqrt(Math.pow(targetX-spawnX,2)+Math.pow(targetY-spawnY,2))) * Math.sin(theta);
    int[] xpoints = new int[4];
    int[] ypoints = new int[4];
    xpoints[0] = (int)(spawnX+WIDTH/2.0*Math.sin(theta));
    ypoints[0] = (int)(spawnY-WIDTH/2.0*Math.cos(theta));
    
    xpoints[1] = (int)(spawnX-WIDTH/2.0*Math.sin(theta));
    ypoints[1] = (int)(spawnY+WIDTH/2.0*Math.cos(theta));
    
    xpoints[2] = (int)(endX+WIDTH/2.0*Math.sin(theta));
    ypoints[2] = (int)(endY-WIDTH/2.0*Math.cos(theta));
    
    xpoints[3] = (int)(endX-WIDTH/2.0*Math.sin(theta));
    ypoints[3] = (int)(endY+WIDTH/2.0*Math.cos(theta));
    
    hitbox = new Polygon(xpoints,ypoints,4);
  }
  
  @Override
  public boolean collides(CanIntersect object){
    Area collisionArea = new Area(object.getHitbox());
    collisionArea.intersect(new Area(hitbox));
    return !(collisionArea.isEmpty());
  }
}
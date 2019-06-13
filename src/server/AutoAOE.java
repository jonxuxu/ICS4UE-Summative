package server;
import java.awt.Polygon;
import java.awt.geom.Area;

/**
 * AutoAOE.java
 *
 * A class extending the general AOE class, designated for square auto-attacks
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.1
 *
 */
class AutoAOE extends AOE{

  private int spawnX, spawnY, targetX, targetY;
  private static int DURATION = 2;
  //private static int WIDTH = 100;
  //private static int HEIGHT = 500;
  private static int ID = 14;
  private Polygon hitbox;
  private int[][] points = new int[2][4];

  /**
   * Basic constructor that initializes and calculates the attacks's values
   *
   * @param spawnX the x-value of origin of the attack
   * @param spawnY the y-value of origin of the attack
   * @param targetX the x-value to which the attack extends to
   * @param targetY the y-value to which the attack extends to
   * @param range the range of the attack
   */
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

  /**
   * Performs a check whether an potentially affected object intersects the attacks's hitbox,
   * overriding the general AOE's method
   *
   * @param object the object Implementing the CanIntersect interface
   *               that is being checked for an intersection with the AOE
   * @return a boolean on whether the target object is intersecting the attack'ss hitbox or not
   */

  public boolean collides(CanIntersect object){
    Area collisionArea = new Area(object.getHitbox());
    collisionArea.intersect(new Area(hitbox));
    return !(collisionArea.isEmpty());
  }

  /**
   * Basic getter for the vertex points of the hitbox, as a 2-D integer array
   *
   * @return the 2-d integer array containing the vertex x,y values
   */
  public int[][] getPoints(){
    return points;
  }
}
package server;

/**
 * GhostE.java
 *
 * This is the class representing the status storing the GhostE spell cast info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class GhostE extends Status{
  private static int SPEED = 20;
  private static int RANGE = 300;
  private static int TOTAL_TIME = RANGE/SPEED;
  private static int ID = 2;
  double dx, dy;
  double totalDX, totalDY;
  double projectedX, projectedY;
  /**
   * Constructor to create a new GhostE
   * @param spawnX the x coordinate from which the aoe was cast
   * @param spawnY the y coordinate from which the aoe was cast
   * @param targetX the x coordinate where the mouse was when cast
   * @param targetY the y coordinate where the mouse was when cast
   */
  GhostE(int spawnX, int spawnY, int targetX, int targetY){
    super(9999,ID);//Essentially infinite
    double theta = Math.atan2(targetY - spawnY, targetX - spawnX);
    double h = RANGE*Math.sin(theta);
    double w = RANGE*Math.cos(theta);
    dx = w/TOTAL_TIME;
    dy = h/TOTAL_TIME;
  }
  
  /**
   * Advances location each frame
   */
  @Override
  public void advance(){
    lifetime++;
    if (lifetime < TOTAL_TIME){
      totalDX+=dx;
      totalDY+=dy;
    }
  }
  
  public void setProjectedX(double playerX){
    projectedX = playerX + totalDX;
  }
  public void setProjectedY(double playerY){
    projectedY = playerY + totalDY;
  }
  
  public int getX(){
    return (int)projectedX;
  }
  public int getY(){
    return (int)projectedY;
  }
}
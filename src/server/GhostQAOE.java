package server;

class GhostQAOE extends AOE{
  private static int RADIUS = 50;
  private static int ID = 5;//?
  /**
   * Constructor to create a new GhostQAOE
   * @param x x coordinate of the AOE
   * @param y y coordinate of the AOE
   * @param duration duration the AOE lasts
   */
  GhostQAOE(int x, int y, int duration){
    super(x,y,duration,RADIUS,ID);
  }
  
  /**
   * Changes x coordinate
   * @param x x coordinate
   */
  public void setX(int x){
    this.x = x;
  }
  /**
   * Changes y coordinate
   * @param y y coordinate
   */
  public void setY(int y){
    this.y = y;
  }
}
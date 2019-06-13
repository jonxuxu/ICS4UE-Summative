package server;
/**
 * JuggernautQAOE.java
 *
 * This is the class representing the status storing the JuggernautQAOE info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class JuggernautQAOE extends AOE{
  private static int RADIUS = 50;
  private static int ID = 10;
  /**
   * Constructor to create a new JuggernautQAOE
   * @param x x coordinate of the AOE
   * @param y y coordinate of the AOE
   */
  JuggernautQAOE(int x, int y, int duration){
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
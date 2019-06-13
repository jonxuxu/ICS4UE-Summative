package server;
/**
 * TimeMageE.java
 *
 * This is the class representing the status storing the TimeMageE spell cast info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class TimeMageE extends Status{
  private int x,y;
  private static int DURATION = 100;
  private static int ID = -1;
  /**
   * Constructor to create a new TimeMageE
   *
   * @param x the x position of the E ability
   * @param y the y position of the E ability
   */
  TimeMageE(int x, int y){
    super(DURATION,ID);
    this.x = x;
    this.y = y;
  }
  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
}
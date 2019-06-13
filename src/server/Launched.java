package server;

/**
 * Launched.java
 *
 * A class handling the specific status effect of being launched in the air, containing constants for it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */

class Launched extends Status{
  double dx,dy;
  private static int ID = -1;

  /**
   * Basic constructor, sending local constants up to the superclass and setting local values
   *
   * @param dx the x-velocity of the launch
   * @param dy the y-velocity of the launch
   * @param duration how long the status effect lasts for
   */

  Launched(double dx, double dy, int duration){
    super(duration,ID);
    this.dx = dx;
    this.dy = dy;
  }

  /**
   * Basic getter for the x-velocity of the launch state
   *
   * @return the x-velocity, as a double
   */
  public double getDX(){
    return dx;
  }

  /**
   * Basic getter for the y-velocity of the launch state
   *
   * @return the y-velocity, as a double
   */
  public double getDY(){
    return dy;
  }
}
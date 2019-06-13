package server;

/**
 * FlareAOE.java
 *
 * The class dealing with the specific case of the flare illumination AOE
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 *
 */
class FlareAOE extends AOE{
  // Constants for the specific flare case
  private static int DURATION = 400;
  private static int RADIUS = 225;
  private static int ID = 0;

  /**
   * Basic constructor, sending all local constant data up to AOE superclass
   *
   * @param x the x-position of the AOE
   * @param y the y-position of the AOE
   */

  FlareAOE(int x, int y){
    super(x,y,DURATION,RADIUS, ID);
  }

  /**
   * Basic getter returning the local constant for Radius
   *
   * @return the integer value of the AOE's radius
   */
  public static int getRADIUS(){
    return RADIUS;
  }
}
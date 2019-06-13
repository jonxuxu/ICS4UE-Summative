package server;
/**
 * JuggernautEAOE.java
 *
 * This is the class representing the status storing the JuggernautEAOE info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class JuggernautEAOE extends AOE{
  private static int DURATION = 2;
  private static int RADIUS = 200;
  private static int ID = 11;
  /**
   * Constructor to create a new JuggernautEAOE
   * @param x x coordinate of the AOE
   * @param y y coordinate of the AOE
   */
  JuggernautEAOE(int x, int y){
    super(x,y,DURATION,RADIUS, ID);
  }
}
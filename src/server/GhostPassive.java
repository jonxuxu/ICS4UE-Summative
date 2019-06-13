package server;
/**
 * GhostPassive.java
 *
 * This is the class representing the status storing the GhostPassive info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class GhostPassive extends Status{
  private static int DURATION = 100;
  private static int ID = 3;
  /**
   * Constructor to create a new GhostE
   */
  GhostPassive(){
    super(DURATION,ID);
  }
}
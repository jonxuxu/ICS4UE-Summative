package server;

/**
 * Dead.java
 *
 * The class containing the information an special state for a scenario of a player's death, extending Stun
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */
class Dead extends Stun{
  private static int DURATION = 300;
  private static int ID = 1;

  /**
   * Basic constructor, sending up all local data to the Stun superclass
   */
  Dead(){
    super(DURATION,ID);
  }

}
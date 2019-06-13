package server;
/**
 * JuggernautEStun.java
 *
 * This is the class representing the status storing the JuggernautEStun info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class JuggernautEStun extends Stun{
  private static int DURATION = 100;
  private static int ID = 12;
  /**
   * Constructor to create a new JuggernautEStun
   */
  JuggernautEStun(){
    super(DURATION,ID);
  }
}
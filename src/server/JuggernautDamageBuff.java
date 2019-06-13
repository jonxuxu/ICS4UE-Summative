package server;
/**
 * JuggernautDamageBuff.java
 *
 * This is the class representing the status storing the JuggernautDamageBuff info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class JuggernautDamageBuff extends DamageBuff{
  private static int STRENGTH = 50;
  private static int DURATION = 2;
  /**
   * Constructor to create a new JuggernautDamageBuff
   */
  JuggernautDamageBuff(){
    super(STRENGTH, DURATION);
  }
}
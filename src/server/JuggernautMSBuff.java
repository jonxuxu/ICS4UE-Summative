package server;
/**
 * JuggernautMSBuff.java
 *
 * This is the class representing the status storing the JuggernautMSBuff info
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */
class JuggernautMSBuff extends MSBuff{
  private static int STRENGTH = 3;
  private static int DURATION = 2;
  /**
   * Constructor to create a new JuggernautMSBuff
   */
  JuggernautMSBuff(){
    super(STRENGTH, DURATION);
  }
}
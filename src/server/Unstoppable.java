package server;
/**
 * Unstoppable.java
 *
 * A class handling the specific status effect of being unstoppable, containing constants for it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */
class Unstoppable extends Status{
  // Local constants
  private static int ID = 11;
  /**
   * Basic constructor, sending local constants up to the superclass
   *
   * @param duration how long the status effect lasts for
   */
  Unstoppable(int duration){
    super(duration, ID);
  }
}
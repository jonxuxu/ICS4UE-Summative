package server;

/**
 * Stun.java
 *
 * A class handling the specific status effect of being stunned, containing constants for it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */

class Stun extends Status{
  /**
   * Basic constructor, sending local constants up to the superclass
   *
   * @param duration how long the status effect lasts for
   */
  Stun(int duration, int id){
    super(duration, id);
  }
}
package server;

/**
 * Illuminated.java
 *
 * A class handling the specific status effect of illumination, containing constants for it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */
class Illuminated extends Status{
  // Constants for specific case
  private static int ID = 4;

  /**
   * Basic constructor, sending local constants up to the superclass
   *
   * @param duration how long the status effect lasts for
   */

  Illuminated(int duration){
    super(duration,ID);
  }
}
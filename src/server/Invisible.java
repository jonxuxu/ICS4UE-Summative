package server;

/**
 * Invisible.java
 *
 * A class handling the specific status effect of invisibility, containing constants for it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */
class Invisible extends Status{
  //
  private static int ID = 5;

  /**
   * Basic constructor, sending local constants up to the superclass
   *
   * @param duration how long the status effect lasts for
   */

  Invisible(int duration){
    super(duration,ID);
  }
}
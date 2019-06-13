package server;

/**
 * Uncollidable.java
 *
 * A class handling the specific status effect of being uncollidable, containing constants for it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */

class Uncollidable extends Status{
  private static int ID = 10;

  /**
   * Basic constructor, sending local constants up to the superclass
   *
   * @param duration how long the status effect lasts for
   */

  Uncollidable(int duration){
    super(duration,ID);
  }
}
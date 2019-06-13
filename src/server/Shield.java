package server;

/**
 * Shield.java
 *
 * Has the data for a damage shield that can be applied to a player that takes damage instead of the player who has it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-30
 * @version 1.1
 */

class Shield{
  private int strength;
  private int duration;
  private int lifetime;

  /**
   * Basic constructor initializing the basic values of the shield instance
   *
   * @param strength the strength of the shield
   * @param duration the duration for which the shield lasts
   */
  Shield(int strength, int duration){
    this.strength = strength;
    this.duration = duration;
  }

  /**
   * Basic getter for the strength of the shield
   *
   * @return the strength of the shield, as an integer
   */
  public int getStrength(){
    return strength;
  }

  /**
   * Basic getter for the remaining duration of the shield
   *
   * @return the duration for which the shield lasts, as an integer
   */
  public int getRemainingDuration(){
    return duration-lifetime;
  }

  /**
   * Applies damage to the shield
   *
   * @param damage how much damage is to be applied to the shield*
   */
  public void damage(int damage){
    strength -= damage;
  }

  /**
   * Counts down the lifetime of the shield by one tick
   */
  public void advance(){
    lifetime++;
  }
}
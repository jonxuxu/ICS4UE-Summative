package server;

/**
 * DamageBuff.java
 *
 * The class containing all the information for damage buffs to the player
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 *
 */
class DamageBuff extends Status{
  int strength;
  private static int ID = 0;

  /**
   * Constructor for the basic values of the buff
   *
   * @param strength
   * @param duration
   */
  DamageBuff(int strength, int duration){
    super(duration,ID);
    this.strength = strength;
  }

  /**
   * Basic getter for the strength of the buff
   *
   * @return the strength of the buff, as an integer
   */
  public int getStrength(){
    return strength;
  }
}
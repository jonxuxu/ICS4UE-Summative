package server;

/**
 * ReduceDamage.java
 *
 * A class handling the specific status effect of damage reduction, containing constants for it
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-27
 * @version 1.0
 */

class ReduceDamage extends Status{
  double damageReduction;//damage = damage * (1 - damageReduction)
  private static int ID = 9;

  /**
   * Constructor for the basic values of the buff
   *
   * @param damageReduction how much the damage being reduced is
   * @param duration how long the buff lasts for
   */

  ReduceDamage(double damageReduction, int duration){
    super(duration,ID);
    this.damageReduction = damageReduction;
  }

  /**
   * Basic getter for the reduction factor of the buff
   *
   * @return the reduction factor of the buff, as an integer
   */
  public double getDamageReduction(){
    return damageReduction;
  }
}
package server;
/**
 * Status.java
 * This is the general superclass for all status effects in the game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-21
 */

public class Status {
  int duration;
  int lifetime;
  int id;

  /**
   * Constructor for basic values
   *
   * @param duration how long the status effect
   * @param id the id for the type of effect
   */
  Status(int duration, int id){
    this.duration = duration;
    this.id = id;
  }

  /**
   * Advances the lifetime of the effect by one tick
   */
  public void advance(){
    lifetime++;
  }

  /**
   * Basic getter for the remaining lifespan of the status effect
   *
   * @return the integer value of the remaining lifespan
   */
  public int getRemainingDuration(){
    return duration-lifetime;
  }

  /**
   * Sets the lifetime of the effect to 0, re-setting it
   */
  public void refresh(){
    lifetime = 0;
  }

  /**
   * Basic setter for the id of the status effect
   *
   * @param id, the id to be assigned to the status effect
   */
  public void setID(int id){
    this.id=id;
  }

  /**
   * Basic getter for the id of the status effect
   *
   * @return the id of the status effect
   */
  public int getID(){
    return id;
  }
}

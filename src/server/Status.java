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
  public int getRemainingDuration(){
    return duration-lifetime;
  }
  public void refresh(){
    lifetime = 0;
  }
  public void setID(int id){
    this.id=id;
  }
  public int getID(){
    return id;
  }
}

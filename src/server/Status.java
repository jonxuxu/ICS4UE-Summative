package server;
/**
 * Status.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-21
 */

public class Status {
  int duration;
  int lifetime;
  int id;
  Status(int duration, int id){
    this.duration = duration;
    this.id = id;
  }
  public void advance(){
    lifetime++;
  }
  public int getRemainingDuration(){
    return duration-lifetime;
  }
  public void setID(int id){
    this.id=id;
  }
  public int getID(){
    return id;
  }
}

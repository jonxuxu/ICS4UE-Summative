package server;
/**
 * Status.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-21
 */

public class Status {
  int duration;
  int lifetime;
  int ID;
  Status(int duration){
    this.duration = duration;
  }
  public void advance(){
    lifetime++;
  }
  public int getRemainingDuration(){
    return duration-lifetime;
  }
  public void setID(int ID){
    this.ID=ID;
  }
}

package server;

import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;

/**
 * AOE.java
 *
 * The class containing information about a performed area-of-effect (AOE) attacks
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-30
 * @version 2.0
 */

class AOE implements HasID{
  int x, y;
  int duration;
  int radius;
  int lifetime;
  int ID;
  Ellipse2D hitbox;

  /**
   * Basic constructor initializing core values
   *
   * @param x the x-position of the AOE attack
   * @param y the y-position of the AOE attack
   * @param duration how long the AOE attack will last
   * @param radius the radius of the AOE attack
   * @param ID the identifier of the attack, for communication purposes
   */
  
  AOE(int x, int y, int duration, int radius, int ID) {
    this.x = x;
    this.y = y;
    this.duration = duration;
    this.radius = radius;
    hitbox = new Ellipse2D.Double(x-radius, y-radius, radius * 2, radius * 2);
    this.ID = ID;
  }

  /**
   * A method for checking whether a point is within the range of the AOE attack
   *
   * @param x the x-value of the point to be checked
   * @param y the y-value of the point to be checked
   * @return a boolean signifying whether the point was within the bounding box of the AOE attack or not
   */
  public boolean contains(int x, int y){
    return hitbox.contains(x,y);
  }

  /**
   * Basic getter for the x-value of the AOE attack
   *
   * @return the x-value as an integer
   */
  public int getX() {
    return x;
  }

  /**
   * Basic getter for the y-value of the AOE attack
   *
   * @return the y-value as an integer
   */
  public int getY() {
    return y;
  }

  /**
   * Setter for the x-value of the AOE attack, which also does a refactoring of the AOE's hitbox
   *
   * @param x the x-location of the AOE's position
   */
  public void setX(int x){
    hitbox = new Ellipse2D.Double(x-radius, y-radius, radius * 2, radius * 2);
    this.x = x;
  }

  /**
   * Setter for the y-value of the AOE attack, which also does a refactoring of the AOE's hitbox
   *
   * @param y the y-location of the AOE's position
   */
  public void setY(int y){
    hitbox = new Ellipse2D.Double(x-radius, y-radius, radius * 2, radius * 2);
    this.y = y;
  }
  /*
  public void incrementX(int dx){
    x+=dx;
  }
  public void incrementY(int dy){
    y+=dy;
  }*/

  /**
   * Increments the lifetime of the AOE, for calculation reasons
   */
  public void advance() {
    lifetime++;
  }

  /**
   * Returns the remaining duration of the AOE
   *
   * @return the integer representation of how many ticks the AOE should persist for
   */
  public int getRemainingDuration() {
    return duration - lifetime;
  }

  /**
   * Performs a check whether an potentially affected object intersects the AOE's hitbox
   *
   * @param object the object Implementing the CanIntersect interface
   *               that is being checked for an intersection with the AOE
   * @return a boolean on whether the target object is intersecting the AOE's hitbox or not
   */
  public boolean collides(CanIntersect object) {
    Area collisionArea = new Area(object.getHitbox());
    collisionArea.intersect(new Area(hitbox));
    return !(collisionArea.isEmpty());
  }

  /**
   * The interface method returning the ID of the object
   *
   * @return the identifier of this object's instance
   */
  public int getID() {
    return (ID);
  }

  /**
   * Basic getter for the radius of the AOE attack
   *
   * @return the integer value for the radius of the AOE
   */
  public int getRadius() {
    return (radius);
  }

  /**
   * A method that expires the AOE's lifetime.
   */
  public void removeNextTurn(){
    lifetime=duration;
  }
}
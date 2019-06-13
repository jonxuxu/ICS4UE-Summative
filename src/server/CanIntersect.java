package server;
import java.awt.geom.Area;

/**
 * CanIntersect.java
 *
 * The interface for objects that can intersect polygon geometry
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-05-30
 * @version 1.0
 *
 */
public interface CanIntersect{
  /**
   * The abstract method for returning the Area representation of the object
   *
   * @return the hitbox, as a java.awt.geom.Area
   */
  public Area getHitbox();
}
package server;

/**
 * HasID.java
 * This is an interface for adding identifiers to objects
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public interface HasID {
   /**
    * An abstract method for returning the identifier as an integer
    *
    * @return the identifier of the object instance
    */
   public int getID();
}

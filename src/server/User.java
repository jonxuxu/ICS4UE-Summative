package server;

/**
 * User.java
 *
 * This is superclass containing the framework for the various user classes
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-04-24
 */

public class User {
   private String username ="";

   /**
    * Basic constructor, setting the username of the user
    *
    * @param username
    */
   User(String username){
      this.username=username;
   }

   /**
    * Basic getter for the username
    *
    * @return the username string
    */
   public String getUsername(){
      return (username);
   }
}

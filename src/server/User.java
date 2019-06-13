package server;

/**
 * User.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-04-24
 */

public class User {
   private String username ="";
   User(String username){
      this.username=username;
   }
   public String getUsername(){
      return (username);
   }
}

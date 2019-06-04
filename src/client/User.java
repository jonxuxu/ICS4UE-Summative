package client;

/**
 * User.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public class User {
   private String username ="";
   private int teamNumber=9;//which means that it is invalid
   User(String username){
      this.username=username;
   }
   public String getUsername(){
      return (username);
   }

   public void setTeam(int teamNumber){
      this.teamNumber=teamNumber;
   }

   public int getTeam(){
      return teamNumber;
   }
}

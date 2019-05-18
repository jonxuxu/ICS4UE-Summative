/**
 * This is a class which helps regulate the paint component speed
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-17
 */

public class Clock {

   //Instance variables
   //Set as long to maintain precision
   private long oldTime;
   private long secondTime;
   private long currentTime;
   //Indicate frames per second
   private final long DELTA_LIMIT = 5000000;//Every time this much time elapses, a frame is passed

   /**
    * Sets the instance variables upon creation
    */
   public Clock() {
      this.oldTime = System.nanoTime();
      this.secondTime = System.nanoTime();
      this.currentTime = System.nanoTime();
   }

   //Getters and setters

   /**
    * Checks if the time for a frame passing has passed, and returns accordingly.
    *
    * @return a boolean, this is true if 0.01 seconds has passed
    */
   public boolean getFramePassed() {
      if ((currentTime - oldTime) >= DELTA_LIMIT) {
         oldTime = currentTime;
         return (true);
      } else {
         return (false);
      }
   }

   /**
    * Sets the clocks time.
    */
   public void setTime() {
      currentTime = System.nanoTime();
   }
}

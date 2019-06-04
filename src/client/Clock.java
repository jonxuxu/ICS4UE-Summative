package client;

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
   private long currentTime;
   private int frame;
   private int fps;
   //Indicate frames per second
   private long DELTA_LIMIT;//Every time this much time elapses, a frame is passed
   /**
    * Sets the instance variables upon creation
    */
   public Clock(int delay) {
      DELTA_LIMIT = delay;
      this.oldTime = System.currentTimeMillis();
      this.currentTime = System.currentTimeMillis();
   }

   //Getters and setters

   /**
    * Checks if the time for a frame passing has passed, and returns accordingly.
    *
    * @return a boolean, this is true if 0.01 seconds has passed
    */
   public boolean getFramePassed() {
      currentTime = System.currentTimeMillis();
      frame++;
      if ((currentTime - oldTime) >= DELTA_LIMIT) {
         oldTime = currentTime;
         fps = frame;
         frame = 0;
         return (true);
      } else {
         return (false);
      }
   }

   public int getFPS() {
      return fps;
   }
}

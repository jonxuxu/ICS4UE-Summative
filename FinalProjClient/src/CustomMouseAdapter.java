//General import

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * This mouse adapter uses the extended methods to scroll, pan, and more.
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-02-12
 */

public class CustomMouseAdapter extends MouseAdapter {
   //Controls the x and y position of the mouse
   //The xy when it is dragged
   private int[] dragXy = new int[2];
   //The xy when it is clicked
   private int[] clickXy = new int[2];
   //The xy at all times
   private int[] xy = new int[2];
   //The following booleans control the state of the mouse
   private boolean pressed;
   private int rotation;
   private boolean rotated = false;

//Methods that are implemented from MouseListener

   /**
    * Not used. Normally responds to the mouse click.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseClicked(MouseEvent e) {
   }

   /**
    * Determines if pressed is true, gets the position of all the xy's.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mousePressed(MouseEvent e) {
      clickXy[0] = e.getX();
      clickXy[1] = e.getY();
      dragXy[0] = e.getX();
      dragXy[1] = e.getY();
      xy[0] = e.getX();
      xy[1] = e.getY();
      pressed = true;
   }

   /**
    * Sets the pressed to false if the mouse is released.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseReleased(MouseEvent e) {
      pressed = false;

   }

   /**
    * Not used. Normally determines hovering above a component to be true.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseEntered(MouseEvent e) {

   }

   /**
    * Not used. Normally determines hovering to be false.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseExited(MouseEvent e) {

   }
//End of methods that are implemented from MouseListener

   /**
    * Determines if the mouse wheel is scrolled, and sets a rotation variable.
    * This is +ve if the wheel is negative, and -ve if the wheel is positive.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseWheelMoved(MouseWheelEvent e) {
      if (e.getWheelRotation() < 0) {
         rotation = 1;
      } else {
         rotation = -1;
      }
      rotated = true;
   }

   /**
    * Returns whether or not the mouse has been rotated and its information.
    *
    * @return rotation, an int which is determined by mouse wheel moved
    */
   public int scroller() {
      if (rotated) {
         rotated = false;
         return (rotation);
      } else {
         return (0);
      }
   }

   /**
    * Sets the xy coordinates of the mouse at all times.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseMoved(MouseEvent e) {
      xy[0] = e.getX();
      xy[1] = e.getY();
   }

   /**
    * Sets the xy and dragXY coordinates of the mouse when it is dragged.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseDragged(MouseEvent e) {
      dragXy[0] = e.getX();
      dragXy[1] = e.getY();
      xy[0] = e.getX();
      xy[1] = e.getY();
   }
   //End of methods that are implemented from MouseAdapter

//Getters and setters

   /**
    * Returns the mouse coordinates.
    *
    * @return dragXy, an int[] with the x as the 0 index and y as the 1 index
    */
   public int[] getMouseDragXy() {
      return dragXy;
   }

   /**
    * Returns the mouse clicking coordinates.
    *
    * @return clickXy, an int[] with the x as the 0 index and y as the 1 index
    */
   public int[] getMouseClickXy() {
      return clickXy;
   }

   /**
    * Returns the mouse coordinates.
    *
    * @return xy, an int[] with the x as the 0 index and y as the 1 index
    */
   public int[] getMouseXy() {
      return xy;
   }

   /**
    * Returns the pressed state.
    *
    * @return pressed, a boolean
    */
   public boolean getPressed() {
      return pressed;
   }
}

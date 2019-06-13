package client;//General import

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * This mouse adapter uses the extended methods to scroll, pan, and more.
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
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
   private int[] state = new int[3];
   //The following booleans control the state of the mouse
   private int rotation;
   private boolean rotated = false;
   private int[] centerXy;
   private boolean[] leftRight = new boolean[2];

   private Client main;

   public CustomMouseAdapter(Client main, int[] centerXy) {
      this.main = main;
      this.centerXy = centerXy;
   }

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
      state[0] = e.getX();
      state[1] = e.getY();
      state[2] = 1;
      leftRight[0] = false;
      leftRight[1] = false;
      if (e.getButton() == 1) {
         leftRight[0] = true;
      }
      if (e.getButton() == 3) {
         leftRight[1] = true;
      }

      main.updateMouse(state);
   }

   /**
    * Sets the pressed to false if the mouse is released.
    *
    * @param e, MouseEvent
    */
   @Override
   public void mouseReleased(MouseEvent e) {
      state[2] = 0;
      main.updateMouse(state);
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
      state[0] = e.getX();
      state[1] = e.getY();
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
      state[0] = e.getX();
      state[1] = e.getY();
   }
   //End of methods that are implemented from MouseAdapter

//Getters and setters

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

   /**
    * Returns the pressed state.
    *
    * @return pressed, a boolean
    */
   public int getState(int mouseArrayNumber){
      return this.state[mouseArrayNumber];
   }

   public int[] getDispXy() { //Returns the displacement from the top left corner in game coordinates
      int[] dispXy = new int[2];
      dispXy[0] = (int) ((state[0] - centerXy[0]));
      dispXy[1] = (int) ((state[1] - centerXy[1]));
      return (dispXy);
   }

   public boolean[] getLeftRight() { //Determines whether the left or right buttons were clicked
      return (leftRight);
   }

   public double getAngle() {
      double tempAngle = 0;
      //direction[0] is for the x values, direction[1] is for the y values
      if (!(((state[0] - centerXy[0]) == 0) && ((state[1] - centerXy[1]) == 0))) {
         tempAngle = Math.atan2(state[1] - centerXy[1], state[0] - centerXy[0]);
      }
      return (tempAngle);
   }
}

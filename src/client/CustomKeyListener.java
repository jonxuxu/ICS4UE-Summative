package client;//General import

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.HashSet;

/**
 * This is a key listener which is used to register ctrl z, ctrl y, and ctrl s.
 *
 * @author Will Jeong
 * @version 2.0
 * @since 2019-02-19
 */


public class CustomKeyListener implements KeyListener {

   //W is -1, S is 1, A is -1, D is 1
   //Instance variables
   private Set<Integer> pressed = new HashSet<Integer>();
   private int[] direction = new int[2];//WASD
   private boolean[] spellsUsed = new boolean[3];

   //Start of methods that are implemented from KeyListener

   /**
    * This is not used, just implemented.
    *
    * @param e, a KeyEvent
    */
   @Override
   public void keyTyped(KeyEvent e) {
   }

   /**
    * This activates the direction
    *
    * @param e, a KeyEvent
    */
   @Override
   public void keyPressed(KeyEvent e) {
      System.out.println(e.getKeyCode());
      pressed.add(e.getKeyCode());
   }

   /**
    * This removes the keys in the set to ensure that sensing multiple buttons works.
    *
    * @param e, a KeyEvent
    */
   @Override
   public void keyReleased(KeyEvent e) {
      pressed.remove(e.getKeyCode());
   }

   //End of methods that are implemented from KeyListener

   //Getters and setters

   public int getAngle() {
      direction[0] = 0;
      direction[1] = 0;
      if (pressed.contains(87)) {
         direction[1] = -1;
      }
      if (pressed.contains(65)) {
         direction[0] = -1;
      }
      if (pressed.contains(83)) {
         direction[1] = 1;
      }
      if (pressed.contains(68)) {
         direction[0] = 1;
      }
      double tempAngle;
      if ((pressed.contains(87)) && (pressed.contains(83))) {
         direction[1] = 0;
      }
      if ((pressed.contains(68)) && (pressed.contains(65))) {
         direction[0] = 0;
      }
      //direction[0] is for the x values, direction[1] is for the y values
      tempAngle = Math.atan2(direction[1], direction[0]);
      int roundedAngle = (int) (4 * (tempAngle / Math.PI));
      if ((direction[0] == 0) && (direction[1] == 0)) {
         //   System.out.println("-1");
         return (-10);//Check to see if the return works
      } else {
         // System.out.println("tempAngle"+tempAngle);
         return (roundedAngle);
      }
   }

   public boolean getEnter() {
      if (pressed.contains(10)) {
         return true;
      }else{
         return false;
      }
   }

   public boolean[] getSpell() {
      //0 refers to q, 1 refers to e, 2 refers to space
      spellsUsed[0] = false;
      spellsUsed[1] = false;
      spellsUsed[2] = false;
      if (pressed.contains(81)) {
         spellsUsed[0] = true;
      }
      if (pressed.contains(69)) {
         spellsUsed[1] = true;
      }
      if (pressed.contains(32)) {
         spellsUsed[2] = true;
      }
      return (spellsUsed);
   }
}


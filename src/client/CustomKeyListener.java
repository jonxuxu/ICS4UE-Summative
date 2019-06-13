package client;//General import

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.HashSet;

/**
 * This is a key listener which is used to register ctrl z, ctrl y, and ctrl s.
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 2.0
 * @since 2019-02-19
 */


public class CustomKeyListener implements KeyListener {

   //W is -1, S is 1, A is -1, D is 1
   //Instance variables
   private Set<Character> pressed = new HashSet<Character>();
   private int[] direction = new int[2];//WASD
   private boolean[] spellsUsed = new boolean[3];
   private Client main;
   private boolean flashlightOn;
   private boolean released;
   //Characters
   private char ESC = ((char) (27));
   private char CAPS_LOCK = ((char) (20));

   public CustomKeyListener(Client main) {
      this.main = main;
   }

   @Override
   public void keyTyped(KeyEvent e) {
      main.typeKey(e.getKeyChar());
   }

   /**
    * This activates the direction
    *
    * @param e, a KeyEvent
    */
   @Override
   public void keyPressed(KeyEvent e) {
      pressed.add(e.getKeyChar());
   }

   /**
    * This removes the keys in the set to ensure that sensing multiple buttons works.
    *
    * @param e, a KeyEvent
    */
   @Override
   public void keyReleased(KeyEvent e) {
      if ((e.getKeyChar() == 'f') || (e.getKeyChar() == 'F')) {
         released = true;
      }
      pressed.remove(e.getKeyChar());
   }

   //End of methods that are implemented from KeyListener

   //Getters and setters
   public int getAngle() {
      direction[0] = 0;
      direction[1] = 0;
      if ((pressed.contains('w')) || (pressed.contains('W'))) {
         direction[1] += -1;
      }
      if ((pressed.contains('a')) || (pressed.contains('A'))) {
         direction[0] += -1;
      }
      if ((pressed.contains('s')) || (pressed.contains('S'))) {
         direction[1] += 1;
      }
      if ((pressed.contains('d')) || (pressed.contains('D'))) {
         direction[0] += 1;
      }
      double tempAngle;
      //direction[0] is for the x values, direction[1] is for the y values
      tempAngle = Math.atan2(direction[1], direction[0]);
      int roundedAngle = (int) (4 * (tempAngle / Math.PI));
      if ((direction[0] == 0) && (direction[1] == 0)) {
         //   System.out.println("-1");
         return (-10);//Check to see if the return works
      } else {
         // System.out.println("tempAngle"+tempAngle);
         return (roundedAngle); //So pi becomes 4, 3pi/4 becomes 3,pi/2 becomes 2 ...
      }
   }


   public boolean[] getSpell() {
      //0 refers to q, 1 refers to e, 2 refers to space
      spellsUsed[0] = false;
      spellsUsed[1] = false;
      spellsUsed[2] = false;
      if ((pressed.contains('q')) || (pressed.contains('Q'))) {
         spellsUsed[0] = true;
      }
      if ((pressed.contains('e')) || (pressed.contains('E'))) {
         spellsUsed[1] = true;
      }
      if (pressed.contains(' ')) {
         spellsUsed[2] = true;
      }
      return (spellsUsed);
   }

   public boolean getFlashlightOn() {
      if ((pressed.contains('f')) || (pressed.contains('F'))) {
         if (released) {
            if (flashlightOn) {
               flashlightOn = false;
            } else {
               flashlightOn = true;
            }
            released=false;
         }
      }
      return (flashlightOn);
   }

}
/*
   public HashSet<> getKeysPressed(){
   public Set<Character> getKeysPressed(){
      return pressed;
   }

*/
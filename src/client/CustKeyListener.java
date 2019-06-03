package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class CustKeyListener implements KeyListener {
  private Client.GamePanel main;
  //Characters
  private char ESC = ((char) (27));

  public CustKeyListener(Client.GamePanel main) {
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
  }

  /**
   * This removes the keys in the set to ensure that sensing multiple buttons works.
   *
   * @param e, a KeyEvent
   */
  @Override
  public void keyReleased(KeyEvent e) {
  }

}
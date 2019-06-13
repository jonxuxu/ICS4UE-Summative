package client.particle;

import java.awt.*;
/**
 * AshParticle.java
 * This is an ash particle class used during the display screen
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */

public class AshParticle extends Particle {
  private double dx;
  private double dy = Math.random() * 5 + 5;
  private static Color[] ashColors = {new Color(224,224,224), new Color(192,192,192), new Color(96,96,96), new Color(64,64,64)};
  private int screenY;

  /**
   * Constructor for the ash particle
   * @param x x-location
   * @param y y-location
   * @param size size of the particle
   * @param screenY movement along the screen
   */
  public AshParticle(double x, double y, int size, int screenY){
    super(x, y, size, Integer.MAX_VALUE);

    int randomNum = (int) (Math.random() * ashColors.length);
    super.setColor(ashColors[randomNum]);

    this.dx = Math.random()*2 -4;
    this.screenY = screenY;
  }

  /**
   * update method to update the movement of the particle and erase it after it has passed
   * @return the state of the update
   */
  public boolean update(){

    if(super.getInfo()[0] < 0 || super.getInfo()[1] > screenY){
      super.kill();
      //System.out.println("KILL!");
    }
    return super.update(dx, dy);
  }
}

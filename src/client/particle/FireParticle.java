package client.particle;

import java.awt.*;
/**
 * FireParticle.java
 * This is an fire particle class used to animate spells
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public class FireParticle extends Particle{
  private double angle;
  private double dx, dy;
  private static Color fireColours[] = {new Color(255, 249, 202), new Color(209, 106, 4), new Color(227, 238, 35), new Color(254, 69, 0), new Color(234, 185, 79)};

  /**
   * Class constructor for the fire particle
   * @param x x-location
   * @param y y-location
   * @param size size of the particle
   */
  public FireParticle(double x, double y, int size){
    super(x, y, size, (int)(Math.random()*20+20));

    int randomNum = (int) (Math.random() * fireColours.length);
    super.setColor(fireColours[randomNum]);

    // Creates particles that travel in a random circular direction
    angle = Math.random()*2*Math.PI;
    dx = Math.cos(angle) * 20;
    dy = Math.sin(angle) * 20;
  }

  /**
   * Method to update the particle; ran through particle
   * @return the state of the update
   */
  public boolean update(){
    return super.update(dx, dy);
  }
}

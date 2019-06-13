package client.particle;

import java.awt.*;
/**
 * GhostParticle.java
 * This is an particle class used during spells of Ghost
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public class GhostParticle extends Particle{
  private double angle;
  private double dx, dy;
  private static Color colours[] = {new Color(75,0,130), new Color(0, 0, 0), new Color(139,0,139)};

  /**
   * Class constructor
   * @param x x-location
   * @param y y-location
   * @param size size of particle
   */
  public GhostParticle(double x, double y, int size){
    super(x, y, size, (int)(Math.random()*20+20));

    int randomNum = (int) (Math.random() * colours.length);
    super.setColor(colours[randomNum]);

    // Creates particles that travel in a random circular direction
    angle = Math.random()*2*Math.PI;
    dx = Math.cos(angle) * 20;
    dy = Math.sin(angle) * 20;
  }

  /**
   * Method to update the movement of the particle
   * @return the state of the update
   */
  public boolean update(){
    return super.update(dx, dy);
  }
}

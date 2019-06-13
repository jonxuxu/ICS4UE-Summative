package client.particle;

import java.awt.*;

/**
 * JuggernautParticle.java
 * This is a particle class used for Juggernaut's animations
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public class JuggernautParticle extends Particle{
  private double angle;
  private double dx, dy;
  private static Color colours[] = {new Color(255, 249, 202), new Color(209, 106, 4), new Color(227, 238, 35), new Color(254, 69, 0), new Color(234, 185, 79)};

  /**
   * Class Constructor
   * @param x x-location
   * @param y y-location
   * @param size size of particle
   */
  public JuggernautParticle(double x, double y, int size){
    super(x, y, size, (int)(Math.random()*20+20));

    int randomNum = (int) (Math.random() * colours.length);
    super.setColor(colours[randomNum]);

    // Creates particles that travel in a random circular direction
    angle = Math.random()*2*Math.PI;
    dx = Math.cos(angle) * 20;
    dy = Math.sin(angle) * 20;
  }

  /**
   * method to update the particle
   * @return the state of the update
   */
  public boolean update(){
    return super.update(dx, dy);
  }
}

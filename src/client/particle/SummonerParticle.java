package client.particle;

import java.awt.*;

/**
 * SummonerParticle.java
 * This is a particle class used for Summoner's animations
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public class SummonerParticle extends Particle{
  private double angle;
  private double dx, dy;
  private static Color colours[] = {new Color(65,105,225), new Color(30,144,255), new Color(0,0,128), new Color(25,25,112)};

  /**
   * Class Constructor
   * @param x x-location
   * @param y y-location
   * @param size size of particle
   */
  public SummonerParticle(double x, double y, int size){
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
   * @return the state of the particle
   */
  public boolean update(){
    return super.update(dx, dy);
  }
}

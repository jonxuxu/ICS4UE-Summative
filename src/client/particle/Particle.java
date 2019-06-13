package client.particle;

import java.awt.*;
/**
 * Particle.java
 * This is a particle class used as a superclass for all the particle animations
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */
public abstract class Particle {
  private double x, y;
  private int size;
  private int life;
  private Color color;

  /**
   * Class constructor if colour is specified
   * @param x x-location
   * @param y y-location
   * @param size size of particle
   * @param life duration of particle
   * @param c colour of particle
   */
  public Particle(double x, double y, int size, int life, Color c){
    this.x = x;
    this.y = y;
    this.size = size;
    this.life = life;
    this.color = c;
  }

  /**
   * Class constructor if no colour is specified
   * @param x x-location
   * @param y y-location
   * @param size size of particle
   * @param life duration of particle
   */
  public Particle(double x, double y, int size, int life){
    this.x = x;
    this.y = y;
    this.size = size;
    this.life = life;
  }

  /**
   * method to update the particle's existence
   * @param dx change in x
   * @param dy change in y
   * @return
   */
  public boolean update(double dx, double dy){
    this.x += dx;
    this.y += dy;

    life --;
    return life <= 0;
  }

  /**
   * method to render the particle
   * @param g graphics
   */
  public void render(Graphics2D g) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(color);
    g2d.fillRect((int) (x - (size / 2)), (int) (y - (size / 2)), size, size);

    g2d.dispose();
  }

  /**
   * Getter for the particle information
   * @return the particle's information as an array of double numbers
   */
  public double[] getInfo(){
    double[] output = new double[3];
    output[0] = x;
    output[1] = y;
    output[2] = life;
    return output;
  }

  /**
   * Setter of particle colour
   * @param c
   */
  public void setColor(Color c){
    this.color = c;
  }

  /**
   * method to end the duration of the particle
   */
  public void kill(){
    this.life = 0;
  }

}

package client.particle;

import java.awt.*;

public abstract class Particle {
  private double x, y;
  private int size;
  private int life;
  private Color color;

  public Particle(double x, double y, int size, int life, Color c){
    this.x = x;
    this.y = y;
    this.size = size;
    this.life = life;
    this.color = c;
  }
  public Particle(double x, double y, int size, int life){
    this.x = x;
    this.y = y;
    this.size = size;
    this.life = life;
  }

  public boolean update(double dx, double dy){
    this.x += dx;
    this.y += dy;

    life --;
    return life <= 0;
  }

  public void render(Graphics2D g) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(color);
    g2d.fillRect((int) (x - (size / 2)), (int) (y - (size / 2)), size, size);

    g2d.dispose();
  }

  public double[] getInfo(){
    double[] output = new double[3];
    output[0] = x;
    output[1] = y;
    output[2] = life;
    return output;
  }

  public void setColor(Color c){
    this.color = c;
  }

  public void kill(){
    this.life = 0;
  }

}

package client;

import java.awt.*;

public class AshParticle extends Particle{
  private double dx;
  private static double dy = Math.random() * 5 + 5;
  private static Color[] ashColors = {new Color(224,224,224), new Color(192,192,192), new Color(96,96,96), new Color(64,64,64)};
  private int screenY;

  AshParticle(double x, double y, int size, int screenY){
    super(x, y, size, Integer.MAX_VALUE);

    int randomNum = (int) (Math.random() * ashColors.length);
    super.setColor(ashColors[randomNum]);

    this.dx = Math.random()*2 -4;
    this.screenY = screenY;
  }

  public boolean update(){

    if(super.getInfo()[0] < 0 || super.getInfo()[1] > screenY){
      super.kill();
      //System.out.println("KILL!");
    }
    return super.update(dx, dy);
  }
}

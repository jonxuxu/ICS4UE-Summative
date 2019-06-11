package client.particle;

import java.awt.*;

public class FireParticle extends Particle{
  private double angle;
  private double dx, dy;
  private static Color fireColours[] = {new Color(255, 249, 202), new Color(209, 106, 4), new Color(227, 238, 35), new Color(254, 69, 0), new Color(234, 185, 79)};


  public FireParticle(double x, double y, int size){
    super(x, y, size, (int)(Math.random()*20+20));

    int randomNum = (int) (Math.random() * fireColours.length);
    super.setColor(fireColours[randomNum]);

    // Creates particles that travel in a random circular direction
    angle = Math.random()*2*Math.PI;
    dx = Math.cos(angle) * 20;
    dy = Math.sin(angle) * 20;
  }
  public boolean update(){
    return super.update(dx, dy);
  }
}

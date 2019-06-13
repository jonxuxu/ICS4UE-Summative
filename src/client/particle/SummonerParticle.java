package client.particle;

import java.awt.*;

public class SummonerParticle extends Particle{
  private double angle;
  private double dx, dy;
  private static Color colours[] = {new Color(65,105,225), new Color(30,144,255), new Color(0,0,128), new Color(25,25,112)};


  public SummonerParticle(double x, double y, int size){
    super(x, y, size, (int)(Math.random()*20+20));

    int randomNum = (int) (Math.random() * colours.length);
    super.setColor(colours[randomNum]);

    // Creates particles that travel in a random circular direction
    angle = Math.random()*2*Math.PI;
    dx = Math.cos(angle) * 20;
    dy = Math.sin(angle) * 20;
  }
  public boolean update(){
    return super.update(dx, dy);
  }
}

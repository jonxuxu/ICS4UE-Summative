package client;
import client.particle.GhostParticle;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

class GhostQAOE extends AOE{
  private ArrayList<GhostParticle> particles = new ArrayList<GhostParticle>();
  GhostQAOE(int x, int y, int radius){
    super (x,y,radius);
  }
  
  public void draw(Graphics2D g2){
    for (int i = 0; i < 10; i++){
      particles.add(new GhostParticle(getX() + getXyAdjust()[0], getY() + getXyAdjust()[1], (int) ((Math.random() * 5 + 5))));
    }
    
    //Draws particles
    for (int i = 0; i < particles.size(); i++) {
      try {
        if (particles.get(i).update()) {
          particles.remove(i);
        } else {
          particles.get(i).render(g2);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
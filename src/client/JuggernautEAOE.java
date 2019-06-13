package client;
import client.particle.JuggernautParticle;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

class JuggernautEAOE extends AOE{
  private ArrayList<JuggernautParticle> particles = new ArrayList<JuggernautParticle>();
  JuggernautEAOE(int x, int y, int radius){
    super (x,y,radius);
  }
  
  public void draw(Graphics2D g2){
    for (int i = 0; i < 100; i++){
      double angle = Math.random() * 2 * Math.PI;
      double radius = getRadius() * Math.sqrt(Math.random());
      int x = (int)(radius * Math.cos(angle) + getX());
      int y = (int)(radius * Math.sin(angle) + getY());
      particles.add(new JuggernautParticle(x + getXyAdjust()[0], y + getXyAdjust()[1], (int) ((Math.random() * 5 + 5))));
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
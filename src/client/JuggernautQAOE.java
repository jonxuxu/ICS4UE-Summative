package client;
import client.particle.JuggernautParticle;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

class JuggernautQAOE extends AOE{
  private ArrayList<JuggernautParticle> particles = new ArrayList<JuggernautParticle>();
  JuggernautQAOE(int x, int y, int radius){
    super (x,y,radius);
  }
  
  public void draw(Graphics2D g2){
    for (int i = 0; i < 10; i++){
      particles.add(new JuggernautParticle(getX() + getXyAdjust()[0], getY() + getXyAdjust()[1], (int) ((Math.random() * 5 + 5))));
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
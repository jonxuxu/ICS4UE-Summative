package client;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.AlphaComposite;
class SafeMarksmanSpaceAOE2 extends AOE{
  SafeMarksmanSpaceAOE2(int x, int y, int radius) {
    super(x,y,radius);
  }
  public void draw(Graphics2D g2){
    g2.setColor(Color.WHITE);
    float alpha = (float)0.5; //draw half transparent
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
    g2.fillOval(getX() + getXyAdjust()[0] - getRadius(), getY() + getXyAdjust()[1] - getRadius(), getRadius() * 2, getRadius() * 2);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)1));
  }
}
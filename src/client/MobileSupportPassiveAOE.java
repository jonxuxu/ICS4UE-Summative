package client;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
class MobileSupportPassiveAOE extends AOE{
  private BufferedImage image;
  MobileSupportPassiveAOE(int x, int y, int radius) {
    super(x,y,radius);
    try {
      image = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/orb/P_blue.png"));
    } catch (IOException e) {
      System.out.println("Unable to find image");
    }
  }
  public void draw(Graphics2D g2) {
    g2.drawImage(image, getX() + getXyAdjust()[0] - getRadius(), getY() + getXyAdjust()[1] - getRadius(), getRadius() * 2, getRadius() * 2 , null);
  }
}
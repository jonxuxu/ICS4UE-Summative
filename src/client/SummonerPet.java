package client;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
class SummonerPet extends AOE{
  private BufferedImage[] animations;
  private static int INDEX = 0;
  SummonerPet(int x, int y, int radius) {
    super(x,y,radius);
    try {
      BufferedImage movementSheet = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/summoner/minion.png"));
      animations = new BufferedImage[3];
      for (int i = 0; i < 3; i++) {
        animations[i] = movementSheet.getSubimage(i * 32, 0, 32, 32);
      }
    } catch (IOException e) {
      System.out.println("Unable to find image");
    }
  }
  public void draw(Graphics2D g2) {
    if (INDEX >= 30){
      INDEX = 0;
    }
    g2.drawImage(animations[INDEX/10], getX() + getXyAdjust()[0] - getRadius(), getY() + getXyAdjust()[1] - getRadius(), getRadius() * 2, getRadius() * 2 , null);
    INDEX++;
    
  }
}
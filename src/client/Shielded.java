package client;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.AlphaComposite;

class Shielded extends Status{
  private BufferedImage image;
  Shielded(){
    try {
      image = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/orb/Shield_Yellow.png"));
    } catch (IOException e) {
      System.out.println("Unable to find image");
    }
  }
  
  public void draw(Graphics2D g2, int playerX, int playerY, int index){
    float alpha = (float)0.5; //draw half transparent
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
    g2.drawImage(image, playerX + getXyAdjust()[0] - getPlayerLength()/2, playerY + getXyAdjust()[1] - getPlayerLength()/2, getPlayerLength(), getPlayerLength(), null);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)1));
  }
}
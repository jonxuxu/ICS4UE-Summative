package client;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

class Stun extends Status{
  private BufferedImage image;
  Stun(){
    try {
      image = ImageIO.read(new File(System.getProperty("user.dir") + "/res/status/stunned.png"));
    } catch (IOException e) {
      System.out.println("Unable to find image");
    }
  }
  
  public void draw(Graphics2D g2, int playerX, int playerY, int index){
    g2.drawImage(image, playerX + getXyAdjust()[0] - getPlayerLength()/2 + index*getLength(), playerY + getXyAdjust()[1] - getPlayerLength()/2 - getLength(), getLength(), getLength(), null);
  }
}
package client;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

class Unstoppable extends Status{
  private BufferedImage image;
  private static int LENGTH = 10;
  Unstoppable(){
    try {
      image = ImageIO.read(new File(System.getProperty("user.dir") + "/res/status/invincible.png"));
    } catch (IOException e) {
      System.out.println("Unable to find image");
    }
  }
  
  public void draw(Graphics2D g2, int playerX, int playerY, int index){
    g2.drawImage(image, playerX + getXyAdjust()[0] - 60/2 + index*LENGTH, playerY + getXyAdjust()[1] - 60/2 - LENGTH, LENGTH, LENGTH, null);
  }
}
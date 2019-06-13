package client;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * Artifact.java
 * This is the artifact class for the object that the teams must bring together to win the game
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-13
 */

public class Artifact {
   private boolean isHeld;
   private int[] xy = new int[2];
   private int teamNumber;
   private static Image[] artifactImage = new Image[2];

   Artifact(int x, int y, int teamNumber) {
      this.xy[0] = x;
      this.xy[1] = y;
      System.out.println("wWw"+x+" "+y);
      this.teamNumber = teamNumber;
      if (artifactImage[0] == null) {
         try {
            artifactImage[0] = ImageIO.read(new File(".\\res\\HalfLeaf0.png"));
            artifactImage[1] = ImageIO.read(new File(".\\res\\HalfLeaf1.png"));
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public int[] getXy() {
      return xy;
   }

   public void drawArtifact(Graphics2D g2, int[] xyAdjust) {
      g2.setColor(Color.YELLOW);
     // g2.drawRect(xy[0] + xyAdjust[0] - 19, xy[1] + xyAdjust[1] - 21, 100, 100);
      g2.drawImage(artifactImage[teamNumber], xy[0] + xyAdjust[0] - 19, xy[1] + xyAdjust[1] - 21, 38, 42, null);
   }
}

package client;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * SafeMarksman.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public class SafeMarksman extends Player {
   private int maxHealth = 100;
   private int attack = 10;
   private int mobility = 10;
   private int range = 10;
   private int positionIndex;
   private int movementIndex;
   private boolean moving;
   private int[] animationIndex = {80, 80, 80};
   private int[] ANIMATION_LENGTH = {80, 80, 80};
   private BufferedImage[][][] ALL_ANIMATIONS = new BufferedImage[4][][];

   SafeMarksman(String username) {
      super(username);
      try {
         BufferedImage movementSheet = ImageIO.read(new File(".\\res\\SafeMarksman.png"));
         ALL_ANIMATIONS[0] = new BufferedImage[4][3];
         for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
               ALL_ANIMATIONS[0][j][i] = movementSheet.getSubimage(j * 32, i * 32, 32, 32);
               //i refers to row number, j refers to column number
            }
         }
         /*
         BufferedImage animationSheet = ImageIO.read(new File(".\\res\\C.png"));
         ALL_ANIMATIONS[1] = new BufferedImage[4];
         for (int i = 0; i < 4; i++) {
            ALL_ANIMATIONS[1][i] = animationSheet.getSubimage(0, i * 15, 15, 15);
         }
         */
      } catch (IOException e) {
         System.out.println("Unable to find image");
      }
   }

   public void spellAnimation(Graphics2D g2, int x, int y, int width, int height, int spellIndex) {
      if (animationIndex[spellIndex] < ANIMATION_LENGTH[spellIndex]) {
         g2.drawImage(ALL_ANIMATIONS[spellIndex + 1][animationIndex[spellIndex] % 4][0], x, y, width, height, null);
         animationIndex[spellIndex]++;
      }
   }

   public void resetSpell(int spellIndex) {
      animationIndex[spellIndex] = 0;
   }

   public void setMovementIndex(int positionIndex, boolean moving) {
     this.moving=moving;
      this.positionIndex = positionIndex;
   }

   public void move(Graphics2D g2, int x, int y, int width, int height) {
      if (moving) {
         movementIndex++;//Alternates between 1 and 0
      }
      if (movementIndex==10){
         movementIndex=0;
      }
      g2.drawImage(ALL_ANIMATIONS[0][positionIndex][movementIndex/5], x, y, width, height, null);
   }

   public void drawReal(Graphics2D g2, int x, int y, int width, int height, int spellIndex) {
      if (spellIndex != -1) {
         resetSpell(spellIndex);
         spellAnimation(g2, x, y, width, height, spellIndex);
      } else {
         if (animationIndex[0] != ANIMATION_LENGTH[0]) {
            spellAnimation(g2, x, y, width, height, 0);
         } else if (animationIndex[1] != ANIMATION_LENGTH[1]) {
            spellAnimation(g2, x, y, width, height, 1);
         } else if (animationIndex[2] != ANIMATION_LENGTH[2]) {
            spellAnimation(g2, x, y, width, height, 2);
         } else {
            move(g2, x, y, width, height);
         }
      }
   }
}

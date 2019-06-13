package client;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * SafeMarksman.java
 * This is the character class for SafeMarksman
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-19
 */

public class SafeMarksman extends Player {
   private int positionIndex;
   private int movementIndex;
   private boolean moving;
   private int attackIndex;
   private boolean attacking;
   private int[] animationIndex = {80, 80, 80};
   private int[] ANIMATION_LENGTH = {80, 80, 80};
   private BufferedImage[][][] ALL_ANIMATIONS = new BufferedImage[5][][];
   //private BufferedImage[] SPELL_SPRITES = new BufferedImage[3];

   SafeMarksman(String username, CustomMouseAdapter myMouseAdapter) {
      super(username, myMouseAdapter);
      try {
         //walking and attack sprites
         BufferedImage movementSheet = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/archer/C_archer.png"));
         ALL_ANIMATIONS[0] = new BufferedImage[3][4];
         for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
               ALL_ANIMATIONS[0][j][i] = movementSheet.getSubimage(j * 32, i * 32, 32, 32);
               //i refers to row number, j refers to column number
            }
         }
         //animations [1] and up are for spell sprites
         //BufferedImage arrow1 = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/archer/P_arrow1.png"));
         //ALL_ANIMATIONS[1][0][0] = arrow1;
         //BufferedImage arrow2 = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/archer/P_arrow2.png"));
         //ALL_ANIMATIONS[2][0][0] = arrow2;
         //BufferedImage shield = ImageIO.read(new File(System.getProperty("user.dir") + "/res/characters/archer/shield_blue.png"));
         //ALL_ANIMATIONS[3][0][0] = shield;
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
      this.moving = moving;
      this.positionIndex = positionIndex;
   }

   public void setAttackIndex(int attackIndex, boolean attacking) {
      this.attacking = attacking;
      this.attackIndex = attackIndex;
   }

   public void move(Graphics2D g2, int x, int y, int width, int height) {
      if (moving) {
         movementIndex++;
      }
      if (movementIndex == 10) {
         movementIndex = 0;
      }
      g2.drawImage(ALL_ANIMATIONS[0][movementIndex / 5][positionIndex], x, y, width, height, null);
   }

   public void attack(Graphics2D g2, int x, int y, int width, int height) {
      if (attacking) {
         attackIndex++;
      }
      if (attackIndex == 20) {
         attackIndex = 0;
      }
      g2.drawImage(ALL_ANIMATIONS[0][2][positionIndex], x, y, width, height, null);
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
         } else if (this.getMouse() == true) {
            attack(g2, x, y, width, height);
         } else {
            move(g2, x, y, width, height);
         }
      }
   }
}

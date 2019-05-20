import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * TestClass.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public class TestClass extends GeneralClass {
   private int maxHealth = 100;
   private int attack = 10;
   private int mobility = 10;
   private int range = 10;
   private int animation1Index = 0;
   private int spell1Cooldown = 500;//Measured in ticks

   private BufferedImage[][] allAnimations = new BufferedImage[4][];

   TestClass() {
      try {
         BufferedImage movementSheet = ImageIO.read(new File(".\\res\\LightningRoomba.png"));
         allAnimations[0] = new BufferedImage[12];
         for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
               allAnimations[0][(i * 3) + j] = movementSheet.getSubimage(j * 15, i * 15, 15, 15);
            }
         }
         BufferedImage animationSheet = ImageIO.read(new File(".\\res\\LightningRoomba.png"));
         allAnimations[1] = new BufferedImage[4];
         for (int i = 0; i < 4; i++) {
            allAnimations[1][i] = animationSheet.getSubimage(0, i * 15, 15, 15);
         }
      } catch (IOException e) {
         System.out.println("Unable to find image");
      }
   }

   public void spell1(Graphics2D g2, int x, int y, int width, int height) {
      if (animation1Index < 80) {
         g2.drawImage(allAnimations[1][animation1Index % 4], x, y, width, height, null);
         animation1Index++;
      }
   }

   public void resetSpell1() {
      animation1Index = 0;
   }

   public void move(Graphics2D g2, int x, int y, int width, int height) {
      g2.drawImage(allAnimations[0][2], x, y, width, height, null);
   }

   public void drawReal(Graphics2D g2, int x, int y, int width, int height, boolean animation1) {
      if (animation1) {
         resetSpell1();
         spell1(g2, x, y, width, height);
      } else {
         if (animation1Index != 80) {
            spell1(g2, x, y, width, height);
         } else {
            move(g2, x, y, width, height);
         }
      }
   }
}

import java.awt.Graphics2D;

/**
 * GeneralClass.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public abstract class GeneralClass {
   public abstract void spell1(Graphics2D g2, int x, int y, int width, int height);
   //public abstract void spell2(Graphics2D g2, int x, int y, int width, int height);
 //  public abstract void spell3(Graphics2D g2, int x, int y, int width, int height);

   public abstract void move(Graphics2D g2, int x, int y, int width, int height);
   public abstract void drawReal(Graphics2D g2, int x, int y, int width, int height, boolean animation1) ;
}

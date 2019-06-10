package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * AOE.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class AOE {
   private int ID;
   private int x;
   private int y;
   private int radius;
   private Area area;
   private static int[] xyAdjust;

   AOE(int ID, int x, int y, int radius) {
      this.ID = ID;
      this.x = x;
      this.y = y;
      this.radius = radius;
   }

   public void draw(Graphics2D g2) {
      if (ID!=0) {
         g2.setColor(Color.WHITE);
         g2.fillOval(x + xyAdjust[0] - radius, y + xyAdjust[1] - radius, radius * 2, radius * 2);
      }
   }

   public static void setXyAdjust(int[] xyAdjust1) {
      xyAdjust = xyAdjust1;
   }
   public int[] getXyAdjust(){
     return xyAdjust;
   }
   public int getID(){
      return ID;
   }
   public Area getArea(){
      //if (ID==0) {
         return (new Area(new Ellipse2D.Double(x + xyAdjust[0] - radius, y + xyAdjust[1] - radius, radius * 2, radius * 2)));
      //}
   }
}
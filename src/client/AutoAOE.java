package client;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * TimeMageAOE.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class AutoAOE extends AOE{
   private int[][] points;

   AutoAOE(int id, int[][] points) {
     super(id, 0, 0, 0);
     this.points = points;
   }
   
   @Override
   public void draw(Graphics2D g2) {
      g2.setColor(Color.WHITE);
      int[][] temp = new int[2][4];
      for (int i = 0; i < points.length; i++){
        for (int j = 0; j < points[i].length; j++){
          temp[i][j] = points[i][j]+getXyAdjust()[i];
        }
      }
      g2.fillPolygon(temp[0], temp[1], 4);//REE Might need xyAdjust?
   }
}
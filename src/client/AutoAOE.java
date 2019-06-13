package client;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * TimeMageAOE.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */
public class AutoAOE extends AOE{
   private int[][] points;
   /**
     * Constructor
     *
     * @param id used for communication between server and client, used to determine kind of AOE
     * @param points corners of the square for the melee AOE
     */
   AutoAOE(int[][] points) {
     super(0, 0, 0);
     this.points = points;
   }

   /**
    * Draws the square Area Of Effect - can be inherited to change effects
    *
    * @param g2 graphics
    */
   @Override
   public void draw(Graphics2D g2) {
     /*
      g2.setColor(Color.WHITE);
      int[][] temp = new int[2][4];
      for (int i = 0; i < points.length; i++){
        for (int j = 0; j < points[i].length; j++){
          temp[i][j] = points[i][j]+getXyAdjust()[i];
        }
      }
      g2.fillPolygon(temp[0], temp[1], 4);//REE Might need xyAdjust?*/
   }
}
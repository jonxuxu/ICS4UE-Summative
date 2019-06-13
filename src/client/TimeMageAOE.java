package client;

import java.awt.Color;
import java.awt.Graphics2D;
import client.particle.TimeMageParticle;
import java.util.ArrayList;

/**
 * TimeMageAOE.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public class TimeMageAOE extends AOE{
   private int[][] points;
   private ArrayList<TimeMageParticle> particles = new ArrayList<TimeMageParticle>();

   TimeMageAOE(int[][] points) {
     super(0, 0, 0);
     this.points = points;
   }
   
   @Override
   public void draw(Graphics2D g2){
      int[][] temp = new int[2][4];
      for (int i = 0; i < points.length; i++){
        for (int j = 0; j < points[i].length; j++){
          temp[i][j] = points[i][j]+getXyAdjust()[i];
        }
      }
      int[][][] triangles = new int[2][2][3];
      triangles[0][0][0] = temp[0][0];
      triangles[1][0][0] = temp[1][0];
      triangles[0][0][1] = temp[0][1];
      triangles[1][0][1] = temp[1][1];
      triangles[0][0][2] = temp[0][2];
      triangles[1][0][2] = temp[1][2];
      
      triangles[0][1][0] = temp[0][2];
      triangles[1][1][0] = temp[1][2];
      triangles[0][1][1] = temp[0][3];
      triangles[1][1][1] = temp[1][3];
      triangles[0][1][2] = temp[0][0];
      triangles[1][1][2] = temp[1][0];
      /*
      g2.setColor(Color.WHITE);
      g2.drawPolygon(triangles[0][0],triangles[1][0],3);
      
      g2.setColor(Color.BLUE);
      g2.drawPolygon(triangles[0][1],triangles[1][1],3);*/
      
      int triangle = 0;
      for (int i = 0; i < 50; i++){
        double rootR1 = Math.sqrt(Math.random());
        double r2 = Math.random();
        int x = (int)((1-rootR1) * triangles[0][triangle][0] + (rootR1 * (1-r2)) * triangles[0][triangle][1] + (r2*rootR1)*triangles[0][triangle][2]);
        int y = (int)((1-rootR1) * triangles[1][triangle][0] + (rootR1 * (1-r2)) * triangles[1][triangle][1] + (r2*rootR1)*triangles[1][triangle][2]);
        particles.add(new TimeMageParticle(x, y, (int) ((Math.random() * 5 + 5))));
      }
      triangle = 1;
      for (int i = 0; i < 50; i++){
        double rootR1 = Math.sqrt(Math.random());
        double r2 = Math.random();
        int x = (int)((1-rootR1) * triangles[0][triangle][0] + (rootR1 * (1-r2)) * triangles[0][triangle][1] + (r2*rootR1)*triangles[0][triangle][2]);
        int y = (int)((1-rootR1) * triangles[1][triangle][0] + (rootR1 * (1-r2)) * triangles[1][triangle][1] + (r2*rootR1)*triangles[1][triangle][2]);
        particles.add(new TimeMageParticle(x, y, (int) ((Math.random() * 5 + 5))));
      }
      for (int i = 0; i < particles.size(); i++) {
        try {
          if (particles.get(i).update()) {
            particles.remove(i);
          } else {
            particles.get(i).render(g2);
          }
        }  catch (Exception e) {
          e.printStackTrace();
        }
      }
   }
}
package server;

import java.util.ArrayList;

/**
 * CustomPolygon.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-06-05
 */

public class CustomPolygon {
   private int pointNum;
   private int[][] points;
   private int[][] vectors;
   private int[] intersection = new int[2];
   private int[] savedIntersection = new int[2];
   private double[] xyVector = new double[2];
   private int magnitude;
   private int FLASHLIGHT_RADIUS = 200;

   private int xCo;//x coefficient
   private int yCo;//y coefficient
   private int cVal; //last value

   private double tVal;
   private int intersectingVectorIndex;

   CustomPolygon(int[] xPoints, int[] yPoints, int pointNum) {
      this.pointNum = pointNum;
      //POINTS SHOULD BE GIVEN IN ORDER. (either clockwise or counterclockwise is fine)
      points = new int[pointNum][2];
      vectors = new int[pointNum][2];
      for (int i = 0; i < pointNum; i++) {
         points[i][0] = xPoints[i];
         points[i][1] = yPoints[i];
         if (i < pointNum - 1) {
            vectors[i][0] = xPoints[i + 1] - xPoints[i];
            vectors[i][1] = yPoints[i + 1] - yPoints[i];
         } else {
            vectors[i][0] = xPoints[0] - xPoints[i]; //Cycles back to the first point
            vectors[i][1] = yPoints[0] - yPoints[i];
         }
      }
   }

   public void setPlayerScalar(int xCo, int yCo, int cVal) { //player is initial, final is mouse
      this.xCo = xCo;
      this.yCo = yCo;
      this.cVal = cVal;
   }
   public void setPlayerVector(double xVector, double yVector) { //player is initial, final is mouse
      this.xyVector[0] = xVector;
      this.xyVector[1] = yVector;
   }

   public void setCenter(int[] xPoints, int []yPoints){
      for (int i = 0; i < pointNum; i++) {
         points[i][0] = xPoints[i];
         points[i][1] = yPoints[i];
         if (i < pointNum - 1) {
            vectors[i][0] = xPoints[i + 1] - xPoints[i];
            vectors[i][1] = yPoints[i + 1] - yPoints[i];
         } else {
            vectors[i][0] = xPoints[0] - xPoints[i]; //Cycles back to the first point
            vectors[i][1] = yPoints[0] - yPoints[i];
         }
      }
   }

   public boolean intersect(int[] playerXy) {
      //No need to check the entire shape? thought this is easier
      intersectingVectorIndex = -1;
      tVal = -1;
      for (int i = 0; i < pointNum; i++) {
         double possibleT = -1.0 * (cVal + yCo * points[i][1] + xCo * points[i][0]) / (xCo * vectors[i][0] + yCo * vectors[i][1]);
         if ((possibleT <= 1) && (possibleT >= 0)) {
            intersection[0] = (int) (points[i][0] + vectors[i][0] * possibleT);
            intersection[1] = (int) (points[i][1] + vectors[i][1] * possibleT);
            if ((((intersection[0] - playerXy[0]) * xyVector[0]) >= 0) && (((intersection[1] - playerXy[1]) * xyVector[1]) >= 0)) {
               int distance = ((intersection[0] - playerXy[0]) * (intersection[0] - playerXy[0]) + (intersection[1] - playerXy[1]) * (intersection[1] - playerXy[1]));
               if (distance < magnitude) {
                  tVal = possibleT;
                  magnitude = distance;
                  intersectingVectorIndex = i;
                  savedIntersection[0] = intersection[0];
                  savedIntersection[1] = intersection[1];
               }
            }
            // System.out.println("a"+intersection[0]+" "+intersection[1]);
            //System.out.println("d"+distance);
         }
      }
      if (tVal != -1) {
         return (true);
      } else {
         return (false);
      }
   }

   public int[] getIntersect() {
      return (savedIntersection);
   }

   public void setVectorMagnitude(double angle) {
      magnitude = (int) (FLASHLIGHT_RADIUS / Math.cos(angle));
      magnitude = magnitude * magnitude;//Because it must be squared
   }

   public int getIntersectionIndex() { //If these are both the same, then no point is added.
      //Have a variable which is set as the previous angle. If there is no
      return (intersectingVectorIndex);
   }
}

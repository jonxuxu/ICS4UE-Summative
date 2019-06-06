package client;

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
   int pointNum;
   int[][] points;
   int[][] vectors;
   int[] intersection = new int[2];
   int[] savedIntersection = new int[2];
   double[] xyVector = new double[2];

   int xCo;//x coefficient
   int yCo;//y coefficient
   int cVal; //last value

   double tVal;
   int intersectingVectorIndex;

   CustomPolygon(int pointNum, ArrayList<Integer> xPoints, ArrayList<Integer> yPoints) {
      this.pointNum = pointNum;
      //POINTS SHOULD BE GIVEN IN ORDER. (either clockwise or counterclockwise is fine)
      points = new int[pointNum][2];
      vectors = new int[pointNum][2];
      for (int i = 0; i < pointNum; i++) {
         points[i][0] = xPoints.get(i);
         points[i][1] = yPoints.get(i);
         if (i < pointNum - 1) {
            vectors[i][0] = xPoints.get(i + 1) - xPoints.get(i);
            vectors[i][1] = yPoints.get(i + 1) - yPoints.get(i);
         } else {
            vectors[i][0] = xPoints.get(0) - xPoints.get(i); //Cycles back to the first point
            vectors[i][1] = yPoints.get(0) - yPoints.get(i);
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

   public boolean intersect(int[] playerXy) {
      //No need to check the entire shape? thought this is easier
      intersectingVectorIndex = -1;
      int smallestDistance = 40000;//The range of the flashlight squared is this parameter
      tVal = -1;
      for (int i = 0; i < pointNum; i++) {
         double possibleT = -1.0 * (cVal + yCo * points[i][1] + xCo * points[i][0]) / (xCo * vectors[i][0] + yCo * vectors[i][1]);
         if ((possibleT <= 1) && (possibleT >= 0)) {
            intersection[0] = (int) (points[i][0] + vectors[i][0] * possibleT);
            intersection[1] = (int) (points[i][1] + vectors[i][1] * possibleT);
            if ((((intersection[0] - playerXy[0]) * xyVector[0]) >= 0) && (((intersection[1] - playerXy[1]) * xyVector[1]) >= 0)) {
               int distance = ((intersection[0] - playerXy[0]) * (intersection[0] - playerXy[0]) + (intersection[1] - playerXy[1]) * (intersection[1] - playerXy[1]));
               if (distance < smallestDistance) {
                  tVal = possibleT;
                  smallestDistance = distance;
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

   public int getIntersectionIndex() { //If these are both the same, then no point is added.
      //Have a variable which is set as the previous angle. If there is no
      return (intersectingVectorIndex);
   }
}

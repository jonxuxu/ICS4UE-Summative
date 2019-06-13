package client.map;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * FogMap.java
 * This is responsible for keeping track of team fog of war
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-02
 */

public class FogMap {
   private Area fogShape, viewedShape, activelyViewing, outputShape;
   private static double fogRadius = 250;
   private int MAP_WIDTH, MAP_HEIGHT;

   /**
    * This is the constrcutor to intialize the size of the map
    * @param xy Initial xy of player
    * @param MAP_WIDTH Width of map in pixels
    * @param MAP_HEIGHT Height of map in pixels
    */
   public FogMap(int[] xy, int MAP_WIDTH, int MAP_HEIGHT) {
      double x = xy[0];
      double y = xy[1];
      this.MAP_WIDTH = MAP_WIDTH;
      this.MAP_HEIGHT = MAP_HEIGHT;
      fogShape = new Area(new Rectangle.Double(-MAP_WIDTH , -MAP_HEIGHT , 3 * MAP_WIDTH , 3 * MAP_HEIGHT )); // Goes over bounds to account for edges
      activelyViewing = new Area(new Ellipse2D.Double(x, y, fogRadius , fogRadius ));
   }

   /**
    * This method increases the team fog of war
    * @param xy The xy of a player
    */
   public void scout(int[] xy) {
      Area circle = new Area(new Ellipse2D.Double((xy[0] - fogRadius) , (xy[1] - fogRadius) , 2 * fogRadius , 2 * fogRadius ));
      activelyViewing.add(circle);
   }

   /**
    * This method outputs the dark team fog of war (unexplored areas)
    * @param scope The zoom amount of the resultant fog
    * @return ouputShape The area of the black fog
    */
   public Area getFog(int scope) {
      fogShape.subtract(activelyViewing);
      if (scope == 1) { //Minimap scope
         return fogShape;
      } else { // Visible only in window
         outputShape = new Area(new Rectangle.Double(0, 0, MAP_WIDTH , MAP_HEIGHT ));
         outputShape.intersect(fogShape);
         return outputShape;
      }

   }

   /**
    * This method outputs the light team fog of war (previously explored)
    * @param scope The zoom amount of the resultant fog
    * @return ouputShape The area of the light fog
    */
   public Area getExplored(int scope) {
      viewedShape = new Area(new Rectangle.Double(0, 0, MAP_WIDTH , MAP_HEIGHT ));
      viewedShape.subtract(fogShape);
      viewedShape.subtract(activelyViewing);
      activelyViewing.reset();
      return viewedShape;
   }
}

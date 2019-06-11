package client.map;
/*
import javafx.scene.shape.Circle;
import sun.java2d.loops.FillRect;
*/

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

public class FogMap {
   private Area fogShape, viewedShape, activelyViewing, outputShape;
   private static double fogRadius = 250;
   private int MAP_WIDTH, MAP_HEIGHT;

   public FogMap(int[] xy, int MAP_WIDTH, int MAP_HEIGHT) {
      //fog = new int[y][x];
      //currentlyExploring = new boolean[y][x];

      double x = xy[0];
      double y = xy[1];
      this.MAP_WIDTH = MAP_WIDTH;
      this.MAP_HEIGHT = MAP_HEIGHT;
      fogShape = new Area(new Rectangle.Double(-MAP_WIDTH , -MAP_HEIGHT , 3 * MAP_WIDTH , 3 * MAP_HEIGHT )); // Goes over bounds to account for edges
      activelyViewing = new Area(new Ellipse2D.Double(x, y, fogRadius , fogRadius ));
   }

   public void scout(int[] xy) {
      Area circle = new Area(new Ellipse2D.Double((xy[0] - fogRadius) , (xy[1] - fogRadius) , 2 * fogRadius , 2 * fogRadius ));
      activelyViewing.add(circle);
   }

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

   public Area getExplored(int scope) {
      viewedShape = new Area(new Rectangle.Double(0, 0, MAP_WIDTH , MAP_HEIGHT ));
      viewedShape.subtract(fogShape);
      viewedShape.subtract(activelyViewing);
      activelyViewing.reset();
      return viewedShape;
   }
}

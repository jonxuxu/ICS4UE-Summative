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
   private double SCALING;
   private static double fogRadius = 250;
   private int MAP_WIDTH, MAP_HEIGHT;

   public FogMap(int[] xy, double SCALING, int MAP_WIDTH, int MAP_HEIGHT) {
      //fog = new int[y][x];
      //currentlyExploring = new boolean[y][x];
      this.SCALING = SCALING;

      double x = xy[0] * SCALING;
      double y = xy[1] * SCALING;
      this.MAP_WIDTH=MAP_WIDTH;
      this.MAP_HEIGHT=MAP_HEIGHT;
      fogShape = new Area(new Rectangle.Double(0, 0, MAP_WIDTH*SCALING, MAP_HEIGHT*SCALING));
      activelyViewing = new Area(new Ellipse2D.Double(x, y, fogRadius*SCALING, fogRadius*SCALING));
   }

   public void scout(int[] xy) {
      Area circle = new Area(new Ellipse2D.Double((xy[0]-fogRadius)*SCALING, (xy[1]-fogRadius) *SCALING, 2*fogRadius*SCALING, 2*fogRadius*SCALING));
      activelyViewing.add(circle);
   }

   public Area getFog(int scope) {
      fogShape.subtract(activelyViewing);
      if(scope == 1){ //Minimap scope
         outputShape = new Area(new Rectangle.Double(0, 0, 2*MAP_WIDTH*SCALING, 2*MAP_WIDTH*SCALING));
      } else { // Visible only in window
         outputShape = new Area(new Rectangle.Double(0, 0, MAP_WIDTH*SCALING, MAP_HEIGHT*SCALING));
      }
      outputShape.intersect(fogShape);
      return outputShape;
   }

   public Area getExplored(int scope){
      viewedShape = new Area(new Rectangle.Double(0, 0, MAP_WIDTH*SCALING, MAP_HEIGHT*SCALING));
      viewedShape.subtract(fogShape);
      viewedShape.subtract(activelyViewing);
      activelyViewing.reset();
      if(scope == 1){ // Minimap scope
         outputShape = new Area(new Rectangle.Double(0, 0, 2*MAP_WIDTH*SCALING, 2*MAP_WIDTH*SCALING));
      } else { // Only within window
         outputShape = new Area(new Rectangle.Double(0, 0, MAP_WIDTH*SCALING, MAP_HEIGHT*SCALING));
      }
      outputShape.intersect(viewedShape);
      return outputShape;
   }
}

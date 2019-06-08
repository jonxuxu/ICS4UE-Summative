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

   public FogMap(int[] xy, double SCALING) {
      //fog = new int[y][x];
      //currentlyExploring = new boolean[y][x];
      this.SCALING = SCALING;

      double x = xy[0] * SCALING;
      double y = xy[1] * SCALING;
      fogShape = new Area(new Rectangle.Double(0, 0, 10000*SCALING, 10000*SCALING));
      activelyViewing = new Area(new Ellipse2D.Double(x, y, fogRadius*SCALING, fogRadius*SCALING));
   }

   public void scout(int[] xy) {
      Area circle = new Area(new Ellipse2D.Double((xy[0]-fogRadius)*SCALING, (xy[1]-fogRadius) *SCALING, 2*fogRadius*SCALING, 2*fogRadius*SCALING));
      activelyViewing.add(circle);
   }

   public Area getFog() {
      fogShape.subtract(activelyViewing);
      outputShape = new Area(new Rectangle.Double(0, 0, 10000*SCALING, 10000*SCALING));
      outputShape.intersect(fogShape);
      return outputShape;
   }

   public Area getExplored(){
      viewedShape = new Area(new Rectangle.Double(0, 0, 10000*SCALING, 10000*SCALING));
      viewedShape.subtract(fogShape);
      viewedShape.subtract(activelyViewing);
      activelyViewing.reset();
      outputShape = new Area(new Rectangle.Double(0, 0, 10000*SCALING, 10000*SCALING));
      outputShape.intersect(viewedShape);
      return outputShape;
   }
}

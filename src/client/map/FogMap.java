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
   private double scaling;
   private static double fogRadius = 250;

   public FogMap(int[] xy, double scaling) {
      //fog = new int[y][x];
      //currentlyExploring = new boolean[y][x];
      this.scaling = scaling;

      double x = xy[0] * scaling;
      double y = xy[1] * scaling;
      fogShape = new Area(new Rectangle.Double(0, 0, 10000*scaling, 10000*scaling));
      activelyViewing = new Area(new Ellipse2D.Double(x, y, fogRadius*scaling, fogRadius*scaling));
   }

   public void scout(int[] xy) {
      Area circle = new Area(new Ellipse2D.Double((xy[0]-fogRadius)*scaling, (xy[1]-fogRadius) *scaling, 2*fogRadius*scaling, 2*fogRadius*scaling));
      activelyViewing.add(circle);
   }

   public Area getFog() {
      fogShape.subtract(activelyViewing);
      outputShape = new Area(new Rectangle.Double(0, 0, 10000*scaling, 10000*scaling));
      outputShape.intersect(fogShape);
      return outputShape;
   }

   public Area getExplored(){
      viewedShape = new Area(new Rectangle.Double(0, 0, 10000*scaling, 10000*scaling));
      viewedShape.subtract(fogShape);
      viewedShape.subtract(activelyViewing);
      activelyViewing.reset();
      outputShape = new Area(new Rectangle.Double(0, 0, 10000*scaling, 10000*scaling));
      outputShape.intersect(viewedShape);
      return outputShape;
   }
}

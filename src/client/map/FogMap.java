package client.map;
/*
import javafx.scene.shape.Circle;
import sun.java2d.loops.FillRect;
*/

import sun.font.GraphicComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

public class FogMap {
   private Area fogShape, viewedShape, activelyViewing, outputShape;
   private static int fogRadius = 250;
   private int WIDTH, HEIGHT;

   public FogMap(int[] xy, int MAP_WIDTH, int MAP_HEIGHT,int  WIDTH, int HEIGHT) {
      int x = xy[0];
      int y = xy[1];
      this.WIDTH = MAP_WIDTH;
      this.HEIGHT = MAP_HEIGHT;
      outputShape = new Area(new Rectangle(0, 0, WIDTH, HEIGHT));
      fogShape = new Area(new Rectangle(0,0 , MAP_WIDTH/1000, MAP_HEIGHT/1000)); // Goes over bounds to account for edges
      activelyViewing = new Area(new Ellipse2D.Float(x/1000f, y/1000f, fogRadius/1000f, fogRadius/1000f));
   }

   public void scout(int[] xy) {
      Area circle = new Area(new Ellipse2D.Float((xy[0] - fogRadius)/1000f, (xy[1] - fogRadius)/1000f , 2 * fogRadius /1000f, 2 * fogRadius /1000f));
      activelyViewing.add(circle);
   }

   public void drawFog(Graphics2D g2, int[] xyAdjust){
      AffineTransform tx = new AffineTransform();
      tx.translate(xyAdjust[0], xyAdjust[1]);
      tx.scale(1000, 1000);
      g2.setClip(outputShape);

      g2.setColor(Color.black);
      fogShape.subtract(activelyViewing);
      g2.fill(fogShape.createTransformedArea(tx));

      g2.setColor(new Color(0, 0, 0, 128));
      viewedShape = new Area(new Rectangle(0, 0, WIDTH , HEIGHT ));
      viewedShape.subtract(activelyViewing.createTransformedArea(tx));
      activelyViewing.reset();
      g2.fill(viewedShape);
      //g2.dispose();
   }
/*
   public Area getFog(int scope) {
      fogShape.subtract(activelyViewing);
      if (scope == 1) { //Minimap scope
         return fogShape;
      } else { // Visible only in window
         outputShape = new Area(new Rectangle(0, 0, MAP_WIDTH , MAP_HEIGHT ));
         outputShape.intersect(fogShape);
         return outputShape;
      }

   }

   public Area getExplored(int scope) {
      viewedShape = new Area(new Rectangle(0, 0, MAP_WIDTH , MAP_HEIGHT ));
      viewedShape.subtract(fogShape);
      viewedShape.subtract(activelyViewing);
      activelyViewing.reset();
      return viewedShape;
   } */
}

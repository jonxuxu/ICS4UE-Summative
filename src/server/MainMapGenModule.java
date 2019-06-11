package server;

import javax.imageio.ImageIO;


import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

class MainMapGenModule {
   private Disp display;
   private MapGen gen;

   private int loopRadiusSize = 5000;
   private double ellipticalAdjust = 1.75;
   private int nodeGenRange = 3750;
   private double nodeGenStDev = 0.5;
   private File newImageFile = new File("Map.png");
   private BufferedImage mapImage;
   private Socket socket;

   MainMapGenModule() {

      String config = "";

      gen = new MapGen(7500, 5000, ellipticalAdjust);

      if (config == "test") {
         gen.configueScenario1();
         gen.generateRegions();
      } else {
         gen.generateMap2(40, loopRadiusSize, nodeGenRange, nodeGenStDev);
         gen.tetherAllNodes2();
         gen.makeNodesElliptical();
         gen.generateRegions();
         gen.smokeTrees(7500, 1000, 0, false);
         gen.smokeRocks(7500, 100, true);
         gen.makeObstaclesElliptical();
         gen.genClearingByNum(8, 500);
         gen.purgeRedundanices();
         gen.generateCrevices(2);
      }
      display = new Disp();
      //this.add(display);
      //  display.repaint();
      display.paintImage();
      System.out.println("done");
   }


   class Disp {

      private void drawOvalCustom(int radius, Graphics g) {
         g.drawOval(-radius, -radius, radius * 2, radius * 2);
      }

      private void fillOvalCustom(int radius, int xOffset, int yOffset, Graphics g) {
         g.fillOval(xOffset - radius, yOffset - radius, radius * 2, radius * 2);
      }

      private void fillOvalCustom(int radius, double eAdjust, Graphics g) {
         g.fillOval((int) (-radius * eAdjust), -radius, (int) (radius * 2 * eAdjust), radius * 2);
      }

      private void drawLineCustom(Point start, Point end, Graphics g) {
         g.drawLine(start.x, start.y, end.x, end.y);
      }

      public void paintImage() {
         //6000 by 4000
         mapImage = new BufferedImage(6000, 4000, BufferedImage.TYPE_INT_RGB);
         Graphics g = mapImage.createGraphics();
         //this.setCenter(g);
         g.translate(3000, 2000);
         Graphics2D g2 = (Graphics2D) (g);
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.scale(0.2, 0.2);

         if (!gen.testingState) {
            g.setColor(Color.BLUE);
            this.fillOvalCustom(loopRadiusSize, ellipticalAdjust, g);
            g.setColor(Color.GREEN);
            this.fillOvalCustom(loopRadiusSize - nodeGenRange / 2, ellipticalAdjust, g);
            this.fillOvalCustom(loopRadiusSize + nodeGenRange / 2, ellipticalAdjust, g);
            g.setColor(new Color(0, 80, 0));
            this.fillOvalCustom(7500, ellipticalAdjust, g);
         }
         g.fillRect(-15000, -10000, 30000, 20000);

         g.setColor(Color.RED);

         if (gen.regionLayer != null) {
            for (int idx = 0; idx < gen.regionLayer.regions.size(); idx++) {
               if (gen.regionLayer.regions.get(idx).regionType.equals("crevice")) {
                  g.setColor(Color.BLACK);
               } else if (gen.regionLayer.regions.get(idx).regionType.equals("swamp")) {
                  g.setColor(new Color(0, 50, 0));
               } else {
                  g.setColor(new Color(0, 100, 0));
               }
               if (gen.regionLayer.regions.get(idx).regionType.equals("road")) {
                  g.setColor(new Color(150, 97, 37));
                  g.fillPolygon(gen.regionLayer.regions.get(idx));
               } else {
                  g.fillPolygon(gen.regionLayer.regions.get(idx));
               }
            }
         }

         for (int i = 0; i < gen.nodes.size(); i++) {
//        g.fillOval((int)gen.nodes.get(i).getPoint().getX() - 5,
//        		(int)gen.nodes.get(i).getPoint().getY() - 5,50,50);
            if (gen.nodes.get(i).isClearing) {
               g.setColor(new Color(150, 97, 37));
               this.fillOvalCustom(gen.nodes.get(i).clearingSize, gen.nodes.get(i).location.x,
                       gen.nodes.get(i).location.y, g);
            } else {
               this.fillOvalCustom(50, gen.nodes.get(i).location.x, gen.nodes.get(i).location.y, g);
            }
            for (int j = 0; j < gen.nodes.get(i).connections.size(); j++) {
               this.drawLineCustom(gen.nodes.get(i).location, gen.nodes.get(i).connections.get(j), g);
            }

         }

         for (int i = 0; i < gen.obstacles.size(); i++) {
            if (gen.obstacles.get(i).type.equals("TREE")) {
               g.setColor(new Color(0, 75, 0));
            } else if (gen.obstacles.get(i).type.equals("ROCK")) {
               g.setColor(Color.GRAY);
            }
            if (gen.obstacles.get(i).radius != 0) {
               g.fillOval(gen.obstacles.get(i).location.x, gen.obstacles.get(i).location.y,
                       gen.obstacles.get(i).radius, gen.obstacles.get(i).radius);
            } else {
               g.fillOval(gen.obstacles.get(i).location.x, gen.obstacles.get(i).location.y, 50, 50);
            }
         }
      }
   }

   public void sendMap(Socket socket) {
      try {
         ImageIO.write(mapImage, "PNG", socket.getOutputStream());//also try png
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
//Make it extend from JPanel
  /*    public void paintComponent(Graphics g) {
         super.paintComponent(g);
         this.setCenter(g);
         Graphics2D g2 = (Graphics2D) (g);
         g2.scale(0.04, 0.04);

         if (!gen.testingState) {
            g.setColor(Color.BLUE);
            this.fillOvalCustom(loopRadiusSize, ellipticalAdjust, g);
            g.setColor(Color.GREEN);
            this.fillOvalCustom(loopRadiusSize - nodeGenRange / 2, ellipticalAdjust, g);
            this.fillOvalCustom(loopRadiusSize + nodeGenRange / 2, ellipticalAdjust, g);
            g.setColor(Color.black);
            this.fillOvalCustom(7500, ellipticalAdjust, g);
         }
         //6000 by 4000
         //Currently 1200x800
         g.fillRect(-15000, -10000, 30000, 20000);

         g.setColor(Color.RED);

         if (gen.regionLayer != null) {
            for (int idx = 0; idx < gen.regionLayer.regions.size(); idx++) {
               if (gen.regionLayer.regions.get(idx).regionType == "crevice") {
                  g.setColor(Color.GRAY);
               } else if (gen.regionLayer.regions.get(idx).regionType == "swamp") {
                  g.setColor(new Color (0,50,0));
               } else {
                  g.setColor(new Color(0, 100, 0));
               }
               if (gen.regionLayer.regions.get(idx).regionType.equals("road")) {
                  g.fillPolygon(gen.regionLayer.regions.get(idx));
               } else {
                  g.drawPolygon(gen.regionLayer.regions.get(idx));
               }
            }
         }

         for (int i = 0; i < gen.nodes.size(); i++) {
//        g.fillOval((int)gen.nodes.get(i).getPoint().getX() - 5,
//        		(int)gen.nodes.get(i).getPoint().getY() - 5,50,50);
            if (gen.nodes.get(i).isClearing) {
               this.fillOvalCustom(gen.nodes.get(i).clearingSize, gen.nodes.get(i).location.x,
                       gen.nodes.get(i).location.y, g);
            } else {
               this.fillOvalCustom(50, gen.nodes.get(i).location.x, gen.nodes.get(i).location.y, g);
            }
            for (int j = 0; j < gen.nodes.get(i).connections.size(); j++) {
               this.drawLineCustom(gen.nodes.get(i).location, gen.nodes.get(i).connections.get(j), g);
            }

         }

         for (int i = 0; i < gen.obstacles.size(); i++) {
            if (gen.obstacles.get(i).type == "TREE") {
               g.setColor(new Color (0,75,0));
            } else if (gen.obstacles.get(i).type == "ROCK") {
               g.setColor(Color.BLACK);
            }
            if (gen.obstacles.get(i).radius != 0) {
               g.fillOval(gen.obstacles.get(i).location.x, gen.obstacles.get(i).location.y,
                       gen.obstacles.get(i).radius, gen.obstacles.get(i).radius);
            } else {
               g.fillOval(gen.obstacles.get(i).location.x, gen.obstacles.get(i).location.y, 50, 50);
            }
         }
      }

      private void setCenter(Graphics g) {
         g.translate((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                 (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);

      }
   }*/
}
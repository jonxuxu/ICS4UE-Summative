package server;

import javax.imageio.ImageIO;


import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * MainMapGenModule.java
 * <p>
 * Handles the sending and processing of all the data produced by the MapGen class
 *
 * @author Artem Sotnikov, Will Jeong
 * @version 3.3
 * @since 2019-03-25
 */

class MainMapGenModule extends JFrame {
   private Disp display;
   private MapGen gen;

   private int loopRadiusSize = 5000;
   private double ellipticalAdjust = 1.75;
   private int nodeGenRange = 3750;
   private double nodeGenStDev = 0.5;
   private File newImageFile = new File("Map.png");
   private BufferedImage mapImage;
   private BufferedImage pathImage;
   private BufferedImage darkPathImage;
   private BufferedImage clearingImage;
   private BufferedImage groundImage;
   private BufferedImage swampImage;
   private Socket socket;

   /**
    * The constructor that sets up all necessary data and handles all the MapGen methods
    */
   MainMapGenModule() {

      String config = "";

      //Get images from files
      try {
         pathImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/Full_Path.png"));
         darkPathImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/Full_PathDark.png"));
         clearingImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/Full_Clearing.png"));
         groundImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/Full_Ground.png"));
         swampImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/Full_Swamp.png"));
      } catch (IOException e) {
         System.out.println("Unable to find an image");
      }

      gen = new MapGen(7500, 5000, ellipticalAdjust);
      System.out.println("yay1");
      config = "NO";

      if (config == "test") {
         gen.configueScenario1();
         gen.generateRegions();
      } else {
         gen.generateMap2(40, loopRadiusSize, nodeGenRange, nodeGenStDev);
         gen.tetherAllNodes2();
         gen.makeNodesElliptical();
         gen.generateRegions();
         gen.insertArtifactClearing();
         gen.generateCrevices(2);
         gen.smokeTrees(7500, 130, 0, false);
         System.out.println("generation");
         gen.smokeRocks(7500, 20, true);
         gen.makeObstaclesElliptical();
         gen.genClearingByNum(10, 500);
         gen.purgeRedundancies();
         gen.generateBarrier();

         gen.addObstacleBoundingBoxes();

      }


      display = new Disp();

      // Code for a potential JFrame implementation

      display = new Disp();
      //this.add(display);
      //display.repaint();


      display.paintImage();
      System.out.println("done");
   }


   /**
    * An internal class handling the graphics for a JFrame implementation or for a direct image send
    */

   class Disp {
      /**
       * Draws a oval with a custom radius centered at (0,0)
       *
       * @param radius the radius of the oval to be drawn
       * @param g      the graphics module with which the oval should be drawn
       */
      private void drawOvalCustom(int radius, Graphics g) {
         g.drawOval(-radius, -radius, radius * 2, radius * 2);
      }

      /**
       * Draws a oval centered at a custom location, with a custom radius
       *
       * @param radius  the radius of the oval to be drawn
       * @param xOffset the xCoordinate at which to start drawing
       * @param yOffset the yCoordinate at which to start drawing
       * @param g       the graphics module with which the oval should be drawn
       */

      private void fillOvalCustom(int radius, int xOffset, int yOffset, Graphics g) {
         g.fillOval(xOffset - radius, yOffset - radius, radius * 2, radius * 2);
      }

      /**
       * Draws a oval centered at a custom location, with a custom radius
       *
       * @param radius  the radius of the oval to be drawn
       * @param eAdjust the horizontal elliptical adjustment of the oval
       * @param g       the graphics module with which the oval should be drawn
       */

      private void fillOvalCustom(int radius, double eAdjust, Graphics g) {
         g.fillOval((int) (-radius * eAdjust), -radius, (int) (radius * 2 * eAdjust), radius * 2);
      }

      /**
       * Paints the map into a image to be exported for use in the game
       */

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

         g.drawImage(swampImage, -15000, -10000, 30000, 20000, null);

         g.setColor(Color.BLACK);

         if (gen.regionLayer != null) {
            for (int idx = 0; idx < gen.regionLayer.regions.size(); idx++) {
               /*if (gen.regionLayer.regions.get(idx).regionType.equals("crevice")) {
                  g.setColor(Color.BLACK);
               } else if (gen.regionLayer.regions.get(idx).regionType.equals("swamp")) {
                   g.setColor(new Color(0, 50, 0));
               } else */
               if (gen.regionLayer.regions.get(idx).regionType.equals("team_one_clearing") || gen.regionLayer.regions.get(idx).regionType.equals("team_two_clearing")) {
                   Polygon temp = (Polygon)(g.getClip());
                   ((Graphics2D) g).clip(gen.regionLayer.regions.get(idx));
                   g.drawImage(clearingImage, -15000, -10000, 30000, 20000, null);
                   g.setClip(temp);
               } else if (gen.regionLayer.regions.get(idx).regionType.equals("road")) {
                  Polygon temp = (Polygon)(g.getClip());
                  ((Graphics2D) g).clip(gen.regionLayer.regions.get(idx));
                  g.drawImage(pathImage, -15000, -10000, 30000, 20000, null);
                  g.setClip(temp);
               } else {
                  if (idx == 0){
                     Polygon temp = (Polygon)(g.getClip());
                     ((Graphics2D) g).clip(gen.regionLayer.regions.get(idx));
                     g.drawImage(groundImage, -15000, -10000, 30000, 20000, null);
                     g.setClip(temp);
                  } else if (idx == 1){
                     Polygon temp = (Polygon)(g.getClip());
                     ((Graphics2D) g).clip(gen.regionLayer.regions.get(idx));
                     g.drawImage(swampImage, -15000, -10000, 30000, 20000, null);
                     g.setClip(temp);
                  } else {
                     g.fillPolygon(gen.regionLayer.regions.get(idx));
                  }
               }
            }
         }

         for (int i = 0; i < gen.nodes.size(); i++) {
//        g.fillOval((int)gen.nodes.get(i).getPoint().getX() - 5,
//        		(int)gen.nodes.get(i).getPoint().getY() - 5,50,50);
            if (gen.nodes.get(i).isClearing) {
               /*g.setColor(new Color(150, 97, 37));
               this.fillOvalCustom(gen.nodes.get(i).clearingSize, gen.nodes.get(i).location.x,
                       gen.nodes.get(i).location.y, g);*/
               Polygon temp = (Polygon)(g.getClip());
               int clearingSize = gen.nodes.get(i).clearingSize;
               ((Graphics2D) g).clip(new Ellipse2D.Double(gen.nodes.get(i).location.x - clearingSize, gen.nodes.get(i).location.y - clearingSize, clearingSize * 2, clearingSize * 2));
               g.drawImage(darkPathImage, -15000, -10000, 30000, 20000, null);
               g.setClip(temp);
               /*g.setColor(new Color(150, 97, 37));
               this.fillOvalCustom(gen.nodes.get(i).clearingSize, gen.nodes.get(i).location.x, gen.nodes.get(i).location.y, g);*/
            } else {
               //this.fillOvalCustom(50, gen.nodes.get(i).location.x, gen.nodes.get(i).location.y, g);
            }
            for (int j = 0; j < gen.nodes.get(i).connections.size(); j++) {
               //this.drawLineCustom(gen.nodes.get(i).location, gen.nodes.get(i).connections.get(j), g);
            }
         }

         for (int i = 0; i < gen.obstacles.size(); i++) {
            if (gen.obstacles.get(i).type.equals("TREE")) {
               g.setColor(new Color(0, 75, 0));
            } else if (gen.obstacles.get(i).type.equals("ROCK")) {
               g.setColor(Color.GRAY);
            }
            if (gen.obstacles.get(i).radius != 0) {
               g2.fill(gen.obstacles.get(i).boundingBox);
            } else {
               this.fillOvalCustom(50, gen.obstacles.get(i).location.x,
                       gen.obstacles.get(i).location.y, g);
            }
         }
       /*  try {
            ImageIO.write(mapImage, "PNG", new File("Map.png"));//also try png
         } catch (Exception e) {
            System.out.println("this is bad");
         }*/
      }
   }

   /**
    * Returns the full list of obstacles
    *
    * @return ArrayList<Obstacle>, the full list of obstacles contained within the instance of MapGen
    */
   public ArrayList<Obstacle> getObstacle() {
      return (gen.obstacles);
   }

   /**
    * Returns the index of the team clearing region
    *
    * @param teamNumber the team number for which the index is to be retrieved
    * @return the index within the region ArrayList of that team clearing
    */

   public Region getTeamClearing(int teamNumber) {
      if (teamNumber == 0) {
         for (int idx = 0; idx < gen.regionLayer.regions.size(); idx++) {
            if (gen.regionLayer.regions.get(idx).regionType.equals("team_one_clearing")) {
               return (gen.regionLayer.regions.get(idx));
            }
         }
      } else {
         for (int idx = 0; idx < gen.regionLayer.regions.size(); idx++) {
            if (gen.regionLayer.regions.get(idx).regionType.equals("team_two_clearing")) {
               return (gen.regionLayer.regions.get(idx));
            }
         }
      }
      return(null);
   }

   /**
    * sends the map image over to socket to the clients
    *
    * @param socket, the socket on which to send the map
    */
   public void sendMap(Socket socket) {
      try {
         ImageIO.write(mapImage, "PNG", socket.getOutputStream());//also try png
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

// Code for a JFrame implementation, unused as of last update (2019-06-11)

// Make it extend from JPanel
  /*    public void paintComponent(Graphics g) {
         super.paintComponent(g);
         this.setCenter(g);
         Graphics2D g2 = (Graphics2D) (g);
         g2.(0.04, 0.04);

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
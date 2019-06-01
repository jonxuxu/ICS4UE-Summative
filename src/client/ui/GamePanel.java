package client.ui;

import client.Client;
import client.Player;
import client.Sector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * GamePanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class GamePanel extends GeneralPanel {//State=7
   private Graphics2D g2;
   private boolean generateGraphics = true;
   int[] midXy = new int[2];
   private Shape rect;
   private Shape largeCircle;
   private Area areaRect;
   private Area largeRing;
   private Polygon BOTTOM_BAR = new Polygon();
   private Rectangle drawArea;
   private BufferedImage fogMap;
   private int fogTicks = 0;
   private double scaling = super.getScaling();
   private int width= super.getWidth();
   private int height= super.getWidth();


   public GamePanel() {
      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(40, 40, 40));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      this.addMouseListener(myMouseAdapter);
      this.addMouseWheelListener(myMouseAdapter);
      this.addMouseMotionListener(myMouseAdapter);
   }

   @Override
   public void paintComponent(Graphics g) {
      g2 = (Graphics2D) g;
      if ((state == 7) && (generateGraphics)) {
         midXy[0] = (int) (DESIRED_X * scaling / 2);
         midXy[1] = (int) (DESIRED_Y * scaling / 2);
         for (Player currentPlayer : players) {
            currentPlayer.setScaling(scaling);
            currentPlayer.setCenterXy(midXy);
         }
         g2.setFont(MAIN_FONT);
         generateGraphics = false;
         largeCircle = new Ellipse2D.Double(400 * scaling, 175 * scaling, 150 * scaling, 150 * scaling);

         rect = new Rectangle2D.Double(0, 0, 950 * scaling, 500 * scaling);
         areaRect = new Area(rect);
         largeRing = new Area(largeCircle);
         areaRect.subtract(largeRing);
         BOTTOM_BAR.addPoint((int) (272 * scaling), (int) (500 * scaling));
         BOTTOM_BAR.addPoint((int) (265 * scaling), (int) (440 * scaling));
         BOTTOM_BAR.addPoint((int) (270 * scaling), (int) (435 * scaling));
         BOTTOM_BAR.addPoint((int) (680 * scaling), (int) (435 * scaling));
         BOTTOM_BAR.addPoint((int) (685 * scaling), (int) (440 * scaling));
         BOTTOM_BAR.addPoint((int) (678 * scaling), (int) (500 * scaling));
         //Game set up
         centerXy[0] = (int) (DESIRED_X * scaling / 2);
         centerXy[1] = (int) (DESIRED_Y * scaling / 2);
         try {
            sheet = ImageIO.read(new File(".\\res\\Map.png"));
            sectors = new Sector[10][10];
            for (int i = 0; i < 10; i++) {
               for (int j = 0; j < 10; j++) {
                  sectors[j][i] = new Sector();
                  sectors[j][i].setImage(sheet.getSubimage(j * 1000, i * 1000, 1000, 1000));
                  sectors[j][i].setSectorCoords(j, i);
                  sectors[j][i].setSize((int) (1000 * scaling));
               }
            }
         } catch (IOException e) {
            System.out.println("Image not found");
         }
         drawArea = new Rectangle(0, 0, (int) (DESIRED_X * scaling), (int) (DESIRED_Y * scaling));
      }
      super.paintComponent(g2);
      if (drawArea != null) {
         g2.clip(drawArea);
         g2.setFont(MAIN_FONT);
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
         //Sectors
         int startX = (int) ((myPlayer.getXy()[0] - 475.0) / 1000.0);
         int finalX = (int) (Math.ceil((myPlayer.getXy()[0] + 475.0) / 1000.0)) + 1;
         int startY = (int) ((myPlayer.getXy()[1] - 250.0) / 1000.0);
         int finalY = (int) (Math.ceil((myPlayer.getXy()[1] + 250.0) / 1000.0)) + 1;

         for (int i = startY; i < finalY; i++) {
            for (int j = startX; j < finalX; j++) {
               if ((i >= 0) && (j >= 0) && (i < 10) && (j < 10)) {
                  sectors[j][i].drawSector(g2, xyAdjust);
               }
            }
         }
         //Game player
         for (Player currentPlayer : players) {
            if (currentPlayer != null) {
               currentPlayer.draw(g2, myPlayer.getXy());
            }
         }
         // System.out.println(System.nanoTime() - time);

         // Updating fog
         for (int i = 0; i < players.length; i++) {
            // TODO: Separate by teams
            // TODO: Account for players that quit?
            fog.scout(players[i].getXy());
         }
         //Creating shapes
         AffineTransform tx = new AffineTransform();
         tx.translate(centerXy[0] - myPlayer.getXy()[0] * scaling, centerXy[1] - myPlayer.getXy()[1] * scaling);
         Area darkFog = fog.getFog().createTransformedArea(tx);
         Area lightFog = fog.getExplored().createTransformedArea(tx);

         //Draws fog
         g2.setColor(Color.black); //Unexplored
         g2.fill(darkFog);
         g2.setColor(new Color(0, 0, 0, 128)); //Previously explored
         g2.fill(lightFog);

         for (int i = 0; i < projectiles.size(); i++) { //For some reason, a concurrent modification exception is thrown if i use the other for loop
            projectiles.get(i).draw(g2);
         }
         for (int i = 0; i < aoes.size(); i++) { //For some reason, a concurrent modification exception is thrown if i use the other for loop
            aoes.get(i).draw(g2);
         }
         g2.setColor(new Color(165, 156, 148));
         //Minimap
         g2.drawRect((int) (830 * scaling), (int) (379 * scaling), (int) (120 * scaling), (int) (120 * scaling));
         //Bottom bar
         g2.drawPolygon(BOTTOM_BAR);


         //Stat bars
         g2.setColor(new Color(190, 40, 40));
         g2.fillRect(0, (int) (486 * scaling), (int) (121 * scaling * myPlayer.getHealth() / myPlayer.getMaxHealth()), (int) (5 * scaling));

         g2.setColor(new Color(165, 156, 148));
         g2.drawRect(0, (int) (486 * scaling), (int) (121 * scaling), (int) (5 * scaling));
         g2.drawRect(0, (int) (495 * scaling), (int) (121 * scaling), (int) (5 * scaling));
         //Bottom bar contents

         //Spells
         g2.fillRect((int) (565 * scaling), (int) (442 * scaling), (int) (30 * scaling), (int) (50 * scaling));
         g2.fillRect((int) (604 * scaling), (int) (442 * scaling), (int) (30 * scaling), (int) (50 * scaling));
         g2.fillRect((int) (643 * scaling), (int) (442 * scaling), (int) (30 * scaling), (int) (50 * scaling));
      }
      g2.dispose();
   }
}
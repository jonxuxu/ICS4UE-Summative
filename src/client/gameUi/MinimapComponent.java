package client.gameUi;

import client.Player;
import client.map.FogMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

/**
 * MinimapComponent.java
 * This is for the minimap
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-06-02
 */

public class MinimapComponent extends GameComponent {
   private FogMap fog;
   private Player[] players;
   private static int myPlayerId;
   private AffineTransform tx;

   private double zoom = 1;

   private Area outputShape;
   private Area darkFog, lightFog;
   private Area playerShape, allyShape, enemyShape;
   private int MAP_WIDTH, MAP_HEIGHT;

   /**
    * Minimap display
    * @param WIDTH
    * @param HEIGHT
    * @param MAP_WIDTH
    * @param MAP_HEIGHT
    */

   public MinimapComponent(int WIDTH, int HEIGHT, int MAP_WIDTH, int MAP_HEIGHT) {
      this.MAP_WIDTH = MAP_WIDTH;
      this.MAP_HEIGHT = MAP_HEIGHT;
      outputShape = new Area(new Rectangle(1250, 550, 300, 300));
   }

   /**
    * Draws the map, and players in the minimap
    * @param g2 Graphics2D, used to draw the minimap
    * @param fog Team Fog of War (in progress)
    * @param bgImage The image of the map
    * @param players List of all players in the game
    * @param myPlayerId The id number of the player player
    * @param xyAdjust The translation amount based on the player
    */
   public void draw(Graphics2D g2, FogMap fog, BufferedImage bgImage, Player[] players, int myPlayerId, int[] xyAdjust) {
      // Draws map
      Shape oldClip = g2.getClip();
      g2.clip(outputShape);
      tx = new AffineTransform();
      tx.translate(1400 - players[myPlayerId].getXy()[0] * 0.1, 700 - players[myPlayerId].getXy()[1] * 0.1);
      tx.scale(0.1 * MAP_WIDTH / bgImage.getWidth(), 0.1 * MAP_HEIGHT / bgImage.getHeight());

      g2.drawRenderedImage(bgImage, tx);

      // Draws fog
      /*
      xyAdjust[0] = (int) ((890) - (players[myPlayerId].getXy()[0]) * 0.05);
      xyAdjust[1] = (int) ((440) - (players[myPlayerId].getXy()[1]) * 0.05);
      darkFog = fog.getFog(1).createTransformedArea(tx);
      lightFog = fog.getExplored(1).createTransformedArea(tx);
      g2.setColor(Color.black); //Unexplored
      g2.setClip(new Area(INNER_RECT));
      g2.fill(outputShape);
      g2.setColor(new Color(0, 0, 0, 128)); //Previously explored
      g2.fill(lightFog);*/

      // Draws characters and mobs
      allyShape = new Area();
      enemyShape = new Area();
      for (Player player : players) {
         if (player != null) {
            tx = new AffineTransform();
            xyAdjust[0] = -(int) ((players[myPlayerId].getXy()[0] - player.getXy()[0]) * 0.1);
            xyAdjust[1] = -(int) ((players[myPlayerId].getXy()[1] - player.getXy()[1]) * 0.1);
            tx.translate(xyAdjust[0], xyAdjust[1]);
            playerShape = new Area(new Rectangle(1396, 696, 8, 8)).createTransformedArea(tx);
            if (player.getTeam() == players[myPlayerId].getTeam()) { // On same team
               allyShape.add(playerShape);
            } else {
               if (player.getIlluminated()) {
                  enemyShape.add(playerShape); //Added a way to avoid being seen by the enemy
               }
            }
         }
      }
      g2.setColor(Color.green);
      g2.fill(allyShape);
      g2.setColor(Color.red);
      g2.fill(enemyShape);
      Rectangle tempRect = new Rectangle(1250, 550, 300, 300);
      g2.setColor(new Color(141, 130, 103));
      g2.setStroke(new BasicStroke(10));
      g2.setColor(new Color(72, 60, 32));
      g2.setStroke(new BasicStroke(18));
      g2.draw(tempRect);
      g2.setColor(new Color(141, 130, 103));
      g2.setStroke(new BasicStroke(12));
      g2.draw(tempRect);
      g2.setColor(new Color(95, 87, 69));
      g2.setStroke(new BasicStroke(4));
      g2.draw(tempRect);
      g2.setColor(new Color(50, 46, 41));
      g2.draw(tempRect);



      g2.clip(oldClip);
   }

}

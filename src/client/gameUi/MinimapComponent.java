package client.gameUi;

import client.Client;
import client.Player;
import client.map.FogMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * MinimapComponent.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-06-02
 */

public class MinimapComponent extends GameComponent {
   private FogMap fog;
   private Player[] players;
   private static int myPlayerId;
   private int[] xyAdjust = new int[2];
   private AffineTransform tx;

   private final int MAX_X = super.getMAX_X();
   private final int MAX_Y = super.getMAX_Y();
   private double zoom = 1;

   private Rectangle2D BORDER_RECT;
   private Rectangle2D BORDER_RECT2;
   private Rectangle2D BORDER_RECT3;
   private Rectangle2D BORDER_RECT4;
   private Rectangle2D INNER_RECT;
   private Area BORDER_FILL;
   private Area BORDER_FILL2;
   private Area BORDER_FILL3;
   private Area BORDER_FILL4;
   private Area outputShape;
   private Area darkFog, lightFog;
   private Area playerShape, allyShape, enemyShape;

   public MinimapComponent(FogMap fog, Player[] players, int myPlayerId){
      // Setting up refs
      this.fog = fog;
      this.players = players;
      this.myPlayerId = myPlayerId;

      // Border
      BORDER_RECT = new Rectangle(scale(830), scale(379), scale(120), scale(120));
      BORDER_RECT2 = new Rectangle(scale(832), scale(381), scale(116), scale(116));
      BORDER_RECT3 = new Rectangle(scale(833), scale(382), scale(114), scale(114));
      BORDER_RECT4 = new Rectangle(scale(835), scale(384), scale(110), scale(110));
      INNER_RECT = new Rectangle(scale(836), scale(385), scale(108), scale(108));
      BORDER_FILL = new Area(BORDER_RECT);
      BORDER_FILL2 = new Area(BORDER_RECT2);
      BORDER_FILL3 = new Area(BORDER_RECT3);
      BORDER_FILL4 = new Area(BORDER_RECT4);
      Area tempArea = new Area(INNER_RECT);
      BORDER_FILL.subtract(BORDER_FILL2);
      BORDER_FILL2.subtract(BORDER_FILL3);
      BORDER_FILL3.subtract(BORDER_FILL4);
      BORDER_FILL4.subtract(tempArea);
   }


   public void draw(Graphics2D g2) {
      // Draws border
      g2.setColor(new Color(72, 60, 32));
      g2.fill(BORDER_FILL);
      g2.setColor(new Color(141, 130, 103));
      g2.fill(BORDER_FILL2);
      g2.setColor(new Color(95, 87, 69));
      g2.fill(BORDER_FILL3);
      g2.setColor(new Color(50, 46, 41));
      g2.fill(BORDER_FILL4);
      outputShape = new Area(INNER_RECT);
      g2.setColor(Color.white);
      g2.fill(outputShape);

      // Draws map

      // Draws fog
      xyAdjust[0] =  (int)(scale(890) -scale(players[myPlayerId].getXy()[0])*0.05);
      xyAdjust[1] =  (int)(scale(440) - scale(players[myPlayerId].getXy()[1])*0.05);
      tx = new AffineTransform();
      tx.translate(xyAdjust[0], xyAdjust[1]);
      tx.scale(0.05,0.05);
      darkFog = fog.getFog(1).createTransformedArea(tx);
      lightFog = fog.getExplored(1).createTransformedArea(tx);
      g2.setColor(Color.black); //Unexplored
      outputShape = new Area(INNER_RECT);
      outputShape.intersect(darkFog);
      g2.fill(outputShape);
      g2.setColor(new Color(0, 0, 0, 128)); //Previously explored
      outputShape = new Area(INNER_RECT);
      outputShape.intersect(lightFog);
      g2.fill(outputShape);

      // Draws players and mobs
      allyShape = new Area();
      enemyShape = new Area();
      for(Player player:players){
         tx = new AffineTransform();
         xyAdjust[0] = -(int)(scale(players[myPlayerId].getXy()[0] - player.getXy()[0]) * 0.05);
         xyAdjust[1] = -(int)(scale(players[myPlayerId].getXy()[1] - player.getXy()[1])*0.05);
         tx.translate(xyAdjust[0], xyAdjust[1]);
         playerShape = new Area(new Rectangle(scale(888), scale(438), scale(4), scale(4))).createTransformedArea(tx);
         if(player.getTeam() == players[myPlayerId].getTeam()){ // On same team
            allyShape.add(playerShape);
         } else {
            enemyShape.add(playerShape);
         }
      }
      outputShape = new Area(INNER_RECT);
      outputShape.intersect(allyShape);
      g2.setColor(Color.green);
      g2.fill(outputShape);
      outputShape = new Area(INNER_RECT);
      outputShape.intersect(enemyShape);
      g2.setColor(Color.red);
      g2.fill(outputShape);

   }

   public void update(){

   }
}

package client.gameUi;

import client.Player;
import client.gameUi.GameComponent;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

/**
 * BottomComponent.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-06-02
 */

public class BottomComponent extends GameComponent {
   private final int MAX_X = super.getMAX_X();
   private final int MAX_Y = super.getMAX_Y();
   private Polygon BOTTOM_BORDER;
   private Polygon BOTTOM_BORDER2;
   private Polygon BOTTOM_BORDER3;
   private Polygon BOTTOM_BORDER4;
   private Polygon BOTTOM_INNER;
   private Polygon BOTTOM_POLYGON;
   private Player myPlayer;
   private final Font STATS_FONT = super.getFont("stats");

   public BottomComponent(Player myPlayer) {

      int[] xPoints = {(MAX_X/2-370), (MAX_X/2-360), (MAX_X/2-350), (MAX_X/2+350), (MAX_X/2+360), (MAX_X/2+370)};
      int[] yPoints = {(MAX_Y), (MAX_Y-80), (MAX_Y-85), (MAX_Y-85), (MAX_Y-80), (MAX_Y)};

      BOTTOM_BORDER = new Polygon(xPoints, yPoints, 6);
    /*  int[] x2Points = {(274), (267), (272), (678), (683), (676)};
      int[] y2Points = {(499), (442), (437), (437), (442), (499)};
      BOTTOM_BORDER2 = new Polygon(x2Points, y2Points, 6);
      int[] x3Points = {(275), (268), (273), (677), (682), (675)};
      int[] y3Points = {(498), (443), (438), (438), (443), (498)};
      BOTTOM_BORDER3 = new Polygon(x3Points, y3Points, 6);
      int[] x4Points = {(277), (270), (275), (675), (680), (673)};
      int[] y4Points = {(497), (445), (440), (440), (445), (497)};
      BOTTOM_BORDER4 = new Polygon(x4Points, y4Points, 6);
      int[] x5Points = {(278), (271), (276), (674), (679), (672)};
      int[] y5Points = {(496), (446), (441), (441), (446), (496)};
      BOTTOM_INNER = new Polygon(x5Points, y5Points, 6);*/
      this.myPlayer = myPlayer;
   }


   public void draw(Graphics2D g2) {
      //Bottom panel
      g2.setColor(new Color(72, 60, 32));
      g2.fillPolygon(BOTTOM_BORDER);
     /* g2.setColor(new Color(141, 130, 103));
      g2.fillPolygon(BOTTOM_BORDER2);
      g2.setColor(new Color(95, 87, 69));
      g2.fillPolygon(BOTTOM_BORDER3);
      g2.setColor(new Color(50, 46, 41));
      g2.fillPolygon(BOTTOM_BORDER4);
      g2.setColor(new Color(33, 35, 37));
      g2.fillPolygon(BOTTOM_INNER);*/
      //Spells
      /*
      g2.setStroke(new BasicStroke(2));
      g2.setColor(new Color(141, 130, 103));
      g2.drawRect((562), (442), (30), (50));
      g2.drawRect((601), (442), (30), (50));
      g2.drawRect((640), (442), (30), (50));
      //Character icon
      g2.drawRect((287), (442), (50), (50));
      //Stat border
      g2.drawRect((345), (465), (121), (5));
      */
      //Health bar
      g2.setColor(new Color(190, 40, 40));
      if (myPlayer.getMaxHealth()!=0) {
         g2.fillRect(MAX_X / 2 - 260, (MAX_Y - 20), (121 * myPlayer.getHealth() / myPlayer.getMaxHealth()), (5));
      }
      //g2.fillRect(MAX_X/2-260, (MAX_Y-20), (121  * myPlayer.getHealth() /  myPlayer.getMaxHealth()), (5));
      g2.setColor(Color.white);
      g2.setFont(STATS_FONT);
      if (myPlayer!=null) {
         g2.drawString("Gold: " + myPlayer.getGold(), MAX_X/2-260, (MAX_Y-20) - g2.getFontMetrics().getHeight());
         g2.drawString("User: " + myPlayer.getUsername(), MAX_X/2-260, (MAX_Y-20) - g2.getFontMetrics().getHeight() * 2);
      }
   }
}

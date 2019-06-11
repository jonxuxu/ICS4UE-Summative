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
      int[] xPoints = {scale(272), scale(265), scale(270), scale(680), scale(685), scale(678)};
      int[] yPoints = {scale(500), scale(440), scale(435), scale(435), scale(440), scale(500)};
      BOTTOM_BORDER = new Polygon(xPoints, yPoints, 6);
      int[] x2Points = {scale(274), scale(267), scale(272), scale(678), scale(683), scale(676)};
      int[] y2Points = {scale(499), scale(442), scale(437), scale(437), scale(442), scale(499)};
      BOTTOM_BORDER2 = new Polygon(x2Points, y2Points, 6);
      int[] x3Points = {scale(275), scale(268), scale(273), scale(677), scale(682), scale(675)};
      int[] y3Points = {scale(498), scale(443), scale(438), scale(438), scale(443), scale(498)};
      BOTTOM_BORDER3 = new Polygon(x3Points, y3Points, 6);
      int[] x4Points = {scale(277), scale(270), scale(275), scale(675), scale(680), scale(673)};
      int[] y4Points = {scale(497), scale(445), scale(440), scale(440), scale(445), scale(497)};
      BOTTOM_BORDER4 = new Polygon(x4Points, y4Points, 6);
      int[] x5Points = {scale(278), scale(271), scale(276), scale(674), scale(679), scale(672)};
      int[] y5Points = {scale(496), scale(446), scale(441), scale(441), scale(446), scale(496)};
      BOTTOM_INNER = new Polygon(x5Points, y5Points, 6);
      this.myPlayer = myPlayer;
   }


   public void draw(Graphics2D g2) {
      g2.setColor(new Color(152, 162, 169));
      //Bottom panel
      g2.setColor(new Color(72, 60, 32));
      g2.fillPolygon(BOTTOM_BORDER);
      g2.setColor(new Color(141, 130, 103));
      g2.fillPolygon(BOTTOM_BORDER2);
      g2.setColor(new Color(95, 87, 69));
      g2.fillPolygon(BOTTOM_BORDER3);
      g2.setColor(new Color(50, 46, 41));
      g2.fillPolygon(BOTTOM_BORDER4);
      g2.setColor(new Color(33, 35, 37));
      g2.fillPolygon(BOTTOM_INNER);
      //Spells
      g2.setStroke(new BasicStroke(2));
      g2.setColor(new Color(141, 130, 103));
      g2.drawRect(scale(562), scale(442), scale(30), scale(50));
      g2.drawRect(scale(601), scale(442), scale(30), scale(50));
      g2.drawRect(scale(640), scale(442), scale(30), scale(50));
      //Character icon
      g2.drawRect(scale(287), scale(442), scale(50), scale(50));
      //Stat border
      g2.drawRect(scale(345), scale(465), scale(121), scale(5));
      //Health bar
      g2.setColor(new Color(190, 40, 40));
      g2.fillRect(scale(345), scale(465), scale(121  * myPlayer.getHealth() /  myPlayer.getMaxHealth()), scale(5));
      g2.setColor(new Color(80, 80, 80));
      g2.setFont(STATS_FONT);
      if (myPlayer!=null) {
         g2.drawString("Gold: " + myPlayer.getGold(), scale(345), scale(470) - g2.getFontMetrics().getHeight());
         g2.drawString("User: " + myPlayer.getUsername(), scale(345), scale(470) - g2.getFontMetrics().getHeight() * 2);
      }
   }
}

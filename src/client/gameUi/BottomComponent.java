package client.gameUi;

import client.Player;
import client.gameUi.GameComponent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

/**
 * BottomComponent.java
 * This is responsible for drawing spell, health, and username
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
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
   private static Border border1, border2, border3, border4, compound;

   /**
    * This is the constrcutor to intialize the size/shape of the component
    * @param myPlayer Player, this is the main client player
    */
   public BottomComponent(Player myPlayer) {

      int[] xPoints = {(MAX_X / 2 - 370), (MAX_X / 2 - 360), (MAX_X / 2 - 350), (MAX_X / 2 + 350), (MAX_X / 2 + 360), (MAX_X / 2 + 370)};
      int[] yPoints = {(MAX_Y), (MAX_Y - 80), (MAX_Y - 85), (MAX_Y - 85), (MAX_Y - 80), (MAX_Y)};

      BOTTOM_BORDER = new Polygon(xPoints, yPoints, 6);
      border1 = BorderFactory.createLineBorder(new Color(72, 60, 32), 4);
      border2 = BorderFactory.createLineBorder(new Color(141, 130, 103), 3);
      border3 = BorderFactory.createLineBorder(new Color(95, 87, 69), 4);
      border4 = BorderFactory.createLineBorder(new Color(50, 46, 41), 2);
      compound = BorderFactory.createCompoundBorder(border1, border2);
      compound = BorderFactory.createCompoundBorder(compound, border3);
      compound = BorderFactory.createCompoundBorder(compound, border4);

      this.myPlayer = myPlayer;
   }

   /**
    * Draws the correct panel with the appropriate amount of stroke
    * @param g2 Graphics2D
    */
   public void draw(Graphics2D g2) {
      //Bottom panel
      g2.setColor(new Color(72, 60, 32));
      g2.setStroke(new BasicStroke(18));
      g2.drawPolygon(BOTTOM_BORDER);
      g2.setColor(new Color(141, 130, 103));
      g2.setStroke(new BasicStroke(12));
      g2.drawPolygon(BOTTOM_BORDER);
      g2.setColor(new Color(95, 87, 69));
      g2.setStroke(new BasicStroke(4));
      g2.drawPolygon(BOTTOM_BORDER);
      g2.setColor(new Color(50, 46, 41));
      g2.fillPolygon(BOTTOM_BORDER);
      //Health bar
      g2.setColor(new Color(190, 40, 40));
      if (myPlayer.getMaxHealth() != 0) {
         g2.fillRect(MAX_X / 2 - 260, (MAX_Y - 20), (140 * myPlayer.getHealth() / myPlayer.getMaxHealth()), (5));
      }
      g2.setColor(Color.white);
      g2.setFont(STATS_FONT);
      if (myPlayer != null) {
         g2.drawString("User: " + myPlayer.getUsername(), MAX_X / 2 - 260, (MAX_Y - 20) - g2.getFontMetrics().getHeight() * 2);
      }
      g2.setColor(new Color (1f,1f,1f,0.2f));
      for (int i = 0; i < 3; i++) {
         if (myPlayer != null) {
            g2.fillRect(MAX_X / 2 + 120 * i, (MAX_Y - 70), 60, (int)(6.0*myPlayer.getSpellPercent(i)/10.0));
         }
      }
   }
}

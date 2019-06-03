package client.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

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
   private final Polygon BOTTOM_BORDER = new Polygon();
   private final Polygon BOTTOM_BORDER2 = new Polygon();
   private final Polygon BOTTOM_POLYGON = new Polygon();

   private int health;
   private int maxHealth;

   public BottomComponent() {
      BOTTOM_BORDER.addPoint(scale(272), scale(500));
      BOTTOM_BORDER.addPoint(scale(265), scale(440));
      BOTTOM_BORDER.addPoint(scale(270), scale(435));
      BOTTOM_BORDER.addPoint(scale(680), scale(435));
      BOTTOM_BORDER.addPoint(scale(685), scale(440));
      BOTTOM_BORDER.addPoint(scale(678), scale(500));
   }


   public void draw(Graphics2D g2) {
      //Health bar
      g2.setColor(new Color(190, 40, 40));
      g2.fillRect(0, scale(486), scale(121 * health / maxHealth), scale(5));
      //Stat border
      g2.setColor(new Color(152, 162, 169));
      g2.drawRect(0, scale(486), scale(121), scale(5));

      g2.setColor(new Color(152, 162, 169));
      //Bottom panel
      g2.drawPolygon(BOTTOM_BORDER);

      //Spells
      g2.setColor(new Color(75, 85, 90));
      g2.fillRect(scale(565), scale(442), scale(30), scale(50));
      g2.fillRect(scale(604), scale(442), scale(30), scale(50));
      g2.fillRect(scale(643), scale(442), scale(30), scale(50));
   }

   public void setBothHealth(int health, int maxHealth) {
      this.health = health;
      this.maxHealth = maxHealth;
   }
}

package client.ui;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * PauseComponent.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-06-02
 */

public class PauseComponent extends GameComponent {
   private final int MAX_X = super.getMAX_X();
   private final int MAX_Y = super.getMAX_Y();
   private Rectangle2D BORDER_RECT;
   private Rectangle2D BORDER_RECT2;
   private Rectangle2D BORDER_RECT3;
   private Rectangle2D BORDER_RECT4;
   private Rectangle2D INNER_RECT;
   private Area BORDER_FILL;
   private Area BORDER_FILL2;
   private Area BORDER_FILL3;
   private Area BORDER_FILL4;

   private boolean visible;
   private boolean justPressed;

   public PauseComponent() {
      BORDER_RECT = new Rectangle(MAX_X / 2 - scale(200), MAX_Y / 2 - scale(150), scale(400), scale(300));
      BORDER_RECT2 = new Rectangle(MAX_X / 2 - scale(198), MAX_Y / 2 - scale(148), scale(396), scale(296));
      BORDER_RECT3 = new Rectangle(MAX_X / 2 - scale(197), MAX_Y / 2 - scale(147), scale(394), scale(294));
      BORDER_RECT4 = new Rectangle(MAX_X / 2 - scale(195), MAX_Y / 2 - scale(145), scale(390), scale(290));
      INNER_RECT = new Rectangle(MAX_X / 2 - scale(194), MAX_Y / 2 - scale(144), scale(388), scale(288));
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

   public void openMenu() {
      if (visible) {
         visible = false;
      } else {
         visible = true;
      }
   }

   public void draw(Graphics2D g2) {
      if (visible) {
         g2.setColor(new Color(72, 60, 32));
         g2.fill(BORDER_FILL);
         g2.setColor(new Color(141, 130, 103));
         g2.fill(BORDER_FILL2);
         g2.setColor(new Color(95, 87, 69));
         g2.fill(BORDER_FILL3);
         g2.setColor(new Color(50, 46, 41));
         g2.fill(BORDER_FILL4);
         g2.setColor(new Color(33, 35, 37));
         g2.fill(INNER_RECT);
      }
   }
}

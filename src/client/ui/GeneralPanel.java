package client.ui;

import client.Client;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * GeneralPanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public abstract class GeneralPanel extends JPanel {
   private static double scaling, introScaling;
   private static int width, height;
   private static BufferedImage TITLE_SCREEN;
   private static BufferedImage TITLE;
   private static BufferedImage LOADED_TITLE_SCREEN;
   private static BufferedImage LOADED_TITLE;
   private static Client client;
   private static Map<String, Font> fonts = new HashMap<String, Font>();

   GeneralPanel() {
      this.setPreferredSize(new Dimension(width, height));

   }

   public static void setParameters(int width1, int height1, double scaling1, double introScaling1, Client client1) {
      width = width1;
      height = height1;
      scaling = scaling1;
      introScaling = introScaling1;
      client = client1;
      try {
         TITLE_SCREEN = ImageIO.read(new File(".\\res\\TitleScreenDark.png"));
         TITLE = ImageIO.read(new File(".\\res\\Title.png"));
      } catch (IOException e) {
         System.out.println("Font not available");
      }
      //Setting images
      GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
      GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
      LOADED_TITLE_SCREEN = graphicsConfiguration.createCompatibleImage((int) (1800 * introScaling), (int) (1198 * introScaling), Transparency.TRANSLUCENT);
      Graphics2D graphicsTS = LOADED_TITLE_SCREEN.createGraphics();
      graphicsTS.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphicsTS.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphicsTS.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphicsTS.drawImage(TITLE_SCREEN, 0, 0, (int) (1800 * introScaling), (int) (1198 * introScaling), null);
      graphicsTS.dispose();
      LOADED_TITLE = graphicsConfiguration.createCompatibleImage((int) (height / 4.0 * 1316 / 625), (int) (height / 4.0), Transparency.TRANSLUCENT);
      Graphics2D graphicsT = LOADED_TITLE.createGraphics();
      graphicsT.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphicsT.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphicsT.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphicsT.drawImage(TITLE, 0, 0, (int) (height / 4.0 * 1316 / 625), (int) (height / 4.0), null);
      graphicsT.dispose();

      // Setting fonts
      fonts.put("main", new Font("Cambria Math", Font.PLAIN, (int) (12 * scaling)));
      fonts.put("header", new Font("Akura Popo", Font.PLAIN, (int) (25 * scaling)));
   }

   public double getScaling() {
      return (scaling);
   }

   public double getIntroScaling() {
      return (introScaling);
   }

   public int getWidth() {
      return (width);
   }

   public int getHeight() {
      return (width);
   }

   public void drawAllParticles(Graphics2D g2) {
      //Draws particles
      for (int i = 0; i < particles.size(); i++) {
         try {
            if (particles.get(i).update()) {
               particles.remove(i);
            } else {
               particles.get(i).render(g2);
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public Font getFont(String fontName) {
      return fonts.get(fontName);
   }

   public void drawBackground(Graphics2D g2) {
      g2.drawImage(LOADED_TITLE_SCREEN, width - (int) (1800 * introScaling), height - (int) (1198 * introScaling), null);
   }
}

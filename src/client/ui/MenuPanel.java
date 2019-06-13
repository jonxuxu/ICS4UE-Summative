package client.ui;

import client.Client;
import client.particle.AshParticle;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MenuPanel.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public abstract class MenuPanel extends JPanel {
   private static double  INTRO_SCALING;
   private static int MAX_X, MAX_Y;
   private static BufferedImage TITLE_SCREEN;
   private static BufferedImage TITLE;
   private static BufferedImage LOADED_TITLE_SCREEN;
   private static BufferedImage LOADED_TITLE;
   private static Client CLIENT;
   private static Map<String, Font> fonts = new HashMap<String, Font>();
   private static ArrayList<AshParticle> particles = new ArrayList<AshParticle>();
   private static final int BG_Y = 1198;
   private static final int BG_X = 1800;
   private String errorMessage = "";

   public MenuPanel() {
      this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
   }

   public static void setParameters(int MAX_X1, int MAX_Y1, double INTRO_SCALING1, Client CLIENT1) {
      MAX_X = MAX_X1;
      MAX_Y = MAX_Y1;
      INTRO_SCALING = INTRO_SCALING1;
      CLIENT = CLIENT1;
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
      LOADED_TITLE_SCREEN = graphicsConfiguration.createCompatibleImage((int) (BG_X * INTRO_SCALING), (int) (BG_Y * INTRO_SCALING), Transparency.TRANSLUCENT);
      Graphics2D graphicsTS = LOADED_TITLE_SCREEN.createGraphics();
      graphicsTS.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphicsTS.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphicsTS.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphicsTS.drawImage(TITLE_SCREEN, 0, 0, (int) (BG_X * INTRO_SCALING), (int) (BG_Y * INTRO_SCALING), null);
      graphicsTS.dispose();
      LOADED_TITLE = graphicsConfiguration.createCompatibleImage((int) (MAX_Y / 4.0 * 1316 / 625), (int) (MAX_Y / 4.0), Transparency.TRANSLUCENT);
      Graphics2D graphicsT = LOADED_TITLE.createGraphics();
      graphicsT.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      graphicsT.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphicsT.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphicsT.drawImage(TITLE, 0, 0, (int) (MAX_Y / 4.0 * 1316 / 625), (int) (MAX_Y / 4.0), null);
      graphicsT.dispose();

      // Setting fonts
      fonts.put("main", new Font("Cambria Math", Font.PLAIN, (int) (24 )));
      fonts.put("header", new Font("Akura Popo", Font.PLAIN, (int) (50 )));

   }

   public void setErrorUpdate(String errorMessage) {
      this.errorMessage = errorMessage;
   }

   public void writeError(Graphics2D g2, int x, int y) {
      if (!errorMessage.isEmpty()) {
         String[] errorMessages = errorMessage.split("_", -1);
         for (int i = 0; i < errorMessages.length; i++) {
            g2.drawString(errorMessages[i], x - g2.getFontMetrics().stringWidth(errorMessages[i]) / 2, y + g2.getFontMetrics().getHeight() * i);
         }
      }
   }

   public double getIntroScaling() {
      return (INTRO_SCALING);
   }

   public int getWidth() {
      return (MAX_X);
   }

   public int getHeight() {
      return (MAX_Y);
   }

   public Client getClient() {
      return CLIENT;
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

   public static ArrayList<AshParticle> getParticles() {
      return particles;
   }

   public void drawBackground(Graphics2D g2) {
      g2.drawImage(LOADED_TITLE_SCREEN, MAX_X - (int) (BG_X * INTRO_SCALING), MAX_Y - (int) (BG_Y * INTRO_SCALING), null);
   }

   public void drawTitle(Graphics2D g2) {
      g2.drawImage(LOADED_TITLE, (int) ((MAX_X - (MAX_Y / 4.0 * 1316 / 625)) / 2.0), (int) (MAX_Y / 10.0), null);
   }
}

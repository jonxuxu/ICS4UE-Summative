package client.map;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Sector.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-19
 */

public class Sector {
   private BufferedImage image;
   private int[] sectorCoords = new int[2];
   private int[][] corners = new int[4][2];
   private int[] centerXy = new int[2];
   private int size;

  public void setImage(BufferedImage image) {
    this.image = image;
  }

   public void setSectorCoords(int sectorX, int sectorY) {
      this.sectorCoords[0] = sectorX;
      this.sectorCoords[1] = sectorY;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public void drawSector(Graphics2D g2, int[] xyAdjust) {
      g2.drawImage(image,  sectorCoords[0] * size + xyAdjust[0], sectorCoords[1] * size + xyAdjust[1], size, size, null);
      g2.setColor(Color.red);
    //  g2.fillRect(300, 300, size, size);

   }

}
/* continue with enough memory
  image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
         Graphics2D graphicsT = image.createGraphics();
         graphicsT.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         graphicsT.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         graphicsT.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         graphicsT.drawImage(unscaledImage, size, size, null);
         graphicsT.dispose();
 */
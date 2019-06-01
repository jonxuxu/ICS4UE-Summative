package client.ui;

import client.Client;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * IntermediatePanel.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-05-31
 */

public class IntermediatePanel extends GeneralPanel { //State=7 (intermediate)=
   private boolean begin = true;
   private double scaling = super.getScaling();
   private int width= super.getWidth();
   private int height= super.getWidth();

   public IntermediatePanel() {
      //Scaling is a factor which reduces the MAX_X/MAX_Y so that it eventually fits
      //Setting up the size
      this.setPreferredSize(new Dimension(super.getWidth(), super.getHeight()));
      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(0, 0, 0));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
   }
   public void initializeSize() {
      int[] tempXy = {(int) (DESIRED_X * scaling / 2), (int) (DESIRED_Y * scaling / 2)};
      myMouseAdapter.setCenterXy(tempXy);
      myMouseAdapter.setScaling(scaling);
      gamePanel.setBounds((int) ((this.getWidth() - (DESIRED_X * scaling)) / 2), (int) ((this.getHeight() - (DESIRED_Y * scaling)) / 2), (int) (DESIRED_X * scaling), (int) (DESIRED_Y * scaling));
      this.add(gamePanel);
   }

   public void repaintReal() {
      gamePanel.repaint();
   }
}

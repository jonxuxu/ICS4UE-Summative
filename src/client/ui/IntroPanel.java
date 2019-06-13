package client.ui;

import client.particle.IntroParticle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * IntroPanel.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-27
 */

public class IntroPanel extends MenuPanel {

   //Variable names
   static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   static double w = screenSize.getWidth();
   static double h = screenSize.getHeight();//This adjusts height to be a bit smaller to fit nicer on screen
   static int x;
   static int y;
   static ArrayList<IntroParticle> particles = new ArrayList<IntroParticle>();
   static final int SIZE = 100;
   static double fade1 = 0;
   static Font font;
   static FontMetrics metrics;
   static int fontX, fontY;
   static boolean end, dissolve = false;
   static String text = "7";
   static int lineY = 0;
   static BufferedImage img;
   static boolean animationOver = false;
   static int animationWait = 120;

   /**
    * Setup for the intro panel
    */
   public IntroPanel() {
      try {
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Kiona-Regular.ttf")));
         font = new Font("Kiona", Font.PLAIN, 70);
      } catch (IOException | FontFormatException e) {
         System.out.println("Font not available");
      }
      this.setBackground(new Color(17, 17, 17));
   }

   /**
    * Runs the intro animation
    */
   public void go() {
      for (double i = 0; i < Math.PI * 7 / 2; i += 0.01) {
         //particles.add(new IntroParticle(x,y,(Math.random()*10-5),(Math.random()*10),(int)(Math.random()*12),(int)(Math.random()*(50)+200),new Color(255-(int)(Math.random()*0),255-(int)(Math.random()*0),(int)(Math.random()*255))));
         for (double j = -SIZE / 2; j < SIZE / 2; j += 10) {
            particles.add(new IntroParticle(x + j, y + Math.sqrt(Math.pow(SIZE / 2, 2) - Math.pow(j, 2)), (w * ((7 * Math.PI - 2 * i) * Math.sin(i) + 2 * Math.cos(i))) / (14 * Math.PI) * Math.random() / 10, -(h * ((7 * Math.PI - 2 * i) * Math.cos(i) - 2 * Math.sin(i))) / (14 * Math.PI) * Math.random() / 10, (int) (Math.random() * 12), (int) (Math.random() * (50) + 200)));
            //System.out.println(x + " " + y + " " + (x + j) + " " + (y + Math.sqrt(Math.pow(SIZE/2,2) - Math.pow((j),2))));
            particles.add(new IntroParticle(x + j, y - Math.sqrt(Math.pow(SIZE / 2, 2) - Math.pow(j, 2)), (w * ((7 * Math.PI - 2 * i) * Math.sin(i) + 2 * Math.cos(i))) / (14 * Math.PI) * Math.random() / 10, -(h * ((7 * Math.PI - 2 * i) * Math.cos(i) - 2 * Math.sin(i))) / (14 * Math.PI) * Math.random() / 10, (int) (Math.random() * 12), (int) (Math.random() * (50) + 200)));
         }
         double r1 = w / 2 - i * w / (7 * Math.PI);
         double r2 = h / 2 - i * h / (7 * Math.PI);
         x = (int) (r1 * Math.cos(i) + w / 2);
         y = (int) (r2 * Math.sin(i) + h / 2);
         //System.out.println("REE");
         try {
            Thread.sleep(2);
         } catch (Exception E) {
         }
      }
      for (double i = 0; i < 5; i += 0.01) {
         x = (int) (w / 2);
         y = (int) (h - h / (1 + Math.pow(Math.E, -i)));
         try {
            Thread.sleep(1);
         } catch (Exception E) {
         }
      }
      for (double i = 5; i > 0; i -= 0.01) {
         x = (int) (w / 2);
         y = (int) (h - h / (1 + Math.pow(Math.E, -i)));
         try {
            Thread.sleep(1);
         } catch (Exception E) {
         }
      }
      while (h - y > -SIZE / 2) {
         y += 4;
         try {
            Thread.sleep(1);
         } catch (Exception E) {
         }
      }
      for (int i = 0; i < 10000; i++) {
         double angle = (Math.random() * 1) * Math.PI;
         double dx = (Math.random() * 50) * Math.cos(angle);
         double dy = -(Math.random() * 50) * Math.sin(angle);
         particles.add(new IntroParticle(x + SIZE / 2, y + SIZE / 2, dx, dy, (int) (Math.random() * 12), (int) (Math.random() * (50) + 250)));
         //try{ Thread.sleep(1); } catch (Exception E){}
      }
      try {
         Thread.sleep(100);
      } catch (Exception E) {
      }
      text = "7";
      fontX = (int) ((w - metrics.stringWidth("7SPEED")) / 2);
      fontY = (int) (((h - 40 - metrics.getHeight()) / 2) + metrics.getAscent());
      for (double i = 0; i < 1; i += 0.01) {
         fade1 = i;
         try {
            Thread.sleep(10);
         } catch (Exception E) {
         }
      }
      try {
         Thread.sleep(1000);
      } catch (Exception E) {
      }

    /*
    dissolve = true;
    for (int i = fontY-metrics.getHeight()/2; i <= h; i+=5){
      for (int j = (int)(fontX); j < (int)(fontX + metrics.stringWidth("7SPEED")); j+=5){
        lineY = i;
        ///*
        if (img.getRGB(j,i) != -1118482){
          System.out.println(i + " " + j + " " + img.getRGB(j,i) + " " + Color.WHITE.getRGB());
        }
        if (img.getRGB(j,i) == Color.WHITE.getRGB()){
          particles.add(new IntroParticle(j,i,0,0,(int)(Math.random()*12),(int)(Math.random()*(50)+250),Color.WHITE));
        } else if (img.getRGB(j,i) == Color.YELLOW.getRGB()){
          particles.add(new IntroParticle(j,i,0,0,(int)(Math.random()*12),(int)(Math.random()*(50)+250),Color.YELLOW));
        }
        /
        particles.add(new IntroParticle(j,i,Math.random()*10-5,Math.random()*10-5,(int)(Math.random()*12),(int)(Math.random()*(50)+250),new Color(255-(int)(Math.random()*0),255-(int)(Math.random()*0),(int)(Math.random()*255))));
      }
      try{ Thread.sleep(10); } catch (Exception E){}
    }*/

      for (double i = 1; i >= 0; i -= 0.01) {
         fade1 = i;
         try {
            Thread.sleep(5);
         } catch (Exception E) {
         }
      }
      fade1 = 0;
   }

   /**
    * Paints all of the required elements in the JPanel.
    *
    * @param g a Graphics object
    */
   @Override
   public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      super.paintComponent(g2); //required
      setDoubleBuffered(true);
      g2.setBackground(Color.BLACK);
      for (int i = SIZE; i > 0; i--) {
         g2.setColor(new Color(255, 255, 255 - i ^ 2 * 255 / SIZE ^ 2));
         g2.fillOval(x - i / 2, y - i / 2, i, i);
         //g2.fillOval(x,y,SIZE,SIZE);
      }
      for (int i = particles.size() - 1; i >= 0; i--) {
         if (i < particles.size()) {
            if (particles.get(i) != null) {
               try {
                  if (particles.get(i).update()) {
                     particles.remove(i);
                  } else {
                     particles.get(i).render(g2);
                  }
               } catch (Exception e) {
                  System.out.println(i + " " + particles.size());
               }
            }
         }
      }
      g2.setFont(font);
      //System.out.println(metrics.getHeight());
      metrics = g2.getFontMetrics();
      g2.setColor(new Color((float) 1, (float) 1, (float) 1, (float) fade1));
      g2.drawString("7", fontX, fontY);
      g2.setColor(new Color((float) 1, (float) 1, (float) 0, (float) fade1));
      g2.drawString("SPEED", fontX + metrics.stringWidth("7"), fontY);
      //img = (BufferedImage)(this.createImage((int)w,(int)h));
      //for (int i = 0; i <
      if (dissolve) {
         g2.setColor(Color.BLACK);
         g2.fillRect(0, 0, (int) w, lineY);
         //System.out.println(w + " " + lineY);
      }
      // if (!particles.isEmpty()) {
      repaint();
      // }
   }

   public boolean checkAnimationOver() {

      return (particles.isEmpty());
   }
}
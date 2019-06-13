package client.ui;

import client.Client;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * InstructionPanel.java
 * This is the panel for displaying instructions
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-05-31
 */

public class InstructionPanel extends MenuPanel { //State=5
   private Graphics2D g2;
   private final int MAX_X= super.getWidth();
   private final int MAX_Y= super.getHeight();
   private final Client CLIENT = super.getClient();
   private final Font MAIN_FONT = super.getFont("main");
   private final Font HEADER_FONT = super.getFont("header");

   private CustomButton backButton = new CustomButton("Back");
   private CustomButton nextButton = new CustomButton("Next Page");
   private CustomButton prevButton = new CustomButton("Previous Page");

   //Instructions pages
   private BufferedImage[] pages = new BufferedImage[5];
   private int currentPage = 0;

    /**
     * Sets up the panel
     */
   public InstructionPanel() {

      //Import instructions pages
      try {
         pages[0] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/instructions/Page1.png"));
         pages[1] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/instructions/Page2.png"));
         pages[2] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/instructions/Page3.png"));
         pages[3] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/instructions/Page4.png"));
         pages[4] = ImageIO.read(new File(System.getProperty("user.dir") + "/res/instructions/Page5.png"));
      } catch (IOException e) {
         System.out.println("Unable to find an image");
      }

      //Setting up buttons
      backButton.addActionListener((ActionEvent e) -> {
         CLIENT.setNextPanel(2);
      });
      backButton.setBounds(MAX_X / 2 - 100, MAX_Y * 8 / 10, 200, MAIN_FONT.getSize() + 20);
      this.add(backButton);
      //Next page button
      nextButton.addActionListener((ActionEvent e) -> {
         currentPage++;
         if (currentPage == 4){ //Remove next button on last page
            nextButton.setBounds(0, 0, 0, 0);
         } else if (currentPage == 1){ //Show previous button
            prevButton.setBounds(MAX_X / 2 - 350, MAX_Y * 8 / 10, 200, MAIN_FONT.getSize() + 20);
         }
      });
      nextButton.setBounds(MAX_X / 2 + 150, MAX_Y * 8 / 10, 200, MAIN_FONT.getSize() + 20);
      this.add(nextButton);
      //Previous page button
      prevButton.addActionListener((ActionEvent e) -> {
         currentPage--;
         if (currentPage == 0){ //Remove previous button on first page
            prevButton.setBounds(0, 0, 0, 0);
         } else if (currentPage == 3){ //Show next button
            nextButton.setBounds(MAX_X / 2 + 150, MAX_Y * 8 / 10, 200, MAIN_FONT.getSize() + 20);
         }
      });
      prevButton.setBounds(0, 0, 0, 0); //Start off invisible
      this.add(prevButton);


      //Basic visuals
      this.setDoubleBuffered(true);
      this.setBackground(new Color(150, 150, 150));
      this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
      this.setVisible(true);
      this.setFocusable(true);
   }

    /**
     * Paints the create panel on the screen
     *
     * @param g used to draw the panel
     */
   @Override
   public void paintComponent(Graphics g) {
      g2 = (Graphics2D) g;
      super.paintComponent(g);

      //Background
      drawBackground(g2);
      drawAllParticles(g2);
      g2.setColor(Color.WHITE);
      g2.setFont(HEADER_FONT);
      g2.drawString("Instructions", (MAX_X - g2.getFontMetrics().stringWidth("Instructions")) / 2, (MAX_Y / 5));

      //Draw current instructions page
      g2.drawImage(pages[currentPage], MAX_X / 2 - 360, MAX_Y * 8 / 10 - 475, 720, 450, null);
   }
}

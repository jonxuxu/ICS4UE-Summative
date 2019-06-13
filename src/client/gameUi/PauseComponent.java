package client.gameUi;

import client.Client;
import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseComponent extends ThemedPanel {
   public Client client;
   public PauseComponent thisComponent;
   private final static Font titleFont = new Font("Cambria Math", Font.PLAIN, 35);
   private final static Font headerFont1 = new Font("Cambria Math", Font.PLAIN, 25);
   private final static Font headerFont2 = new Font("Cambria Math", Font.PLAIN, 22);

   public PauseComponent(int width, int height, Client client) {
      super();
      thisComponent = this;
      this.client = client;
      // UI Components
      this.setLayout(new BorderLayout());
      this.setBackground(new Color(33, 35, 37));

      JLabel title = new JLabel("Pause");
      title.setForeground(Color.WHITE);
      title.setFont(titleFont);
      title.setHorizontalAlignment(JLabel.CENTER);

      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());
      //mainPanel.setPreferredSize(new Dimension(width, (int)(height*0.8)));
      mainPanel.setBackground(null);
         JPanel leftPanel = new JPanel();
         leftPanel.setBackground(null);
         leftPanel.setPreferredSize(new Dimension(width/2, (int)(height*0.6)));
         leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
            JLabel volumeTitle = new JLabel("   Volume");
            volumeTitle.setForeground(Color.WHITE);
            volumeTitle.setFont(headerFont1);
            JPanel row1 = new JPanel();
            row1.setBackground(null);
               JLabel masterTitle = new JLabel("Master Gain");
               masterTitle.setForeground(Color.WHITE);
               masterTitle.setFont(headerFont2);
               JSlider masterVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, 50); // Setting master gain
               masterVolume.addChangeListener(new ChangeListener() {
                  @Override
                  public void stateChanged(ChangeEvent e) {
                     JSlider source = (JSlider)e.getSource();
                     if (!source.getValueIsAdjusting()) {
                        float val = (float)(20f * Math.log10(source.getValue()/100.0));
                        System.out.println(val);
                        client.changeSoundLevel(0, val);
                     }
                  }
               });
               masterVolume.setBackground(null);
            row1.add(masterTitle);
            row1.add(masterVolume);
         leftPanel.add(volumeTitle);
         leftPanel.add(row1);
         JPanel rightPanel = new JPanel();
         rightPanel.setBackground(null);
         rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
            JLabel graphicsTitle = new JLabel("   Graphics");
            graphicsTitle.setForeground(Color.WHITE);
            graphicsTitle.setFont(headerFont1);
         rightPanel.add(graphicsTitle);
      mainPanel.add(leftPanel, BorderLayout.WEST);
      mainPanel.add(rightPanel, BorderLayout.CENTER);

      JPanel bottomPanel = new JPanel();
      bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
      bottomPanel.setPreferredSize(new Dimension(width, (int)(height*0.2)));
      bottomPanel.setBackground(null);
        JPanel bottomRow1 = new JPanel();
        bottomRow1.setBackground(null);
          JButton surrender = new JButton("Surrender");
          surrender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
          });
          JButton quit = new JButton("Quit Game");
          quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              System.exit(0);
            }
          });
        bottomRow1.add(surrender);
        bottomRow1.add(quit);
        JPanel bottomRow2 = new JPanel();
        bottomRow2.setBackground(null);
          JButton back = new JButton("Return to game");
          back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             client.requestFocus();
             thisComponent.setVisible(false);
            }
          });
        bottomRow2.add(back);
      bottomPanel.add(bottomRow1);
      bottomPanel.add(bottomRow2);

      this.add(title, BorderLayout.NORTH);
      this.add(mainPanel, BorderLayout.CENTER);
      this.add(bottomPanel, BorderLayout.SOUTH);

      this.setVisible(false);

   }

}
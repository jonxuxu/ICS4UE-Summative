package client.gameUi;

import client.Client;
import client.ui.CustomLabel;

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
   private final static Font headerFont = new Font("Cambria Math", Font.PLAIN, 25);

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
      this.add(title, BorderLayout.NORTH);

      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());
      //mainPanel.setPreferredSize(new Dimension(width, (int)(height*0.8)));
      mainPanel.setBackground(new Color(0,0,0,0));
         JPanel leftPanel = new JPanel();
         leftPanel.setBackground(Color.red);
         leftPanel.setPreferredSize(new Dimension(width/2, (int)(height*0.8)));
         leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
            JLabel volumeTitle = new JLabel("   Volume");
            volumeTitle.setForeground(Color.WHITE);
            volumeTitle.setFont(headerFont);
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
         leftPanel.add(volumeTitle);
         leftPanel.add(masterVolume);
         JPanel rightPanel = new JPanel();
         rightPanel.setBackground(Color.blue);
         rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
            JLabel graphicsTitle = new JLabel("   Graphics");
            graphicsTitle.setForeground(Color.WHITE);
            graphicsTitle.setFont(headerFont);
         rightPanel.add(graphicsTitle);
      mainPanel.add(leftPanel, BorderLayout.WEST);
      mainPanel.add(rightPanel, BorderLayout.CENTER);
      this.add(mainPanel, BorderLayout.CENTER);

      JButton back = new JButton("Return to game");
      back.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            client.requestFocus();
            thisComponent.setVisible(false);
         }
      });
      this.add(back, BorderLayout.SOUTH);

      this.setVisible(false);

   }

}
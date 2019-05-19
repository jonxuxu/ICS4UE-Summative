import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/*
Here is how the messages work.
First, the server must send a message to the client. The client immediately deciphers and sends it's own message, but it is
limited by waiting for the server to send a message again. However, in the menu this is irrelevant
If a server is trying to send two messages, then both can be recieved as the server is limiting here. Essentially, what will
occur is the client sending an output that does not reach anyone, which is perfectly fine.
 */

/**
 * Client.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-17
 */

public class Client extends JFrame implements WindowListener {
   private Socket socket;
   private BufferedReader input;
   private PrintWriter output;
   private String username;
   private boolean start = true;
   private boolean connected = false;
   private JPanel[] allPanels = new JPanel[6];
   private String[] panelNames = {"LOGIN_PANEL", "MAIN_PANEL", "CREATE_PANEL", "JOIN_PANEL", "WAITING_PANEL", "INTERMEDIATE_PANEL"};
   private CustomMouseAdapter myMouseAdapter = new CustomMouseAdapter();
   private CustomKeyListener myKeyListener = new CustomKeyListener();
   private boolean sendName = false;
   private boolean testGame = false;
   private Font MAIN_FONT;
   //State legend:
   //0: Login panel, 1: Create/Join game, 2:Create game, 3:Join game , 4:Waiting , 5:Game
   private int state = 0;
   private int newState = 0;
   private CardLayout cardLayout = new CardLayout(5, 5);
   private JPanel mainContainer = new JPanel(cardLayout);
   private String gameName;
   private String gamePassword;
   private String attemptedGameName;
   private String attemptedGamePassword;
   private boolean host = false;
   private boolean notifyReady = false;
   private ArrayList<Player> onlineList = new ArrayList<Player>();
   private GamePlayer[] gamePlayers;
   private Player myPlayer;
   private boolean gameBegin;
   private String outputString;//This is what is outputted to the game
   private boolean loading = false;
   private int DESIRED_Y = 500;
   private int DESIRED_X = 800;
   private int MAX_Y;
   private int MAX_X;
   private double scaling;

   public Client() {
      super("Dark");

      //Control set up (the mouse listeners are attached to the game panel)
      this.addKeyListener(myKeyListener);

      //Font set up
      try {
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Kiona-Bold.ttf")));
      } catch (IOException | FontFormatException e) {
         System.out.print("Font not available");
      }
      MAIN_FONT = new Font("Kiona Bold", Font.PLAIN, 16);

      //Basic set up
      MAX_X = (int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth());
      MAX_Y = (int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
      this.setSize(MAX_X, MAX_Y);
      this.setVisible(true);
      Dimension actualSize = this.getContentPane().getSize();
      MAX_Y = (int) (actualSize.getHeight()); //Re-adjust after removing the top
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocationRelativeTo(null);
      this.setFocusable(true); //Necessary so that the buttons and stuff do not take over the focus
      this.setExtendedState(JFrame.MAXIMIZED_BOTH);

      //Creating components
      allPanels[0] = new LoginPanel();
      allPanels[1] = new MenuPanel();
      allPanels[2] = new CreatePanel();
      allPanels[3] = new JoinPanel();
      allPanels[4] = new WaitingPanel();
      allPanels[5] = new IntermediatePanel();
      //Adding to mainContainer cards
      mainContainer.setBackground(new Color(37, 37, 37));
      for (int i = 0; i < allPanels.length; i++) {
         mainContainer.add(allPanels[i], panelNames[i]);
      }
      this.add(mainContainer);
      cardLayout.show(mainContainer, panelNames[0]);
      this.setVisible(true);//Must be called again so that it appears visible
   }

   public static void main(String[] args) {
      new Client().go();
   }

   public void go() {
      connect();
      boolean inputReady = false;
      try {
         input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         output = new PrintWriter(socket.getOutputStream());
         //Start with entering the name. This must be separated from the rest
         while (start) {
            repaintPanels();
            if (sendName) {
               sendName = false;
               output.println(username);
               System.out.println(username);
               output.flush();
               waitForInput();
               if (start) {
                  System.out.println("Invalid username");
               } else {
                  System.out.println("Valid username");
               }
            }
         }
         //Username successfully entered in
         newState = 1; //Only update newState, state will follow shortly
         Clock time = new Clock();//This is only for the game panel
         while (connected) {
            time.setTime();
            if (time.getFramePassed()) {
               repaintPanels();
               if (input.ready()) {
                  decipherInput(input.readLine());
               }
               //Otherwise, continue to send messages. The lines below are for when something is going to be sent
               if (!gameBegin) {
                  if (testGame) {
                     testGame = false;
                     if (state == 2) {
                        output.println("C" + attemptedGameName + " " + attemptedGamePassword);
                     } else {
                        output.println("J" + attemptedGameName + " " + attemptedGamePassword);
                     }
                     output.flush();
                     waitForInput();
                  }
                  if (notifyReady) {
                     notifyReady = false;
                     output.println("R");
                     loading = true;
                     output.flush();
                     waitForInput();
                  }
               } else {
                  //This is where everything is output. Output the key controls
                  //Always begin with clearing the outputString
                  //The output string contains all the information required for the server.
                  //I'm unsure if I should process some here, or just send all the raw data
                  //If the raw data was to be sent, the following should be sent: MAX_X/MAX_Y (only once),
                  //the x and y of the mouse, the relevant keyboard presses maybe? (not all)
                  outputString = "";
                  double angleOfMovement = myKeyListener.getAngle();
                  double angleOfClick = -1;
                  double lengthOfClick = -1;
                  if (myMouseAdapter.getPressed()) {
                     angleOfClick = myMouseAdapter.getAngleOfClick(); //Sends the correct angle
                     lengthOfClick = myMouseAdapter.getLengthOfClick(); //Sends the fully calculated length
                  }
                  if (angleOfClick != -1) {
                     System.out.println(angleOfClick + " " + lengthOfClick);
                  }
                  //Check to see if it can only reach within the boundaries of the JFrame. Make sure that this is true, otherwise you
                  //must add the mouse adapter to the JPanel.
                  outputString = "" + angleOfMovement;//If it is -1, then the server will recognize to stop
                  output.println(outputString);
                  output.flush();
               }
            }
            //If a message is sent, wait until a response is received before doing anything
         }
      } catch (IOException e) {
         System.out.println("Unable to read/write");
      }
   }

   public void waitForInput() {
      boolean inputReady = false;
      try {
         while (!inputReady) {
            if (input.ready()) {
               inputReady = true;
               decipherInput(input.readLine());
            }
         }
      } catch (IOException e) {
         System.out.println("Lost connection");
      }
   }

   public boolean isParsable(char input) {
      try {
         int test = Integer.parseInt(input + "");
         return (true);
      } catch (NumberFormatException e) {
         return (false);
      }
   }

   public void decipherInput(String input) {
      char initializer = input.charAt(0); //Hopefully, every message should have something
      //For the menu, numbers represent error/success, A represents add all (if you join),
      //N represents add one new player, and B represents begin the game
      input = input.substring(1);//Remove the initializer
      if (!gameBegin) {
         if (isParsable(initializer)) {
            if (start) {
               if (initializer == '0') {
                  start = false; //Otherwise, nothing occurs
               }
            } else if ((state == 2) || (state == 3)) {
               if (initializer == '0') {
                  newState = 4;//Sends to a waiting room
                  gameName = attemptedGameName;
                  gamePassword = attemptedGamePassword;
                  System.out.println("Valid Game");
                  if (state == 2) {
                     host = true;
                     myPlayer = new Player(username);
                     onlineList.add(myPlayer);
                  }
               } else {
                  System.out.println("Invalid Game");
               }
            } else if (state == 4) {
               if (initializer == '0') {
                  System.out.println("Starting Game");
               } else {
                  System.out.println("Unable to Start Game");
               }
            }
         } else if (initializer == 'A') {
            while (input.contains(" ")) {
               onlineList.add(new Player(input.substring(0, input.indexOf(" "))));
               input = input.substring(input.indexOf(" ") + 1);
            }
         } else if (initializer == 'N') {
            onlineList.add(new Player(input));
         } else if (initializer == 'B') {
            gamePlayers = new GamePlayer[onlineList.size()];
            for (int i = 0; i < onlineList.size(); i++) {
               gamePlayers[i] = new GamePlayer(onlineList.get(i).getUsername());
            }
            newState = 5;//Sends to the game screen
            gameBegin = true;
         }
      } else {
         //Below is all temp. In reality, a serializable class should be decoded and output here

         input = input.trim();
         String[] initialSplit = input.split(" ", -1);
         for (int i = 0; i < initialSplit.length; i++) {
            String[] secondSplit = initialSplit[i].split(",", -1);
            int[] tempXy = {Integer.parseInt(secondSplit[1]), Integer.parseInt(secondSplit[2])};
            //System.out.println(tempXy[0]+" "+tempXy[1]);
            gamePlayers[Integer.parseInt(secondSplit[0])].setXy(tempXy);
         }
      }
   }

   public void repaintPanels() {
      if (state != newState) {
         state = newState;
         cardLayout.show(mainContainer, panelNames[state]);
      }
      if (state < 5) {
         allPanels[state].repaint();
      } else {
         ((IntermediatePanel) (allPanels[state])).repaintReal();
      }
   }

   public void connect() {
      try {
         socket = new Socket("localhost", 5001);
         System.out.println("Successfully connected");
         connected = true;
      } catch (Exception e) {
         System.out.println("Unable to connect");
      }
   }

   public void windowClosing(WindowEvent e) {
      output.println("X");
      output.flush();
      dispose();
      System.exit(0);
   }

   public void windowOpened(WindowEvent e) {
   }

   public void windowActivated(WindowEvent e) {
   }

   public void windowIconified(WindowEvent e) {
   }

   public void windowDeiconified(WindowEvent e) {
   }

   public void windowDeactivated(WindowEvent e) {
   }

   public void windowClosed(WindowEvent e) {
   }

   private class LoginPanel extends JPanel { //State=0
      private Graphics2D g2;
      private JTextField nameField = new JTextField(3);
      private CustomButton nameButton = new CustomButton("Click to test name");

      public LoginPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic username field
         nameButton.addActionListener((ActionEvent e) -> {
            if (!(nameField.getText().contains(" "))) {
               username = nameField.getText();
               sendName = true;
            } else {
               System.out.println("Error: Spaces exist");
            }
         });
         nameField.setFont(MAIN_FONT);
         nameField.setBounds(MAX_X / 2 - 75, MAX_Y / 5, 150, 30);
         nameButton.setBounds(MAX_X / 2 - 130, MAX_Y * 3 / 10, 260, 30);
         this.add(nameField);
         this.add(nameButton);

         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);

      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);
         //this.requestFocusInWindow();// Removed, this interferes with the textboxes. See if this is truly necessary

         //Begin drawing

      }
   }

   private class MenuPanel extends JPanel {//State=1
      private Graphics2D g2;
      private CustomButton createButton = new CustomButton("Create game");
      private CustomButton joinButton = new CustomButton("Join game");

      public MenuPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic create and join server buttons
         createButton.addActionListener((ActionEvent e) -> {
            newState = 2;
         });
         createButton.setBounds(MAX_X / 2 - 130, MAX_Y * 3 / 10, 260, 30);
         this.add(createButton);

         joinButton.addActionListener((ActionEvent e) -> {
            newState = 3;
         });
         joinButton.setBounds(MAX_X / 2 - 130, MAX_Y / 2, 260, 30);
         this.add(joinButton);

         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
      }
   }

   private class CreatePanel extends JPanel { //State =2
      private Graphics2D g2;
      private boolean generateGraphics = true;
      private JTextField gameNameField = new JTextField(3);
      private JTextField gamePasswordField = new JTextField(3);
      private CustomButton testGameButton = new CustomButton("Confirm game");

      public CreatePanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic create and join server buttons
         testGameButton.addActionListener((ActionEvent e) -> {
            if (!(gameNameField.getText().contains(" ")) && (!(gamePasswordField.getText().contains(" ")))) {
               attemptedGameName = gameNameField.getText();
               attemptedGamePassword = gamePasswordField.getText();
               testGame = true;
            } else {
               System.out.println("Error: Spaces exist");
            }
         });
         testGameButton.setBounds(MAX_X / 2 - 130, MAX_Y * 4 / 10, 260, 30);
         this.add(testGameButton);
         gameNameField.setFont(MAIN_FONT);
         gameNameField.setBounds(MAX_X / 2 - 75, MAX_Y / 5, 150, 30);
         this.add(gameNameField);
         gamePasswordField.setFont(MAIN_FONT);
         gamePasswordField.setBounds(MAX_X / 2 - 75, MAX_Y * 3 / 10, 150, 30);
         this.add(gamePasswordField);
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         if (generateGraphics) {
            generateGraphics = false;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(MAIN_FONT);
         }
         super.paintComponent(g);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
      }
   }

   private class JoinPanel extends JPanel { //State =3
      private Graphics2D g2;
      private JTextField gameNameTestField = new JTextField(3);
      private JTextField gamePasswordTestField = new JTextField(3);
      private CustomButton testGameButton = new CustomButton("Join game");

      public JoinPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic create and join server buttons
         testGameButton.addActionListener((ActionEvent e) -> {
            attemptedGameName = gameNameTestField.getText();
            attemptedGamePassword = gamePasswordTestField.getText();
            testGame = true;
         });
         testGameButton.setBounds(MAX_X / 2 - 130, MAX_Y * 4 / 10, 260, 30);
         this.add(testGameButton);
         gameNameTestField.setFont(MAIN_FONT);
         gameNameTestField.setBounds(MAX_X / 2 - 75, MAX_Y / 5, 150, 30);
         this.add(gameNameTestField);
         gamePasswordTestField.setFont(MAIN_FONT);
         gamePasswordTestField.setBounds(MAX_X / 2 - 75, MAX_Y * 3 / 10, 150, 30);
         this.add(gamePasswordTestField);
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
      }
   }

   private class WaitingPanel extends JPanel { //State=4
      private Graphics2D g2;
      private boolean buttonAdd = true;
      private boolean buttonRemove = true;
      private CustomButton readyGameButton = new CustomButton("Begin game");

      public WaitingPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Setting up buttons
         readyGameButton.setBounds(MAX_X / 2 - 130, MAX_Y * 4 / 10, 260, 30);
         readyGameButton.addActionListener((ActionEvent e) -> {
            notifyReady = true;
         });
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(70, 70, 70));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
         //if host==true, then display the ready button
         g2.setColor(Color.white);
         if ((host) && (buttonAdd)) {
            this.add(readyGameButton);
            buttonAdd = false;
         }
         for (int i = 0; i < onlineList.size(); i++) {
            g2.drawString(onlineList.get(i).getUsername(), 0, 40 * (i + 1));
         }
         if (loading) {
            if (buttonRemove) {
               this.remove(readyGameButton);
               buttonRemove = false;
            }
            g2.drawString("LOADING", MAX_X / 2, MAX_Y / 2);
         }
      }
   }

   private class IntermediatePanel extends JPanel { //State=5 (intermediate)=
      private GamePanel gamePanel = new GamePanel();

      public IntermediatePanel() {
         if ((1.0 * MAX_Y / MAX_X) > (1.0 * DESIRED_Y / DESIRED_X)) { //Make sure that these are doubles
            //Y is excess
            scaling = 1.0 * MAX_X / DESIRED_X;
         } else {
            //X is excess
            scaling = 1.0 * MAX_Y / DESIRED_Y;
         }
         int[] tempXy = {(int) (DESIRED_X * scaling / 2), (int) (DESIRED_Y * scaling / 2)};
         myMouseAdapter.setCenterXy(tempXy);
         myMouseAdapter.setScaling(scaling);
         //Scaling is a factor which reduces the MAX_X/MAX_Y so that it eventually fits
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         this.add(gamePanel);
         gamePanel.setBounds((int) ((MAX_X - (DESIRED_X * scaling)) / 2), (int) ((MAX_Y - (DESIRED_Y * scaling)) / 2), (int) (DESIRED_X * scaling), (int) (DESIRED_Y * scaling));
         //System.out.println(scaling);
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
      }

      public void repaintReal() {
         gamePanel.repaint();
      }
   }

   private class GamePanel extends JPanel {//State=5
      private Graphics2D g2;
      private boolean generateGraphics = true;

      public GamePanel() {
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(40, 40, 40));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
         this.addMouseListener(myMouseAdapter);
         this.addMouseWheelListener(myMouseAdapter);
         this.addMouseMotionListener(myMouseAdapter);
      }

      @Override
      public void paintComponent(Graphics g) {
         if ((state == 5) && (generateGraphics)) {
            g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(MAIN_FONT);
            generateGraphics = false;
         } else {
            g2 = (Graphics2D) g;
         }
         super.paintComponent(g2);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
         //This is temp, the decoded serialized class should be called here for .draw
         for (GamePlayer currentGamePlayer : gamePlayers) {
            currentGamePlayer.draw(g2);
         }
         g2.setColor(Color.white);
         g2.drawLine((int) (DESIRED_X * scaling / 2), (int) (DESIRED_Y * scaling / 2), (int) (DESIRED_X * scaling / 2), (int) (DESIRED_Y * scaling / 2) + 100);
         g2.setColor(Color.white);
         g2.drawLine((int) (DESIRED_X * scaling / 2), (int) (DESIRED_Y * scaling / 2), (int) (DESIRED_X * scaling / 2) + 100, (int) (DESIRED_Y * scaling / 2));
      }
   }

   private class CustomButton extends JButton {
      private Color foregroundColor = new Color(180, 180, 180);
      private Color backgroundColor = new Color(50, 50, 50);

      CustomButton(String description) {
         super(description);
         super.setContentAreaFilled(false);
         this.setFont(MAIN_FONT);
         this.setForeground(foregroundColor);
         this.setBackground(backgroundColor);
         this.setFocusPainted(false);
      }

      CustomButton(String description, Color foregroundColor, Color backgroundColor) {
         super(description);
         super.setContentAreaFilled(false);
         this.setFont(MAIN_FONT);
         this.foregroundColor = foregroundColor;
         this.backgroundColor = backgroundColor;
         this.setForeground(foregroundColor);
         this.setBackground(backgroundColor);
         this.setFocusPainted(false);
      }

      @Override
      protected void paintComponent(Graphics g) {
         if (getModel().isPressed()) {
            g.setColor(backgroundColor.brighter().brighter());
         } else if (getModel().isRollover()) {
            g.setColor(backgroundColor.brighter());
         } else {
            g.setColor(getBackground());
         }
         g.fillRect(0, 0, getWidth(), getHeight());
         super.paintComponent(g);
      }
   }
}

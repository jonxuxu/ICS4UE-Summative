package client;

import client.map.FogMap;
import client.particle.AshParticle;
import client.sound.soundEffectManager;
import client.ui.CustomTextField;
import client.ui.IntroPanel;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
   private boolean connected = false;
   private JPanel[] allPanels = new JPanel[8];
   private final String[] PANEL_NAMES = {"LOGIN_PANEL", "INTRO_PANEL", "MAIN_PANEL", "CREATE_PANEL", "JOIN_PANEL", "INSTRUCTION_PANEL", "WAITING_PANEL", "INTERMEDIATE_PANEL"};
   private CustomMouseAdapter myMouseAdapter = new CustomMouseAdapter();
   private CustomKeyListener myKeyListener = new CustomKeyListener();
   private boolean sendName = false;
   private boolean testGame = false;
   private Font MAIN_FONT;
   private Font HEADER_FONT;
   //State legend:
   private int state = 0;//should be 0
   private int newState = 0;//should be 0
   private CardLayout cardLayout = new CardLayout(5, 5);
   private JPanel mainContainer = new JPanel(cardLayout);
   private String gameName;
   private String gamePassword;
   private String attemptedGameName;
   private String attemptedGamePassword;
   private boolean host = false;
   private boolean notifyReady = false;
   private ArrayList<User> onlineList = new ArrayList<User>();
   private Player[] players;
   private User myUser;
   private Player myPlayer;
   private boolean gameBegin;
   private String outputString;//This is what is outputted to the game
   private boolean loading = false;
   private int DESIRED_Y = 500;
   private int DESIRED_X = 950;
   private int MAX_Y;
   private int MAX_X;
   private double scaling;
   private Sector[][] sectors;
   private ArrayList<Integer> disconnectedPlayerID = new ArrayList<Integer>();
   private int[] errors = new int[3];
   private String errorMessages[] = {"Success", "This name is already taken", "Only letters and numbers are allowed", "This exceeds 15 characters", "This is blank", "Wrong username/password", "Game is full/has already begun"};
   private BufferedImage sheet;
   private BufferedImage loadedSheet;
   private boolean logout = false;
   private boolean leaveGame = false;
   private BufferedImage TITLE_SCREEN;
   private BufferedImage TITLE;
   private BufferedImage LOADED_TITLE_SCREEN;
   private BufferedImage LOADED_TITLE;
   private boolean unableToConnect = false;
   private FogMap fog;
   private boolean testingBegin = false;
   private double introScaling;
   private ArrayList<AshParticle> particles = new ArrayList<AshParticle>();
   private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
   private ArrayList<AOE> aoes = new ArrayList<AOE>();
   private int[] centerXy = new int[2];
   private int myTeam;
   private boolean teamChosen = false;
   private int[] xyAdjust = new int[2];
   private soundEffectManager soundEffect = new soundEffectManager();

   public Client() {
      super("Dark");

      //Control set up (the mouse listeners are attached to the game panel)
      this.addKeyListener(myKeyListener);

      //Font+image set up
      try {
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Quicksand-Regular.ttf")));
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Quicksand-Bold.ttf")));
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Quicksand-Light.ttf")));
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Quicksand-Medium.ttf")));
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Akura Popo.ttf")));
         TITLE_SCREEN = ImageIO.read(new File(".\\res\\TitleScreenDark.png"));
         TITLE = ImageIO.read(new File(".\\res\\Title.png"));
      } catch (IOException | FontFormatException e) {
         System.out.println("Font not available");
      }


      //Basic set up
      MAX_X = (int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth());
      MAX_Y = (int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
      this.setSize(MAX_X, MAX_Y);
      this.setVisible(true);
      Dimension actualSize = this.getContentPane().getSize();
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocationRelativeTo(null);
      this.setFocusable(true); //Necessary so that the buttons and stuff do not take over the focus
      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
      //Creating components
      allPanels[7] = new IntermediatePanel();
      ((IntermediatePanel) (allPanels[7])).initializeScaling();//must be called before the rest of the fonts

      allPanels[0] = new LoginPanel();
      allPanels[1] = new IntroPanel();
      allPanels[2] = new MenuPanel();
      allPanels[3] = new CreatePanel();
      allPanels[4] = new JoinPanel();
      allPanels[5] = new InstructionPanel();
      allPanels[6] = new WaitingPanel();
      //Adding to mainContainer cards
      mainContainer.setBackground(new Color(0, 0, 0));
      for (int i = 0; i < allPanels.length; i++) {
         mainContainer.add(allPanels[i], PANEL_NAMES[i]);
      }
      this.add(mainContainer);
      //cardLayout.show(mainContainer, PANEL_NAMES[0]);
      cardLayout.show(mainContainer, PANEL_NAMES[0]);
      this.setVisible(true);//Must be called again so that it appears visible
      this.addKeyListener(myKeyListener);
      this.addWindowListener(this);
      ((IntermediatePanel) (allPanels[7])).initializeSize();

      // Setting up fog (should be moved soon TM)
      int[] xy = {300, 300};
      fog = new FogMap(xy, scaling);
      // TODO: Set player spawn xy later
      // myPlaer.getXy();
   }

   public static void main(String[] args) {
      new Client().go();
   }

   public void go() {
      boolean inputReady = false;
      while (!connected) {
         //Idle
         repaintPanels();
         connect();
      }
      try {
         input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         output = new PrintWriter(socket.getOutputStream());
         //Start with entering the name. This must be separated from the rest
         //Username successfully entered in
         int fogTicks = 0;
         Clock time = new Clock();
         while (connected) {
            //Otherwise, continue to send messages. The lines below are for when something is going to be sent
            if (!gameBegin) {
               if (time.getFramePassed()) {
                  repaintPanels();
               }
               //Recieves input if possible
               if (input.ready()) {
                  decipherInput(input.readLine());
               }

               //Deal with output when going back through the menu
               if (logout) {
                  output.println("B");//for back
                  output.flush();
                  username = null;
                  logout = false;
               }
               if (leaveGame) {
                  output.println("B");//for back
                  output.flush();
                  onlineList.clear();
                  leaveGame = false;
               }
               if (sendName) {
                  sendName = false;
                  if (username != null) {
                     if (verifyString(username, 0)) {
                        output.println("U" + username);
                        output.flush();
                        waitForInput();
                     }
                     if (errors[0] == 0) {
                        System.out.println("Valid username");
                     } else {
                        System.out.println("Error: " + errorMessages[errors[0]]);
                     }
                  }
               }
               if (testGame) {
                  testGame = false;
                  if ((verifyString(attemptedGameName, 1)) && (verifyString(attemptedGamePassword, 2))) {
                     if (state == 3) {
                        output.println("C" + attemptedGameName + " " + attemptedGamePassword);
                     } else {
                        output.println("J" + attemptedGameName + " " + attemptedGamePassword);
                     }
                     output.flush();
                     waitForInput();
                  }
                  System.out.println(errors[2] + " " + attemptedGameName + " " + attemptedGamePassword);
                  if ((errors[1] == 0) && (errors[2] == 0)) {
                     System.out.println("Valid game");
                  } else {
                     System.out.println("Error: " + errorMessages[errors[1]]);
                     System.out.println("Error: " + errorMessages[errors[2]]);
                  }
               }
               if (notifyReady) {
                  notifyReady = false;
                  output.println("R");
                  output.flush();
                  waitForInput();
               }
               if (teamChosen) {
                  teamChosen = false;
                  output.println("E" + myTeam);//E for now, when testing is removed it will be T
                  output.flush();
               }
               if (testingBegin) {
                  username = Math.random() + "";
                  myUser = new User(username);
                  output.println("T" + username);//test
                  output.flush();
                  waitForInput();
                  host = true;
                  newState = 6;
                  gameName = "";
                  gamePassword = "";
                  players = new Player[onlineList.size()];
                  for (int i = 0; i < onlineList.size(); i++) {
                     players[i] = new SafeMarksman(onlineList.get(i).getUsername());
                     if (onlineList.get(i).getUsername().equals(myUser.getUsername())) {
                        myPlayer = players[i];
                     }
                  }
                  testingBegin = false;
               }
            } else {
               // TODO: Initialize map ONCE after game begin

               if (input.ready()) {
                  xyAdjust[0] = (int) (centerXy[0] - myPlayer.getXy()[0] * scaling);
                  xyAdjust[1] = (int) (centerXy[1] - myPlayer.getXy()[1] * scaling);
                  decipherInput(input.readLine());//read input
                  //This is where everything is output. Output the key controls
                  //Always begin with clearing the outputString
                  //The output string contains all the information required for the server.
                  //I'm unsure if I should process some here, or just send all the raw data
                  //If the raw data was to be sent, the following should be sent: MAX_X/MAX_Y (only once),
                  //the x and y of the mouse, the relevant keyboard presses maybe? (not all)
                  outputString = "";
                  int angleOfMovement = myKeyListener.getAngle();
                  int[] xyPos = new int[2]; //Scaled to the map

                  xyPos[0] = myMouseAdapter.getDispXy()[0] + myPlayer.getXy()[0]; //Make it for hover
                  xyPos[1] = myMouseAdapter.getDispXy()[1] + myPlayer.getXy()[1];

                  //Check to see if it can only reach within the boundaries of the JFrame. Make sure that this is true, otherwise you
                  //must add the mouse adapter to the JPanel.

                  boolean[] spellsPressed = myKeyListener.getSpell();
                  boolean[] leftRight = myMouseAdapter.getLeftRight();
                  StringBuilder outputString = new StringBuilder();
                  for (int i = 0; i < spellsPressed.length; i++) {
                     if (spellsPressed[i]) {
                        outputString.append("S" + i);
                     }
                  }
                  if ((spellsPressed[0]) || (spellsPressed[1]) || (spellsPressed[2])) {
                     outputString.append(" "); //Add the seperator
                  }
                  if (angleOfMovement != -10) {
                     outputString.append("M" + myPlayer.getDisp(angleOfMovement)[0] + "," + myPlayer.getDisp(angleOfMovement)[1] + " ");
                  }
                  if (myMouseAdapter.getPressed()) {
                     System.out.println("wdw");
                     if (myMouseAdapter.getLeftRight()[0]) {
                        outputString.append("A" + " ");
                        System.out.println("1w");
                     }
                     if (myMouseAdapter.getLeftRight()[1]) {
                        soundEffect.playSound("cow");
                        outputString.append("F" + " ");
                        System.out.println("2wwwdw");
                     }
                  }
                  // outputString = angleOfMovement + " " + xyDisp[0] + " " + xyDisp[1] + " " + spellsPressed[0] + " " + spellsPressed[1] + " " + spellsPressed[2] + " " + leftRight[0] + " " + leftRight[1];//If it is -1, then the server will recognize to stop
                  outputString.append("P" + xyPos[0] + "," + xyPos[1] + " ");
                  if (!outputString.toString().trim().isEmpty()) {
                     output.println(outputString.toString().trim());
                     output.flush();
                  }
                  repaintPanels();
               }
            }
         }
         //If a message is sent, wait until a response is received before doing anything
      } catch (IOException e) {
         System.out.println("Unable to read/write");
      }
   }

   public boolean verifyString(String testString, int errorIndex) {
      errors[errorIndex] = 0;
      if (testString.length() < 15) {
         if (testString.isEmpty()) {
            errors[errorIndex] = 4;
         } else {
            for (int i = 0; i < testString.length(); i++) {
               if (!letterOrNumber(testString.charAt(i))) {
                  errors[errorIndex] = 2;
               }
            }
         }
      } else {
         errors[errorIndex] = 3;
      }
      if (errors[errorIndex] == 0) {
         return true;
      } else {
         return false;
      }
   }

   public boolean letterOrNumber(char letter) {
      if (((letter >= 97) && (letter <= 122)) || ((letter >= 65) && (letter <= 90)) || ((letter >= 48) && (letter <= 57))) {
         return true;
      } else {
         return false;
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
      //Hopefully, every message should have something
      //For the menu, numbers represent error/success, A represents add all (if you join),
      //N represents add one new player, and B represents begin the game
      //Remove the initializer
      input = input.trim();//in case something is wrong
      if (!gameBegin) {
         char initializer = input.charAt(0);
         input = input.substring(1);
         if (isParsable(initializer)) {
            if (state == 0) {
               errors[0] = Integer.parseInt(initializer + "");
               if (initializer == '0') {

                  //Start the opening here
/*
                  cardLayout.show(mainContainer, PANEL_NAMES[1]);
                  ((IntroPanel) (allPanels[1])).go();
                  try {
                     Thread.sleep(3000);
                  } catch (Exception E) {
                  }
*/
                  cardLayout.show(mainContainer, PANEL_NAMES[2]);

                  newState = 2;
               } else {
                  username = null;
               }
            } else if ((state == 3) || (state == 4)) {
               if (initializer == '0') {
                  newState = 6;//Sends to a waiting room
                  gameName = attemptedGameName;
                  gamePassword = attemptedGamePassword;
                  myUser = new User(username);//Sets the player
                  if (state == 3) {
                     host = true;
                     onlineList.add(myUser);
                  }
               }
               errors[1] = Integer.parseInt(initializer + "");
               /*
               else if (initializer == '1') {
                  if (state == 2) {
                     System.out.println("Game name in use");
                  } else {
                     System.out.println("Wrong username/password"); //Make this one print out state==5
                  }
               } else if (initializer == '2') {
                  System.out.println("Game is full");//Make this one print out state==6
               }
               */
            } else if (state == 6) {
               if (initializer == '0') {
                  System.out.println("Starting Game");
                  loading = true;
               } else {
                  System.out.println("Unable to Start Game");
               }
            }
         } else if (initializer == 'A') {
            String[] allPlayers = input.split(" ", -1);
            for (String aPlayer : allPlayers) {
               if ((testingBegin) && (myUser.getUsername().equals(aPlayer))) {
                  onlineList.add(myUser);
               } else {
                  onlineList.add(new User(aPlayer));
               }
            }
         } else if (initializer == 'N') {
            onlineList.add(new User(input));
         } else if (initializer == 'X') {
            for (int i = 0; i < onlineList.size(); i++) {
               if (onlineList.get(i).getUsername().equals(input)) {
                  onlineList.remove(i);
               }
            }
         } else if (initializer == 'B') {
            players = new Player[onlineList.size()];
            for (int i = 0; i < onlineList.size(); i++) {
               players[i] = new SafeMarksman(onlineList.get(i).getUsername());
               if (onlineList.get(i).getUsername().equals(myUser.getUsername())) {
                  myPlayer = players[i];
               }
            }
            newState = 7;//Sends to the game screen
            gameBegin = true;
         } else if (initializer == 'P') { //Then leave the game
            onlineList.clear();
            newState = 2;
         }
      } else {
         projectiles.clear();
         aoes.clear();
         String[] firstSplit = input.split(" ", -1);
         for (String firstInput : firstSplit) {
            char initializer = firstInput.charAt(0);
            String[] secondSplit = firstInput.split(initializer + "", -1);
            for (String secondInput : secondSplit) {
               if (!secondInput.equals("")) {
                  String[] thirdSplit = secondInput.split(",", -1);
                  if (initializer == 'P') {
                     //REPLACE THIS WITH A SET PLAYER METHOD.
                     int playerID = Integer.parseInt(thirdSplit[0]);
                     players[playerID].setXy(Integer.parseInt(thirdSplit[1]), Integer.parseInt(thirdSplit[2]));
                     players[playerID].setHealth(Integer.parseInt(thirdSplit[3]));
                     players[playerID].setMaxHealth(Integer.parseInt(thirdSplit[4]));
                     players[playerID].setAttack(Integer.parseInt(thirdSplit[5]));
                     players[playerID].setMobility(Integer.parseInt(thirdSplit[6]));
                     players[playerID].setRange(Integer.parseInt(thirdSplit[7]));
                     players[playerID].setArtifact(Boolean.parseBoolean(thirdSplit[8]));
                     players[playerID].setGold(Integer.parseInt(thirdSplit[9]));
                     players[playerID].setSpriteID(Integer.parseInt(thirdSplit[10]));
                     for (int j = 11; j < 14; j++) {
                        players[playerID].setSpellPercent(Integer.parseInt(thirdSplit[j]), j - 11);
                     }
                     players[playerID].setDamaged(Boolean.parseBoolean(thirdSplit[14]));
                     for (int j = 15; j < 15 + Integer.parseInt(thirdSplit[15]); j++) {
                        players[playerID].addStatus(Integer.parseInt(thirdSplit[j]));
                     }
                  } else if (initializer == 'O') {
                     //REPLACE THIS WITH A SET OTHERS METHOD.
                     int playerID = Integer.parseInt(thirdSplit[0]);
                     players[playerID].setXy(Integer.parseInt(thirdSplit[1]), Integer.parseInt(thirdSplit[2]));
                     players[playerID].setHealth(Integer.parseInt(thirdSplit[3]));
                     players[playerID].setMaxHealth(Integer.parseInt(thirdSplit[4]));
                     players[playerID].setArtifact(Boolean.parseBoolean(thirdSplit[5]));
                     players[playerID].setSpriteID(Integer.parseInt(thirdSplit[6]));
                     players[playerID].setDamaged(Boolean.parseBoolean(thirdSplit[7]));
                     for (int j = 8; j < 8 + Integer.parseInt(thirdSplit[8]); j++) {
                        players[playerID].addStatus(Integer.parseInt(thirdSplit[j]));
                     }
                  } else if (initializer == 'D') {
                     players[Integer.parseInt(thirdSplit[0])] = null;
                  } else if (initializer == 'R') {
                     projectiles.add(new Projectile(Integer.parseInt(thirdSplit[0]), (int) (Integer.parseInt(thirdSplit[1]) * scaling + centerXy[0] - myPlayer.getXy()[0] * scaling), (int) (Integer.parseInt(thirdSplit[2]) * scaling + centerXy[1] - myPlayer.getXy()[1] * scaling)));
                  } else if (initializer == 'E') {
                     aoes.add(new AOE(Integer.parseInt(thirdSplit[0]), (int) (Integer.parseInt(thirdSplit[1]) * scaling + centerXy[0] - myPlayer.getXy()[0] * scaling), (int) (Integer.parseInt(thirdSplit[2]) * scaling + centerXy[1] - myPlayer.getXy()[1] * scaling), (int) (Integer.parseInt(thirdSplit[3]) * scaling)));
                  }
               }
            }
         }
      }
   }

   public void repaintPanels() {
      if (state != newState) {
         state = newState;
         cardLayout.show(mainContainer, PANEL_NAMES[state]);
      }
      if (state != 7) {
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
         unableToConnect = false;
      } catch (Exception e) {
         System.out.println("Unable to connect");
         unableToConnect = true;
         connected = false;
      }
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

   public void windowClosing(WindowEvent e) {
      dispose();
      try {
         output.println("X");
         output.flush();
         System.out.println("X");
      } catch (Exception E) {
         System.out.println("Not connected");
      }
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
      private CustomTextField nameField = new CustomTextField(3, scaling);
      private CustomButton testButton = new CustomButton("Test");

      public LoginPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic username field
         //sendName = true;
         nameField.addActionListener((ActionEvent e) -> {
            if (!sendName) {
               if (!(nameField.getText().contains(" "))) {
                  username = nameField.getText();
                  sendName = true;
               } else {
                  System.out.println("Error: Spaces exist");
               }
            }
         });
         nameField.setFont(MAIN_FONT);
         nameField.setBounds(MAX_X / 2 - (int) (45 * scaling), MAX_Y / 5, (int) (90 * scaling), (int) (19 * scaling));
         this.add(nameField);


         testButton.addActionListener((ActionEvent e) -> {
            testingBegin = true;
         });

         testButton.setBounds(MAX_X / 2 - (int) (45 * scaling), MAX_Y * 2 / 5, (int) (90 * scaling), (int) (19 * scaling));
         this.add(testButton);
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);

         //Begin drawing
         g2.setColor(Color.WHITE);
         g2.setFont(HEADER_FONT);
         g2.drawString("Login", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Login")) / 2.0), (int) (MAX_Y / 5.0 - 5 * scaling));
         g2.setFont(MAIN_FONT);
         if ((!connected) && (!unableToConnect)) {
            g2.drawString("Connecting...", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Connecting...")) / 2.0), (int) (MAX_Y * 5 / 16.0));
         } else if ((connected) && (!unableToConnect)) {
            g2.drawString("Connected", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Connected")) / 2.0), (int) (MAX_Y * 5 / 16.0));
         } else if (unableToConnect) {
            g2.drawString("Unable to Connect", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Unable to Connect")) / 2.0), (int) (MAX_Y * 5 / 16.0));
         }
      }
   }

   private class MenuPanel extends JPanel {//State=2
      private Graphics2D g2;
      private CustomButton createButton = new CustomButton("Create Game");
      private CustomButton joinButton = new CustomButton("Join Game");
      private CustomButton instructionButton = new CustomButton("Instructions");
      private CustomButton logoutButton = new CustomButton("Logout");
      private double introAlpha = 1;

      public MenuPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic create and join server buttons
         createButton.addActionListener((ActionEvent e) -> {
            newState = 3;
         });
         createButton.setBounds(MAX_X / 2 - (int) (65 * scaling), (int) (MAX_Y * 8.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));
         this.add(createButton);

         joinButton.addActionListener((ActionEvent e) -> {
            newState = 4;
         });
         joinButton.setBounds(MAX_X / 2 - (int) (65 * scaling), (int) (MAX_Y * 10.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));
         this.add(joinButton);
         instructionButton.addActionListener((ActionEvent e) -> {
            newState = 5;//I added this later so I didn't want to move everything around
         });
         instructionButton.setBounds(MAX_X / 2 - (int) (65 * scaling), (int) (MAX_Y * 12.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));

         this.add(instructionButton);
         logoutButton.addActionListener((ActionEvent e) -> {
            newState = 0;
            logout = true;
         });
         logoutButton.setBounds(MAX_X / 2 - (int) (65 * scaling), (int) (MAX_Y * 14.0 / 20.0), (int) (130 * scaling), (int) (19 * scaling));
         this.add(logoutButton);

         //Setting up intro scaling
         if ((1.0 * MAX_Y / MAX_X) > (1.0 * 1198 / 1800)) { //Make sure that these are doubles
            //Y is excess
            introScaling = 1.0 * MAX_Y / 1198;
         } else {
            //X is excess
            introScaling = 1.0 * MAX_X / 1800;
         }
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
         LOADED_TITLE = graphicsConfiguration.createCompatibleImage((int) (MAX_Y / 4.0 * 1316 / 625), (int) (MAX_Y / 4.0), Transparency.TRANSLUCENT);
         Graphics2D graphicsT = LOADED_TITLE.createGraphics();
         graphicsT.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         graphicsT.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         graphicsT.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         graphicsT.drawImage(TITLE, 0, 0, (int) (MAX_Y / 4.0 * 1316 / 625), (int) (MAX_Y / 4.0), null);
         graphicsT.dispose();

         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);
         //Adds particles
         //Background
         g2.drawImage(LOADED_TITLE_SCREEN, MAX_X - (int) (1800 * introScaling), MAX_Y - (int) (1198 * introScaling), null);
         //Title
         g2.drawImage(LOADED_TITLE, (int) ((MAX_X - (MAX_Y / 4.0 * 1316 / 625)) / 2.0), (int) (MAX_Y / 10.0), null);
         if (introAlpha != 0) {
            introAlpha -= 0.05;
            g2.setColor(new Color(0f, 0f, 0f, (float) (introAlpha)));
            g2.fillRect(0, 0, MAX_X, MAX_Y);
            if (introAlpha < 0.05) {
               introAlpha = 0;
            }
         }
         if (Math.random() < 0.2) {
            particles.add(new AshParticle(Math.random() * MAX_X + MAX_X / 20, 0, (int) ((Math.random() * 3 + 3) * scaling), MAX_Y));
         }
         drawAllParticles(g2);
      }
   }

   private class CreatePanel extends JPanel { //State =3
      private Graphics2D g2;
      private CustomTextField gameNameField = new CustomTextField(3, scaling);
      private CustomTextField gamePasswordField = new CustomTextField(3, scaling);
      private CustomButton backButton = new CustomButton("Back");
      private CustomButton confirmButton = new CustomButton("Confirm Game");

      public CreatePanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic create and join server buttons
         gameNameField.addActionListener((ActionEvent e) -> {
            if (!testGame) {
               attemptedGameName = gameNameField.getText();
               attemptedGamePassword = gamePasswordField.getText();
               testGame = true;
            }
         });
         gameNameField.setFont(MAIN_FONT);
         gameNameField.setBounds(MAX_X / 2 - (int) (45 * scaling), MAX_Y * 3 / 10, (int) (90 * scaling), (int) (19 * scaling));
         this.add(gameNameField);
         gamePasswordField.addActionListener((ActionEvent e) -> {
            if (!testGame) {
               attemptedGameName = gameNameField.getText();
               attemptedGamePassword = gamePasswordField.getText();
               testGame = true;
            }
         });
         gamePasswordField.setFont(MAIN_FONT);
         gamePasswordField.setBounds(MAX_X / 2 - (int) (45 * scaling), MAX_Y * 2 / 5, (int) (90 * scaling), (int) (19 * scaling));
         this.add(gamePasswordField);
         confirmButton.addActionListener((ActionEvent e) -> {
            if (!testGame) {
               attemptedGameName = gameNameField.getText();
               attemptedGamePassword = gamePasswordField.getText();
               testGame = true;
            }
         });
         confirmButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y / 2, (int) (130 * scaling), (int) (19 * scaling));
         this.add(confirmButton);
         backButton.addActionListener((ActionEvent e) -> {
            newState = 2;
         });
         backButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
         this.add(backButton);
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         super.paintComponent(g);
         //Background
         g2.drawImage(LOADED_TITLE_SCREEN, MAX_X - (int) (1800 * introScaling), MAX_Y - (int) (1198 * introScaling), null);
         g2.setColor(Color.WHITE);
         g2.setFont(HEADER_FONT);
         g2.drawString("Create Server", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Create Server")) / 2.0), (MAX_Y / 5));
         //Server name
         g2.setFont(MAIN_FONT);
         g2.drawString("Server Name", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Server Name")) / 2.0), (MAX_Y * 3 / 10 - g2.getFontMetrics().getHeight()));
         //Server password
         g2.drawString("Server Password", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Server Password")) / 2.0), (MAX_Y * 2 / 5 - g2.getFontMetrics().getHeight()));
         //Draws particles
         drawAllParticles(g2);
      }
   }

   private class JoinPanel extends JPanel { //State =4
      private Graphics2D g2;
      private CustomTextField gameNameField = new CustomTextField(3, scaling);
      private CustomTextField gamePasswordField = new CustomTextField(3, scaling);
      private CustomButton backButton = new CustomButton("Back");
      private CustomButton confirmButton = new CustomButton("Confirm Game");

      public JoinPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic create and join server buttons
         gameNameField.addActionListener((ActionEvent e) -> {
            if (!testGame) {
               attemptedGameName = gameNameField.getText();
               attemptedGamePassword = gamePasswordField.getText();
               testGame = true;
            }
         });
         gameNameField.setFont(MAIN_FONT);
         gameNameField.setBounds(MAX_X / 2 - (int) (45 * scaling), MAX_Y * 3 / 10, (int) (90 * scaling), (int) (19 * scaling));
         this.add(gameNameField);
         gamePasswordField.addActionListener((ActionEvent e) -> {
            if (!testGame) {
               attemptedGameName = gameNameField.getText();
               attemptedGamePassword = gamePasswordField.getText();
               testGame = true;
            }
         });
         gamePasswordField.setFont(MAIN_FONT);
         gamePasswordField.setBounds(MAX_X / 2 - (int) (45 * scaling), MAX_Y * 2 / 5, (int) (90 * scaling), (int) (19 * scaling));
         this.add(gamePasswordField);
         confirmButton.addActionListener((ActionEvent e) -> {
            if (!testGame) {
               attemptedGameName = gameNameField.getText();
               attemptedGamePassword = gamePasswordField.getText();
               testGame = true;
            }
         });
         confirmButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y / 2, (int) (130 * scaling), (int) (19 * scaling));
         this.add(confirmButton);
         backButton.addActionListener((ActionEvent e) -> {
            newState = 2;
         });
         backButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
         this.add(backButton);
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(20, 20, 20));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);
         //Background
         g2.drawImage(LOADED_TITLE_SCREEN, MAX_X - (int) (1800 * introScaling), MAX_Y - (int) (1198 * introScaling), null);
         g2.setColor(Color.WHITE);
         g2.setFont(HEADER_FONT);
         g2.drawString("Join Server", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Join Server")) / 2.0), (MAX_Y / 5));
         //Server name
         g2.setFont(MAIN_FONT);
         g2.drawString("Server Name", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Server Name")) / 2.0), (MAX_Y * 3 / 10 - g2.getFontMetrics().getHeight()));
         //Server password
         g2.drawString("Server Password", (int) ((MAX_X - g2.getFontMetrics().stringWidth("Server Password")) / 2.0), (MAX_Y * 2 / 5 - g2.getFontMetrics().getHeight()));
         //Draws particles
         drawAllParticles(g2);
      }
   }

   private class InstructionPanel extends JPanel { //State=5
      private Graphics2D g2;
      private CustomButton backButton = new CustomButton("Back");


      public InstructionPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Setting up buttons
         backButton.addActionListener((ActionEvent e) -> {
            newState = 2;
            leaveGame = true;
         });
         backButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
         this.add(backButton);

         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(150, 150, 150));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         super.paintComponent(g);
         //Background
         g2.drawImage(LOADED_TITLE_SCREEN, MAX_X - (int) (1800 * introScaling), MAX_Y - (int) (1198 * introScaling), null);
         drawAllParticles(g2);
      }
   }

   private class WaitingPanel extends JPanel { //State=6
      private Graphics2D g2;
      private boolean buttonAdd = true;
      private boolean buttonRemove = true;
      private CustomButton readyGameButton = new CustomButton("Begin game");
      private CustomButton backButton = new CustomButton("Back");
      private CustomButton teamOneButton = new CustomButton("Team one");
      private CustomButton teamTwoButton = new CustomButton("Team two");


      public WaitingPanel() {
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Setting up buttons
         readyGameButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 4 / 10, (int) (130 * scaling), (int) (19 * scaling));
         readyGameButton.addActionListener((ActionEvent e) -> {
            notifyReady = true;
         });

         teamOneButton.addActionListener((ActionEvent e) -> {
            myTeam = 1;
            teamChosen = true;
         });
         teamOneButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 3 / 10, (int) (130 * scaling), (int) (19 * scaling));
         this.add(teamOneButton);

         teamTwoButton.addActionListener((ActionEvent e) -> {
            myTeam = 2;
            teamChosen = true;
         });
         teamTwoButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y / 2, (int) (130 * scaling), (int) (19 * scaling));
         this.add(teamTwoButton);

         backButton.addActionListener((ActionEvent e) -> {
            newState = 2;
            leaveGame = true;
         });
         backButton.setBounds(MAX_X / 2 - (int) (65 * scaling), MAX_Y * 7 / 10, (int) (130 * scaling), (int) (19 * scaling));
         this.add(backButton);


         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(70, 70, 70));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.setFocusable(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setFont(MAIN_FONT);
         FontMetrics metrics = g2.getFontMetrics();
         super.paintComponent(g);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
         //if host==true, then display the ready button
         //Background
         g2.drawImage(LOADED_TITLE_SCREEN, MAX_X - (int) (1800 * introScaling), MAX_Y - (int) (1198 * introScaling), null);
         g2.setColor(Color.white);
         if ((host) && (buttonAdd)) {
            this.add(readyGameButton);
            buttonAdd = false;
         }
         StringBuilder players = new StringBuilder("Players: ");
         for (int i = 0; i < onlineList.size(); i++) {
            players.append(onlineList.get(i).getUsername() + ", ");
         }
         g2.drawString(players.toString(), (int) (2 * scaling), (int) (10 * scaling));
         if (loading) {
            if (buttonRemove) {
               this.remove(readyGameButton);
               buttonRemove = false;
            }

            g2.drawString("LOADING", (int) ((MAX_X - metrics.stringWidth("LOADING")) / 2.0), MAX_Y / 2);
         }
         drawAllParticles(g2);
      }
   }

   private class IntermediatePanel extends JPanel { //State=7 (intermediate)=
      private GamePanel gamePanel;
      private boolean begin = true;

      public IntermediatePanel() {
         //Scaling is a factor which reduces the MAX_X/MAX_Y so that it eventually fits
         //Setting up the size
         this.setPreferredSize(new Dimension(MAX_X, MAX_Y));
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(0, 0, 0));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
      }

      //set a method called initialize
      public void repaintReal() {
         gamePanel.repaint();
      }

      public void initializeScaling() {
         if ((1.0 * MAX_Y / MAX_X) > (1.0 * DESIRED_Y / DESIRED_X)) { //Make sure that these are doubles
            //Y is excess
            scaling = 1.0 * MAX_X / DESIRED_X;
         } else {
            //X is excess
            scaling = 1.0 * MAX_Y / DESIRED_Y;
         }
         MAIN_FONT = new Font("Cambria Math", Font.PLAIN, (int) (12 * scaling));
         HEADER_FONT = new Font("Akura Popo", Font.PLAIN, (int) (25 * scaling));
      }

      public void initializeSize() {
         int[] tempXy = {(int) (DESIRED_X * scaling / 2), (int) (DESIRED_Y * scaling / 2)};
         myMouseAdapter.setCenterXy(tempXy);
         myMouseAdapter.setScaling(scaling);
         gamePanel = new GamePanel();
         gamePanel.setBounds((int) ((this.getWidth() - (DESIRED_X * scaling)) / 2), (int) ((this.getHeight() - (DESIRED_Y * scaling)) / 2), (int) (DESIRED_X * scaling), (int) (DESIRED_Y * scaling));
         this.add(gamePanel);
      }
   }

   private class GamePanel extends JPanel {//State=7
      private Graphics2D g2;
      private boolean generateGraphics = true;
      int[] midXy = new int[2];
      private Shape rect;
      private Shape largeCircle;
      private Area areaRect;
      private Area largeRing;
      private Polygon BOTTOM_BAR = new Polygon();
      private Rectangle drawArea;
      private BufferedImage fogMap;
      private int fogTicks = 0;


      public GamePanel() {
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(40, 40, 40));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
         this.addMouseListener(myMouseAdapter);
         this.addMouseWheelListener(myMouseAdapter);
         this.addMouseMotionListener(myMouseAdapter);
      }

      @Override
      public void paintComponent(Graphics g) {
         g2 = (Graphics2D) g;
         if ((state == 7) && (generateGraphics)) {
            midXy[0] = (int) (DESIRED_X * scaling / 2);
            midXy[1] = (int) (DESIRED_Y * scaling / 2);
            for (Player currentPlayer : players) {
               currentPlayer.setScaling(scaling);
               currentPlayer.setCenterXy(midXy);
            }
            g2.setFont(MAIN_FONT);
            generateGraphics = false;
            largeCircle = new Ellipse2D.Double(400 * scaling, 175 * scaling, 150 * scaling, 150 * scaling);

            rect = new Rectangle2D.Double(0, 0, 950 * scaling, 500 * scaling);
            areaRect = new Area(rect);
            largeRing = new Area(largeCircle);
            areaRect.subtract(largeRing);
            BOTTOM_BAR.addPoint((int) (272 * scaling), (int) (500 * scaling));
            BOTTOM_BAR.addPoint((int) (265 * scaling), (int) (440 * scaling));
            BOTTOM_BAR.addPoint((int) (270 * scaling), (int) (435 * scaling));
            BOTTOM_BAR.addPoint((int) (680 * scaling), (int) (435 * scaling));
            BOTTOM_BAR.addPoint((int) (685 * scaling), (int) (440 * scaling));
            BOTTOM_BAR.addPoint((int) (678 * scaling), (int) (500 * scaling));
            //Game set up
            centerXy[0] = (int) (DESIRED_X * scaling / 2);
            centerXy[1] = (int) (DESIRED_Y * scaling / 2);
            try {
               sheet = ImageIO.read(new File(".\\res\\Map.png"));
               sectors = new Sector[10][10];
               for (int i = 0; i < 10; i++) {
                  for (int j = 0; j < 10; j++) {
                     sectors[j][i] = new Sector();
                     sectors[j][i].setImage(sheet.getSubimage(j * 1000, i * 1000, 1000, 1000));
                     sectors[j][i].setSectorCoords(j, i);
                     sectors[j][i].setSize((int) (1000 * scaling));
                  }
               }
            } catch (IOException e) {
               System.out.println("Image not found");
            }
            drawArea = new Rectangle(0, 0, (int) (DESIRED_X * scaling), (int) (DESIRED_Y * scaling));
         }
         super.paintComponent(g2);
         if (drawArea != null) {
            g2.clip(drawArea);
            g2.setFont(MAIN_FONT);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
            //Sectors
            int startX = (int) ((myPlayer.getXy()[0] - 475.0) / 1000.0);
            int finalX = (int) (Math.ceil((myPlayer.getXy()[0] + 475.0) / 1000.0)) + 1;
            int startY = (int) ((myPlayer.getXy()[1] - 250.0) / 1000.0);
            int finalY = (int) (Math.ceil((myPlayer.getXy()[1] + 250.0) / 1000.0)) + 1;

            for (int i = startY; i < finalY; i++) {
               for (int j = startX; j < finalX; j++) {
                  if ((i >= 0) && (j >= 0) && (i < 10) && (j < 10)) {
                     sectors[j][i].drawSector(g2, xyAdjust);
                  }
               }
            }
            //Game player
            for (Player currentPlayer : players) {
               if (currentPlayer != null) {
                  currentPlayer.draw(g2, myPlayer.getXy());
               }
            }
            // System.out.println(System.nanoTime() - time);

            // Updating fog
            for (int i = 0; i < players.length; i++) {
               // TODO: Separate by teams
               // TODO: Account for players that quit?
               fog.scout(players[i].getXy());
            }
            //Creating shapes
            AffineTransform tx = new AffineTransform();
            tx.translate(centerXy[0] - myPlayer.getXy()[0] * scaling, centerXy[1] - myPlayer.getXy()[1] * scaling);
            Area darkFog = fog.getFog().createTransformedArea(tx);
            Area lightFog = fog.getExplored().createTransformedArea(tx);

            //Draws fog
            g2.setColor(Color.black); //Unexplored
            g2.fill(darkFog);
            g2.setColor(new Color(0, 0, 0, 128)); //Previously explored
            g2.fill(lightFog);

            for (int i = 0; i < projectiles.size(); i++) { //For some reason, a concurrent modification exception is thrown if i use the other for loop
               projectiles.get(i).draw(g2);
            }
            for (int i = 0; i < aoes.size(); i++) { //For some reason, a concurrent modification exception is thrown if i use the other for loop
               aoes.get(i).draw(g2);
            }
            g2.setColor(new Color(165, 156, 148));
            //Minimap
            g2.drawRect((int) (830 * scaling), (int) (379 * scaling), (int) (120 * scaling), (int) (120 * scaling));
            //Bottom bar
            g2.drawPolygon(BOTTOM_BAR);


            //Stat bars
            g2.setColor(new Color(190, 40, 40));
            g2.fillRect(0, (int) (486 * scaling), (int) (121 * scaling * myPlayer.getHealth() / myPlayer.getMaxHealth()), (int) (5 * scaling));

            g2.setColor(new Color(165, 156, 148));
            g2.drawRect(0, (int) (486 * scaling), (int) (121 * scaling), (int) (5 * scaling));
            g2.drawRect(0, (int) (495 * scaling), (int) (121 * scaling), (int) (5 * scaling));
            //Bottom bar contents

            //Spells
            g2.fillRect((int) (565 * scaling), (int) (442 * scaling), (int) (30 * scaling), (int) (50 * scaling));
            g2.fillRect((int) (604 * scaling), (int) (442 * scaling), (int) (30 * scaling), (int) (50 * scaling));
            g2.fillRect((int) (643 * scaling), (int) (442 * scaling), (int) (30 * scaling), (int) (50 * scaling));
         }
         g2.dispose();
      }

   }

   private class CustomButton extends JButton {
      private Color foregroundColor = new Color(1f, 1f, 1f, 1f);
      private Color backgroundColor = new Color(0f, 0f, 0f, 0f);
      private Color rolloverColor = new Color(1f, 1f, 1f, 0.1f);
      private Color pressedColor = new Color(1f, 1f, 1f, 0.2f);
      private Font BUTTON_FONT = new Font("Cambria Math", Font.PLAIN, (int) (12 * scaling));

      CustomButton(String description) {
         super(description);
         super.setContentAreaFilled(false);
         this.setFont(BUTTON_FONT);
         this.setBorder(BorderFactory.createLineBorder(Color.white, (int) (1.5 * scaling)));
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
            g.setColor(pressedColor);
         } else if (getModel().isRollover()) {
            g.setColor(rolloverColor);
         } else {
            g.setColor(getBackground());
         }
         g.fillRect(0, 0, getWidth(), getHeight());
         super.paintComponent(g);
      }
   }


}

/*
            createButton.setForeground(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)));
            createButton.setBorder(BorderFactory.createLineBorder(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)), (int) (1.5 * scaling)));
            joinButton.setForeground(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)));
            joinButton.setBorder(BorderFactory.createLineBorder(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)), (int) (1.5 * scaling)));
            instructionButton.setForeground(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)));
            instructionButton.setBorder(BorderFactory.createLineBorder(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)), (int) (1.5 * scaling)));
            backButton.setForeground(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)));
            backButton.setBorder(BorderFactory.createLineBorder(new Color((float) (1 - introAlpha), (float) (1 - introAlpha), (float) (1 - introAlpha)), (int) (1.5 * scaling)));

 */



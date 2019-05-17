import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.Color;
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
   private String[] panelNames = {"LOGIN_PANEL", "MAIN_PANEL", "CREATE_PANEL", "JOIN_PANEL", "WAITING_PANEL", "GAME_PANEL"};
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

   public Client() {
      super("Dark");

      //Control set up
      this.addMouseListener(myMouseAdapter);
      this.addMouseWheelListener(myMouseAdapter);
      this.addMouseMotionListener(myMouseAdapter);
      this.addKeyListener(myKeyListener);

      //Font set up
      try {
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(".\\graphicFonts\\Kiona-Bold.ttf")));
      } catch (IOException | FontFormatException e) {
         System.out.print("Font not available");
      }
      MAIN_FONT = new Font("Kiona Bold", Font.PLAIN, 16);

      //Creating components
      allPanels[0] = new LoginPanel();
      allPanels[1] = new MenuPanel();
      allPanels[2] = new CreatePanel();
      allPanels[3] = new JoinPanel();
      allPanels[4] = new WaitingPanel();
      allPanels[5] = new GamePanel();
      //Adding to mainContainer cards
      mainContainer.setBackground(new Color(37, 37, 37));
      for (int i = 0; i < allPanels.length; i++) {
         mainContainer.add(allPanels[i], panelNames[i]);
      }
      this.add(mainContainer);
      cardLayout.show(mainContainer, panelNames[0]);

      //Basic set up
      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocationRelativeTo(null);
      this.pack();
      this.setVisible(true);
      this.setFocusable(true); //Necessary so that the buttons and stuff do not take over the focus
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
                  double angle = myKeyListener.getAngle();
                  outputString = "" + angle;//If it is -1, then the server will recognize to stop
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
         /*
         When a game message is sent back, it should have the following information
         The () brackets should be included, the {} are not
         G represents general, shows all the player info
         A represents animals
         P represents plants
         I represents items dropped
         All ? represent a boolean value, which will be either t or f
         GID,x,y,health,artifact?,status,gold,level,spell1ready?,spell2ready?,spell3ready? {space}  ID,x,y,health,artifact,status,gold,level,spell1ready?,spell2ready?,
         spell3ready? ... {space} AID,x,y,health {space} ID,x,y,health ... {space}
         PID,x,y,harvestTimeRemaining {space} ID,x,y,harvestTimeRemaining ... {space}
         IID,x,y {space} ID,x,y ... {space}. Actually, just keep the last whitespace
         (I for items? you may want to display what was dropped by a monster)
         The map is only visual, so you can just send what changes in terms of the animals and the plants. So you do not need the speed or attack, only what the player can see
         Every animal has an ID, every plant has an ID, and every item has an ID. This makes it so that less info can be sent. You could also then place all the animals and plants inside two seperate arrays, calling them animal[ID].draw(x,y) to make it more convinient
         Implement a game ID which represents a number based on the array. This would be easier to use
         */
         //For now, just get the ID,x,y (the P was removed at the beginning)
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
      allPanels[state].repaint();
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
      private boolean generateGraphics = true;
      private JTextField nameField = new JTextField(3);
      private CustomButton nameButton = new CustomButton("Click to test name");
      private int MAX_X;
      private int MAX_Y;

      public LoginPanel() {
         //Setting up the size
         this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
         MAX_X = this.getWidth();
         MAX_Y = this.getHeight();
         //Basic username field
         nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (!(nameField.getText().contains(" "))) {
                  username = nameField.getText();
                  sendName = true;
               } else {
                  System.out.println("Error: Spaces exist");
               }
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
         if (generateGraphics) {
            g2 = (Graphics2D) g.create();
            generateGraphics = false;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(MAIN_FONT);
         }
         super.paintComponent(g);
         //this.requestFocusInWindow();// Removed, this interferes with the textboxes. See if this is truly necessary

         //Begin drawing

      }
   }

   private class MenuPanel extends JPanel {//State=1
      private Graphics2D g2;
      private boolean generateGraphics = true;
      private CustomButton createButton = new CustomButton("Create game");
      private CustomButton joinButton = new CustomButton("Join game");
      private int MAX_X;
      private int MAX_Y;

      public MenuPanel() {
         //Setting up the size
         this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
         MAX_X = this.getWidth();
         MAX_Y = this.getHeight();
         //Basic create and join server buttons
         createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               newState = 2;
            }
         });
         createButton.setBounds(MAX_X / 2 - 130, MAX_Y * 3 / 10, 260, 30);
         this.add(createButton);

         joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               newState = 3;
            }
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
         if (generateGraphics) {
            g2 = (Graphics2D) g.create();
            generateGraphics = false;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(MAIN_FONT);
         }
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
      private int MAX_X;
      private int MAX_Y;

      public CreatePanel() {
         //Setting up the size
         this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
         MAX_X = this.getWidth();
         MAX_Y = this.getHeight();
         //Basic create and join server buttons
         testGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (!(gameNameField.getText().contains(" ")) && (!(gamePasswordField.getText().contains(" ")))) {
                  attemptedGameName = gameNameField.getText();
                  attemptedGamePassword = gamePasswordField.getText();
                  testGame = true;
               } else {
                  System.out.println("Error: Spaces exist");
               }
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
         if (generateGraphics) {
            g2 = (Graphics2D) g.create();
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
      private boolean generateGraphics = true;
      private JTextField gameNameTestField = new JTextField(3);
      private JTextField gamePasswordTestField = new JTextField(3);
      private CustomButton testGameButton = new CustomButton("Join game");
      private int MAX_X;
      private int MAX_Y;

      public JoinPanel() {
         //Setting up the size
         this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
         MAX_X = this.getWidth();
         MAX_Y = this.getHeight();
         //Basic create and join server buttons
         testGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               attemptedGameName = gameNameTestField.getText();
               attemptedGamePassword = gamePasswordTestField.getText();
               testGame = true;
            }
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
         if (generateGraphics) {
            g2 = (Graphics2D) g.create();
            generateGraphics = false;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(MAIN_FONT);
         }
         super.paintComponent(g);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
      }
   }

   private class WaitingPanel extends JPanel { //State=4
      private Graphics2D g2;
      private boolean generateGraphics = true;
      private int MAX_X;
      private int MAX_Y;
      private boolean buttonAdd = true;
      private CustomButton readyGameButton = new CustomButton("Begin game");

      public WaitingPanel() {
         //Setting up the size
         this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
         MAX_X = this.getWidth();
         MAX_Y = this.getHeight();

         //Setting up buttons
         readyGameButton.setBounds(MAX_X / 2 - 130, MAX_Y * 4 / 10, 260, 30);
         readyGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               notifyReady = true;
            }
         });

         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(70, 70, 70));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         if (generateGraphics) {
            g2 = (Graphics2D) g.create();
            generateGraphics = false;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(MAIN_FONT);
         }
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
      }
   }

   private class GamePanel extends JPanel { //State=5
      private Graphics2D g2;
      private boolean generateGraphics = true;
      private int MAX_X;
      private int MAX_Y;

      public GamePanel() {
         //Setting up the size
         this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
         MAX_X = this.getWidth();
         MAX_Y = this.getHeight();
         //Basic visuals
         this.setDoubleBuffered(true);
         this.setBackground(new Color(30, 30, 30));
         this.setLayout(null); //Necessary so that the buttons can be placed in the correct location
         this.setVisible(true);
      }

      @Override
      public void paintComponent(Graphics g) {
         if (generateGraphics) {
            g2 = (Graphics2D) g.create();
            generateGraphics = false;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(MAIN_FONT);
         }
         super.paintComponent(g);
         //this.requestFocusInWindow(); Removed, this interferes with the textboxes. See if this is truly necessary
         for (int i = 0; i < gamePlayers.length; i++) {
            gamePlayers[i].draw(g2);
         }
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

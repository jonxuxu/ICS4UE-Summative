import java.awt.Menu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
General Message Conventions:
Starting with * means that it is an initializer message
Sending a number in the beginning indicates whether the msg was a success or failure. Anything other than 0 is a type of error
Send "" if you want to just communicate back to a blank msg
Use .trim() at the end before sending to remove final white spaces if there are any
" " is the major separator, everything else should be dealt using () and ,

Make sure that no special characters are allowed for the password, servername, or username
*/

/*
Things to Fix:
Make sure that it is impossible to leave right before the game begins such that the game only has one player or something along those lines
Make a window listener that sends X when the program closes
Pressing leave game will instead send Q, which will do most of the same things
 */

/**
 * Server.java
 * This is
 *
 * @author Will Jeong
 * @version 1.0
 * @since 2019-04-24
 */

public class Server {
   //All for the main server
   private ServerSocket serverSock;
   private ArrayList<Socket> allSockets = new ArrayList<Socket>();
   private ArrayList<MenuHandler> menuConnections = new ArrayList<MenuHandler>();

   //Used by all
   private ArrayList<Player> onlinePlayers = new ArrayList<Player>();
   private ArrayList<GameServer> games = new ArrayList<GameServer>();
   private ArrayList<GameServer> startedGames = new ArrayList<GameServer>();

   public static void main(String[] args) {
      new Server().go();
   }

   public void go() {
      try {
         serverSock = new ServerSocket(5001);  //assigns an port to the server
         while (true) {  //this loops to accept multiple clients
            Socket newConnection = serverSock.accept();
            allSockets.add(newConnection);
            menuConnections.add(new MenuHandler(newConnection));
            Thread t = new Thread(menuConnections.get(menuConnections.size() - 1));
            t.start();
            System.out.println("Client connected");
         }
      } catch (Exception e) {
         System.exit(-1);
      }
   }

   class MenuHandler implements Runnable {
      private Socket myConnection;
      private Player myPlayer;
      private GameServer myGame;
      private PrintWriter output;
      private BufferedReader input;
      private boolean start = true;
      private boolean stop = false;
      private int error = 0;//0 means no error, anything beyond this can correspond to a different error number

      MenuHandler(Socket newConnection) {
         myConnection = newConnection;
         try {
            this.output = new PrintWriter(myConnection.getOutputStream());
            this.input = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
         } catch (IOException e) {
            e.printStackTrace();
         }
         //Send a message to the client so that it can now send back the username
      }


      /*
      Data flow:
      First, the menu handler establishes a connection and waits until it receives the name from the client
      This is then checked as start=true. If it is valid, it will send a 0 and start will no longer be true for both
      Otherwise, 1 will be sent and the cycle will repeat itself
      Then, the client sends messages whenever it wants. It is up to the server whether or not the messages are accepted,
      but it waits until it receives a response from the server before it continues
       */

      @Override
      public void run() {
         try {
            //Begin the main loop to receive info on what game they want to join
            //This process for the menu is not time sensitive, so there is no clock
            //When a new player joins, send the full list of players to all players
            while (!stop) {
               if (input.ready()) {
                  String inputString = input.readLine();//Reads as fast as it can. Or you could alternatively slow it down here by making a getFramePassed at this instance
                  //There would normally be a timer on the output, or possibly the input
                  System.out.println("I:" + inputString);
                  if (start) {
                     //Assign the name of the player
                     if (usernameValid(inputString)) {
                        myPlayer = new Player(inputString);
                        onlinePlayers.add(myPlayer);
                        error = 0;
                        start = false;
                     } else {
                        error = 1;
                     }
                     output.println(error);
                     output.flush();
                  } else {
                     char initializer = inputString.charAt(0);
                     //Here, the initializer can be chars. J (Join), C (Create), R (Ready), Q (Quit), and X (Close)
                     //Only the host has the ability to send R by sending Ready. So there is no need to check who the host is
                     inputString = inputString.substring(1);//Remove the initializer
                     if (initializer == 'J') {//J{serverName} {serverPassword}
                        String attemptServerName = inputString.substring(0, inputString.indexOf(" "));
                        inputString = inputString.substring(inputString.indexOf(" ") + 1);
                        String attemptServerPassword = inputString;
                        error = 1;
                        for (int i = 0; i < games.size(); i++) {
                           if ((games.get(i).sameName(attemptServerName)) && (games.get(i).samePassword(attemptServerPassword))) {
                              if (games.get(i).addGamePlayer(myPlayer, myConnection, this)) {
                                 myGame = games.get(i);
                                 error = 0;
                              } else {
                                 error = 2; //2 indicates that the game is full
                              }
                           }
                        }
                        //If nothing happens at all, error ends as 1
                        if (error == 0) {
                           //To the person trying to join, they should have the names of everyone sent to them
                           //To everyone else, they should have the name of the new individual
                           printOnlineList();
                        }
                        output.println(error);
                        output.flush();
                     } else if (initializer == 'C') { //To create, it is C{serverName} {password}
                        String serverName = inputString.substring(0, inputString.indexOf(" "));
                        inputString = inputString.substring(inputString.indexOf(" ") + 1);
                        String serverPassword = inputString;
                        error = 0;
                        for (int i = 0; i < games.size(); i++) {
                           if (games.get(i).sameName(serverName)) {
                              error = 1;
                              i = games.size();//break out of the loop
                           }
                        }
                        if (error == 0) { //When the error is 0, there is no need to print it out
                           myGame = new GameServer(serverName, serverPassword);//Set this game to the specific game for this gameserver
                           myGame.addGamePlayer(myPlayer, myConnection, this);
                           games.add(myGame);
                        }
                        System.out.println(error);
                        output.println(error);
                        output.flush();
                     } else if (initializer == 'R') { //You do not need to account for other players, only the host will have the option to create a game anyways
                        error = 0;
                        if (myGame.getGameSize() <= 1) {
                           error = 1;
                           output.println(error);
                           output.flush();
                        } else {
                           output.println(error);
                           output.flush();
                           myGame.run();
                        }
                        //Each game is a thread that is run. There is only communication between the game and
                        //the players

                     } else if (initializer == 'Q') {
                        myGame.removeGamePlayer(myPlayer, myConnection, this);
                        if (myGame.getGameSize() == 0) {
                           games.remove(myGame);
                        }
                        myGame = null;
                     } else if (initializer == 'X') { //Activated by window listener only
                        myGame.removeGamePlayer(myPlayer, myConnection, this);
                        if (myGame.getGameSize() == 0) {
                           games.remove(myGame);
                        }
                        myGame = null;
                        stop = true;
                     }
                  }
               }
            }
         } catch (IOException e) {
            System.out.println("Failed to transfer information");
         }
      }

      public void kill() {
         stop = true;
      }

      public boolean usernameValid(String attemptedName) {
         //Later on, check for special characters and set a limit
         for (int i = 0; i < onlinePlayers.size(); i++) {
            if (onlinePlayers.get(i).getUsername().equals(attemptedName)) {
               return (false);
            }
         }
         return (true);
      }

      public void printOnlineList() {
         //N means new player, A means all players
         try {
            ArrayList<Socket> currentSocketList = myGame.getOnlineGameSockets();
            String currentPlayerString = myGame.getOnlineGameString();
            for (int i = 0; i < currentSocketList.size(); i++) {
               //If the error is 0, which it is in this case, no error needs to be printed out
               output = new PrintWriter(currentSocketList.get(i).getOutputStream());
               if (currentSocketList.get(i).equals(myConnection)) {
                  System.out.println("A" + currentPlayerString);
                  output.println("A" + currentPlayerString); //Print the entire list
                  output.flush();
               } else {
                  output.println("N" + myPlayer.getUsername()); //Print only the name for the player
                  output.flush();
               }
            }
            output = new PrintWriter(myConnection.getOutputStream());
         } catch (IOException e) {
            System.out.println("Failed to transfer information");
         }
      }
   }

   class GameServer implements Runnable {
      private String serverName;
      private String serverPassword;
      private ArrayList<GamePlayer> onlineGamePlayers = new ArrayList<GamePlayer>();
      private ArrayList<Socket> onlineGameSockets = new ArrayList<Socket>();
      private ArrayList<MenuHandler> handlers = new ArrayList<MenuHandler>();
      private boolean stopGame = false;
      private GamePlayer[] gamePlayers;
      private Socket[] gameSockets;
      private PrintWriter[] gameOutputs;
      private BufferedReader[] gameInputs;
      private int playerNum;
      private Clock time = new Clock();

      @Override
      public void run() {
         //Not called until the game begins
         //Once it is called, this is all that really occurs
         for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).kill();
         }
         try {
            gamePlayers = new GamePlayer[onlineGamePlayers.size()];
            gameSockets = new Socket[onlineGameSockets.size()];
            gameOutputs = new PrintWriter[onlineGameSockets.size()];
            gameInputs = new BufferedReader[onlineGameSockets.size()];
            /*
            This is where a seed is generated
            */
            for (int i = 0; i < gamePlayers.length; i++) {
               gamePlayers[i] = onlineGamePlayers.get(i);
               gameSockets[i] = onlineGameSockets.get(i);
               gameOutputs[i] = new PrintWriter(onlineGameSockets.get(i).getOutputStream());
               gameInputs[i] = new BufferedReader(new InputStreamReader(onlineGameSockets.get(i).getInputStream()));
               gameOutputs[i].println("B"); //B for begin
               gameOutputs[i].flush();
            }
            playerNum = gamePlayers.length;
            for (int i = 0; i < gamePlayers.length; i++) {
               gamePlayers[i].setID(i);
            }//Set the gameplayer ID's
            String allInput[] = new String[playerNum];
            for (int i = 0; i < allInput.length; i++) {
               allInput[i] = "";
            }
            while (!stopGame) {
               time.setTime();
               if (time.getFramePassed()) {
                  for (int i = 0; i < playerNum; i++) {
                     if (gameInputs[i].ready()) {
                        allInput[i] = gameInputs[i].readLine();//Timed
                     }
                  }//This is the input
                  //Calculations here - This is essentially where ALL calculations take place.
                  //The game is essentially made in this space

                  for (int i = 0; i < playerNum; i++) {
                     if (!allInput[i].isEmpty()) {
                        double angleOfMovement = Double.parseDouble(allInput[i].substring(0, allInput[i].indexOf(" ")));
                        allInput[i] = allInput[i].substring(allInput[i].indexOf(" ") + 1);
                        double angleOfClick = Double.parseDouble(allInput[i].substring(0, allInput[i].indexOf(" ")));
                        allInput[i] = allInput[i].substring(allInput[i].indexOf(" ") + 1);
                        double lengthOfClick = Double.parseDouble(allInput[i]);
                        allInput[i] = "";
                        gamePlayers[i].addXy(angleOfMovement);
                     }
                  }

                  //Output will be here
                  String outputString = "G";
                  for (int i = 0; i < gamePlayers.length; i++) {
                     outputString += i + "," + gamePlayers[i].getXy()[0] + "," + gamePlayers[i].getXy()[1] + " ";
                  }
                  for (int i = 0; i < gamePlayers.length; i++) {
                     gameOutputs[i].println(outputString);
                     gameOutputs[i].flush();
                  }
               }
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

      GameServer(String serverName, String serverPassword) {
         this.serverName = serverName;
         this.serverPassword = serverPassword;
      }

      public boolean sameName(String comparedName) {
         if (comparedName.equals(serverName)) {
            return (true);
         } else {
            return (false);
         }
      }

      public boolean samePassword(String comparedPassword) {
         if (comparedPassword.equals(serverPassword)) {
            return (true);
         } else {
            return (false);
         }
      }

      public boolean addGamePlayer(Player player, Socket playerSocket, MenuHandler handler) {
         if (onlineGamePlayers.size() < 6) {
            onlineGamePlayers.add(new GamePlayer(player.getUsername()));
            onlineGameSockets.add(playerSocket);
            handlers.add(handler);
            return (true);
         } else {
            return (false);
         }
      }

      public void removeGamePlayer(Player player, Socket playerSocket, MenuHandler handler) {
         onlineGamePlayers.remove(new GamePlayer(player.getUsername()));
         onlineGameSockets.remove(playerSocket);
         handlers.remove(handler);
      }

      public int getGameSize() {
         return (onlineGamePlayers.size());
      }

      public ArrayList<Socket> getOnlineGameSockets() {
         return (onlineGameSockets);
      }

      public String getOnlineGameString() {
         String onlineString = "";
         for (int i = 0; i < onlineGamePlayers.size(); i++) {
            onlineString = onlineString + onlineGamePlayers.get(i).getUsername() + " ";
         }
         return (onlineString);//There will be a space at the end, this is useful
      }
   }
}

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

//kustard

public class Server {
   //All for the main server
   private ArrayList<MenuHandler> menuConnections = new ArrayList<MenuHandler>();

   //Used by all
   private ArrayList<User> onlineUsers = new ArrayList<User>();
   private ArrayList<GameServer> games = new ArrayList<GameServer>();
   private ArrayList<GameServer> startedGames = new ArrayList<GameServer>();

   public static void main(String[] args) {
      new Server().go();
   }

   private void go() {
      try {
         ServerSocket serverSock = new ServerSocket(5001);  //assigns an port to the server
         while (true) {  //this loops to accept multiple clients
            Socket newConnection = serverSock.accept();
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
      private User myUser;
      private GameServer myGame;
      private PrintWriter output;
      private BufferedReader input;
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
                  //System.out.println("I:" + inputString);
                  //Here, the initializer can be chars. U(Username), J (Join), C (Create), R (Ready), Q (Quit), and X (Close)
                  char initializer = inputString.charAt(0);
                  inputString = inputString.substring(1);//Remove the initializer
                  if (initializer == 'U') { //username
                     if (usernameValid(inputString)) {
                        myUser = new User(inputString);
                        onlineUsers.add(myUser);
                        error = 0;
                     } else {
                        error = 1;
                     }
                     output.println(error);
                     output.flush();
                  } else if (initializer == 'J') {//J{serverName} {serverPassword}
                     String attemptServerName = inputString.substring(0, inputString.indexOf(" "));
                     inputString = inputString.substring(inputString.indexOf(" ") + 1);
                     String attemptServerPassword = inputString;
                     error = 5;//Slightly different, this should give an error message which tells the client that the password/username is wrong
                     for (GameServer thisGame : games) {
                        if ((thisGame.sameName(attemptServerName)) && (thisGame.samePassword(attemptServerPassword))) {
                           if (thisGame.addGamePlayer(myUser, myConnection, this)) {
                              myGame = thisGame;
                              error = 0;
                           } else {
                              error = 6; //2 indicates that the game is full
                           }
                        }
                     }
                     //If nothing happens at all, error ends as 1
                     if (error == 0) {
                        //To the person trying to join, they should have the names of everyone sent to them
                        //To everyone else, they should have the name of the new individual
                        printOnlineList(true);
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
                        myGame.addGamePlayer(myUser, myConnection, this);
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

                  } else if (initializer == 'B') {
                     if (myGame != null) {
                        if (!myGame.isHost(myUser)) {
                           myGame.removeGamePlayer(myUser, myConnection, this);
                           if (myGame.getGameSize() == 0) {
                              games.remove(myGame);
                           } else {
                              printOnlineList(false);
                           }
                           myGame = null;
                        } else {
                           myGame.removeGamePlayer(myUser, myConnection, this);
                           games.remove(myGame);
                           //here is where you will send a message to everyone to remove all the players
                           ArrayList<Socket> currentSocketList = myGame.getOnlineGameSockets();
                           for (int i = 0; i < currentSocketList.size(); i++) {
                              output = new PrintWriter(currentSocketList.get(i).getOutputStream());
                              output.println("P");//Stands for previous, as in go to the previous card
                              output.flush();
                           }
                           myGame = null;
                        }
                     } else {
                        System.out.println("Back");
                        onlineUsers.remove(myUser);
                     }
                  } else if (initializer == 'Q') {
                     System.out.println("Quit");
                     myGame.removeGamePlayer(myUser, myConnection, this);
                     if (myGame.getGameSize() == 0) {
                        games.remove(myGame);
                     }
                     myGame = null;
                  } else if (initializer == 'X') { //Activated by window listener only
                     System.out.println("Exit");
                     onlineUsers.remove(myUser);//If it doesn't exist, nothing happens
                     if (myGame != null) {
                        if (!myGame.isHost(myUser)) {
                           myGame.removeGamePlayer(myUser, myConnection, this);
                           if (myGame.getGameSize() == 0) {
                              games.remove(myGame);
                           } else {
                              printOnlineList(false);
                           }
                           myGame = null;
                        } else {
                           myGame.removeGamePlayer(myUser, myConnection, this);
                           games.remove(myGame);
                           //here is where you will send a message to everyone to remove all the players
                           ArrayList<Socket> currentSocketList = myGame.getOnlineGameSockets();
                           for (int i = 0; i < currentSocketList.size(); i++) {
                              output = new PrintWriter(currentSocketList.get(i).getOutputStream());
                              output.println("P");//Stands for previous, as in go to the previous card
                              output.flush();
                           }
                           myGame = null;
                        }
                     }
                     stop = true;
                  }
               }
            }
         } catch (IOException e) {
            System.out.println("Failed to transfer information");
         }
      }

      private void kill() {
         stop = true;
      }

      private boolean usernameValid(String attemptedName) {
         //Later on, check for special characters and set a limit
         for (int i = 0; i < onlineUsers.size(); i++) {
            if (onlineUsers.get(i).getUsername().equals(attemptedName)) {
               return (false);
            }
         }
         return (true);
      }

      private void printOnlineList(boolean someoneLeft) {
         //N means new player, A means all players
         try {
            if (someoneLeft) {
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
                     output.println("N" + myUser.getUsername()); //Print only the name for the player
                     System.out.println("N" + myUser.getUsername());
                     output.flush();
                  }
               }
               output = new PrintWriter(myConnection.getOutputStream());
            } else {
               ArrayList<Socket> currentSocketList = myGame.getOnlineGameSockets();
               String currentPlayerString = myGame.getOnlineGameString();
               for (int i = 0; i < currentSocketList.size(); i++) {
                  //If the error is 0, which it is in this case, no error needs to be printed out
                  output = new PrintWriter(currentSocketList.get(i).getOutputStream());
                  output.println("X" + myUser.getUsername()); //Print only the name for the player
                  output.flush();
               }
            }
         } catch (IOException e) {
            System.out.println("Failed to transfer information");
         }
      }
   }

   class GameServer implements Runnable {
      private String serverName;
      private String serverPassword;
      private ArrayList<Player> onlinePlayers = new ArrayList<Player>();
      private ArrayList<Socket> onlineGameSockets = new ArrayList<Socket>();
      private ArrayList<MenuHandler> handlers = new ArrayList<MenuHandler>();
      private boolean stopGame = false;
      private Player[] players;//For the ID's, even disconnected players will work
      private Socket[] gameSockets;
      private PrintWriter[] gameOutputs;
      private BufferedReader[] gameInputs;
      //private ObjectInputStream[] gameObjectInputs;
      //private ObjectOutputStream[] gameObjectOutputs;
      private int playerNum;//For the ID's even disconnected players will work
      private Clock time = new Clock();
      private int gameTick = 0;
      private int disconnectedPlayerNum = 0;
      private boolean begin;
      private int playerDisconnected=-1;

      @Override
      public void run() {
         begin = true;
         //Not called until the game begins
         //Once it is called, this is all that really occurs
         for (MenuHandler thisHandler : handlers) {
            thisHandler.kill();
         }
         try {
            players = new Player[onlinePlayers.size()];

            gameSockets = new Socket[players.length];
            gameOutputs = new PrintWriter[players.length];
            gameInputs = new BufferedReader[players.length];
            //gameObjectOutputs = new ObjectOutputStream[players.length];
            //gameObjectInputs = new ObjectInputStream[players.length];

            /*
            This is where a seed is generated
            */
            for (int i = 0; i < players.length; i++) {
               players[i] = onlinePlayers.get(i);
               gameSockets[i] = onlineGameSockets.get(i);
               gameOutputs[i] = new PrintWriter(onlineGameSockets.get(i).getOutputStream());
               gameInputs[i] = new BufferedReader(new InputStreamReader(onlineGameSockets.get(i).getInputStream()));
               //gameObjectOutputs[i] = new ObjectOutputStream(onlineGameSockets.get(i).getOutputStream());

               gameOutputs[i].println("B"); //B for begin
               gameOutputs[i].flush();
            }
            playerNum = players.length;
            for (int i = 0; i < playerNum; i++) {
               players[i].setID(i);
            }//Set the gameplayer ID's
            String allInput[] = new String[playerNum];
            for (int i = 0; i < allInput.length; i++) {
               allInput[i] = "";
            }
            while (!stopGame) {
               for (int i = 0; i < playerNum; i++) {
                  if (players[i] != null) {
                     if (gameInputs[i].ready()) {
                        allInput[i] = gameInputs[i].readLine();//Timed
                     } else {
                        allInput[i] = "";
                     }
                  }
               }//This is the input
               StringBuilder[] outputString = new StringBuilder[playerNum];
               for (int i = 0; i < playerNum; i++) {
                  outputString[i] = new StringBuilder();
               }

               // Input from clients
               for (int i =  0; i < playerNum; i++) {
                  if (players[i] != null) {
                     if (!allInput[i].isEmpty()) {
                        if (allInput[i].equals("X")) {

                           players[i] = null;
                           disconnectedPlayerNum++;
                           playerDisconnected = i;
                           if (disconnectedPlayerNum == playerNum) {
                              stopGame = true;
                           }
                           allInput[i] = "";
                        } else {
                           String[] firstSplit = allInput[i].split(" ", -1);
                           for (String firstInput : firstSplit) {
                              if (!firstInput.equals("")) {
                                 char initializer = firstInput.charAt(0);
                                 String[] secondSplit = allInput[i].split(initializer + "", -1);
                                 for (String secondInput : secondSplit) {
                                    if (!secondInput.equals("")) {
                                       String[] thirdSplit = secondInput.split(",", -1);
                                       if (initializer == 'M') {
                                          players[i].addXy(Double.parseDouble(thirdSplit[0]), Double.parseDouble(thirdSplit[1]));
                                       } else if (initializer == 'S') {
                                          players[i].setSpell(players[i].testSpell(gameTick, Integer.parseInt(thirdSplit[0])), Integer.parseInt(thirdSplit[0]));
                                          //The x y information about the spell is stored as thirdSplit[1] and [2]
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               // Output to clients
               if (time.getFramePassed()) {
                  //Check to see if anything was added from disconnecting players. If this is true, then add a space
                  String[] mainPlayer = new String[playerNum];
                  String[] otherPlayers = new String[playerNum];
                  for (int i = 0; i < playerNum; i++) {
                     if (players[i] != null) {
                        mainPlayer[i] = "P" + i + "," + players[i].getMainOutput(gameTick);
                        otherPlayers[i] = "O" + i + "," + players[i].getOtherOutput();
                     }
                  }
                  for (int i = 0; i < playerNum; i++) {
                     if (players[i] != null) {
                        if (playerDisconnected != -1) {
                           outputString[i].append("D" + playerDisconnected + " ");
                           playerDisconnected = -1;
                        }
                     }
                  }
                  //Output will be here. The first loop generates the full message, the second distributes it

                  for (int i = 0; i < playerNum; i++) {
                     if (players[i] != null) {
                        outputString[i].append(mainPlayer[i] + " ");
                        for (int j = 0; j < playerNum; j++) {
                           if (i != j) {
                              if (players[j] != null) {
                                 outputString[i].append(otherPlayers[j]);
                              }
                           }
                        }
                     }
                  }
                  for (int i = 0; i < playerNum; i++) {
                     if (players[i] != null) {
                        gameOutputs[i].println(outputString[i].toString().trim());

                        gameOutputs[i].flush();
                     }
                  }
                  gameTick++;
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

      private boolean isHost(User myUser) {
         if (myUser.getUsername().equals(onlinePlayers.get(0).getUsername())) {
            return true;
         } else {
            return false;
         }
      }

      private boolean sameName(String comparedName) {
         if (comparedName.equals(serverName)) {
            return (true);
         } else {
            return (false);
         }
      }

      private boolean samePassword(String comparedPassword) {
         if (comparedPassword.equals(serverPassword)) {
            return (true);
         } else {
            return (false);
         }
      }

      private boolean addGamePlayer(User user, Socket playerSocket, MenuHandler handler) {
         if (!begin) {
            if (onlinePlayers.size() < 6) {
               onlinePlayers.add(new TestClass(user.getUsername()));
               onlineGameSockets.add(playerSocket);
               handlers.add(handler);
               return (true);
            } else {
               return (false);
            }
         } else {
            return (false);
         }
      }

      private void removeGamePlayer(User user, Socket playerSocket, MenuHandler handler) {
         System.out.println(user.getUsername());
         System.out.println(onlinePlayers.size());
         for (int i = 0; i < onlinePlayers.size(); i++) {
            if (user.getUsername().equals(onlinePlayers.get(i).getUsername())) {
               onlinePlayers.remove(i);
            }
         }
         onlineGameSockets.remove(playerSocket);
         handlers.remove(handler);
         System.out.println(onlinePlayers.size());
      }

      private int getGameSize() {
         return (onlinePlayers.size());
      }

      private ArrayList<Socket> getOnlineGameSockets() {
         return (onlineGameSockets);
      }

      private String getOnlineGameString() {
         StringBuilder onlineString = new StringBuilder("");
         for (int i = 0; i < onlinePlayers.size(); i++) {
            onlineString.append(onlinePlayers.get(i).getUsername() + " ");
         }
         return (onlineString + "");//There will be a space at the end, this is useful
      }

   }


}

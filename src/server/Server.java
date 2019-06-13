package server;

import javax.imageio.ImageIO;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server.java
 * This is
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 1.0
 * @since 2019-04-24
 */

public class Server {
   //REMEMBER: output = new PrintWriter(myConnection.getOutputStream()); is very important

   //All for the main server
   private ArrayList<MenuHandler> menuConnections = new ArrayList<MenuHandler>();

   //Used by all
   private ArrayList<User> onlineUsers = new ArrayList<User>();
   private ArrayList<GameServer> games = new ArrayList<GameServer>();

   public static void main(String[] args) {
      new Server().go();
   }

   private void go() {
      try {
         ServerSocket serverSock = new ServerSocket(5002);  //assigns an port to the server
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
      private int error;

      MenuHandler(Socket newConnection) {
         myConnection = newConnection;
         try {
            this.output = new PrintWriter(myConnection.getOutputStream());
            this.input = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

      @Override
      public void run() {
         try {
            while (!stop) {
               if (input.ready()) {
                  String inputString = input.readLine();//Reads as fast as it can. Or you could alternatively slow it down here by making a getFramePassed at this instance
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
                        adjustPlayerList(true);
                     } else {
                        output.println(error);
                        output.flush();
                     }
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
                     if (error == 0) {
                        adjustPlayerList(true);
                     } else {
                        output.println(error);
                        output.flush();
                     }
                  } else if (initializer == 'R') { //You do not need to account for other characters, only the host will have the option to create a game anyways
                     System.out.println("R" + inputString);

                     if (myGame.getGameSize() <= 1) {
                        output.println(7);
                        output.flush();
                     } else if ((myGame.emptyTeam(0)) || (myGame.emptyTeam(1))) {
                        output.println(8);
                        output.flush();
                     } else if (myGame.noTeam()) {
                        output.println(9);
                        output.flush();
                     } else if (myGame.noClass()) {
                        output.println(10);
                        output.flush();
                     } else {
                        myGame.run();
                     }

                     //Error verification is ignored
                     //Each game is a thread that is run. There is only communication between the game and the characters
                  } else if (initializer == 'B') {
                     if (myGame != null) {
                        if (!myGame.isHost(myUser)) {
                           myGame.removeGamePlayer(myUser, myConnection, this);
                           if (myGame.getGameSize() == 0) {
                              games.remove(myGame);
                           } else {
                              adjustPlayerList(false);
                           }
                           myGame = null;
                        } else {
                           myGame.removeGamePlayer(myUser, myConnection, this);
                           games.remove(myGame);
                           //here is where you will send a message to everyone to remove all the characters
                           ArrayList<Socket> currentSocketList = myGame.getOnlineGameSockets();
                           for (int i = 0; i < currentSocketList.size(); i++) {
                              output = new PrintWriter(currentSocketList.get(i).getOutputStream());
                              output.println("P");//Stands for previous, as in go to the previous card
                              output.flush();
                           }

                           output = new PrintWriter(myConnection.getOutputStream());
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
                              adjustPlayerList(false);
                           }
                           myGame = null;
                        } else {
                           myGame.removeGamePlayer(myUser, myConnection, this);
                           games.remove(myGame);
                           //here is where you will send a message to everyone to remove all the characters
                           ArrayList<Socket> currentSocketList = myGame.getOnlineGameSockets();
                           for (int i = 0; i < currentSocketList.size(); i++) {
                              output = new PrintWriter(currentSocketList.get(i).getOutputStream());
                              output.println("P");//Stands for previous, as in go to the previous card
                              output.flush();
                           }
                           output = new PrintWriter(myConnection.getOutputStream());
                           myGame = null;
                        }
                     }
                     stop = true;
                  } else if (initializer == 'E') {
                     if (myGame.emptySpot(Integer.parseInt(inputString))) {
                        myGame.setTeam(myUser, Integer.parseInt(inputString));
                        ArrayList<Socket> currentSocketList = myGame.getOnlineGameSockets();
                        for (int i = 0; i < currentSocketList.size(); i++) {
                           output = new PrintWriter(currentSocketList.get(i).getOutputStream());
                           output.println("E" + myGame.getTeam(myUser) + myUser.getUsername());//If -1 (won't happen) then something is wrong
                           output.flush();
                        }
                        output = new PrintWriter(myConnection.getOutputStream());
                     } else {
                        output.println(9);
                        output.flush();
                     }
                  } else if (initializer == 'T') {
                     myUser = new User(inputString.substring(0, inputString.indexOf(",")));
                     onlineUsers.add(myUser);
                     if (games.size() == 0) {
                        myGame = new GameServer(inputString.substring(inputString.indexOf(",") + 1), "0");//Set this game to the specific game for this gameserver
                        games.add(myGame);
                        games.get(0).addGamePlayer(myUser, myConnection, this);
                     } else {
                        myGame = games.get(0);
                        games.get(0).addGamePlayer(myUser, myConnection, this);
                     }
                     adjustPlayerList(true);
                  } else if (initializer == 'Z') { //Choosing class
                     myGame.setSelectedClass(myUser, inputString);
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

      private void adjustPlayerList(boolean noOneLeft) {
         //N means new player, A means all characters
         try {
            if (noOneLeft) {
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
               for (int i = 0; i < currentSocketList.size(); i++) {
                  //If the error is 0, which it is in this case, no error needs to be printed out
                  output = new PrintWriter(currentSocketList.get(i).getOutputStream());
                  output.println("X" + myUser.getUsername()); //Print only the name for the player
                  output.flush();
               }
               output = new PrintWriter(myConnection.getOutputStream());
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
      private Player[] players;//For the ID's, even disconnected characters will work
      private Socket[] gameSockets;
      private PrintWriter[] gameOutputs;
      private BufferedReader[] gameInputs;
      //private ObjectInputStream[] gameObjectInputs;
      //private ObjectOutputStream[] gameObjectOutputs;
      private int playerNum;//For the ID's even disconnected characters will work
      private Clock time = new Clock();
      private int gameTick = 0;
      private int disconnectedPlayerNum = 0;
      private boolean begin;
      private int playerDisconnected = -1;
      private Polygon[] allPolygons;
      private boolean startMessage = true;
      private int winner;


      @Override
      public void run() {
         begin = true;
         //Once it is called, this is all that really occurs
         for (MenuHandler thisHandler : handlers) {
            thisHandler.kill();
         }
         Polygon[] sampleObstacles = new Polygon[2];
         int[] xPoints = new int[4];
         int[] yPoints = new int[4];
         xPoints[0] = 300;
         xPoints[1] = 400;
         xPoints[2] = 400;
         xPoints[3] = 300;
         yPoints[0] = 300;
         yPoints[1] = 300;
         yPoints[2] = 400;
         yPoints[3] = 400;
         sampleObstacles[0] = (new Polygon(xPoints, yPoints, 4));
         int[] x2Points = new int[5];
         int[] y2Points = new int[5];
         x2Points[0] = 100;
         x2Points[1] = 200;
         x2Points[2] = 300;
         x2Points[3] = 400;
         x2Points[4] = 500;
         y2Points[0] = 100;
         y2Points[1] = 200;
         y2Points[2] = 200;
         y2Points[3] = 100;
         y2Points[4] = 0;
         sampleObstacles[1] = (new Polygon(x2Points, y2Points, 5));
         try {
            players = new Player[onlinePlayers.size()];
            gameSockets = new Socket[players.length];
            gameOutputs = new PrintWriter[players.length];
            gameInputs = new BufferedReader[players.length];
            //gameObjectOutputs = new ObjectOutputStream[characters.length];
            //gameObjectInputs = new ObjectInputStream[characters.length];


            for (int i = 0; i < players.length; i++) {
               String playerClass = onlinePlayers.get(i).getSelectedClass();
               System.out.println("P" + playerClass);
               if (playerClass != null) { //TESTING ONLY
                  if (playerClass.equals("Archer") || playerClass.equals("Marksman") || playerClass.equals("SafeMarksman")) {
                     players[i] = new SafeMarksman(onlinePlayers.get(i).getUsername(), onlinePlayers.get(i).getTeam());
                  } else if (playerClass.equals("TimeMage")) {
                     players[i] = new TimeMage(onlinePlayers.get(i).getUsername(), onlinePlayers.get(i).getTeam());
                  } else if (playerClass.equals("Ghost")) {
                     players[i] = new Ghost(onlinePlayers.get(i).getUsername(), onlinePlayers.get(i).getTeam());
                  } else if (playerClass.equals("MobileSupport") || playerClass.equals("Support")) {
                     players[i] = new MobileSupport(onlinePlayers.get(i).getUsername(), onlinePlayers.get(i).getTeam());
                  } else if (playerClass.equals("Juggernaut")) {
                     players[i] = new Juggernaut(onlinePlayers.get(i).getUsername(), onlinePlayers.get(i).getTeam());
                  } else if (playerClass.equals("Summoner")) {
                     players[i] = new Summoner(onlinePlayers.get(i).getUsername(), onlinePlayers.get(i).getTeam());
                  }
                  players[i].setSelectedClass(playerClass);
               } else {
                  System.out.println("Class has not been chosen");
                  players[i] = new SafeMarksman(onlinePlayers.get(i).getUsername(), onlinePlayers.get(i).getTeam());
               }
               gameSockets[i] = onlineGameSockets.get(i);
               gameOutputs[i] = new PrintWriter(onlineGameSockets.get(i).getOutputStream());
               gameInputs[i] = new BufferedReader(new InputStreamReader(onlineGameSockets.get(i).getInputStream()));
               //gameObjectOutputs[i] = new ObjectOutputStream(onlineGameSockets.get(i).getOutputStream());
            }
            for (int i = 0; i < players.length; i++) {
               gameOutputs[i].println("0");//B for begin
               gameOutputs[i].flush();
            }

            MainMapGenModule builder = new MainMapGenModule();

            StringBuilder beginLine = new StringBuilder("B");
            for (int k = 0; k < players.length; k++) {
               beginLine.append(" " + players[k].getSelectedClass());
            }
            System.out.println(beginLine.toString());
            System.out.println("0");
            for (int i = 0; i < players.length; i++) {
               gameOutputs[i].println(beginLine.toString().trim()); //B for begin
               gameOutputs[i].flush();
            }
            System.out.println("1");
            for (int i = 0; i < onlineGameSockets.size(); i++) {
               builder.sendMap(onlineGameSockets.get(i));
            }
            System.out.println("2");
            for (int i = 0; i < players.length; i++) {
               gameOutputs[i].println("FINAL");//B for begin
               gameOutputs[i].flush();
            }
            ///SET SPAWNING

            for (int i = 0; i < players.length; i++) {
               players[i].setSpawn(builder.getTeamClearing(players[i].getTeam()));
            }
            Player.setArtifacts(builder.getTeamClearing(0), 0);
            Player.setArtifacts(builder.getTeamClearing(1), 1);

            System.out.println("done");
            playerNum = players.length;
            //Set up the characters in each player
            Player.setPlayerReference(players, playerNum);
            for (int i = 0; i < playerNum; i++) {
               players[i].setID(i);
            }//Set the gameplayer ID's
            String allInput[] = new String[playerNum];
            for (int i = 0; i < allInput.length; i++) {
               allInput[i] = "";
            }
            //Set the teams
            for (Player myPlayer : players) {
               myPlayer.sendInfo();
            }
            allPolygons = new Polygon[builder.getObstacle().size()];
            for (int i = 0; i < allPolygons.length; i++) {
               allPolygons[i] = builder.getObstacle().get(i).boundingBox;
               int[] xP = new int[allPolygons[i].npoints];
               int[] yP = new int[allPolygons[i].npoints];
               for (int j = 0; j < allPolygons[i].npoints; j++) {
                  xP[j] = allPolygons[i].xpoints[j]/2 + 7500;
                  yP[j] = allPolygons[i].ypoints[j]/2 + 5000;
               }
               allPolygons[i] = new Polygon(xP, yP, xP.length);
            }

            Player.setConstantHitboxes(players.length, builder.getObstacle());
            for (int i = 0; i < players.length; i++) {
               players[i].setLightingHitbox(i);
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

               for (int i = 0; i < playerNum; i++) {
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
                              char initializer = firstInput.charAt(0);
                              firstInput = firstInput.substring(1);
                              String[] secondSplit = firstInput.split(",", -1);
                              if (secondSplit.length > 0) {
                                 if (initializer == 'M') {
                                    if (!checkColliding(players[i], Double.parseDouble(secondSplit[0]), Double.parseDouble(secondSplit[1]))) {
                                       players[i].addXy(Double.parseDouble(secondSplit[0]), Double.parseDouble(secondSplit[1]));
                                    }

                                 } else if (initializer == 'S') {
                                    players[i].setSpell(players[i].castSpell(Integer.parseInt(secondSplit[0])), Integer.parseInt(secondSplit[0]));
                                    //The x y information about the spell is stored as secondSplit[1] and [2]
                                 } else if (initializer == 'A') {
                                    players[i].autoAttack();
                                 } else if (initializer == 'F') {
                                    players[i].flare();
                                 } else if (initializer == 'R') {
                                    players[i].setMouseAngle(Double.parseDouble(secondSplit[0]));
                                 } else if (initializer == 'P') {
                                    players[i].setMouse(Integer.parseInt(secondSplit[0]), Integer.parseInt(secondSplit[1]));
                                 } else if (initializer == 'W') {
                                    players[i].setPositionIndex(Integer.parseInt(secondSplit[0]));
                                    players[i].setWalking(true);
                                 } else if (initializer == 'L') {
                                    players[i].setFlashlightOn(true);
                                    players[i].calculateFlashlightPolygon(Double.parseDouble(secondSplit[0]));
                                    players[i].addStatus(new Illuminated(2));
                                 } else if (initializer == 'C') { // Chat coming in
                                    String mode = secondSplit[0];
                                    String message = secondSplit[1];
                                    System.out.println("Message: " + message);
                                    if (mode.equals("1")) { // To everyone
                                       for (int j = 0; j < playerNum; j++) {
                                          gameOutputs[j].println("C" + players[i].getUsername() + "," + message);
                                          gameOutputs[j].flush();
                                       }
                                    } else if (mode.equals("2")) { // To team
                                       for (int j = 0; j < playerNum; j++) {
                                          if (players[j].getTeam() == players[i].getTeam()) {
                                             gameOutputs[j].println("C" + players[i].getUsername() + "," + message);
                                          }
                                       }
                                    } else if (mode.equals("3")) { // DM
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
               Player.updateHitbox();
               for (int i = 0; i < 2; i++) {
                  if (Player.getArtifacts(i).getWinner()) {
                     stopGame = true;
                     winner = i;
                  }
               }
               if (stopGame) {
                  for (int i = 0; i < playerNum; i++) {
                     if (players[i] != null) {
                        gameOutputs[i].println("WINNER" + winner);
                        gameOutputs[i].flush();
                     }
                  }
               } else {
                  // Output to clients
                  if (time.getFramePassed()) {
                     //Check to see if anything was added from disconnecting characters. If this is true, then add a space
                     StringBuilder[] mainPlayer = new StringBuilder[playerNum];
                     StringBuilder[] otherPlayers = new StringBuilder[playerNum];
                     StringBuilder projectileOutput = new StringBuilder();
                     StringBuilder aoeOutput = new StringBuilder();
                     StringBuilder statusOutput = new StringBuilder();
                     for (int i = 0; i < playerNum; i++) {
                        if (players[i] != null) {
                           mainPlayer[i] = new StringBuilder();
                           otherPlayers[i] = new StringBuilder();
                           if (startMessage) {
                              mainPlayer[i].append("A" + Player.getArtifacts(0).getXy()[0] + "," + Player.getArtifacts(0).getXy()[1] + "," + Player.getArtifacts(1).getXy()[0] + "," + Player.getArtifacts(1).getXy()[1] + " ");
                           }
                           mainPlayer[i].append("P" + i + "," + players[i].getMainOutput());
                           otherPlayers[i].append("O" + i + "," + players[i].getOtherOutput());
                           ArrayList<Projectile> theseProjectiles = players[i].getAllProjectiles();
                           ArrayList<AOE> theseAOES = players[i].getAllAOES();
                           //HashSet<Status> statusSet = new HashSet<Status>(players[i].getAllStatuses());
                           ArrayList<Status> theseStatuses = players[i].getAllStatuses();
                           boolean[] hasStatus = new boolean[15];
                           Status[] parallelStatus = new Status[15];
                           for (int j = 0; j < theseStatuses.size(); j++) {
                             if (theseStatuses.get(j).getID() >= 0){
                               hasStatus[theseStatuses.get(j).getID()] = true;
                               parallelStatus[theseStatuses.get(j).getID()] = theseStatuses.get(j);
                             }
                           }
                           for (int j = 0; j < theseProjectiles.size(); j++) {
                              projectileOutput.append("R" + theseProjectiles.get(j).getID() + "," + theseProjectiles.get(j).getX() + "," + theseProjectiles.get(j).getY() + " ");
                           }
                           for (int j = 0; j < theseAOES.size(); j++) {
                              if ((theseAOES.get(j).getID() != 4) && (theseAOES.get(j).getID() != 14)) {
                                 aoeOutput.append("E" + theseAOES.get(j).getID() + "," + theseAOES.get(j).getX() + "," + theseAOES.get(j).getY() + "," + theseAOES.get(j).getRadius() + " ");
                              } else if (theseAOES.get(j).getID() == 4) {//Time Mage AOE is different
                                 aoeOutput.append("E" + theseAOES.get(j).getID());
                                 int[][] points = ((TimeMageQAOE) theseAOES.get(j)).getPoints();
                                 for (int m = 0; m < points.length; m++) {
                                    for (int n = 0; n < points[m].length; n++) {
                                       aoeOutput.append("," + points[m][n]);//xpoints, then ypoints
                                    }
                                 }
                                 aoeOutput.append(" ");
                              } else if (theseAOES.get(j).getID() == 14) {//AutAOE is also different
                                 aoeOutput.append("E" + theseAOES.get(j).getID());
                                 int[][] points = ((AutoAOE) theseAOES.get(j)).getPoints();
                                 for (int m = 0; m < points.length; m++) {
                                    for (int n = 0; n < points[m].length; n++) {
                                       aoeOutput.append("," + points[m][n]);//xpoints, then ypoints
                                    }
                                 }
                                 aoeOutput.append(" ");
                              }
                           }
                           int ghostPassiveCount = 0;
                           for (int j = 0; j < hasStatus.length; j++) {
                              if ((j == 2) && (hasStatus[j])) {
                                 statusOutput.append("S" + j + "," + i + "," + ((GhostE) parallelStatus[j]).getX() + "," + ((GhostE) parallelStatus[j]).getY() + " ");
                              } else if ((j == 3) && (hasStatus[j])) {
                                 ghostPassiveCount++;
                              } else if ((j == -1) && (hasStatus[j])) {
                                 statusOutput.append("S" + j + "," + i + " ");
                              }
                           }
                           if (ghostPassiveCount > 0) {
                              statusOutput.append("S" + 3 + "," + i + "," + ghostPassiveCount + " ");
                           }
                           if (players[i].getShieldsSize() > 0) {
                              statusOutput.append("S" + 13 + "," + i + " ");
                           }
                        }
                     }
                     if (startMessage) {
                        startMessage = false;
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
                                    outputString[i].append(otherPlayers[j] + " ");
                                 }
                              }
                           }
                        }
                     }
                     //Write out all the projectiles
                     for (int i = 0; i < playerNum; i++) {
                        if (players[i] != null) {
                           if (!projectileOutput.toString().isEmpty()) {
                              outputString[i].append(projectileOutput);
                           }
                        }
                     }
                     //Write out all the AOEs
                     for (int i = 0; i < playerNum; i++) {
                        if (players[i] != null) {
                           if (!aoeOutput.toString().isEmpty()) {
                              outputString[i].append(aoeOutput);
                           }
                        }
                     }
                     //Write out all the Statuses
                     for (int i = 0; i < playerNum; i++) {
                        if (players[i] != null) {
                           if (!statusOutput.toString().isEmpty()) {
                              outputString[i].append(statusOutput);
                           }
                        }
                     }
                     for (int i = 0; i < playerNum; i++) {
                        if (players[i] != null) {
                           outputString[i].append("W" + i + "," + players[i].getPositionIndex() + "," + players[i].getWalking() + " ");
                        }
                     }
                     for (int i = 0; i < playerNum; i++) {
                        if (players[i] != null) {
                           if (players[i].getFlashlightOn()) {
                              int[] tempX = players[i].getFlashlightPointX();
                              int[] tempY = players[i].getFlashlightPointY();
                              for (int j = 0; j < playerNum; j++) {
                                 if (players[j] != null) {
                                    outputString[j].append("L" + i + "," + players[i].getFlashlightPointNum());
                                    for (int k = 0; k < players[i].getFlashlightPointNum(); k++) {
                                       outputString[j].append("," + tempX[k] + "," + tempY[k]);
                                    }
                                 }
                                 outputString[j].append(" ");
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
                     for (int i = 0; i < playerNum; i++) {
                        if (players[i] != null) {
                           players[i].update();
                           players[i].setFlashlightOn(false);
                        }
                     }
                     gameTick++;
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
         //Initialize teams
      }

      private boolean checkColliding(Player thisPlayer, double x, double y) {
         for (int i = 0; i < allPolygons.length; i++) {
            if (allPolygons[i].intersects(thisPlayer.getAdjustedHitboxRectangle(x, y))) {
               return (true);
            }
         }
         return (false);
      }

      private boolean isHost(User myUser) {
         if (myUser.getUsername().equals(onlinePlayers.get(0).getUsername())) {
            return true;
         } else {
            return false;
         }
      }

      private void setSelectedClass(User myUser, String selectedClass) {
         for (Player myPlayer : onlinePlayers) {
            if (myPlayer.getUsername().equals(myUser.getUsername())) {
               myPlayer.setSelectedClass(selectedClass);
            }
         }
      }

      private boolean emptySpot(int teamNum) {
         int dupeCount = 0;
         for (Player myPlayer : onlinePlayers) {
            if (myPlayer.getTeam() == teamNum) {
               dupeCount++;
            }
         }
         if (dupeCount != 3) {
            return (true);
         } else {
            return (false);
         }
      }

      private boolean emptyTeam(int teamNum) {
         for (Player myPlayer : onlinePlayers) {
            if (myPlayer.getTeam() == teamNum) {
               return false;
            }
         }
         return (true);
      }

      private void setTeam(User myUser, int teamNum) {
         for (Player myPlayer : onlinePlayers) {
            if (myPlayer.getUsername().equals(myUser.getUsername())) {
               myPlayer.setTeam(teamNum);
            }
         }
      }

      private int getTeam(User myUser) {
         for (Player myPlayer : onlinePlayers) {
            if (myPlayer.getUsername().equals(myUser.getUsername())) {
               return (myPlayer.getTeam());
            }
         }
         return (9);
      }

      private boolean noTeam() {
         for (Player myPlayer : onlinePlayers) {
            if (myPlayer.getTeam() == 9) {
               return (true);
            }
         }
         return (false);
      }

      private boolean noClass() {
         for (Player myPlayer : onlinePlayers) {
            if (myPlayer.getSelectedClass() == null) {
               return (true);
            }
         }
         return (false);
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
               onlinePlayers.add(new SafeMarksman(user.getUsername(), 9));//The team will not be set
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
         //Also gets ride of the team
         for (int i = 0; i < onlinePlayers.size(); i++) {
            if (user.getUsername().equals(onlinePlayers.get(i).getUsername())) {
               onlinePlayers.remove(i);
            }
         }
         onlineGameSockets.remove(playerSocket);
         handlers.remove(handler);
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
            onlineString.append(onlinePlayers.get(i).getTeam() + onlinePlayers.get(i).getUsername() + " ");
         }
         return (onlineString + "");//There will be a space at the end, this is useful
      }

   }


}

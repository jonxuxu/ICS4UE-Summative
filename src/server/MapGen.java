package server;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.Polygon;

/**
 * MapGen.java
 *
 * The class responsible for doing the mathematics behind map generation and task delegation to lower classes
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @version 2.3
 * @since 2019-03-25
 *
 */

public class MapGen {
 // Constants that are used for map gen operations
 private final static int ROAD_WIDTH = 100;
 public final static int MAP_REGION_IDX = 0;
 public final static int SWAMP_REGION_IDX = 1;


 //Variables for setting up the map generation parameters
 public int mapSize;
 public int loopRadius;
 public int swampRadius;
 public double ellipticalAdjust;

 //Data created during a generation cycle
    public ArrayList<RoadNode> nodes; 
    public ArrayList<Obstacle> obstacles;
    public RegionLayer regionLayer;

    //Boolean for configuring test states
    public boolean testingState;


 /**
  *
  * The constructor for the MapGen class
  *
  * @param mSize the size of the map for generation
  * @param lRad the radius of the loop for road node generation
  * @param eAdjust the eccentricity of the horizonatl adjust, tuning the map from a circle to an ellipse
  */
 MapGen(int mSize, int lRad, double eAdjust) {
      this.nodes = new ArrayList<RoadNode>(0);
      this.obstacles = new ArrayList<Obstacle>(0);
      this.testingState = false;
      this.ellipticalAdjust = eAdjust;
    }

 /**
  * Runs essential functions to generate an entire map without acessing other classes
  *
  * @return MapData, an object that contains the essential data contained within the map
  */
 public MapData finalGenerate() {
     this.selfInitialize();
     
     MapData returnMap = new MapData();
     returnMap.obstacles = this.obstacles;
     returnMap.rLayer = this.regionLayer;
     
     return returnMap;
    }

 /**
  * Runs the sequence of all methods performing the mathematics for creating one entire map
  */
 public void selfInitialize() {
     this.nodes = new ArrayList<RoadNode>(0);
        this.obstacles = new ArrayList<Obstacle>(0);
        this.testingState = false;
        
        //this.ellipticalAdjust = eAdjust;

  // initializes basic values for generation
     int loopRadiusSize = 10;
     int nodeGenRange = 3750;
     double nodeGenStDev = 0.5;
     

     // runs the method sequence
     this.generateMap2(40,loopRadiusSize,nodeGenRange,nodeGenStDev);
     this.tetherAllNodes2(); 
     this.makeNodesElliptical();
     this.generateRegions();
     this.generateCrevices(2);
     this.insertArtifactClearing();
     this.smokeTrees(7500, 1000, 0, false);    
     this.smokeRocks(7500, 100, true);
     this.makeObstaclesElliptical();
     this.genClearingByNum(8, 500);       
     this.purgeRedundancies();
    }

 /**
  * Configures a scenario for testing basic functionality
  */
 public void configueScenario1() {
     this.testingState = true;
     
     RoadNode node1 = new RoadNode(5000,5000);
     RoadNode node2 = new RoadNode(1000, 10000);
     node1.connections.add(node2.location);
     node1.connections.add(new Point(500,7000));
     
     this.nodes = new ArrayList<RoadNode>(0);
     this.nodes.add(node1);
     this.nodes.add(node2);
    }

 /**
  * Generates the regions in the map based upon earlier generated nodes and connections
  */
 public void generateRegions() {
     this.regionLayer = new RegionLayer();
     
     Region swamp = new Region("swamp",1);
     Region map = new Region("map", 0); 
       
     map.mimicEllipse(0, 0, 7500, 1.75, 100);
     swamp.mimicEllipse(0, 0, 3125, 1.75, 20);
     
     
     this.regionLayer.regions.add(MAP_REGION_IDX, map);
     this.regionLayer.regions.add(SWAMP_REGION_IDX,swamp);
     
     
     for (int idx = 0; idx < nodes.size(); idx++) {
      RoadNode activeNode = nodes.get(idx);
      
      for (int connectionIdx = 0; connectionIdx < activeNode.connections.size(); connectionIdx++) {
       
       Region creation = new Region("road",3);
       creation.adoptRoadShape(activeNode.location, 
         activeNode.connections.get(connectionIdx), 250);
       this.regionLayer.regions.add(creation);
       
      }
     }    
    }

 /**
  *  Makes all objects generated elliptical
  *
  * @param eAdjust, the value by which the adjustment is made
  */
 public void makeElliptical(double eAdjust) {
     for (int i = 0; i < nodes.size(); i++) {
      nodes.get(i).location.x = (int) (nodes.get(i).location.x*eAdjust);
     }
     for (int i = 0; i < obstacles.size(); i++) {
      obstacles.get(i).location.x = (int) (obstacles.get(i).location.x*eAdjust);
     }
    }

 /**
  * Makes all node positions elliptical, based upon an earlier-initialized value
  */
 public void makeNodesElliptical() {
     for (int i = 0; i < nodes.size(); i++) {
      nodes.get(i).location.x = (int) (nodes.get(i).location.x*ellipticalAdjust);
     }
    }

 /**
  * Makes all obstacle position elliptical, based upon an earlier-initialized value
  */
 public void makeObstaclesElliptical() {
     for (int i = 0; i < obstacles.size(); i++) {
      obstacles.get(i).location.x = (int) (obstacles.get(i).location.x*ellipticalAdjust);
     }
    }

 /**
  * Generates clearings on road nodes, implemented within the nodes themselves
  *
  * @param clearingChance, the chance that any individual node becomes a clearing/
  * @param clearingSize, the size that the clearing is.
  */

 public void genClearing(double clearingChance, int clearingSize) {
     for (int i = 0; i < nodes.size(); i++) {
      if (roll(clearingChance)) {
       nodes.get(i).isClearing = true;
       nodes.get(i).clearingSize = clearingSize;
      }
     }
    }

 /**
  * Generate clearings on road nodes, implemented within the nodes themselves
  *
  * @param numClearings
  * @param clearingSize
  */

 public void genClearingByNum(int numClearings, int clearingSize) {
     for (int i = 0; i < numClearings; i++) {
      int randomIdx = (int) (Math.random()*nodes.size()) ;
      if (nodes.get(randomIdx).isClearing) {
       i--;
      } else {
       nodes.get(randomIdx).isClearing = true;
       nodes.get(randomIdx).clearingSize = clearingSize;
      }
     }
    }

 /**
  *
  * Generates trees in the 'obstacles' list based on given parameters
  *
  * @param mapRadius how large the range of tree generation is
  * @param numOfTrees how many trees should be spawned
  * @param intensity how intense the spawning of the tress is tuned to the center
  * @param randomization whether randomization of trees is allowed
  */

 public void smokeTrees(int mapRadius, int numOfTrees, int intensity, boolean randomization) {
     for (int i = 0; i < numOfTrees;i++) {
      double angle;
      double radius;
      
      Obstacle temp = new Obstacle();
      temp.type = "TREE";
      temp.location = new Point();

   int maxRadius = 400;
      
      int tempX, tempY;
      int tempDeltaX, tempDeltaY;
      boolean exit; 
      
      
      do {
       angle = Math.random()*2*Math.PI;
       radius = Math.random()*mapRadius;
       
       tempX = (int) (Math.cos(angle)*radius);
       tempY = (int) (Math.sin(angle)*radius);
       
       exit = true;

    if (regionLayer.checkCoordinate( (int) (tempX*1.75), tempY) == ("swamp")) {
     if (randRoll(700)) {
      exit = false;
     }
    }

    if (exit) {
     if ((regionLayer.checkCoordinate((int) (tempX * 1.75), tempY) == ("map") ||
       regionLayer.checkCoordinate((int) (tempX * 1.75), tempY) == ("swamp")) &&
       regionLayer.checkCoordinate((int) (tempX * 1.75), tempY) != ("road")
     ) {
      for (int idx = obstacles.size() - 1; idx > -1; idx--) {
       tempDeltaX = tempX - obstacles.get(idx).location.x;
       tempDeltaY = tempY - obstacles.get(idx).location.y;

       if ((Math.pow(tempDeltaX, 2) +
         Math.pow(tempDeltaY, 2)) <= Math.pow(maxRadius, 2)) {
//            System.out.println((Math.pow(tempDeltaX,2) +
//                 Math.pow(tempDeltaY,2)));
        exit = false;
       }
      }
     } else {
      exit = false;
     }
    }

       
       
      } while (!exit);
      
      temp.location.x = tempX;
      temp.location.y = tempY;
      temp.radius = maxRadius;
      this.obstacles.add(temp);
     }
    }

 /**
  *
  * Generates trees in the 'obstacles' list based on given parameters
  *
  * @param mapRadius how large the range of rock generation is
  * @param numOfRocks how many rocks should be spawned
  * @param randomization whether randomization of rocks is allowed
  */
    public void smokeRocks(int mapRadius, int numOfRocks, boolean randomization) {
     for (int i = 0; i < numOfRocks;i++) {
      double angle;
      double radius;
      
      int maxRadius = 700;
      
      Obstacle temp = new Obstacle();
      temp.type = "ROCK";
      temp.location = new Point();
      
      int tempX, tempY;
      int tempDeltaX, tempDeltaY;
      boolean exit; 
      
      
      do {
       angle = Math.random()*2*Math.PI;
       radius = Math.random()*mapRadius;
       
       tempX = (int) (Math.cos(angle)*radius);
       tempY = (int) (Math.sin(angle)*radius);
       
       exit = true;
           
       if (regionLayer.checkCoordinate((int)(tempX*1.75), tempY) == ("map")) {
        for (int idx = obstacles.size() - 1; idx > -1; idx--) {
         tempDeltaX = tempX - obstacles.get(idx).location.x;
         tempDeltaY = tempY - obstacles.get(idx).location.y;        
         
         if ((Math.pow(tempDeltaX,2) + 
           Math.pow(tempDeltaY,2)) <= Math.pow(maxRadius,2)) {
//            System.out.println((Math.pow(tempDeltaX,2) +
//                 Math.pow(tempDeltaY,2)));
            exit = false;
         }
        }
       } else {
        exit = false;
       }
       
       
      } while (!exit);
      
      temp.location.x = tempX;
      temp.location.y = tempY;
      
      if (randomization) {       
       double percentRand = 0.8;
       temp.radius = (int) (Math.random()*maxRadius*(percentRand) + maxRadius*(1.0 - percentRand));
      }
      
      this.obstacles.add(temp);
     }
    }

 /**
  * Accesses the region layer retroactively to insert artifact clearings at the rightmost and leftmost nodes
  */
 public void insertArtifactClearing() {
     int teamOneDistance = 0;  
     int teamTwoDistance = 0;
     int teamOneIdx = 0; 
     int teamTwoIdx = 0;
     
     for (int idx = 0; idx < this.nodes.size(); idx++) {
      if (nodes.get(idx).location.x > teamTwoDistance) {
       teamTwoDistance = nodes.get(idx).location.x;
       teamTwoIdx = idx;
      }
      if (nodes.get(idx).location.x < teamOneDistance) {
       teamOneDistance = nodes.get(idx).location.x;
       teamOneIdx = idx;
      }
     }
     
     Region t1Clearing = new Region("team_one_clearing",9);
     Region t2Clearing = new Region("team_two_clearing",9);
     
     t1Clearing.mimicCircle(nodes.get(teamOneIdx).location.x,nodes.get(teamOneIdx).location.y,1000,12);
     t2Clearing.mimicCircle(nodes.get(teamTwoIdx).location.x,nodes.get(teamTwoIdx).location.y,1000,12);
     
     regionLayer.regions.add(t1Clearing);
     regionLayer.regions.add(t2Clearing);

     nodes.get(teamOneIdx).isClearing = false;
     nodes.get(teamOneIdx).clearingSize = 0;
     nodes.get(teamTwoIdx).isClearing = false;
     nodes.get(teamTwoIdx).clearingSize = 0;


    }

 /**
  * Adds polygon bounding shapes to all the obstacles
  */

 public void addObstacleBoundingBoxes() {
     for (int idx = 0; idx < obstacles.size(); idx++) {
      Polygon creation = new Polygon();

      int numVertices = 8;
      int radius = obstacles.get(idx).radius;

   creation.xpoints = new int[numVertices];
   creation.ypoints = new int[numVertices];
   creation.npoints = numVertices;
   double tempAngle;

   for (int idx2 = 0; idx2 < numVertices; idx2++) {
    tempAngle = 2*Math.PI*idx2/numVertices;
    creation.xpoints[idx2] = (int) (radius*Math.cos(tempAngle)) + obstacles.get(idx).location.x;
    //System.out.println(creation.xpoints[idx2]);
    creation.ypoints[idx2] = (int) (radius*Math.sin(tempAngle)) +  obstacles.get(idx).location.y;
    //System.out.println(creation.ypoints[idx2]);
   }

   obstacles.get(idx).boundingBox = creation;
  }
 }

 /**
  * Tethers all the generated nodes to create paths for road generation
  * note: outdated
  */
    
    public void tetherAllNodes() {
     int numOfNodes = nodes.size();
     
     int[] xDeltas = new int[numOfNodes];
     int[] yDeltas = new int[numOfNodes];
     int tempDeltaX, tempDeltaY;
     int negativeBest;
     int positiveBest;
     int positiveBestIdx = 0;
     int negativeBestIdx = 0;     
     for (int idx = 0; idx < numOfNodes; idx++) {
      positiveBest = 999;
      negativeBest = 999;
      for (int idx2 = 0; idx2 < numOfNodes; idx2++) {
       xDeltas[idx2] = nodes.get(idx).location.x - nodes.get(idx2).location.x;
       yDeltas[idx2] = nodes.get(idx).location.y - nodes.get(idx2).location.y;
       
       tempDeltaX = xDeltas[idx2];
       tempDeltaY = yDeltas[idx2];
       
       
       if (tempDeltaX > 0) {
        if (tempDeltaX < positiveBest) {
         positiveBest = tempDeltaX;
         positiveBestIdx = idx2;         
        }
       } else {
        if (tempDeltaX > negativeBest) {
         negativeBest = tempDeltaX;
         negativeBestIdx = idx2;         
        }
       }
       
       if (tempDeltaY > 0) {
        if (tempDeltaY < positiveBest) {
         positiveBest = tempDeltaY;
         positiveBestIdx = idx2;         
        }
       } else {
        if (tempDeltaY > negativeBest) {
         negativeBest = tempDeltaY;
         negativeBestIdx = idx2;         
        }
       }
       
       if (idx == idx2 + 1) {
        idx2++;
       }
       
      }
      
      nodes.get(idx).connections.add(nodes.get(positiveBestIdx).location);
      nodes.get(idx).connections.add(nodes.get(negativeBestIdx).location);
      
      
     }   
     
     
    }

 /**
  * Tethers all the generated nodes to create paths for road generation
  * NOTE: most recent version
  */



    public void tetherAllNodes2() {
     int quad1Best,quad2Best,quad3Best,quad4Best;
     int q1Idx,q2Idx,q3Idx,q4Idx;
     
     int tempDeltaX, tempDeltaY;
     
     int numOfNodes = nodes.size();
     
     for (int sourceIdx = 0; sourceIdx < numOfNodes; sourceIdx++) {
      quad1Best = 9999;
      quad2Best = 9999;
      quad3Best = 9999;
      quad4Best = 9999;
      q1Idx = 0;
      q2Idx = 0;
      q3Idx = 0;
      q4Idx = 0;
      
      for (int targetIdx = 0; targetIdx < numOfNodes; targetIdx++) {
       tempDeltaX = nodes.get(sourceIdx).location.x - nodes.get(targetIdx).location.x;
       tempDeltaY = nodes.get(sourceIdx).location.y - nodes.get(targetIdx).location.y;
       
       int tempDeltaSum = Math.abs(tempDeltaX) + Math.abs(tempDeltaY);
       
       if (tempDeltaX >= 0 && tempDeltaY >= 0) {
        if (tempDeltaSum < quad2Best) {
         quad2Best = tempDeltaSum;
         q2Idx = targetIdx;
        }
       } else if (tempDeltaX >= 0 && tempDeltaY <= 0) {
        if (tempDeltaSum < quad3Best) {
         quad3Best = tempDeltaSum;
         q3Idx = targetIdx;
        }
       } else if (tempDeltaX <= 0 && tempDeltaY <= 0) {
        if (tempDeltaSum < quad4Best) {
         quad4Best = tempDeltaSum;
         q4Idx = targetIdx;
        }
       } else if (tempDeltaX <= 0 && tempDeltaY >= 0) {
        if (tempDeltaSum < quad1Best) {
         quad1Best = tempDeltaSum;
         q1Idx = targetIdx;
        }
       }
      }
      
      ArrayList newConnections = new ArrayList<Point>(0);      
      
      if (quad1Best < 150*25) {
       newConnections.add(nodes.get(q1Idx).location);
      }
      if (quad2Best < 150*25) {
       newConnections.add(nodes.get(q2Idx).location);
      }
      if (quad3Best < 150*25) {
       newConnections.add(nodes.get(q3Idx).location);
      }
      if (quad4Best < 150*25) {
       newConnections.add(nodes.get(q4Idx).location);
      }
      
      
      nodes.get(sourceIdx).connections = newConnections;

     }
     
    }

 /**
  * Calculates a boolean based on a probability
  *
  * @param chance how large the integer probability out of 1000 is
  * @return boolean whether the roll succeeded or not
  */

 private boolean randRoll(int chance) {
      if (Math.random()*1000 < chance) {
        return true;
      }
      
      return false;
    }

 /**
  * Calculates a boolean based on a probability
  *
  * @param chance how large the double probability out of 1 is
  * @return boolean whether the roll succeeded or not
  */

 private boolean roll (double chance) {
      if (Math.random() < chance) {
        return true;
      }
      
      return false;
    }

 /**
  *
  * Generates the initial nodes around which the rest of the map is generated
  *
  * NOTE: outdated
  *
  * @param numOfNodes the number  of nodes that should be generated
  * @param targetRadius what the optimal radius of node generation is
  * @param offsetRange what the maximum range of node offset is
  * @param offsetPacking how much the offset is packed by
  */

 public void generateMap(int numOfNodes, int targetRadius, int offsetRange, int offsetPacking) {
      for (int i = 0; i < numOfNodes; i ++) {
        double angle = Math.random()*2*Math.PI;
        int distance = targetRadius;
        int offset = 0;
        
        if (offsetPacking != 0) {
          for (int j = 0; j <= offsetPacking; j++) {
            int randNum = (int) (Math.random()*(offsetRange) - offsetRange/2);
            offset += randNum;
          }
          
          offset = (int)(offset/offsetPacking);
          
          distance += offset;
        }
        int pointX = (int) (distance*Math.cos(angle));
        int pointY = (int) (distance*Math.sin(angle));
        
        nodes.add(new RoadNode(new Point(pointY,pointX)));        
      }
    }

 /**
  *
  * Generates the initial nodes around which the rest of the map is generated,
  * based on a modified normal distribution
  *
  * NOTE: most recent version
  *
  * @param numOfNodes the number  of nodes that should be generated
  * @param targetRadius what the optimal radius of node generation is
  * @param offsetFactor what the maximum range of node offset is
  * @param stDev how much the offset is packed by
  */
    
    public void generateMap2(int numOfNodes, double targetRadius, double offsetFactor, double stDev) {
      for (int i = 0; i < numOfNodes; i++) {
        //double angle = Math.random()*2*Math.PI;
        double angle = (2*Math.PI/numOfNodes)*i;
        double distance = targetRadius;
        double offset = 0;
        
        boolean exit = false;
        
        do {
        
          offset = (int) (Math.random()*(offsetFactor) - offsetFactor/2);
          //System.out.println(offset);
          
          double inclusionChance = ((1/Math.sqrt(2*Math.PI*stDev))*
            Math.pow(Math.E,(-(Math.pow((offset/offsetFactor),2))/(2*Math.pow(stDev,2)))));
          
//          double coefficent = 1/(Math.sqrt(2*Math.PI*stDev));
//          double numerator = Math.pow(offset/offsetFactor,2);
//          //System.out.println(numerator);
//          double denominator = 2*Math.pow(stDev,2);
//          double exponent = -(numerator/denominator);
          
          //inclusionChance = coefficent*Math.pow(Math.E,exponent);
          
          //System.out.println(inclusionChance);
          
            if (roll(inclusionChance)) {
              exit = true;
            }
            
        } while (!exit);
        
        distance += offset;
        
        int pointX = (int) (distance*Math.cos(angle));
        int pointY = (int) (distance*Math.sin(angle));
        
        nodes.add(new RoadNode(new Point(pointY,pointX)));        
      }
            
    }

 /**
  * Purges unnecessary connections created during node tethering
  */

 public void purgeRedundancies() {
     for (int idx = 0; idx < nodes.size(); idx++) {
      RoadNode activeNode = nodes.get(idx);
      for (int cIdx = 0; cIdx < activeNode.connections.size(); cIdx++) {

        activeNode.connections.remove(cIdx);

      }
     }
    }

 /**
  * Using additional classes, inserts crevice regions into the region layer retroactively
  *
  * @param creviceNum how many crevices should be generates
  */

 public void generateCrevices(int creviceNum) {
     CreviceGenerator creviceEngine = new CreviceGenerator();
     Point source;
     Region creation = null;
     
     for (int iter = 0; iter < creviceNum; iter++) {
      source = new Point((int) (Math.random()*5000 - 2500),(int) (Math.random()*5000 - 2500));
      
   do {
    creviceEngine.generateFullCrevice(source,1250,1.0,4,true,1000);
       creation = new Region("crevice",4);  
       creation.uploadPolygon(creviceEngine.getPolygon());
   } while (!regionLayer.regions.get(SWAMP_REGION_IDX).contains(creation));
            
      regionLayer.regions.add(creation);
     }
    }

 /**
  * Returns the raw data of the nodes
  *
  * @return the ArrayList of nodes that are contained within the MapGen instance
  */
 public ArrayList<RoadNode> getNodes() {
      return this.nodes;
    }


 /**
  * Generates a barrier of rocks around the map
  */

 public void generateBarrier() {
  ArrayList<Obstacle> tempList = new ArrayList<Obstacle>(0);
  Obstacle creation1 = null;
  Obstacle creation2 = null;
  Obstacle creation3 = null;

  for (int idx = 0; idx < regionLayer.regions.get(MAP_REGION_IDX).npoints; idx++) {
   creation1 = new Obstacle();
   creation2 = new Obstacle();

   creation1.type = "ROCK";
   creation2.type = "ROCK";

   creation1.location = new Point(regionLayer.regions.get(MAP_REGION_IDX).xpoints[idx],
     regionLayer.regions.get(MAP_REGION_IDX).ypoints[idx]);
   creation2.location = new Point((int) (regionLayer.regions.get(MAP_REGION_IDX).xpoints[idx]*1.05),
     (int)  (regionLayer.regions.get(MAP_REGION_IDX).ypoints[idx]*1.1));
   creation1.radius = 1000;
   creation2.radius = 1000;

//   if (idx != 0) {
//    creation3 = new Obstacle();
//    creation3.type = "ROCK";
//
//    creation3.location = averagePoints(creation2.location,tempList.get(tempList.size() - 1).location);
//
//   }

   //tempList.add(creation1);
//   if (idx != 0) {
//    creation3.radius = 1000;
//    tempList.add(creation3);
//   }
   tempList.add(creation2);


  }

  obstacles.addAll(tempList);
 }

 /**
  * This method averages the x and y values between two points to return a point in the middle
  *
  * @param point1 first point to be averaged
  * @param point2 second point to be averaged
  * @return the resultant point
  */

 public Point averagePoints(Point point1, Point point2) {
  int finalX = (int) ((point1.x + point2.x)/2);
  int finalY = (int) ((point1.y + point2.y)/2);

  return new Point(finalX,finalY);
 }
}


import java.awt.Point;
import java.util.ArrayList;


public class MapGen {

 //private final static int ROAD_WIDTH = 100;
 public final static int MAP_REGION_IDX = 0;
 public final static int SWAMP_REGION_IDX = 1;
 
 public int mapSize;
 public int loopRadius;
 public int swampRadius;
 public double ellipticalAdjust;
 
    public ArrayList<RoadNode> nodes; 
    public ArrayList<Obstacle> obstacles;
    public RegionLayer regionLayer; 
    public boolean testingState;
    
    
    
    MapGen(int mSize, int lRad, double eAdjust) {
      this.nodes = new ArrayList<RoadNode>(0);
      this.obstacles = new ArrayList<Obstacle>(0);
      this.testingState = false;
      
      this.ellipticalAdjust = eAdjust;
    }
    
    public void selfInitialize() {
     int loopRadiusSize = 10;
     int nodeGenRange = 3750;
     double nodeGenStDev = 0.5;
     
     
     this.generateMap2(40,loopRadiusSize,nodeGenRange,nodeGenStDev);
     this.tetherAllNodes2(); 
     this.makeNodesElliptical();
     this.generateRegions();
     this.generateCrevices(2);
     //this.insertArtifactClearing();
     this.smokeTrees(7500, 1000, 0, false);    
     this.smokeRocks(7500, 100, true);
     this.makeObstaclesElliptical();
     this.genClearingByNum(8, 500);       
     this.purgeRedundanices();
    }
        
    
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
    
    public void generateRegions() {
     this.regionLayer = new RegionLayer();
     
     Region swamp = new Region("swamp",1);
     Region map = new Region("map", 0); 
       
     map.mimicEllipse(0, 0, 7500, 1.75, 100);
     swamp.mimicEllipse(0, 0, 3000, 1.75, 20);         
     
     
     this.regionLayer.regions.add(MAP_REGION_IDX, map);
     this.regionLayer.regions.add(SWAMP_REGION_IDX,swamp);
     
     
     for (int idx = 0; idx < nodes.size(); idx++) {
      RoadNode activeNode = nodes.get(idx);
      
      for (int connectionIdx = 0; connectionIdx < activeNode.connections.size(); connectionIdx++) {
       
       Region creation = new Region("road",3);
       creation.adoptRoadShape(activeNode.location, 
         activeNode.connections.get(connectionIdx), 100);    
       this.regionLayer.regions.add(creation);
       
      }
     }    
    }
    
    public void makeElliptical(double eAdjust) {
     for (int i = 0; i < nodes.size(); i++) {
      nodes.get(i).location.x = (int) (nodes.get(i).location.x*eAdjust);
     }
     for (int i = 0; i < obstacles.size(); i++) {
      obstacles.get(i).location.x = (int) (obstacles.get(i).location.x*eAdjust);
     }
    }
    
    public void makeNodesElliptical() {
     for (int i = 0; i < nodes.size(); i++) {
      nodes.get(i).location.x = (int) (nodes.get(i).location.x*ellipticalAdjust);
     }
    }
    
    public void makeObstaclesElliptical() {
     for (int i = 0; i < obstacles.size(); i++) {
      obstacles.get(i).location.x = (int) (obstacles.get(i).location.x*ellipticalAdjust);
     }
    }
    
    
    public void genClearing(double clearingChance, int clearingSize) {
     for (int i = 0; i < nodes.size(); i++) {
      if (roll(clearingChance)) {
       nodes.get(i).isClearing = true;
       nodes.get(i).clearingSize = clearingSize;
      }
     }
    }
    
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
    
    public void smokeTrees(int mapRadius, int numOfTrees, int intensity, boolean randomization) {
     for (int i = 0; i < numOfTrees;i++) {
      double angle;
      double radius;
      
      Obstacle temp = new Obstacle();
      temp.type = "TREE";
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
              
       for (int idx = obstacles.size() - 1; idx > -1; idx--) {
        tempDeltaX = tempX - obstacles.get(idx).location.x;
        tempDeltaY = tempY - obstacles.get(idx).location.y;
        
        if ((Math.pow(tempDeltaX,2) + 
          Math.pow(tempDeltaY,2)) <= 2500) {
           System.out.println((Math.pow(tempDeltaX,2) + 
                Math.pow(tempDeltaY,2)));
           exit = false;
        }
       }
       
       
      } while (!exit);
      
      temp.location.x = tempX;
      temp.location.y = tempY;
      this.obstacles.add(temp);
     }
    }
    
    public void smokeRocks(int mapRadius, int numOfRocks, boolean randomization) {
     for (int i = 0; i < numOfRocks;i++) {
      double angle;
      double radius;
      
      int maxRadius = 200;
      
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
           
       if (regionLayer.checkCoordinate(tempX, tempY) == ("map")) {
        for (int idx = obstacles.size() - 1; idx > -1; idx--) {
         tempDeltaX = tempX - obstacles.get(idx).location.x;
         tempDeltaY = tempY - obstacles.get(idx).location.y;        
         
         if ((Math.pow(tempDeltaX,2) + 
           Math.pow(tempDeltaY,2)) <= Math.pow(maxRadius,2)) {
            System.out.println((Math.pow(tempDeltaX,2) + 
                 Math.pow(tempDeltaY,2)));
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
       double percentRand = 0.4;
       temp.radius = (int) (Math.random()*100*(percentRand) + 100*(1.0 - percentRand));
      }
      
      this.obstacles.add(temp);
     }
    }
    
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

    
    private boolean randRoll(int chance) {
      if (Math.random()*1000 < chance) {
        return true;
      }
      
      return false;
    }
    
    private boolean roll (double chance) {
      if (Math.random() < chance) {
        return true;
      }
      
      return false;
    }
    
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
        
    public void purgeRedundanices() {
     for (int idx = 0; idx < nodes.size(); idx++) {
      RoadNode activeNode = nodes.get(idx);
      for (int cIdx = 0; cIdx < activeNode.connections.size(); cIdx++) {
       if (activeNode.location == activeNode.connections.get(cIdx)) {
        activeNode.connections.remove(cIdx);
       }
      }
     }
    }
    
    public void generateCrevices(int creviceNum) {
     CreviceGenerator creviceEngine = new CreviceGenerator();
     Point source;
     Region creation = null;
     
     for (int iter = 0; iter < creviceNum; iter++) {
      source = new Point((int) (Math.random()*6000 - 3000),(int) (Math.random()*6000 - 3000)); 
      
   do {
    creviceEngine.generateFullCrevice(source,750,1.0,4,true,400);
       creation = new Region("crevice",4);  
       creation.uploadPolygon(creviceEngine.getPolygon());
   } while (!regionLayer.regions.get(SWAMP_REGION_IDX).contains(creation));
            
      regionLayer.regions.add(creation);
     }
    }
    
    public ArrayList<RoadNode> getNodes() {
      return this.nodes;
    }             

}

package server;

import java.awt.Point;
import java.util.ArrayList;

/**
 * RoadNode.java
 *
 * A class for generating and storing the data of the road network on the map
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-03-25
 * @version 1.0
 *
 */

class RoadNode {
    public Point location;
    public ArrayList<Point> connections;
    public boolean isClearing;
    public int clearingSize;

    /**
     * Basic constructor that initializes the position through two integers
     *
     * @param xLoc the x-location of the node
     * @param yLoc the y-location of the node
     */

    RoadNode(int xLoc, int yLoc) {
      location = new Point(xLoc,yLoc);
      this.connections = new ArrayList<Point>(0);
    }


    /**
     * Basic constructor that initializes the position through a point object
     *
     * @param point the point location of the node
     */

    RoadNode(Point point) {
      this.location = point;
      this.connections = new ArrayList<Point>(0);
    }

    /**
     * Basic getter for the point location of the node
     *
     * @return the location of the node, as a java.awt.Point
     */
    public Point getPoint() {
      return this.location;
    }
    
  }

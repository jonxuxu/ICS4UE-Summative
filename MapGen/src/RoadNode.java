import java.awt.Point;
import java.util.ArrayList;

class RoadNode {
    public Point location;
    public ArrayList<Point> connections;
    public boolean isClearing;
    public int clearingSize;
    
    RoadNode(int xLoc, int yLoc) {
      location = new Point(xLoc,yLoc);
      this.connections = new ArrayList<Point>(0);
    }
    
    RoadNode(Point point) {
      this.location = point;
      this.connections = new ArrayList<Point>(0);
    }
    
    public Point getPoint() {
      return this.location;
    }
    
  }

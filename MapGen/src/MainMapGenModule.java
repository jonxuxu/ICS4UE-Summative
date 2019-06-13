/*import javax.swing.JPanel;

import javafx.scene.shape.Polygon;

import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Point;

import java.util.ArrayList;

class MainMapGenModule extends JFrame{
 private Disp display;
 private MapGen gen;
 
 private int loopRadiusSize = 5000;
 private double ellipticalAdjust = 1.75;
 private int nodeGenRange = 3750;
 private double nodeGenStDev = 0.5;
 
  
  MainMapGenModule() {
    
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    
    String config = "";
    
    gen = new MapGen(7500,5000,ellipticalAdjust);
    
    if (config == "test") {		
		gen.configueScenario1();
		gen.generateRegions();
    } else  {        
	    gen.generateMap2(40,loopRadiusSize,nodeGenRange,nodeGenStDev);
	    gen.tetherAllNodes2();	
	    gen.makeNodesElliptical();
	    gen.generateRegions();
	    gen.smokeTrees(7500, 1000, 0, false);    
	    gen.smokeRocks(7500, 100, true);
	    gen.makeObstaclesElliptical();
	    gen.genClearingByNum(8, 500);  	    
	    gen.purgeRedundancies();
	    gen.generateCrevices(2);
    }
    display = new Disp();
    this.add(display);
    display.repaint();
    this.setVisible(true);
  }
  
  
  
   class Disp extends JPanel {
    
    private void drawOvalCustom(int radius, Graphics g) {
      g.drawOval(-radius,-radius,radius*2,radius*2);
    }
    
    private void drawOvalCustom(int radius, int xOffset, int yOffset, Graphics g) {
    	g.drawOval(xOffset - radius, yOffset - radius, radius*2, radius*2);
    }
    
    private void drawOvalCustom(int radius, double eAdjust, Graphics g) {
    	g.drawOval((int) (-radius*eAdjust),-radius,(int) (radius*2*eAdjust),radius*2);
    }
    
    private void drawLineCustom(Point start, Point end, Graphics g) {
    	g.drawLine(start.x,start.y,end.x,end.y);
    }
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      this.setCenter(g);
      
      Graphics2D g2 = (Graphics2D) (g);
     
      if (!gen.testingState) {
	      g.setColor(Color.BLUE);
	      this.drawOvalCustom(loopRadiusSize,ellipticalAdjust, g);
	      g.setColor(Color.GREEN);
	      this.drawOvalCustom(loopRadiusSize - nodeGenRange/2,ellipticalAdjust,g);
	      this.drawOvalCustom(loopRadiusSize + nodeGenRange/2,ellipticalAdjust,g);
	      g.setColor(Color.black);
	      this.drawOvalCustom(7500,ellipticalAdjust, g);      
      }           
      g.drawRect(-15000, -10000, 30000, 20000);      
      
      g.setColor(Color.RED);
         
      if (gen.regionLayer != null) {
	      for (int idx = 0; idx < gen.regionLayer.regions.size(); idx++) {	   
	    	  if (gen.regionLayer.regions.get(idx).regionType == "crevice") {
	    		  g.setColor(Color.GRAY);	    		  
	    	  } else if (gen.regionLayer.regions.get(idx).regionType == "swamp") {
	    		  g.setColor(Color.CYAN);
	    	  } else {
	    		  g.setColor(Color.RED);
	    	  }
	    	  g.drawPolygon(gen.regionLayer.regions.get(idx));    	  
	      }
      }
      
      for (int i = 0; i < gen.nodes.size(); i++) {
//        g.fillOval((int)gen.nodes.get(i).getPoint().getX() - 5,
//        		(int)gen.nodes.get(i).getPoint().getY() - 5,50,50);
    	  if (gen.nodes.get(i).isClearing) {
    		  this.drawOvalCustom(gen.nodes.get(i).clearingSize, gen.nodes.get(i).location.x, 
    				  gen.nodes.get(i).location.y, g);  
    	  } else {
    		  this.drawOvalCustom(50, gen.nodes.get(i).location.x, gen.nodes.get(i).location.y, g);
    	  }
        for (int j = 0; j < gen.nodes.get(i).connections.size(); j++) {
        	this.drawLineCustom(gen.nodes.get(i).location,gen.nodes.get(i).connections.get(j),g);
        }
        
      }
      
      for (int i = 0; i < gen.obstacles.size(); i++) {
    	  if (gen.obstacles.get(i).type == "TREE") {
    		  g.setColor(Color.green);
    	  } else if (gen.obstacles.get(i).type == "ROCK") {
    		  g.setColor(Color.BLACK);
    	  }
    	  if (gen.obstacles.get(i).radius != 0) {
    		  g.fillOval(gen.obstacles.get(i).location.x, gen.obstacles.get(i).location.y, 
    				  gen.obstacles.get(i).radius, gen.obstacles.get(i).radius);
    	  } else {
    		  g.fillOval(gen.obstacles.get(i).location.x, gen.obstacles.get(i).location.y, 50, 50);
    	  }
      }
    }

	private void setCenter(Graphics g) {
		g.translate( (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
		
	}              
  }      
}*/
package server;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Point;
import java.awt.Polygon;

import java.util.ArrayList;

/**
 *
 * CreviceGenerator.java
 *
 * A special class that handles the complicated mathematics behind crevice generation
 *
 * @author Artem Sotnikov
 * @since 2019-06-05
 * @version 1.3
 */

public class CreviceGenerator {
	public int[] xpoints;
	public int[] ypoints;
	ArrayList<Point> sourcePoints;

	/**
	 * A implementation to be tested using a seperate JFrame
	 */
	
	public void test() {
		JFrame frame = new JFrame();
		
		//this.generateCreviceSpine();
		this.generateFullCrevice(new Point(500,500), 100 , (3.0/3.0), 3,true,50);
		
		CustomPanel panel = new CustomPanel();
		
		panel.setPreferredSize(new Dimension(1000,1000));
		
		frame.setSize(new Dimension(1000,1000));
		frame.add(panel);		
		frame.setVisible(true);
		panel.repaint();
	}

	/**
	 * An intermediary test method to generate the basic direction and turns of the crevice
	 */
	
	public void generateCreviceSpine() {
		ArrayList<Point> sourcePoints = new ArrayList<Point>(0);
		ArrayList<Double> angles = new ArrayList<Double>(0);
		sourcePoints.add(0,new Point(500,500));
		
		double sourceAngle = Math.random()*2*Math.PI;
		angles.add(new Double(sourceAngle));
		int jumpDist = 100;
		Point source2 = new Point((int) (500 + jumpDist*Math.cos(sourceAngle)),
				(int) (500 + jumpDist*Math.sin(sourceAngle)));
		sourcePoints.add(source2);
		
		double modAngle = sourceAngle;
		double modifier;
		double premod1, premod2;
		Point creation;
		
		int numJumps = 3;
		
		for (int iter = 0; iter < numJumps; iter++) {
			premod1 = Math.random()*(0.33333)*Math.PI;
			premod2 = Math.PI*(0.16666);
			modifier = premod1 - premod2;
			
			modAngle += modifier;
			angles.add(new Double(modAngle));
			
			creation = new Point();
			creation.x = (int) (sourcePoints.get(sourcePoints.size() - 1).x + 
					jumpDist*Math.cos(modAngle));
			creation.y = (int) (sourcePoints.get(sourcePoints.size() - 1).y + 
					jumpDist*Math.sin(modAngle));
						
			sourcePoints.add(creation);			
		}
		
		xpoints = new int[sourcePoints.size()];
		ypoints = new int[sourcePoints.size()];
		
		for (int idx = 0; idx < sourcePoints.size(); idx++) {
			xpoints[idx] = sourcePoints.get(idx).x;
			ypoints[idx] = sourcePoints.get(idx).y;
		}
		
		
	}
	/**
	 * A parameter-based method that generates a full crevice inside the CreviceGenerator instance
	 *
	 * @param source the point from which to begin generation
	 * @param jumpDist how long each leg of the crevice is
	 * @param angleVar how much the crevice can bend by
	 * @param numBreaks how many segments the crevices have
	 * @param randomizedOffsets whther randomizing the side-to-side offsets is enabled
	 * @param offsetRange how much the offsets are by.
	 */
	
	
	public void generateFullCrevice(Point source, int jumpDist, double angleVar, int numBreaks,
			boolean randomizedOffsets, int offsetRange) {
		sourcePoints = new ArrayList<Point>(0);
		ArrayList<Double> angles = new ArrayList<Double>(0);
		sourcePoints.add(source);
		
		double sourceAngle = Math.random()*2*Math.PI;
		angles.add(new Double(sourceAngle));
		Point source2 = new Point((int) (source.x + jumpDist*Math.cos(sourceAngle)),
				(int) (source.y + jumpDist*Math.sin(sourceAngle)));
		sourcePoints.add(source2);
		
		double modAngle = sourceAngle;
		double modifier;
		double premod1, premod2;
		Point creation;
		
		int numJumps = numBreaks;
		
		for (int iter = 0; iter < numJumps; iter++) {
			premod1 = Math.random()*(angleVar)*Math.PI;
			premod2 = Math.PI*(angleVar/2);
			modifier = premod1 - premod2;
			
			modAngle += modifier;
			angles.add(new Double(modAngle));
			
			creation = new Point();
			creation.x = (int) (sourcePoints.get(sourcePoints.size() - 1).x + 
					jumpDist*Math.cos(modAngle));
			creation.y = (int) (sourcePoints.get(sourcePoints.size() - 1).y + 
					jumpDist*Math.sin(modAngle));
						
			sourcePoints.add(creation);			
		}
		
		int coreSize = sourcePoints.size();
		
		xpoints = new int[coreSize*2 - 2];
		ypoints = new int[coreSize*2 - 2];
		
		xpoints[0] = source.x;
		ypoints[0] = source.y;
		
		xpoints[coreSize - 1] = sourcePoints.get(coreSize - 1).x;
		ypoints[coreSize - 1] = sourcePoints.get(coreSize - 1).y;
			
		double deltaAngle, inBetweenAngle, targetAngle;
		int offset1 = 0;
		int offset2 = 0;
		
		for (int i = 1; i < coreSize - 1; i++) {
			deltaAngle = angles.get(i-1) - angles.get(i);
			inBetweenAngle = Math.PI - deltaAngle;
			targetAngle = angles.get(i) - (inBetweenAngle/2);
			
			if (!randomizedOffsets) {
				offset1 = 50;
				offset2 = 50;
			} else {
				offset1 = (int) ((Math.random()*offsetRange)*0.8 + offsetRange*0.2);
				offset2 = (int) (Math.random()*offsetRange);
			}
			xpoints[i] = (int) (sourcePoints.get(i).x + offset1*Math.cos(targetAngle));
			xpoints[xpoints.length - i] = (int) (sourcePoints.get(i).x + offset2*Math.cos(targetAngle + Math.PI)); 
			
			ypoints[i] = (int) (sourcePoints.get(i).y + offset1*Math.sin(targetAngle));
			ypoints[ypoints.length - i] = (int) (sourcePoints.get(i).y + offset2*Math.sin(targetAngle + Math.PI));
			
		}
	}

	/**
	 * Returns the generated crevice as a polygon
	 *
	 * @return the polygon representation of the crevice
	 */

	public Polygon getPolygon() {		
		return new Polygon(this.xpoints,this.ypoints,this.xpoints.length);
	}

	/**
	 * A testing method to run trial crevice generations
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		CreviceGenerator tester = new CreviceGenerator();
		tester.test();
	}

	/**
	 * A private subclass that handles he drawing for the JFrame testing implementation
	 *
	 * @author Artem Sotnikov
	 * @since 2019-06-05
	 * @version 1.0
	 */
	
	private class CustomPanel extends JPanel {
		/**
		 * The paintComponent that actually performs the drawing for the testing implementation
		 *
		 * @param g, the graphics to be used for painting
		 */

		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			this.setDoubleBuffered(true);
			
			g.drawPolygon(xpoints,ypoints,xpoints.length);	
			
			if (sourcePoints != null) {
				for (int idx = 0; idx < sourcePoints.size(); idx++) {
					g.drawOval(sourcePoints.get(idx).x - 2,sourcePoints.get(idx).y - 2,4,4);
				}
			}
			
			
		}
		
	}
}

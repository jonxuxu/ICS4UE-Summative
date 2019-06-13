package server;

import java.awt.Point;

import java.awt.Polygon;

/**
 * Region.java
 *
 * This class uses the basic polygon framework to store all the info about an in-game region,
 * with some built in methods to interact with the map generation classes
 *
 * @author Artem Sotnikov, Will Jeong
 * @since 2019-06-02
 * @version 2.7
 */


// Look up documentation if you want to work with this (java.awt.Polygon)

public class Region extends Polygon {
	public String regionType;
	public int regionPriority;

	/**
	 *
	 * The constructor that initializes basic values
	 *
	 * @param type the representation of the trye of region
	 * @param priority what is the priority of the region when compared to other regions
	 */

	Region(String type, int priority) {
		this.regionType = type;
		this.regionPriority = priority;
	}

	/**
	 * Checks if the target polygon is entirely contained within the region
	 *
	 * @param target the polygon to be checked
	 * @return the boolean result of the check
	 */
	public boolean contains(Polygon target) {
		boolean ret = true;
		for (int idx = target.npoints - 1; idx >= 0 ; idx--) {
			if (!this.contains(target.xpoints[idx],target.ypoints[idx])) {
				ret = false; 
				idx = -1;
			}
		}
		
		return ret;
		
	}

	/**
	 * This forces the region to adopt a equilateral shape to behave like a circle,
	 * given regular circle parameters and a allowable vertex number
	 *
	 * @param centerX the x-value of the center of the circle to be mimicked
	 * @param centerY the y-value of the center of the circle to be mimicked
	 * @param radius the radius of the center of the circle to be mimicked
	 * @param numVertices the amount of vertices allowed to be generated to achieve a circle-like shape
	 */
	public void mimicCircle(int centerX, int centerY, int radius, int numVertices) {
		this.xpoints = new int[numVertices];
		this.ypoints = new int[numVertices];
		this.npoints = numVertices;
		double tempAngle;
		
		for (int idx = 0; idx < numVertices; idx++) {
			tempAngle = 2*Math.PI*idx/numVertices;
			this.xpoints[idx] = (int) (radius*Math.cos(tempAngle)) + centerX;
			this.ypoints[idx] = (int) (radius*Math.sin(tempAngle)) + centerY;
		}
	}

	/**
	 * This forces the region to adopt a shape to behave like a ellipse,
	 * given regular ellipse parameters and a allowable vertex number
	 *
	 * @param centerX the x-value of the center of the ellipse to be mimicked
	 * @param centerY the y-value of the center of the ellipse to be mimicked
	 * @param radius the radius of the center of the ellipse to be mimicked
	 * @param ellipticalAdjust the amount by which the horizontal radius is multiplied to produce the ellipse
	 * @param numVertices the amount of vertices allowed to be generated to achieve a smooth shape
	 */

	public void mimicEllipse(int centerX, int centerY, int radius, double ellipticalAdjust, int numVertices) {
		this.xpoints = new int[numVertices];
		this.ypoints = new int[numVertices];
		this.npoints = numVertices;
		double tempAngle;
		
		for (int idx = 0; idx < numVertices; idx++) {
			tempAngle = 2*Math.PI*idx/numVertices;
			this.xpoints[idx] = (int) ((radius*Math.cos(tempAngle))*ellipticalAdjust);
			this.ypoints[idx] = (int) (radius*Math.sin(tempAngle));
		}
	}

	/**
	 * Forces the region to adopt the shape of a thick rectangle between two points
	 *
	 * @param source the point at which the center of the road starts
	 * @param target the point at which the center of the road ends
	 * @param width the width of the rectangle drawn between source and target
	 */


	public void adoptRoadShape(Point source, Point target, int width) {
		
		double angle;
		
		if (target.x - source.x == 0) {
			if (target.y - source.y > 0) {
				angle = Math.PI/2;
			} else {
				angle = -Math.PI/2;
			}
		} else {
			angle = Math.atan((double) (target.y - source.y)/(target.x - source.x));
		}
		
		double upAngle = angle + Math.PI/2;
		double downAngle = angle - Math.PI/2;
		
		this.npoints = 4;
		
		xpoints[0] = (int) (source.x + width*Math.cos(upAngle));
		xpoints[1] = (int) (source.x + width*Math.cos(downAngle));
		xpoints[3] = (int) (target.x + width*Math.cos(upAngle));
		xpoints[2] = (int) (target.x + width*Math.cos(downAngle));
		
		ypoints[0] = (int) (source.y + width*Math.sin(upAngle));
		ypoints[1] = (int) (source.y + width*Math.sin(downAngle));
		ypoints[3] = (int) (target.y + width*Math.sin(upAngle));
		ypoints[2] = (int) (target.y + width*Math.sin(downAngle));
		
	}

	/**
	 *
	 * This allows for the direct upload of vertex x and y values for the region
	 *
	 * @param xValues the array of x-values of the vertices
	 * @param yValues the array of y-values of the vertices
	 * @return boolean whether the upload was successful or not
	 */
	public boolean uploadRawVertexData(int[] xValues, int[] yValues) {
		if (xValues.length != yValues.length) {
			System.out.println("Vertex Upload ERROR!");
			return false;
		}
		
		this.xpoints = xValues;
		this.ypoints = yValues;
				
		return true;
	}

	/**
	 * This allows for a polygon to uploaded directly to serve as the basis for the region
	 *
	 * @param input the polygon that the region is to become
	 * @return boolean whether the upload was successful or not
	 */
	
	public boolean uploadPolygon(java.awt.Polygon input) {
		this.xpoints = input.xpoints;
		this.ypoints = input.ypoints;
		this.npoints = input.npoints;
		return true;
	}

	/**
	 * This method calculates the middle of a region based on the first and opposite point
	 *
	 * @return the calculated middle region, as a length-2 array
	 */
	public int[] getMidXy(){
		int[] midXy = {(this.xpoints[0]+this.xpoints[this.npoints/2])/2,(this.ypoints[0]+this.ypoints[this.npoints/2])/2};
		return(midXy);
	}
		
}

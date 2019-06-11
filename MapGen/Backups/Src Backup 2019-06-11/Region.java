import java.awt.Point;

import java.awt.Polygon;
// Look up documentation if you want to work with this (java.awt.Polygon)

public class Region extends Polygon {
	public String regionType;
	public int regionPriority;
	public int assosiatedObjectID;
	
	Region(String type, int priority) {
		this.regionType = type;
		this.regionPriority = priority;
	}
	
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
	
	public void mimicCircle(int centerX, int centerY, int radius, int numVertices) {
		this.xpoints = new int[numVertices];
		this.ypoints = new int[numVertices];
		this.npoints = numVertices;
		double tempAngle;
		
		for (int idx = 0; idx < numVertices; idx++) {
			tempAngle = 2*Math.PI*idx/numVertices;
			this.xpoints[idx] = (int) (radius*Math.cos(tempAngle));
			this.ypoints[idx] = (int) (radius*Math.sin(tempAngle));
		}
	}
	
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
	
	public boolean uploadRawVertexData(int[] xValues, int[] yValues) {
		if (xValues.length != yValues.length) {
			System.out.println("Vertex Upload ERROR!");
			return false;
		}
		
		this.xpoints = xValues;
		this.ypoints = yValues;
				
		return true;
	}
	
	public boolean uploadPolygon(java.awt.Polygon input) {
		this.xpoints = input.xpoints;
		this.ypoints = input.ypoints;
		this.npoints = input.npoints;
		
		return true;
	}
		
}

package server;

import java.awt.Point;

/**
 * Obstacle.java
 *
 * A class containing all the information that individual obstacles possess
 *
 * @author Artem Sotnikov
 * @since 2019-05-28
 * @version 2.0
 */

public class Obstacle {
	public Point location;
	public String type;
	public int radius;

	public java.awt.Polygon boundingBox;
	
}

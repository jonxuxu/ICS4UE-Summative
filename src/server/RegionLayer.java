package server;

import java.util.ArrayList;

/**
 * RegionLayer.java
 *
 * Stores all the regions generated in the MapGen class, containing extra access functionality
 *
 * @author Artem Sotnikov
 * @since 2019-06-01
 * @version 1.1
 */

public class RegionLayer {
	public ArrayList<Region> regions;

	/**
	 * Basic constructor, initializing the arraylist
	 */
	public RegionLayer() {
		this.regions = new ArrayList<Region>(0);
	}

	/**
	 *
	 * returns the list of regions
	 *
	 * @return the ArrayList of regions
	 */
	public ArrayList<Region> getRawData() {
		return this.regions;
	}

	/**
	 *
	 * Takes the target coordinates and returns what region the coordinates are located in
	 *
	 * @param x the x-value to be checked
	 * @param y the y-value to be checked
	 * @return the string value of the highest priority region that contains the checked point
	 */

	public String checkCoordinate(int x, int y) {
		ArrayList<Region> valid = new ArrayList<Region>(0);
		for (int idx = 0; idx < regions.size(); idx++) {
			if (regions.get(idx).contains(x,y)) {
				valid.add(regions.get(idx));
			}
		}
		
		if (valid.size() == 0) {
			return "none";
		} else if (valid.size() == 1) {
			return valid.get(0).regionType;
		} else {			
			int highestPriorityIdx = 0;
			
			for (int idx = 1; idx < valid.size(); idx++) {
				if (valid.get(idx).regionPriority > valid.get(highestPriorityIdx).regionPriority) {
					highestPriorityIdx = idx;
				}
			}
			
			return valid.get(highestPriorityIdx).regionType;
		}
		
	}			
}

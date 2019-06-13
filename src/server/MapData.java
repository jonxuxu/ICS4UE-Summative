package server;

import java.util.ArrayList;

/**
 *
 * A class to store the essential map data to be returned from an instance of MapGen
 *
 * @author Will Jeong, Jonathan Xu, Kamron Zaidi, Artem Sotnikov, Kolby Chong, Bill Liu
 * @since 2019-06-10
 * @version 1.0
 */

public class MapData {
	public RegionLayer rLayer; // The region layer of the map
	ArrayList<Obstacle> obstacles; // The obstacle list of the map
}

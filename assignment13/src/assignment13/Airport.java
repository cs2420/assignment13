package assignment13;

import java.util.HashMap;
/**
 * Class representing an airport
 * 
 * @author Connor Ottenbacher and Doug Garding
 */

public class Airport implements Comparable<Airport>{
	//name of the airport will be the three string representing the city of its location
	protected String name;
	// contains all the flights going out of this airport
	protected HashMap<String, Flight> edges;
	//contains the cost used in Dijkstras algorithm
	protected double cost;
	// marks whether or not Djikstras algorithm has visited this airport
	protected boolean visited;
	// indicates the previous airport, used to reconstruct the path
	protected Airport comingFrom;
	/**
	 * Constructs an airport with a given name, and cost initialized to infinity
	 * 
	 * @param origin
	 */
	public Airport(String origin){
		edges = new HashMap<String, Flight>();
		name = origin;
		cost = Double.POSITIVE_INFINITY;
		visited = false;
	}
	@Override
	public int compareTo(Airport arg0) {
		return (int) (this.cost-arg0.cost);
	}
}

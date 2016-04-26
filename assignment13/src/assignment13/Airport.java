package assignment13;

import java.util.HashMap;

public class Airport implements Comparable<Airport>{
	protected String name;
	protected HashMap<String, Flight> edges;
	protected double cost;
	protected boolean visited;
	protected Airport comingFrom;
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

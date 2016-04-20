package assignment13;

import java.util.LinkedList;

public class Airport {
	protected String name;
	protected LinkedList<Flight> edges;
	public Airport(String origin){
		name = origin;
	}
}

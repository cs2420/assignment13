package assignment13;

import java.util.HashMap;

public class Airport {
	protected String name;
	protected HashMap<String, Flight> edges;
	public Airport(String origin){
		edges = new HashMap<String, Flight>();
		name = origin;
	}
}

package assignment13;

import java.util.LinkedList;

public class Flight {
	protected double COST;
	protected int DELAY, DISTANCE, CANCELED, TIME;
	protected LinkedList<String> CARRIER;
	protected Airport comingFrom;
	protected Airport goingTo;
	protected int duplicates;

	public Flight(double cost, int delay, int distance, int canceled, int time,
			String carrier, Airport comingFrom, Airport goingTo) {
		COST = cost;
		DELAY = delay;
		DISTANCE = distance;
		CANCELED = canceled;
		TIME = time;
		CARRIER.add(carrier);
		this.comingFrom = comingFrom;
		this.goingTo = goingTo;
		duplicates = 1;
	}
}

package assignment13;


import java.util.HashSet;

public class Flight {
	protected double COST, DELAY, DISTANCE, CANCELED, TIME;
	protected HashSet<String> CARRIER;
	protected Airport comingFrom;
	protected Airport goingTo;
	protected int frequency;
	protected String name;
	protected double currentCost;

	public Flight(double cost, double delay, double distance, double canceled, double time,
			String carrier, Airport comingFrom, Airport goingTo) {
		COST = cost;
		DELAY = delay;
		DISTANCE = distance;
		CANCELED = canceled;
		TIME = time;
		CARRIER = new HashSet<String>();
		CARRIER.add(carrier);
		this.comingFrom = comingFrom;
		this.goingTo = goingTo;
		frequency = 1;
		name = comingFrom.name +" to "+ goingTo.name;
		currentCost = 0;
	}
	@Override
	public String toString(){
		return name;
	}
	
}

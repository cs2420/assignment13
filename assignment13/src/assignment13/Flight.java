
package assignment13;
/**
 * Class representing a Flight
 * 
 * @author Connor Ottenbacher and Doug Garding
 */

import java.util.HashSet;

public class Flight {
	//doubles indicating the different types of cost that an airport can have
	protected double COST, DELAY, DISTANCE, CANCELED, TIME;
	//a list of carriers available for this flight
	protected HashSet<String> CARRIER;
	protected Airport comingFrom;
	protected Airport goingTo;
	//the number of times this same flight occurs in the file, used to calculate averge costs
	protected int frequency;
	protected String name;

/**
 * Constructs an airport with the given costs
 * 
 * @param cost
 * @param delay
 * @param distance
 * @param canceled
 * @param time
 * @param carrier
 * @param comingFrom
 * @param goingTo
 */
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
	}
	@Override
	public String toString(){
		return name;
	}
	
}

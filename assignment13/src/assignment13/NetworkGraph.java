/**
 *
 */
package assignment13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * <p>
 * This class represents a graph of flights and airports along with specific
 * data about those flights. It is recommended to create an airport class and a
 * flight class to represent nodes and edges respectively. There are other ways
 * to accomplish this and you are free to explore those.
 * </p>
 *
 * <p>
 * Testing will be done with different criteria for the "best" path so be sure
 * to keep all information from the given file. Also, before implementing this
 * class (or any other) draw a diagram of how each class will work in relation
 * to the others. Creating such a diagram will help avoid headache and confusion
 * later.
 * </p>
 *
 * <p>
 * Be aware also that you are free to add any member variables or methods needed
 * to completed the task at hand
 * </p>
 *
 * @author Connor Ottenbacher and Doug Garding
 */
public class NetworkGraph {

	// contains a list of all the airports contained in the input file
	protected HashMap<String, Airport> airports;

	/**
	 * <p>
	 * Constructs a NetworkGraph object and populates it with the information
	 * contained in the given file. See the sample files or a randomly generated
	 * one for the proper file format.
	 * </p>
	 *
	 * <p>
	 * You will notice that in the given files there are duplicate flights with
	 * some differing information. That information should be averaged and
	 * stored properly. For example some times flights are canceled and that is
	 * represented with a 1 if it is and a 0 if it is not. When several of the
	 * same flights are reported totals should be added up and then reported as
	 * an average or a probability (value between 0-1 inclusive).
	 * </p>
	 *
	 * @param flightInfoPath
	 *            - The path to the file containing flight data. This should be
	 *            a *.csv(comma separated value) file
	 *
	 * @throws FileNotFoundException
	 *             The only exception that can be thrown in this assignment and
	 *             only if a file is not found.
	 */
	public NetworkGraph(String flightInfoPath) throws FileNotFoundException {
		airports = new HashMap<String, Airport>();
		File flightFile = new File(flightInfoPath);
		FileReader flightFileReader = new FileReader(flightFile);
		BufferedReader bufferedReader = new BufferedReader(flightFileReader);
		Scanner scan = new Scanner(bufferedReader);
		scan.next();

		// Add each line of data into the map
		while (scan.hasNext()) {
			String[] flight = scan.next().split(",");
			// helper methods create airports
			Airport originAirport = createAirport(flight)[0];
			Airport destinationAirport = createAirport(flight)[1];

			// If flight doesn't exist, create it
			if (!originAirport.edges.containsKey(originAirport.name + " to "
					+ destinationAirport.name)) {
				Flight currentFlight = createFlight(flight, originAirport,
						destinationAirport);
				originAirport.edges
						.put(currentFlight.toString(), currentFlight);
			}

			// If flight already exists, average in new flight data
			else {
				Flight existingFlight = originAirport.edges
						.get(originAirport.name + " to "
								+ destinationAirport.name);
				existingFlight.CARRIER.add(flight[2]);
				existingFlight.DELAY = ((existingFlight.DELAY * existingFlight.frequency) + Double
						.parseDouble(flight[3]))
						/ (existingFlight.frequency + 1);
				existingFlight.CANCELED = ((existingFlight.CANCELED * existingFlight.frequency) + Double
						.parseDouble(flight[4]))
						/ (existingFlight.frequency + 1);
				existingFlight.TIME = ((existingFlight.TIME * existingFlight.frequency) + Double
						.parseDouble(flight[5]))
						/ (existingFlight.frequency + 1);
				existingFlight.DISTANCE = ((existingFlight.DISTANCE * existingFlight.frequency) + Double
						.parseDouble(flight[6]))
						/ (existingFlight.frequency + 1);
				existingFlight.COST = ((existingFlight.COST * existingFlight.frequency) + Double
						.parseDouble(flight[7]))
						/ (existingFlight.frequency + 1);

				existingFlight.frequency++;
			}
		}
		scan.close();

	}

	/**
	 * Creates two airports using lines of the input file(as a string[]).
	 * Returns them as an array
	 * 
	 * @param flight
	 * @return
	 */
	private Airport[] createAirport(String[] flight) {
		Airport[] list = new Airport[2];

		// Add origin airport to list
		if (airports.containsKey(flight[0])) {
			list[0] = airports.get(flight[0]);
			// creates new airport and adds it
		} else {
			Airport originAirport = new Airport(flight[0]);
			airports.put(flight[0], originAirport);
			list[0] = originAirport;
		}

		// Add destination airport to list
		if (airports.containsKey(flight[1])) {
			list[1] = airports.get(flight[1]);
			// creates new airport and adds it to the list
		} else {
			Airport destinationAirport = new Airport(flight[1]);
			airports.put(flight[1], destinationAirport);
			list[1] = destinationAirport;
		}

		return list;
	}

	/**
	 * Creates a Flight (edge).
	 *
	 * @param Flight
	 *            [] flightArray
	 */
	private Flight createFlight(String[] flightArray, Airport origin,
			Airport destination) {
		String carrier = flightArray[2];
		double delay = Double.parseDouble(flightArray[3]);
		double canceled = Double.parseDouble(flightArray[4]);
		double time = Double.parseDouble(flightArray[5]);
		double distance = Double.parseDouble(flightArray[6]);
		double cost = Double.parseDouble(flightArray[7]);

		return new Flight(cost, delay, distance, canceled, time, carrier,
				origin, destination);

	}

	/**
	 * Helper method used for testing
	 */
	protected HashMap<String, Airport> getAirports() {
		return airports;
	}

	/**
	 * This method returns a BestPath object containing information about the
	 * best way to fly to the destination from the origin. "Best" is defined by
	 * the FlightCriteria parameter <code>enum</code>. This method should throw
	 * no exceptions and simply return a BestPath object with information
	 * dictating the result. For example, if a destination or origin is not
	 * contained in this instance of NetworkGraph simply return a BestPath with
	 * no path (not a <code>null</code> path). If origin or destination are
	 * <code>null</code> return a BestPath object with null as origin or
	 * destination (which ever is <code>null</code>.
	 *
	 * @param origin
	 *            - The starting location to find a path from. This should be a
	 *            3 character long string denoting an airport.
	 *
	 * @param destination
	 *            - The destination location from the starting airport. Again,
	 *            this should be a 3 character long string denoting an airport.
	 *
	 * @param criteria
	 *            - This enum dictates the definition of "best". Based on this
	 *            value a path should be generated and return.
	 *
	 * @return - An object containing path information including origin,
	 *         destination, and everything in between.
	 */
	public BestPath getBestPath(String origin, String destination,
			FlightCriteria criteria) {
		// Uitilizes Djikstras algorithm found in the other getBestPath method,
		// using null as the Carrier parameter
		return getBestPath(origin, destination, criteria, null);

	}

	/**
	 * Helper method used when at least one of the airports are null, returns a
	 * path that includes one or two null airports
	 * 
	 * @param origin
	 * @param destination
	 */
	private BestPath nullAirportPath(Airport origin, Airport destination) {
		BestPath result = new BestPath();
		result.path = new ArrayList<String>();
		// if statements determine which airports are null and creates the
		// appropriate path
		if (origin == null && destination == null) {
			result.path.add(null);
			result.path.add(null);
		} else if (origin == null) {
			result.path.add(null);
			result.path.add(destination.name);
		} else {
			result.path.add(origin.name);
			result.path.add(null);
		}
		// path length with a null airport will always be 0
		result.pathLength = 0;
		return result;
	}

	/**
	 * Creates an empty path. Used when an airport is not found
	 */
	private BestPath createEmptyPath() {
		BestPath result = new BestPath();
		result.path = new ArrayList<String>();
		result.pathLength = 0;
		return result;
	}

	/**
	 * Reconstructs the best path from the destination to the origin
	 * 
	 * @param origin
	 * @param destination
	 */

	private BestPath createPath(Airport origin, Airport destination) {
		BestPath result = new BestPath();
		result.path = new ArrayList<String>();
		Airport path = destination;
		LinkedList<String> list = new LinkedList<String>();
		while (path != origin) {
			list.addFirst(path.name);
			path = path.comingFrom;
		}
		list.addFirst(path.name);
		result.pathLength = (double) Math.round(destination.cost * 100) / 100;
		result.path.addAll(list);
		return result;
	}

	/**
	 * Returns the appropriate cost a flight based on the given criteria
	 * 
	 * @param cost
	 *            , criteria being used
	 * @param flight
	 */
	private double cost(FlightCriteria cost, Flight flight) {
		switch (cost) {
		case COST:
			return flight.COST;
		case DELAY:
			return flight.DELAY;
		case DISTANCE:
			return flight.DISTANCE;
		case CANCELED:
			return flight.CANCELED;
		case TIME:
			return flight.TIME;
		}
		return 0;

	}

	/**
	 * <p>
	 * This overloaded method should do the same as the one above only when
	 * looking for paths skip the ones that don't match the given airliner.
	 * </p>
	 *
	 * @param origin
	 *            - The starting location to find a path from. This should be a
	 *            3 character long string denoting an airport.
	 *
	 * @param destination
	 *            - The destination location from the starting airport. Again,
	 *            this should be a 3 character long string denoting an airport.
	 *
	 * @param criteria
	 *            - This enum dictates the definition of "best". Based on this
	 *            value a path should be generated and return.
	 *
	 * @param airliner
	 *            - a string dictating the airliner the user wants to use
	 *            exclusively. Meaning no flights from other airliners will be
	 *            considered.
	 *
	 * @return - An object containing path information including origin,
	 *         destination, and everything in between.
	 */
	public BestPath getBestPath(String origin, String destination,
			FlightCriteria criteria, String airliner) {
		// One of the airports was not found in the graph, returns an empty path
		if (!airports.containsKey(origin) || !airports.containsKey(destination)) {
			return createEmptyPath();
		}
		// One of the airports was null, returns a path with at least one null
		// airport and 0 cost
		if (airports.get(origin) == null || airports.get(destination) == null) {
			return nullAirportPath(airports.get(origin),
					airports.get(destination));
		}
		// Prepares all airports for use in Djikstras algorithm
		for (HashMap.Entry<String, Airport> entry : airports.entrySet()) {
			entry.getValue().cost = Double.POSITIVE_INFINITY;
			entry.getValue().visited = false;
		}
		// Begins Djikstras algorithm
		PriorityQueue<Airport> queue = new PriorityQueue<Airport>();
		airports.get(origin).cost = 0;
		queue.add(airports.get(origin));
		Airport curr;
		while (!queue.isEmpty()) {
			curr = queue.remove();
			// Checks if the destination was found
			if (curr == airports.get(destination)) {
				return createPath(airports.get(origin), curr);
			}
			curr.visited = true;
			// Creates an arraylist which contains all items in the queue, used
			// to update the cost of items in the queue
			ArrayList<Airport> arr = new ArrayList<Airport>(queue);
			// iterates through all the flights of the current airport
			for (HashMap.Entry<String, Flight> entry : curr.edges.entrySet()) {
				// checks if airliner was used as a parameter and whether or not
				// the specified airliner supports each flight
				if (airliner != null
						&& !entry.getValue().CARRIER.contains(airliner)) {
					// flight is ignored if it does not have the specified
					// airliner/carrier
					continue;
				}
				Airport currDestination = entry.getValue().goingTo;

				// skips airports that have already been visited
				if (currDestination.visited) {
					continue;
				}

				// finds the most cost efficient airport to travel to
				if (currDestination.cost > curr.cost
						+ cost(criteria, entry.getValue())) {
					currDestination.comingFrom = curr;
					currDestination.cost = curr.cost
							+ cost(criteria, entry.getValue());
					// If the airport is already contained within the queue,
					// updates its cost in the arraylist copy
					if (queue.contains(currDestination)) {
						// finds the airport
						for (int i = 0; i < arr.size(); i++) {
							if (arr.get(i).name.equals(currDestination.name)) {
								arr.set(i, currDestination);
								break;
							}
						}
						// New airports are added to the copy arraylist
					} else {
						arr.add(currDestination);
					}
				}

			}
			// queue is cleared and re-set with the updated airports in the copy
			// array
			queue.clear();
			queue.addAll(arr);
		}

		// no possible paths exist from the origin to the destination, returns
		// an empty path
		return createEmptyPath();
	}

}

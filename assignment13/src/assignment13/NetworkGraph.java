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
 * @author CS2420 Teaching Staff - Spring 2016
 */
public class NetworkGraph {

	private HashMap<String, Airport> airports;

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
			Airport originAirport = createAirport(flight).get(0);
			Airport destinationAirport = createAirport(flight).get(1);

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
				existingFlight.DELAY = ((existingFlight.DELAY * existingFlight.duplicates) + Double
						.parseDouble(flight[3]))
						/ (existingFlight.duplicates + 1);
				existingFlight.CANCELED = ((existingFlight.CANCELED * existingFlight.duplicates) + Double
						.parseDouble(flight[4]))
						/ (existingFlight.duplicates + 1);
				existingFlight.TIME = ((existingFlight.TIME * existingFlight.duplicates) + Double
						.parseDouble(flight[5]))
						/ (existingFlight.duplicates + 1);
				existingFlight.DISTANCE = ((existingFlight.DISTANCE * existingFlight.duplicates) + Double
						.parseDouble(flight[6]))
						/ (existingFlight.duplicates + 1);
				existingFlight.COST = ((existingFlight.COST * existingFlight.duplicates) + Double
						.parseDouble(flight[7]))
						/ (existingFlight.duplicates + 1);

				existingFlight.duplicates++;
			}
		}
		scan.close();

	}

	/**
	 * Returns a two item array list with an origin and destination airport.
	 * 
	 * @param flight
	 * @return
	 */
	private ArrayList<Airport> createAirport(String[] flight) {
		ArrayList<Airport> list = new ArrayList<>();

		// Add origin airport to list
		if (airports.containsKey(flight[0])) {
			list.add(airports.get(flight[0]));
		} else {
			Airport originAirport = new Airport(flight[0]);
			airports.put(flight[0], originAirport);
			list.add(originAirport);
		}

		// Add destination airport to list
		if (airports.containsKey(flight[1])) {
			list.add(airports.get(flight[1]));
		} else {
			Airport destinationAirport = new Airport(flight[1]);
			airports.put(flight[1], destinationAirport);
			list.add(destinationAirport);
		}

		return list;
	}

	/**
	 * Helper method that creates a Flight (edge).
	 *
	 * @param Flight
	 *            [] flightArray
	 * @return Flight
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

	// testing
	public static void main(String[] args) throws FileNotFoundException {
		NetworkGraph graph = new NetworkGraph("flights-2015-q3.csv");
	}
	
	public HashMap<String, Airport> getAirports(){
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
		// TODO: First figure out what kind of path you need to get (HINT: Use a
		// switch!) then
		// Search for the shortest path using Dijkstra's algorithm.
		return null;
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
		// TODO:
		return null;
	}
}
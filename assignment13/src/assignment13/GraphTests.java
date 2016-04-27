package assignment13;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class GraphTests {
	NetworkGraph graph;

	@Before
	public void setUp() throws FileNotFoundException {
		graph = new NetworkGraph("testfile.csv");
	}

	@Test
	public void AirportsAdded() {
		HashMap<String, Airport> airports = graph.getAirports();
		assertEquals(5, airports.size());
		assertTrue(airports.containsKey("PTX"));
		assertTrue(airports.containsKey("ZJN"));
		assertTrue(airports.containsKey("QGG"));
		assertTrue(airports.containsKey("EVG"));
		assertTrue(airports.containsKey("JAC"));
	}

	@Test
	public void FlightsAdded() {
		HashMap<String, Airport> airports = graph.getAirports();
		assertEquals(3, airports.get("PTX").edges.size());
		assertEquals(3, airports.get("ZJN").edges.size());
		assertEquals(2, airports.get("QGG").edges.size());
		assertEquals(2, airports.get("EVG").edges.size());
		assertEquals(2, airports.get("JAC").edges.size());
	}
	@Test
	public void PTXtoZJNflight() {
		HashMap<String, Airport> airports = graph.getAirports();
		HashMap<String, Flight> ptxFlights = airports.get("PTX").edges;
		Flight ptxToZjn = ptxFlights.get("PTX to ZJN");
		assertEquals(860.755, ptxToZjn.COST, 0);
		assertEquals(1565, ptxToZjn.DISTANCE, 0);
		assertEquals(226, ptxToZjn.TIME, 0);
		assertEquals(1, ptxToZjn.CANCELED, 0);
		assertEquals(189.5, ptxToZjn.DELAY, 0);
		assertTrue(ptxToZjn.CARRIER.contains("AA"));
		assertTrue(ptxToZjn.CARRIER.contains("DL"));
		assertFalse(ptxToZjn.CARRIER.contains("EV"));
		assertEquals(ptxToZjn.CARRIER.size(), 2);
	}
	@Test
	public void ZJNtoJACflight() {
		HashMap<String, Airport> airports = graph.getAirports();
		HashMap<String, Flight> zjnFlights = airports.get("ZJN").edges;
		Flight zjnToJac = zjnFlights.get("ZJN to JAC");
		assertEquals(658.95, zjnToJac.COST, 0);
		assertEquals(959, zjnToJac.DISTANCE, 0);
		assertEquals(138, zjnToJac.TIME, 0);
		assertEquals(1, zjnToJac.CANCELED, 0);
		assertEquals(470, zjnToJac.DELAY, 0);
		assertTrue(zjnToJac.CARRIER.contains("HA"));
		assertEquals(zjnToJac.CARRIER.size(), 1);
	}
	
	@Test
	public void BestPathPTXtoJAC() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		BestPath result = new BestPath();
		result.pathLength = 1361.86;
		result.path = new ArrayList<String>();
		result.path.add("PTX");
		result.path.add("EVG");
		result.path.add("ZJN");
		result.path.add("JAC");
		assertEquals(result, small.getBestPath("PTX", "JAC", FlightCriteria.COST));
	}
	@Test
	public void NoPathQGGtoJAC() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testnopath.csv");
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		assertEquals(result, small.getBestPath("QGG", "JAC", FlightCriteria.COST));
	}
	@Test
	public void AirportDoesntExist() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		assertEquals(result, small.getBestPath("ZZZ", "JAC", FlightCriteria.COST));
	}
	@Test
	public void AirportDestinationDoesntExist() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		assertEquals(result, small.getBestPath("JAC", "ZZZ", FlightCriteria.COST));
	}
	@Test
	public void NullAirport() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		small.airports.put("JAC", null);
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		result.path.add(null);
		result.path.add("QGG");
		assertEquals(result, small.getBestPath("JAC", "QGG", FlightCriteria.COST));
	}
	@Test
	public void NullDestination() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		small.airports.put("QGG", null);
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		result.path.add("JAC");
		result.path.add(null);
		assertEquals(result, small.getBestPath("JAC", "QGG", FlightCriteria.COST));
	}
	@Test
	public void BothAirportsNull() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		small.airports.put("QGG", null);
		small.airports.put("JAC", null);
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		result.path.add(null);
		result.path.add(null);
		assertEquals(result, small.getBestPath("JAC", "QGG", FlightCriteria.COST));
	}
	@Test
	public void MinuteDifferenceInCost() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("verysmalldifference.csv");
		BestPath result = new BestPath();
		result.pathLength = 20;
		result.path = new ArrayList<String>();
		result.path.add("PTX");
		result.path.add("EVG");
		result.path.add("ZJN");
		result.path.add("JAC");
		assertEquals(result, small.getBestPath("PTX", "JAC", FlightCriteria.COST));
	}
	@Test
	public void DelayCriteria() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		BestPath result = new BestPath();
		result.pathLength = 292.5;
		result.path = new ArrayList<String>();
		result.path.add("PTX");
		result.path.add("ZJN");
		result.path.add("QGG");
		assertEquals(result, small.getBestPath("PTX", "QGG", FlightCriteria.DELAY));
	}
	@Test
	public void CanceledCriteria() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		BestPath result = new BestPath();
		result.pathLength = 1;
		result.path = new ArrayList<String>();
		result.path.add("EVG");
		result.path.add("ZJN");
		result.path.add("JAC");
		assertEquals(result, small.getBestPath("EVG", "JAC", FlightCriteria.CANCELED));
	}
	@Test
	public void TimeCriteria() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		BestPath result = new BestPath();
		result.pathLength = 320;
		result.path = new ArrayList<String>();
		result.path.add("QGG");
		result.path.add("PTX");
		result.path.add("EVG");
		result.path.add("ZJN");
		result.path.add("JAC");
		assertEquals(result, small.getBestPath("QGG", "JAC", FlightCriteria.TIME));
	}
	@Test
	public void DistanceCriteria() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("testfile.csv");
		BestPath result = new BestPath();
		result.pathLength = 1945;
		result.path = new ArrayList<String>();
		result.path.add("JAC");
		result.path.add("QGG");
		result.path.add("PTX");
		assertEquals(result, small.getBestPath("JAC", "PTX", FlightCriteria.DISTANCE));
	}
	@Test
	public void CarrierPath() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("carriertest.csv");
		BestPath result = new BestPath();
		result.pathLength = 1410.24;
		result.path = new ArrayList<String>();
		result.path.add("PTX");
		result.path.add("QGG");
		result.path.add("ZJN");
		result.path.add("JAC");
		assertEquals(result, small.getBestPath("PTX", "JAC", FlightCriteria.COST, "EV"));
	}
	@Test
	public void CarrierDoesntExist() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("carriertest.csv");
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		assertEquals(result, small.getBestPath("PTX", "JAC", FlightCriteria.COST, "ZZ"));
	}
	@Test
	public void CarrierNoPathFound() throws FileNotFoundException{
		NetworkGraph small = new NetworkGraph("nopathcarrier.csv");
		BestPath result = new BestPath();
		result.pathLength = 0;
		result.path = new ArrayList<String>();
		assertEquals(result, small.getBestPath("PTX", "JAC", FlightCriteria.COST, "EV"));
	}

}

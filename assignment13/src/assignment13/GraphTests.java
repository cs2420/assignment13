package assignment13;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
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
	public void FlightsAveragedCorrectly() {
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
	}

}

package assignment13;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

public class GraphTests {

	@Test
	public void test() throws FileNotFoundException {
		NetworkGraph graph = new NetworkGraph("testfile.csv");
		
		assertEquals(5, graph.getAirports().size());
	}

}

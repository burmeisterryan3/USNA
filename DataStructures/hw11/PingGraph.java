import java.util.*;
 
/** A graph of ping delays throughout a network.
 * Each node label is a domain name such as www.usna.edu,
 * and each edge weight is the latency in that transmission,
 * as a floating-point value for the number of milliseconds.
 */
public class PingGraph {
	TreeMap<String, Vertex> map;
 
	/** Constructor for a new, empty graph. */
	public PingGraph() {
		map = new TreeMap<String, Vertex>();
	}
 
	/** Adds a new edge, and possibly new vertices, to the graph. */
	public void addEdge(PingEdge ee) {
		String source = ee.getSource();
		String dest = ee.getDest();
		if (!map.containsKey(source)) 
			map.put(source, new Vertex());
		if (!map.containsKey(dest))
			map.put(dest, new Vertex());
		map.get(source).addEgdge(ee);
	}
 
	/** Gets all edges that have the given source. */
	public List<PingEdge> neighbors(String source) {
		return map.get(source).getEdges();
	}
}
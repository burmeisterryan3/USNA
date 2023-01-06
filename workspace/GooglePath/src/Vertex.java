import java.util.ArrayList;

// A class for representing a particular vertex in a graph.
public class Vertex {
	ArrayList<PingEdge> edgeList;
	
	Vertex() { 
		edgeList = new ArrayList<PingEdge>();
	}
	
	void addEgdge(PingEdge ee) { 
		edgeList.add(ee); 
	}
	
	ArrayList<PingEdge> getEdges() { return edgeList; }
}

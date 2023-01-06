/** A single ping connection in a PingGraph. */
// NOTE: This is entirely written for you, but you can add more
// fields or methods if you want to for some reason.
public class PingEdge implements Comparable<PingEdge>{
	private String source;
	private String dest;
	private float latency;
  
	public PingEdge(String source, String dest, float latency) {
		this.source = source;
		this.dest = dest;
		this.latency = latency;
	}
 
	public String getSource() { return this.source; }
	public String getDest() { return this.dest; }
	public float getLatency() { return this.latency; }

	@Override
	public int compareTo(PingEdge pe) {
		if (this.latency < pe.getLatency()) { return -1; }
		else if (this.latency > pe.getLatency()) { return 1; }
		else return 0;
	}
}
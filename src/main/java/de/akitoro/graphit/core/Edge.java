package de.akitoro.graphit.core;

public class Edge {
	
	private final Vertex source;
	private final Vertex target;
	
	public Properties properties = new Properties();
	
	public Edge(Vertex source, Vertex target) {
		this.source = source;
		this.target = target;
	}
	
	public Vertex getSource() {
		return source;
	}
	
	public Vertex getTarget() {
		return target;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		else if(obj instanceof Edge) {
			Edge other = (Edge) obj;
			return other.source.equals(this.source) && other.target.equals(this.target);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return source.id.hashCode() + target.id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("(%s, %s)", source.id, target.id);
	}
}

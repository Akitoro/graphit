package de.akitoro.graphit.core;

import java.util.Set;

public class Edge {
	
	private final Vertex source;
	private final Vertex target;
	private final boolean isDirected;
	private final double wheight;
	
	public Properties properties = new Properties();
	
	public Edge(Vertex source, Vertex target) {
		this.source = source;
		this.target = target;
		this.isDirected = false;
		this.wheight = 1;
	}
	
	public Vertex getSource() {
		return source;
	}
	
	public Vertex getTarget() {
		return target;
	}
	
	public boolean getIsDirected() {
		return isDirected;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		else if(obj instanceof Edge) {
			Edge other = (Edge) obj;
			if (this.isDirected && other.isDirected) {
				return other.source.equals(this.source) && other.target.equals(this.target);
			}
			else if (!this.isDirected && !other.isDirected) {
				Set<Vertex> a = Set.of(this.source, this.target);
				Set<Vertex> b = Set.of(other.source, other.target);
				
				return a.equals(b);
			}
			
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

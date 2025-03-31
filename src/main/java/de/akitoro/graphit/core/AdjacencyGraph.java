package de.akitoro.graphit.core;

import java.util.Map.Entry;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AdjacencyGraph implements Graph{
	
	public final boolean isDirected;
	
	private HashMap<Vertex, Set<Vertex>> adjacency = new HashMap<Vertex, Set<Vertex>>();
	
	public AdjacencyGraph() {
		this.isDirected = false;
	}
	
	@Override
	public Set<Vertex> getVertices() {
		return adjacency.keySet();
	}

	@Override
	public Set<Edge> getEdges() {
		Set<Edge> totalEdges = new HashSet<Edge>();
		
		for (Entry<Vertex, Set<Vertex>> entry : this.adjacency.entrySet()) {
			for (Vertex neighbour : entry.getValue()) {
				totalEdges.add(new Edge(entry.getKey(), neighbour));
			}
		}
		return totalEdges;
	}
	
	public boolean add(Vertex v) {
		if (this.isContained(v)) {
			return false;
		}
		return null == adjacency.putIfAbsent(v, new HashSet<Vertex>());
	}
	
	
	public boolean remove(Vertex v) {
		Set<Vertex> neighbours = adjacency.get(v);
		
		if (this.isDirected) {
			// Remove vertex from adjacent neighbors
			for (Vertex neighbor : neighbours) {
				adjacency.get(neighbor).remove(v);
			}
		}
		else {
			for (Set<Vertex> candidates : this.adjacency.values()) {
				candidates.remove(v);
			}
		}
		return adjacency.remove(v) != null;
	}
	
	public boolean connect(Vertex source, Vertex target) {
		if (source.equals(target)) {
			return false;
		}
		this.adjacency.putIfAbsent(source, new HashSet<Vertex>());
		this.adjacency.putIfAbsent(target, new HashSet<Vertex>());
//		if (!this.isContained(source) || !this.isContained(target)) {
//			return false;
//		}
		this.adjacency.get(source).add(target);
		if (!this.isDirected()) {
			this.adjacency.get(target).add(source);
		}
		return true;
	}
	
	public boolean disconnect(Vertex source, Vertex target) {
		if (source.equals(target)) {
			return false;
		}
		if (!this.isContained(source) || !this.isContained(target)) {
			return false;
		}
		this.adjacency.get(source).remove(target);
		if (!this.isDirected()) {
			this.adjacency.get(target).remove(source);
		}
		
		return true;
	}

	@Override
	public boolean isDirected() {
		return this.isDirected;
	}
	
	public boolean isContained(Vertex v) {
		return adjacency.containsKey(v);
	}

	@Override
	public boolean isConnected(Vertex source, Vertex target) {
		return this.adjacency.get(source).contains(target);
	}

	@Override
	public List<Vertex> getVertexPath(Vertex start, Vertex end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Edge> getEdgePath(Vertex start, Vertex end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWheighted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCyclic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int degree(Vertex vertex) {
		if (!this.isContained(vertex)) {
			return 0;
		}
		return this.adjacency.get(vertex).size();
	}

	@Override
	public Set<Vertex> getAdjacentVertices(Vertex source) {
		return this.adjacency.get(source);
	}

	
}

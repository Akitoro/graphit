package de.akitoro.graphit.core;

import java.util.List;
import java.util.Set;

public interface Graph {
	Set<Vertex> getVertices();
	Set<Edge> getEdges();
	
	int degree(Vertex vertex);
	
	boolean isDirected();
	boolean isWheighted();
	
	boolean isCyclic();
	boolean isConnected(Vertex source, Vertex target);
	boolean isContained(Vertex vertex);
	
	boolean add(Vertex vertex);
	boolean remove(Vertex vertex);
	
	boolean connect(Vertex source, Vertex target);
	boolean disconnect(Vertex source, Vertex target);
	
	List<Vertex> getVertexPath(Vertex start, Vertex end);
	List<Edge> getEdgePath(Vertex start, Vertex end);
	
	Set<Vertex> getAdjacentVertices(Vertex source);
}

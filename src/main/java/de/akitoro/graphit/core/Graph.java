package de.akitoro.graphit.core;

import java.util.List;
import java.util.Set;

public interface Graph<V, E> {
	Set<V> getVertices();
	Set<E> getEdges();
	
	int degree(V vertex);
	
	boolean isDirected();
	boolean isWheighted();
	
	boolean isCyclic();
	boolean isConnected(V source, V target);
	boolean isContained(V vertex);
	
	boolean add(V vertex);
	boolean remove(V vertex);
	
	boolean connect(V source, V target);
	boolean disconnect(V source, V target);
	
	List<Vertex> getVertexPath(V start, V end);
	List<Edge> getEdgePath(V start, V end);
}

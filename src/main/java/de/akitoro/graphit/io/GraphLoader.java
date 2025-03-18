package de.akitoro.graphit.io;

import de.akitoro.graphit.core.Graph;

public interface GraphLoader<V, E> {
	Graph<V, E> load(String path);
	void save(Graph<V, E> graph);
}

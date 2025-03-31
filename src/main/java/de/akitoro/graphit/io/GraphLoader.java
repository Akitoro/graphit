package de.akitoro.graphit.io;

import de.akitoro.graphit.core.Graph;

public interface GraphLoader {
	Graph load(String path);
	void save(Graph graph);
}

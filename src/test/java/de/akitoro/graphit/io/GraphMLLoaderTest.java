package de.akitoro.graphit.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.akitoro.graphit.core.Edge;
import de.akitoro.graphit.core.Graph;
import de.akitoro.graphit.core.Vertex;

class GraphMLLoaderTest {

	@Test
	void test() {
		GraphLoader<Vertex, Edge> loader = new GraphMLLoader();
    	
    	Graph<Vertex, Edge> graph = loader.load("src/test/resources/example.graphml");
    	
    	graph.getVertices().forEach(v -> System.out.println(v.toString()));
    	graph.getEdges().forEach(e -> System.out.println(e.toString()));
	}

}

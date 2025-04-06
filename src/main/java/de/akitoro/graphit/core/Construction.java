package de.akitoro.graphit.core;

public class Construction {

	/**
	 * Constructs a complete simple Graph.
	 * 
	 * @param n amount of vertices
	 * @return simple complete graph
	 */
	public static Graph completeGraph(int n) {
		Graph graph = new AdjacencyGraph();
		//Construct all possible edges by looping through all 2-combinations of vertices
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				graph.connect(new Vertex(Integer.toString(i)), new Vertex(Integer.toString(j)));
			};
		}
		return graph;
	}
}

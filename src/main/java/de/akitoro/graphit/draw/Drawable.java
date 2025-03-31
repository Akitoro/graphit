package de.akitoro.graphit.draw;

import java.util.Map;

import de.akitoro.graphit.core.Graph;
import de.akitoro.graphit.core.Vertex;
import de.akitoro.graphit.math.Vec2;

public interface Drawable {
	public Map<Vertex, Vec2> draw(Graph graph);
}

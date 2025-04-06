package de.akitoro.graphit.draw.circular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.akitoro.graphit.core.Graph;
import de.akitoro.graphit.core.Vertex;
import de.akitoro.graphit.draw.Drawable;
import de.akitoro.graphit.math.Vec2;

public class CircularDrawer implements Drawable{
	
	private double circleRadius = 20;
	
	private double offsetAngle = 0.0;
	
	public CircularDrawer(double circleRadius, double offsetAngle) {
		this.circleRadius = circleRadius;
		this.offsetAngle = offsetAngle;
	}
	
	@Override
	public Map<Vertex, Vec2> draw(Graph graph) {
		List<Vertex> vertices = new ArrayList<Vertex>();
		vertices.addAll(graph.getVertices());
		
		Map<Vertex, Vec2> positions = new HashMap<>();
		
		//walk around the circle counterclockwise and distribute n vertices evenly
		double step = (2 * Math.PI)/vertices.size();
		
		for (int i = 0; i < vertices.size(); i++) {
			double circularAngle = i * step + offsetAngle;
			
			Vec2 polarPos = new Vec2(circularAngle, circleRadius);
			positions.put(vertices.get(i), polarPos.toCartesian());
		}
		
		return positions;
	}}

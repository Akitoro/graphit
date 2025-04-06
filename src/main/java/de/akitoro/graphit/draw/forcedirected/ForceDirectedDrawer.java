package de.akitoro.graphit.draw.forcedirected;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import de.akitoro.graphit.core.Graph;
import de.akitoro.graphit.core.Vertex;
import de.akitoro.graphit.draw.Drawable;
import de.akitoro.graphit.math.Vec2;

public abstract class ForceDirectedDrawer implements Drawable{
	
	protected double desiredLength = 1;
	
	private double epsilon = 0.1;
	
	private int maxTime = 500;
	
	protected double delta(int time) {
		return Math.pow(0.99, time);
	}
	
	protected abstract Vec2 repulsiveForce(Vec2 pu, Vec2 pv);
	
	protected abstract Vec2 attractiveForce(Vec2 pu, Vec2 pv);
	
	private Vec2 totalRepulsiveForce(Graph graph, Map<Vertex, Vec2> positions, Vertex u) {
		Vec2 total = new Vec2(0,0);
		for (Vertex v : graph.getVertices()) {
			if (!v.equals(u)) {
				Vec2 singleRepulsive = this.repulsiveForce(positions.get(u), positions.get(v));
				total = total.add(singleRepulsive);
			}
		}
		return total;
	}
	
	private Vec2 calculateDisplacement(Graph graph, Map<Vertex, Vec2> positions, Vertex u) {
		Vec2 displacement = Vec2.ZERO;
		for (Vertex v : graph.getVertices()) {
			if (graph.isConnected(u, v)) {
				displacement = displacement.add(this.attractiveForce(positions.get(u), positions.get(v)));
			}
			if (!v.equals(u)) {
				displacement = displacement.add(this.repulsiveForce(positions.get(u), positions.get(v)));
			}
		}
		return displacement;
	}
	
	private Vec2 totalAttractiveForce(Graph graph, Map<Vertex, Vec2> positions, Vertex u) {
		Vec2 total = new Vec2(0,0);
		for (Vertex v : graph.getAdjacentVertices(u)) {
			Vec2 singleAttractive = this.repulsiveForce(positions.get(u), positions.get(v));
			total = total.add(singleAttractive);
		}
		return total;
	}
	
	
	public Map<Vertex, Vec2> draw(Graph graph) {
		int t = 1;
		
		Random random = new Random();
		
		//Randomly initialize Vertex positions
		Map<Vertex, Vec2> positions = graph.getVertices().stream()
				.collect(Collectors.toMap(v -> v, v -> new Vec2(random.nextDouble(), random.nextDouble())));
		
		//Initialize moving forces
		Map<Vertex, Vec2> movingForces = graph.getVertices().stream()
				.collect(Collectors.toMap(v -> v, v -> new Vec2(this.epsilon + 1, this.epsilon + 1)));
		
		// Calculate Forces until either time limit is reached or the forces are to weak
		while (t < this.maxTime && 
				movingForces.values().stream().mapToDouble(v -> v.length()).max().orElse(0) > this.epsilon) {
			//Calculate next Forces each Vertex
			for (Vertex u: positions.keySet()) {
				movingForces.put(u, this.calculateDisplacement(graph, positions, u));
			}
			//Update each Vertex position
			for (Vertex v: positions.keySet()) {
				Vec2 currentPosition = positions.get(v);
				Vec2 nextPosition = currentPosition.add(movingForces.get(v).mul(this.delta(t)));
				positions.put(v, nextPosition);
			}
			t++;
		}	
		return positions;
	}
	
	
}

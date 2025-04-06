package de.akitoro.graphit.math;

public class Ray {
	private Vec2 offset;
	private Vec2 direction;
	
	public Ray(Vec2 offset, Vec2 direction) {
		this.offset = offset;
		this.direction = direction;
	}
	
	public Vec2 get(double t) {
		return direction.mul(t).add(offset);
	}
	
	public Vec2 getOffset() {
		return offset;
	}
	
	public Vec2 getDirection() {
		return direction;
	}
	
	
}

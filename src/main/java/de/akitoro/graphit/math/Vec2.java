package de.akitoro.graphit.math;

import java.util.function.Function;

import de.akitoro.graphit.core.Edge;

public class Vec2 {
	
	public double x;
	
	public double y;
	
	public static Vec2 ZERO = new Vec2(0, 0);
	
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2 add(Vec2 other) {
		return new Vec2(this.x + other.x, this.y + other.y);
	}
	
	public Vec2 sub(Vec2 other) {
		return new Vec2(this.x - other.x, this.y - other.y);
	}
	
	public Vec2 neg() {
		return this.mul(-1);
	}
	
	public Vec2 mul(double scalar) {
		return new Vec2( scalar * this.x,  scalar * this.y);
	}
	
	public Vec2 div(double scalar) {
		return new Vec2( this.x / scalar,  this.y / scalar);
	}
	
	public double length() {
		return Math.sqrt(this.lengthSquared());
	}
	
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
	
	public Vec2 normalize() {
		//TODO Check for null Vector
		double length = this.length();
		return this.div(length);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		else if(obj instanceof Edge) {
			Vec2 other = (Vec2) obj;
			return this.x == other.x && this.y == other.y;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f; %.2f)", this.x, this.y);
	}
}

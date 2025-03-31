package de.akitoro.graphit.math;

public class Mat2x2 {
	
	private double[][] m;
	
	public Mat2x2(double[][] matrix) {
		if (matrix.length != 2) {
			if (matrix[0].length != 2 && matrix[1].length != 2) {
				throw new RuntimeException();
			}
		}
		this.m = matrix;
	}
	
	public Vec2 apply(Vec2 v) {
		return new Vec2(v.x * m[0][0] + v.x * m[0][1], v.y * m[1][0] + v.y * m[1][1]);
	}
}

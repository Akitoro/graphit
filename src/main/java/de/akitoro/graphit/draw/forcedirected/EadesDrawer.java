package de.akitoro.graphit.draw.forcedirected;

import de.akitoro.graphit.math.Vec2;

public class EadesDrawer extends ForceDirectedDrawer {
	
	private double repulsionConstant = 2.0;
	
	private double springConstant = 2.0;
	
	private Vec2 springForce(Vec2 pu, Vec2 pv) {
		return pv.sub(pu).normalize().mul(Math.log(pv.sub(pu).length()/super.desiredLength)).mul(springConstant);
	}
	
	@Override
	protected Vec2 repulsiveForce(Vec2 pu, Vec2 pv) {
		return pu.sub(pv).normalize().mul(repulsionConstant/pv.sub(pu).lengthSquared());
	}

	@Override
	protected Vec2 attractiveForce(Vec2 pu, Vec2 v) {
		return this.springForce(pu, v).sub(this.repulsiveForce(pu, v));
	}

}

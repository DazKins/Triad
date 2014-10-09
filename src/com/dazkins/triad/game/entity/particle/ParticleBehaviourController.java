package com.dazkins.triad.game.entity.particle;

import com.dazkins.triad.util.pool.ObjectPool;

public abstract class ParticleBehaviourController {
	protected Particle operatingParticle;
	protected ObjectPool<Particle> operatingPool;
	
	public abstract void tick();
	
	public void assignOperatingParticle(Particle p) {
		operatingParticle = p;
	}
	
	public void assignOperatingPool(ObjectPool<Particle> op) {
		operatingPool = op;
	}
}
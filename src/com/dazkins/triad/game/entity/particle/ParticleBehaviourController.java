package com.dazkins.triad.game.entity.particle;

public abstract class ParticleBehaviourController {
	protected Particle operatingParticle;
	
	public abstract void tick();
	
	public void assignOperatingParticle(Particle p) {
		operatingParticle = p;
	}
}
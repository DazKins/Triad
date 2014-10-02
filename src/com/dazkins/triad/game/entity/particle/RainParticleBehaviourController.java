package com.dazkins.triad.game.entity.particle;

public class RainParticleBehaviourController extends ParticleBehaviourController {
	public void tick() {
		float x = operatingParticle.getX();
		float y = operatingParticle.getY();
		
		y -= Math.random() * 4.0f + 4.0f;
		
		operatingParticle.setX(x);
		operatingParticle.setY(y);
	}
}
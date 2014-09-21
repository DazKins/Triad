package com.dazkins.triad.game.entity.particle;

public class RainParticleBehaviourController extends ParticleBehaviourController {
	private float ya = -1;
	
	public void tick() {
		float x = operatingParticle.getX();
		float y = operatingParticle.getY();
		
		y += -1.0f;
		
		System.out.println(y);
		
		operatingParticle.setX(x);
		operatingParticle.setY(y);
	}
}
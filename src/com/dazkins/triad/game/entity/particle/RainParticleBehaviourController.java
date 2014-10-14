package com.dazkins.triad.game.entity.particle;

public class RainParticleBehaviourController extends ParticleBehaviourController {
	public void tick() {
		float x = operatingParticle.getX();
		float y = operatingParticle.getY();
		float a = operatingParticle.getA();
		long l = operatingParticle.lifeTicks;
		
		y -= Math.random() * 4 + 2;
		
		if (l > 1 * 60) {
			a -= 0.01f;
		}
		else {
			if (a < 0.5f)
				a += 0.01f;
		}
		
		operatingParticle.lifeTicks = l;
		operatingParticle.setA(a);
		operatingParticle.setX(x);
		operatingParticle.setY(y);
	}
}
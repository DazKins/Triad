package com.dazkins.triad.util.pool.factory;

import com.dazkins.triad.game.entity.particle.Particle;

public class ParticleFactory implements ObjectFactory<Particle> {
	public Particle create(Particle p, float x, float y, float w, float h, float r, float g, float b, float a, float d) {
		p.create(x, y, w, h, r, g, b, a, d);
		return p;
	}
}
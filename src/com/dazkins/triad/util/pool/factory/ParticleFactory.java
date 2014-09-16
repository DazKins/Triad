package com.dazkins.triad.util.pool.factory;

import com.dazkins.triad.game.entity.particle.Particle;

public class ParticleFactory<T> implements ObjectFactory {
	public Particle create(Particle p, float x, float y, float w, float h, float r, float g, float b) {
		p.create(x, y, w, h, r, g, b);
		return p;
	}
}
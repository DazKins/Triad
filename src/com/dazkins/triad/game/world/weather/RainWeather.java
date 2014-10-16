package com.dazkins.triad.game.world.weather;

import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.pool.ObjectPool;
import com.dazkins.triad.util.pool.factory.RainParticleFactory;

public class RainWeather extends Weather {
	public RainWeather(World o) {
		super(o);
	}

	public void tick() {
		Chunk[] c = operatingWorld.getChunks();
		ObjectPool<Particle> op = Particle.particlesPool;
		RainParticleFactory pf = (RainParticleFactory) op.getCurrentFactory();
		for (int i = 0; i < c.length; i++) {
			if (Math.random() < 0.01f && operatingWorld.getCam().getViewportBounds().intersects(c[i].getBounds())) {
				AABB a = c[i].getBounds();
				float xp = (float) (Math.random() * (a.getX1() - a.getX0()) + a.getX0());
				float yp = (float) (Math.random() * (a.getY1() - a.getY0()) + a.getY0());
				Particle p = op.getEmptyObjectForCreation();
				pf.create(p, xp, yp, 10, 30, 0.4f, 0.7f, 1.0f);
				operatingWorld.addParticle(p);
			}
		}
	}
}
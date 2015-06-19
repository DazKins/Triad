package com.dazkins.triad.game.world.weather;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.pool.ObjectPool;
import com.dazkins.triad.util.pool.factory.RainParticleFactory;

public class WeatherRain extends Weather {
	public float intensity;
	
	public WeatherRain(float i) {
		intensity = i;
	}
	
	public void tick() {
		ArrayList<Chunk> cs = operatingWorld.chunkm.getChunksInAABB(operatingWorld.getCam().getViewportBounds());
		ObjectPool<Particle> op = Particle.particlesPool;
		RainParticleFactory pf = (RainParticleFactory) op.getCurrentFactory();
		for (Chunk c : cs) {
			if (Math.random() < (0.01f * intensity)) {
				AABB a = c.getBounds();
				float xp = (float) (Math.random() * (a.getX1() - a.getX0()) + a.getX0());
				float yp = (float) (Math.random() * (a.getY1() - a.getY0()) + a.getY0()) + 512.0f;
				Particle p = op.getEmptyObjectForCreation();
				if (p != null) {
					pf.create(p, operatingWorld.getCam(), xp, yp, 10, 30, 0.4f, 0.7f, 1.0f);
					operatingWorld.addParticle(p);
				}
			}
		}
	}
}
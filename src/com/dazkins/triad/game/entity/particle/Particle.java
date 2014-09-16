package com.dazkins.triad.game.entity.particle;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.pool.ObjectPool;
import com.dazkins.triad.util.pool.PoolableObject;
import com.dazkins.triad.util.pool.factory.ParticleFactory;

public class Particle extends Entity implements PoolableObject {
	public static ObjectPool<Particle> pool;
	private static ParticleFactory pf;
	
	static {
		pf = new ParticleFactory();
		pool = new ObjectPool<Particle>(Particle.class, pf, 1000);
	}
	
	protected float w;
	protected float h;
	
	protected float r;
	protected float g;
	protected float b;
	
	private int id;
	
	public Particle() {
		super(null, 0, 0, "particle");
	}
	
	public float getW() {
		return w;
	}

	public void renderToPlayerGui(Camera c) { }

	public void render() {
		
	}
	
	public int getID() {
		return id;
	}

	public AABB getAABB() {
		return null;
	}

	public void create(float x, float y, float w, float h, float r, float g, float b) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.r = r;
		this.g = g;
		this.b = b;
	}
}
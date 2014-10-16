package com.dazkins.triad.game.entity.particle;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.pool.ObjectPool;
import com.dazkins.triad.util.pool.PoolableObject;
import com.dazkins.triad.util.pool.factory.ParticleFactory;
import com.dazkins.triad.util.pool.factory.RainParticleFactory;

public class Particle extends Entity implements PoolableObject {
	public static ObjectPool<Particle> particlesPool;
	private static ParticleFactory pf;
	
	static {
		pf = new RainParticleFactory();
		particlesPool = new ObjectPool<Particle>(Particle.class, pf, 1000);
	}
	
	protected float w;
	protected float h;
	
	protected float r;
	protected float g;
	protected float b;
	
	protected float a;
	
	private int id;
	
	protected ParticleBehaviourController pbc;
	
	private BufferObject bo;
	
	public Particle() {
		super(null, 0, 0, "particle");
	}
	
	public void assignPBC(ParticleBehaviourController p) {
		p.assignOperatingParticle(this);
		pbc = p;
	}
	
	public float getW() {
		return w;
	}

	public void renderToPlayerGui(Camera c) { }

	public void render() {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glTranslatef(x, y, 0);
		
		GL11.glColor4f(r, g, b, a);
		bo.render();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	public int getID() {
		return id;
	}

	public AABB getAABB() {
		return new AABB(x, y, x + w, y + h);
	}
	
	public void tick() {
		super.tick();
		pbc.tick();
	}

	public void create(float x, float y, float w, float h, float r, float g, float b, float a, float d) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		lifeTicks = 0;
		
		bo = new BufferObject(36);
		bo.start();
		bo.setBrightness(1.0f);
		bo.setDepth(d);
		bo.addVertex(0, 0);
		bo.addVertex(w, 0);
		bo.addVertex(w, h);
		bo.addVertex(0, h);
		bo.stop();
	}
	
	public float getA() {
		return a;
	}
	
	public void setA(float a) {
		this.a = a;
	}

	public boolean needDestruction() {
		if (a <= -1)
			return true;
		return false;
	}
}
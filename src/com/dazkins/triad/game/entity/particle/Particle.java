package com.dazkins.triad.game.entity.particle;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.pool.ObjectPool;
import com.dazkins.triad.util.pool.PoolableObject;
import com.dazkins.triad.util.pool.factory.ParticleFactory;
import com.dazkins.triad.util.pool.factory.RainParticleFactory;

//TODO rethink particle implementation
public class Particle extends Entity implements PoolableObject
{
	public static ObjectPool<Particle> particlesPool;
	private static ParticleFactory pf;

	public static void setUpParticlePool()
	{
		pf = new RainParticleFactory();
		particlesPool = new ObjectPool<Particle>(Particle.class, pf, 3000);
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

	protected boolean needsDestruction = false;

	public Particle()
	{
		super(null, -1, 0, 0, "particle");
	}

	public void assignPBC(ParticleBehaviourController p)
	{
		p.assignOperatingParticle(this);
		pbc = p;
	}

	public float getW()
	{
		return w;
	}

	public void renderToPlayerGui(Camera c)
	{
	}

	public void render()
	{
		GL11.glPushMatrix();

		GL11.glTranslatef(x, y, 0);

		int xx = (int) x >> 5;
		int yy = (int) y >> 5;

		Color c = new Color(255, 255, 255);

		c = world.getTileColor(xx, yy);

		GL11.glColor4f(r * c.getDR(), g * c.getDG(), b * c.getDB(), a);
		bo.render();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		GL11.glPopMatrix();
	}

	public int getTypeID()
	{
		return id;
	}

	public AABB getAABB()
	{
		return new AABB(x, y, x + w, y + h);
	}

	public void tick()
	{
		super.tick();
		pbc.tick();
	}

	public void create(float x, float y, float w, float h, float r, float g, float b, float a, float d)
	{
		needsDestruction = false;
		toBeRemoved = false;

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
		bo.getData().setDepth(d);
		bo.getData().addVertex(0, 0);
		bo.getData().addVertex(w, 0);
		bo.getData().addVertex(w, h);
		bo.getData().addVertex(0, h);
		bo.stop();
	}

	public float getA()
	{
		return a;
	}

	public void setA(float a)
	{
		this.a = a;
	}

	public void markForDestruction()
	{
		needsDestruction = true;
	}

	public boolean needDestruction()
	{
		return needsDestruction;
	}
}
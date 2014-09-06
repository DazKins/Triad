package com.dazkins.triad.game.entity.particle;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.pool.PoolableObject;

public class Particle extends Entity implements PoolableObject {
	protected float w;
	protected float h;
	
	protected float r;
	protected float g;
	protected float b;
	
	private int id;
	
	public Particle() {
		super(null, 0, 0, null);
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

	public void create(Object[] args) {
		this.w = (Float) args[0];
	}
	
}
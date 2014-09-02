package com.dazkins.triad.game.entity.particle;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class Particle extends Entity {
	protected float w;
	protected float h;
	
	protected float r;
	protected float g;
	protected float b;
	
	private int id;
	
	public Particle(int pID) {
		super(null, 0, 0, null);
		this.id = pID;
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
	
}
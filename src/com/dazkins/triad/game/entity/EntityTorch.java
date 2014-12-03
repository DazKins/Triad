package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class EntityTorch extends Entity implements LightEmitter {
	public EntityTorch(World w, float x, float y) {
		super(w, x, y, "torch");
	}

	public byte getLightStrength() {
		return 14;
	}

	public void render() {
		
	}
	
	public void tick() {
		super.tick();
	}

	public AABB getAABB() {
		return null;
	}

	public void renderToPlayerGui(Camera c) {
		
	}
}
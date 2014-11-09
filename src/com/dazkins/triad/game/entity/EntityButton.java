package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class EntityButton extends TriggerableEntity {
	public EntityButton(World w, float x, float y) {
		super(w, x, y, "button");
	}

	public void renderToPlayerGui(Camera c) {
		
	}

	public void render() {
		this.getModel().render();
	}
	
	public void tick() {
		
	}

	public AABB getAABB() {
		return new AABB(x, y, x + Tile.tileSize, y + Tile.tileSize);
	}

	public void onTrigger() {
		
	}
}
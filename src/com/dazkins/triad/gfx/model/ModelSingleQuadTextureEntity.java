package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Image;

public class ModelSingleQuadTextureEntity extends Model {
	public ModelSingleQuadTextureEntity(Image i) {
		super(i);
		addQuad(new Quad(-16, 0, 32, 32, 0, 0, 16, 16));
	}

	public void render(Entity e) {
		setOffset(e.getX(), e.getY());
		setDepth(Tile.yPosToDepth(e.getY()));
		
		super.render();
	}
}
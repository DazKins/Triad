package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Image;

public class ModelTorch extends ModelSingleQuadTextureEntity
{
	public ModelTorch()
	{
		super(Image.getImageFromName("torch"));
	}
}
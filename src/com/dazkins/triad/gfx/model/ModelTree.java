package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.Image;

public class ModelTree extends ModelSingleQuadTextureEntity
{
	public ModelTree()
	{
		super(Image.getImageFromName("tree"), 0, 0, 32, 46);
	}
}
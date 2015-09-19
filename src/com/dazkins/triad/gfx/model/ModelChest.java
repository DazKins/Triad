package com.dazkins.triad.gfx.model;

import com.dazkins.triad.gfx.Image;

public class ModelChest extends ModelSingleQuadTextureEntity
{
	public ModelChest()
	{
		super(Image.getImageFromName("chest"), 0, 0, 32, 16);
	}
}
package com.dazkins.triad.game.entity.renderer;

import com.dazkins.triad.gfx.model.ModelTree;

public class EntityRendererTree extends EntityRenderer
{
	public void initModel()
	{
		setModel(new ModelTree());
	}
}
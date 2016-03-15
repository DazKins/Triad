package com.dazkins.triad.game.entity.renderer;


import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.ModelHumanoid;

public class EntityRendererPlayer extends EntityRenderer
{
	public void initModel()
	{
		setModel(new ModelHumanoid(Image.getImageFromName("player")));
	}
}
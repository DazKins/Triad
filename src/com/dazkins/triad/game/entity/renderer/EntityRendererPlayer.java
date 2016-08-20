package com.dazkins.triad.game.entity.renderer;


import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.ModelHumanoid;

public class EntityRendererPlayer extends EntityRenderer
{
	public void initModel()
	{
		setModel(new ModelHumanoid(Image.getImageFromName("player")));
	}
	
	public void render(Camera cam)
	{
		if (super.shell != null && super.model != null)
		{
			ModelHumanoid m = (ModelHumanoid) super.model;
			m.setEquipmentInventory(super.shell.getEquipmentInventory());
		}
		
		super.render(cam);
	}
}
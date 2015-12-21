package com.dazkins.triad.game.entity.renderer;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.ModelHumanoid;

public class EntityRendererPlayer extends EntityRenderer
{
	private String name = "";
	
	public void initModel()
	{
		setModel(new ModelHumanoid(Image.getImageFromName("player")));
	}
	
	public void render(Camera cam)
	{
		super.render(cam);
		Font.drawString(name, x - name.length() * 8, y + 64, Tile.yPosToDepthRelativeToCamera(cam, y), 1.0f);
	}
	
	public void setName(String s)
	{
		name = s;
	}
}
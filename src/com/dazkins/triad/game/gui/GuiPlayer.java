package com.dazkins.triad.game.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayerClientController;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.gui.object.GuiObjectStatusBar;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class GuiPlayer extends Gui
{
	private EntityPlayerClientController player;
	private World world;

	private GuiObjectStatusBar statusBar;

	public GuiPlayer(Triad t, InputHandler i, World w, EntityPlayerClientController player)
	{
		super(t, i);
		world = w;
		this.player = player;
		statusBar = new GuiObjectStatusBar(this, 0, 0, 0xff0000, 1024, 1);
	}

	public void tick()
	{
//		statusBar.updateStatus((player.lifeTicks % 60) / 60.0f);
	}

	public void render(Camera cam)
	{
		GL11.glPushMatrix();
		cam.attachTranslation();
		ArrayList<Entity> entities = world.getEntitiesInAABB(cam.getViewportBounds().shiftX0(-100).shiftX1(100).shiftY0(-100).shiftY1(100));
		entities.sort(Entity.ySorter);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if (e instanceof Mob)
			{
				Mob m = (Mob) e;
				m.renderToPlayerGui(cam);
			}
		}
		GL11.glPopMatrix();
	}

	public void onExit()
	{
	}

	public void setupGraphics()
	{
	}
}
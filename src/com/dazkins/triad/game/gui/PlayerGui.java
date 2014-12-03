package com.dazkins.triad.game.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class PlayerGui extends Gui {
	private EntityPlayer player;
	private World world;
	
	private GuiStatusBar statusBar;
	
	public PlayerGui(Triad t, InputHandler i, World w, EntityPlayer player) {
		super(t, i);
		world = w;
		this.player = player;
		statusBar = new GuiStatusBar(0, 0, 0xff0000, 1024);
	}

	public void tick() {
		statusBar.updateStatus((player.lifeTicks % 60) / 60.0f);
	}

	public void render(Camera cam) {
		GL11.glPushMatrix();
		GL11.glPushMatrix();
		cam.attachTranslation();
		ArrayList<Entity> entities = world.getEntitiesInAABB(cam.getViewportBounds().shiftX0(-100).shiftX1(100).shiftY0(-100).shiftY1(100));
		entities.sort(Entity.ySorter);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof Mob) {
				Mob m = (Mob) e;
				m.renderToPlayerGui(cam);
			}
		}
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	public void onExit() { }

	public void setupGraphics() { }
}
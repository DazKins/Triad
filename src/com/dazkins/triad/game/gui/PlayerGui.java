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
	
	private GuiBox mainBox;
	private GuiStatusBar statusBar;
	
	public PlayerGui(Triad t, InputHandler i, World w, EntityPlayer player) {
		super(t, i);
		world = w;
		this.player = player;
		mainBox = new GuiBox(0, 0, 1000, 400, 1, false);
		statusBar = new GuiStatusBar(0, 0, 0xff0000, 1024);
	}

	public void tick() {
		statusBar.updateStatus((player.lifeTicks % 60) / 60.0f);
	}

	public void render(Camera cam) {
		GL11.glPushMatrix();
		cam.attachTranslation();
		ArrayList<Entity> entities = world.getEntitiesInAABB(cam.getViewportBounds());
		entities.sort(new Comparator<Entity>() {
			public int compare(Entity o1, Entity o2) {
				return (o1.getY() > o2.getY()) ? -1 : 1;
			}
		});
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof Mob) {
				Mob m = (Mob) e;
				m.renderToPlayerGui(cam);
			}
		}
		GL11.glPopMatrix();
	}

	public void onExit() {
		
	}

	public void setupGraphics() {
		
	}
}
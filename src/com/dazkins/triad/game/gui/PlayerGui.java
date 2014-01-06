package com.dazkins.triad.game.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
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
		mainBox = new GuiBox(0, 0, 1000, 400);
		statusBar = new GuiStatusBar(0, 0, 0xff0000, 1024);
	}

	public void tick() {
		statusBar.updateStatus((player.lifeTicks % 60) / 60.0f);
	}

	public void render(Camera cam) {
		GL11.glPushMatrix();
		cam.attachTranslation();
		ArrayList<Entity> entities = world.getEntitiesInAABB(cam.getViewportBounds());
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).renderToPlayerGui();
		}
		GL11.glPopMatrix();
	}
}
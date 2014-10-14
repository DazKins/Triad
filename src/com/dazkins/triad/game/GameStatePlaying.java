package com.dazkins.triad.game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.game.entity.particle.Particle;
import com.dazkins.triad.game.entity.particle.RainParticleBehaviourController;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.GuiEquipMenu;
import com.dazkins.triad.game.gui.GuiInventory;
import com.dazkins.triad.game.gui.PlayerGui;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.WindowInfo;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.pool.factory.RainParticleFactory;

public class GameStatePlaying implements GameState {
	private Triad triad;
	private World world;
	private InputHandler input;
	private EntityPlayer player;
	private Camera cam;
	
	private Gui currentlyDisplayedGui;
	private WindowInfo winInf;
	
	public void init(Triad triad) {
		this.triad = triad;
		winInf = triad.winInfo;
		input = new InputHandler();
		cam = new Camera(input, triad.winInfo, 0, 0);
		cam.lockZoom(0.0001f, 500f);
		changeWorld("TestingMap");
		player = new EntityPlayer(world, 0, 0, input);
		currentlyDisplayedGui = new PlayerGui(triad, input, world, player);
		world.addEntity(new EntityZombie(world, 500, 650));
		world.addEntity(player);
	}
	
	public void tick() {
		if (input.isKeyJustDown(Keyboard.KEY_I)) {
			if (currentlyDisplayedGui instanceof GuiInventory)
				changeGui(new PlayerGui(triad, input, world, player));
			else
				changeGui(new GuiInventory(triad, input, player.getInventory()));
		}
		if (input.isKeyJustDown(Keyboard.KEY_E)) {
			if (currentlyDisplayedGui instanceof GuiEquipMenu)
				changeGui(new PlayerGui(triad, input, world, player));
			else
				changeGui(new GuiEquipMenu(triad, input, player.getEquipmentInventory()));
		}
		if (input.isKeyJustDown(Keyboard.KEY_ESCAPE)) {
			changeGui(new PlayerGui(triad, input, world, player));
		}
		world.tick();
		cam.tick();
		cam.lockCameraToEntity(player);
		currentlyDisplayedGui.tick();
		input.tick();
	}
	
	public void render() {
		GL11.glPushMatrix();
		cam.attachTranslation();
		world.assignCamera(cam);
		world.render();
		GL11.glPopMatrix();
		
		if (!(currentlyDisplayedGui instanceof PlayerGui)) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
				GL11.glVertex3f(0.0f, 0.0f, -0.1f);
				GL11.glVertex3f(winInf.getW(), 0.0f, -0.1f);
				GL11.glVertex3f(winInf.getW(), winInf.getH(), -0.1f);
				GL11.glVertex3f(0.0f, winInf.getH(), -0.1f);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		
		currentlyDisplayedGui.render(cam);
	}
	
	public void changeWorld(String newWorld) {
		world = World.getWorldFromName(newWorld);
		cam.setBounds(0, 0, world.info.nChunksX * Chunk.chunkW * Tile.tileSize, world.info.nChunksX * Chunk.chunkH * Tile.tileSize);
	}
	
	private void changeGui(Gui g) {
		currentlyDisplayedGui.onExit();
		currentlyDisplayedGui = g;
	}
}
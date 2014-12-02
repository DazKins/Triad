package com.dazkins.triad.game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.EntityButton;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.GuiEquipMenu;
import com.dazkins.triad.game.gui.GuiInventory;
import com.dazkins.triad.game.gui.PlayerGui;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.game.world.weather.RainWeather;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;

public class GameStatePlaying implements GameState {
	private Triad triad;
	private World world;
	private InputHandler input;
	private EntityPlayer player;
	private Camera cam;
	
	private Gui currentlyDisplayedGui;
	private Window win;
	
	public void init(Triad triad) {
		this.triad = triad;
		win = triad.win;
		input = new InputHandler(win);
		cam = new Camera(input, triad.win, 0, 0);
		cam.lockZoom(0.0001f, 500f);
		changeWorld(World.testWorld);
		player = new EntityPlayer(world, 100, 100, input);
		currentlyDisplayedGui = new PlayerGui(triad, input, world, player);
		
		for (int i = 0; i < 1; i++)
			world.addEntity(new EntityZombie(world, (float) Math.random() * 200 + 100, (float) Math.random() * 200 + 100));
		
		world.addEntity(player);
		
		world.assignCamera(cam);
		world.setWeather(new RainWeather(10));
		
		EntityButton b = new EntityButton(world, 64.0f, 64.0f);
		world.addEntity(b);
	}
	
	public void tick() {
		if (input.isKeyJustDown(GLFW.GLFW_KEY_I)) {
			if (currentlyDisplayedGui instanceof GuiInventory)
				changeGui(new PlayerGui(triad, input, world, player));
			else
				changeGui(new GuiInventory(triad, input, player));
		}
		if (input.isKeyJustDown(GLFW.GLFW_KEY_E)) {
			if (currentlyDisplayedGui instanceof GuiEquipMenu)
				changeGui(new PlayerGui(triad, input, world, player));
			else
				changeGui(new GuiEquipMenu(triad, input, player));
		}
		if (input.isKeyJustDown(GLFW.GLFW_KEY_ESCAPE)) {
			changeGui(new PlayerGui(triad, input, world, player));
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && input.mouse1JustDown) {
			int mx = (int) ((input.mouseX / cam.getZoom()) + cam.getX()) >> 5;
			int my = (int) ((input.mouseY / cam.getZoom()) + cam.getY()) >> 5;
			System.err.println("Tile location: " + mx + ", " + my);
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
//		world.renderGrid();
		GL11.glPopMatrix();
		
		if (!(currentlyDisplayedGui instanceof PlayerGui)) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
				GL11.glVertex3f(0.0f, 0.0f, -0.1f);
				GL11.glVertex3f(win.getW(), 0.0f, -0.1f);
				GL11.glVertex3f(win.getW(), win.getH(), -0.1f);
				GL11.glVertex3f(0.0f, win.getH(), -0.1f);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		
		currentlyDisplayedGui.render(cam);
	}
	
	public void changeWorld(World w) {
		world = w;
		cam.setBounds(0, 0, world.nChunksX * Chunk.chunkW * Tile.tileSize, world.nChunksX * Chunk.chunkH * Tile.tileSize);
	}
	
	private void changeGui(Gui g) {
		currentlyDisplayedGui.onExit();
		currentlyDisplayedGui = g;
	}
}
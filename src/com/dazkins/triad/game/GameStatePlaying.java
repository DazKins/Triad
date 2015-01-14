package com.dazkins.triad.game;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.Activeatable;
import com.dazkins.triad.game.entity.EntityButton;
import com.dazkins.triad.game.entity.EntityDoor;
import com.dazkins.triad.game.entity.EntityTorch;
import com.dazkins.triad.game.entity.EntityTree;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.GuiEquipMenu;
import com.dazkins.triad.game.gui.GuiInventory;
import com.dazkins.triad.game.gui.PlayerGui;
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
		cam.lockZoom(0.56f, 500f);
		world = new World();
		player = new EntityPlayer(world, 100, 100, input);
		currentlyDisplayedGui = new PlayerGui(triad, input, world, player);
		
//		for (int i = 0; i < 20; i++)
//			world.addEntity(new EntityZombie(world, (float) Math.random() * 200 + 100, (float) Math.random() * 200 + 100));
		
		world.addEntity(player);
		
		world.assignCamera(cam);
		world.setWeather(new RainWeather(10));
		
		ArrayList<Activeatable> a = new ArrayList<Activeatable>();
		
		EntityDoor d0 = new EntityDoor(world, 200.0f, 400.0f, Facing.DOWN);
		EntityDoor d1 = new EntityDoor(world, 300.0f, 400.0f, Facing.UP);
		EntityDoor d2 = new EntityDoor(world, 400.0f, 400.0f, Facing.LEFT);
		EntityDoor d3 = new EntityDoor(world, 500.0f, 400.0f, Facing.RIGHT);
		
		a.add(d0);
		a.add(d1);
		a.add(d2);
		a.add(d3);
		
		world.addEntity(d0);
		world.addEntity(d1);
		world.addEntity(d2);
		world.addEntity(d3);
		
		world.addEntity(new EntityTree(world, 200, 200));

		EntityButton b = new EntityButton(world, 200.0f, 200.0f, a);
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
		world.tick();
		cam.tick();
		cam.lockCameraToEntity(player);
		currentlyDisplayedGui.tick();
		if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && input.mouse1JustDown) {
			int mx = (int) ((input.mouseX / cam.getZoom()) + cam.getX()) >> 5;
			int my = (int) ((input.mouseY / cam.getZoom()) + cam.getY()) >> 5;
			System.err.println("Tile position: " + mx + ", " + my);
			System.err.println(world.getTileColor(mx, my));
		}
		if (input.isKeyJustDown(GLFW.GLFW_KEY_Q)) {
			world.addEntity(new EntityTorch(world, player.getX(), player.getY()));
		}
		input.tick();
	}
	
	public void render() {
		GL11.glPushMatrix();
		cam.attachTranslation();
		world.assignCamera(cam);
		world.render();
		
//		world.renderGrid();
		GL11.glPopMatrix();
		
//		if (!(currentlyDisplayedGui instanceof PlayerGui)) {
//			GL11.glDisable(GL11.GL_TEXTURE_2D);
//			GL11.glBegin(GL11.GL_QUADS);
//				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
//				GL11.glVertex3f(0.0f, 0.0f, -0.1f);
//				GL11.glVertex3f(win.getW(), 0.0f, -0.1f);
//				GL11.glVertex3f(win.getW(), win.getH(), -0.1f);
//				GL11.glVertex3f(0.0f, win.getH(), -0.1f);
//				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//			GL11.glEnd();
//			GL11.glEnable(GL11.GL_TEXTURE_2D);
//		}
//		
		currentlyDisplayedGui.render(cam);
	}
	
	private void changeGui(Gui g) {
		currentlyDisplayedGui.onExit();
		currentlyDisplayedGui = g;
	}
}
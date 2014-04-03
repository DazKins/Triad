package com.dazkins.triad.game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.EntityTorch;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.GuiEquipMenu;
import com.dazkins.triad.game.gui.GuiInventory;
import com.dazkins.triad.game.gui.PlayerGui;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class GameStatePlaying implements GameState {
	private Triad triad;
	private World world;
	private InputHandler input;
	private EntityPlayer player;
	private Camera cam;
	
	private Gui currentlyDisplayedGui;
	
	
	public void init(Triad triad) {
		this.triad = triad;
		input = new InputHandler();
		cam = new Camera(input, triad.winInfo, 0, 0);
		cam.lockZoom(0.0001f, 500f);
		changeWorld("TestingMap");
		player = new EntityPlayer(world, 0, 0, input);
		currentlyDisplayedGui = new PlayerGui(triad, input, world, player);
		world.addEntity(player);
		world.addEntity(new EntityZombie(world, 500, 500));
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
		world.tick();
		cam.tick();
		cam.lockCameraToEntity(player);
		currentlyDisplayedGui.tick();
		input.tick();
	}
	
	public void render() {
		GL11.glPushMatrix();
		cam.attachTranslation();
		world.render(cam);
		GL11.glPopMatrix();
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
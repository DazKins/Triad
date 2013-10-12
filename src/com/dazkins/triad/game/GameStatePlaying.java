package com.dazkins.triad.game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.EntityTorch;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.gui.Gui;
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
	private Gui gui;
	
	public void init(Triad triad) {
		this.triad = triad;
		input = new InputHandler();
		cam = new Camera(input, triad.viewport, 0, 0);
		cam.lockZoom(0.0001f, 500f);
		changeWorld("TestingMap");
		player = new EntityPlayer(world, 0, 0, input);
		gui = new PlayerGui(triad, input, player);
		world.addEntity(player);
	}
	
	private int cooldown;
	
	public void tick() {
		world.tick();
		cam.tick();
		cam.lockCameraToEntity(player);
		gui.tick();
		input.tick();
		
		if (input.isKeyDown(Keyboard.KEY_SPACE) && cooldown > 20) {
			world.addEntity(new EntityTorch(world, player.getX(), player.getY()));
			cooldown = 0;
		}
		cooldown++;
	}
	
	public void render() {
		GL11.glPushMatrix();
		cam.attachTranslation();
		world.render(cam);
		world.renderGrid(cam);
		GL11.glPopMatrix();
	}
	
	public void changeWorld(String newWorld) {
		world = World.getWorldFromName(newWorld);
		cam.setBounds(0, 0, world.info.nChunksX * Chunk.chunkW * Tile.tileSize, world.info.nChunksX * Chunk.chunkH * Tile.tileSize);
	}
}
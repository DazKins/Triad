package com.dazkins.triad.game;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
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
		
		world = new World();
		input = new InputHandler();
		player = new EntityPlayer(world, 0, 0, input);
		cam = new Camera(input, 0, 0, triad.WIDTH, triad.HEIGHT);
		cam.setBounds(0, 0, world.nChunksX * Chunk.chunkW * Tile.tileSize, world.nChunksY * Chunk.chunkH * Tile.tileSize);
		gui = new PlayerGui(triad, input, player);
	}
	
	public void tick() {
		world.tick();
		cam.lockCameraToEntity(player);
		gui.tick();
		input.tick();
		player.tick();
	}
	
	public void render() {
		GL11.glPushMatrix();
		cam.attachTranslation();
		world.render(cam);
		player.render();
		GL11.glPopMatrix();
		gui.render();
	}
}
package com.dazkins.triad.game;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.EntityPlayerClientController;
import com.dazkins.triad.game.entity.renderer.EntityRendererPlayer;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.networking.client.ClientWorldManager;
import com.dazkins.triad.networking.client.TriadClient;

public class GameStatePlaying implements GameState
{
	private Triad triad;

	private ClientWorldManager cwm;

	private InputHandler input;
	private Camera cam;

	private Gui currentlyDisplayedGui;
	private Window win;

	private TriadClient client;

	private EntityPlayerClientController player;

	public void init(Triad triad)
	{
		win = triad.win;
		this.triad = triad;
		input = new InputHandler(win);
		cam = new Camera(input, triad.win, 0, 0);
		cam.lockZoom(0.56f, 8f);

		player = new EntityPlayerClientController("", 0, 0, input);
	}

	public void initClient(TriadClient c)
	{
		client = c;
		cwm = new ClientWorldManager(client, cam);
		cwm.setPlayer(player);
	}

	public void tick()
	{
		if (!client.isRunning())
			client.start();
		
		cam.tick();

		player.tick();
		
		client.updatePlayerVelocity(player.getXA(), player.getYA());
		cwm.clientUpdatePlayer(player.getX(), player.getY(), player.getFacing());
		
		if (currentlyDisplayedGui != null)
			currentlyDisplayedGui.tick();
		
		// if (input.isKeyJustDown(GLFW.GLFW_KEY_G)) {
		// world.addEntity(new EntityTorch(world, player.getX(),
		// player.getY()));
		// DebugMonitor.addMessage("Torch added at: (" + player.getX() + "," +
		// player.getY() + ")");
		// }
		// DebugMonitor.setVariableValue("Tile light", world.getTileColor((int)
		// player.getX() / Tile.tileSize, (int) player.getY() / Tile.tileSize));
		// DebugMonitor.setVariableValue("Player position", player.getX() + " "
		// + player.getY());
		// DebugMonitor.setVariableValue("Player chunk", (int) ((player.getX() /
		// Tile.tileSize) / Chunk.chunkS) + " " + (int) ((player.getY() /
		// Tile.tileSize) / Chunk.chunkS));
		
		input.tick();

		cwm.tick();
		
		cam.lockCameraToEntity(player);
	}

	public void render()
	{
		GL11.glPushMatrix();
		cam.attachTranslation();

		cwm.render();

		GL11.glPopMatrix();

		if (currentlyDisplayedGui != null)
			currentlyDisplayedGui.render(cam);
	}

	private void changeGui(Gui g)
	{
		currentlyDisplayedGui.onExit();
		currentlyDisplayedGui = g;
	}
}
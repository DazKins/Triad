package com.dazkins.triad.game;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.chat.Chat;
import com.dazkins.triad.game.entity.mob.EntityPlayerClientController;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.GuiChat;
import com.dazkins.triad.game.gui.GuiSingleInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.networking.client.ClientWorldManager;
import com.dazkins.triad.networking.client.TriadClient;
import com.dazkins.triad.networking.client.update.ClientUpdateChatMessage;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;

public class GameStatePlaying implements GameState
{
	private Triad triad;

	private ClientWorldManager cwm;

	private InputHandler input;
	private Camera cam;
	
	private Chat chat;
	private GuiChat chatGui;

	private Gui currentlyDisplayedGui;
	private Window win;

	private TriadClient client;

	private EntityPlayerClientController player;

	public void init(Triad triad, InputHandler inp)
	{
		win = triad.win;
		this.triad = triad;
		input = inp;
		cam = new Camera(input, triad.win, 0, 0);
		cam.lockZoom(0.56f, 8f);
		
		chat = new Chat();
		chatGui = new GuiChat(triad, input, chat);

		player = new EntityPlayerClientController("", 0, 0, input);
	}

	public void initClient(TriadClient c)
	{
		client = c;
		chat.initClient(client);
		cwm = new ClientWorldManager(client, cam);
		cwm.setPlayer(player);
	}

	public void tick()
	{
		if (!client.isRunning())
			client.start();
		
		if (client.isLoginAccepted())
		{
			if (!client.hasSentReadyToReceive())
				client.markReadyToReceive();
			
			cam.tick();
	
			player.tick();
			
			client.updatePlayerVelocity(player.getXA(), player.getYA());
			cwm.clientUpdatePlayer(player.getX(), player.getY(), player.getFacing());
			
			if (currentlyDisplayedGui != null)
				currentlyDisplayedGui.tick();
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_E))
			{
				client.sendInteractionRequest();
			}
			
			Inventory inv = player.getInventory();
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_I))
			{
				changeGui(new GuiSingleInventory(inv, cwm.getPlayerEntityShell().getGlobalID(), triad, input, client));
			}
			
			if (inv.hasChanged())
			{
				client.updatePlayerInventory(inv);
			}
			inv.resetHasChangedFlag();
			
			EntityShell playerEntityShell = cwm.getPlayerEntityShell();
			if (playerEntityShell != null)
			{
				player.setInteractingObject(playerEntityShell.getInteractingEntity());
			}
			
			EntityShell interactingObject = player.getInteractingObject();
			if (interactingObject != null)
			{
				Inventory i = interactingObject.getInventory();
				if (i != null && currentlyDisplayedGui == null)
				{
					changeGui(new GuiSingleInventory(interactingObject, triad, input, client));
				}
			}
			
			if (currentlyDisplayedGui != null)
			{
				if (input.isKeyJustDown(GLFW.GLFW_KEY_ESCAPE))
				{
					client.cancelInteraction();
					changeGui(null);
				}
			}
			
//			if (currentlyDisplayedGui instanceof GuiSingleInventory)
//			{
//				if (player.getInteractingObject() == null)
//				{
//					changeGui(null);
//				}
//			}
	
			cwm.tick();
			
			cam.centreOnLocation(player.getX(), player.getY());
			
			chat.tick();
			chatGui.tick();
		}
		
		DebugMonitor.setVariableValue("Packets sent", client.getPacketSendCount());
		DebugMonitor.setVariableValue("Packets received", client.getPacketReceiveCount());
		
		client.resetCounters();
	}

	public void render()
	{
		GL11.glPushMatrix();
		cam.attachTranslation();

		cwm.render();

		GL11.glPopMatrix();
		
		chatGui.render();

		if (currentlyDisplayedGui != null)
			currentlyDisplayedGui.render(cam);
	}

	private void changeGui(Gui g)
	{
		if (currentlyDisplayedGui != null)
			currentlyDisplayedGui.onExit();
		
		currentlyDisplayedGui = g;
	}
}
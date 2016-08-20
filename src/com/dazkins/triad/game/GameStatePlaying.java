package com.dazkins.triad.game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.chat.Chat;
import com.dazkins.triad.game.entity.mob.PlayerClientController;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.GuiChat;
import com.dazkins.triad.game.gui.GuiPlayerAbilityBar;
import com.dazkins.triad.game.gui.GuiSingleInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.networking.client.ClientWorldManager;
import com.dazkins.triad.networking.client.TriadClient;
import com.dazkins.triad.util.TriadLogger;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;

public class GameStatePlaying implements GameState
{
	private Triad triad;

	private ClientWorldManager cwm;

	private InputHandler input;
	private Camera cam;
	
	private Chat chat;
	private GuiChat chatGui;
	private GuiPlayerAbilityBar guiPlayerAbilityBar;
	private Gui currentlyDisplayedGui;
	private Window win;

	private TriadClient client;

	private PlayerClientController player;

	public void init(Triad triad, InputHandler inp)
	{
		win = triad.win;
		this.triad = triad;
		input = inp;
		cam = new Camera(input, triad.win, 0, 0);
		cam.lockZoom(0.56f, 8f);
		
		chat = new Chat();
		chatGui = new GuiChat(triad, input, chat);
		guiPlayerAbilityBar = new GuiPlayerAbilityBar(triad, input);

		player = new PlayerClientController("", 0, 0, input);
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
			
			if (currentlyDisplayedGui != null)
				currentlyDisplayedGui.tick();
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_E))
			{
				client.sendInteractionRequest();
			}
			
			EntityShell playerEntityShell = cwm.getPlayerEntityShell();
			if (playerEntityShell != null)
			{
					Inventory inv = playerEntityShell.getInventory();
					AbilityBar ab = playerEntityShell.getAbilityBar();
					
					if (ab != null)
					{
						if (!guiPlayerAbilityBar.hasAbilityBar())
							guiPlayerAbilityBar.setAbilityBar(ab);
					}
					
					if (input.isKeyJustDown(GLFW.GLFW_KEY_I))
					{
						changeGui(new GuiSingleInventory(inv, cwm.getPlayerEntityShell().getGlobalID(), triad, input, client));
					}
				
					if (inv.getAndPurgeHasChangedFlag())
					{
						client.updatePlayerInventory(inv);
					}
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
		
		client.tick();
		client.resetCounters();
	}

	public void render()
	{
		GL11.glPushMatrix();
		cam.attachTranslation();

		cwm.render();

		GL11.glPopMatrix();
		
		chatGui.render();
		guiPlayerAbilityBar.render();

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
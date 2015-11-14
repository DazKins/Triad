package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.gui.GuiMainMenu;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.networking.server.TriadServer;

public class GameStateMainMenu implements GameState
{
	private Triad triad;
	
	private GuiMainMenu mainMenu;

	private InputHandler input;

	public void init(Triad triad)
	{
		this.triad = triad;
		Window win = triad.win;
		input = new InputHandler(win);
		
		mainMenu = new GuiMainMenu(triad, input, this);
	}
	
	public void onPlayButtonPress()
	{
		TriadServer.main(null);
		triad.setGameState(new GameStatePlaying());
	}

	public void render()
	{
		mainMenu.render(null);
	}

	public void tick()
	{
		mainMenu.tick();
		input.tick();
	}
}
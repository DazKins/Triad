package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.gui.GuiMainMenu;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;

public class GameStateMainMenu implements GameState
{
	private Triad triad;
	
	private GuiMainMenu mainMenu;

	private InputHandler input;

	public void init(Triad triad, InputHandler inp)
	{
		this.triad = triad;
		Window win = triad.win;
		input = inp;
		
		mainMenu = new GuiMainMenu(triad, input, this);
	}
	
	public void onPlayButtonPress()
	{
		triad.setGameState(new GameStatePlaying());
	}

	public void render()
	{
		RenderFormatManager.TEXT.setColour(new Color(0));
		mainMenu.render(null);
	}

	public void tick()
	{
		mainMenu.tick();
		input.tick();
	}
}
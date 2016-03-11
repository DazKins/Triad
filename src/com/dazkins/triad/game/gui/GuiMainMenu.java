package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.GameStateMainMenu;
import com.dazkins.triad.game.gui.event.GuiEventButtonPress;
import com.dazkins.triad.game.gui.object.GuiObjectButton;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.util.TriadLogger;

public class GuiMainMenu extends Gui
{
	private GuiObjectButton playButton;
	private GameStateMainMenu menu;
	
	public GuiMainMenu(Triad t, InputHandler i, GameStateMainMenu m)
	{
		super(t, i);
		
		menu = m;
		
		playButton = new GuiObjectButton(this, "Play", 300, 300, 100, 1);
		playButton.setPressEvent(new GuiEventButtonPress()
		{
			public void onPress()
			{
				menu.onPlayButtonPress();
			}
		});
	}

	public void render(Camera cam)
	{
		super.render(cam);
		playButton.render();
	}

	public void onExit()
	{
		
	}

	public void setupGraphics()
	{
		
	}
	
}
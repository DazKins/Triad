package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.GameStateMainMenu;
import com.dazkins.triad.game.gui.event.GuiEventButtonPress;
import com.dazkins.triad.game.gui.object.GuiObjectButton;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.input.InputHandler;

public class GuiMainMenu extends Gui
{
	private GuiObjectButton playButton;
	private GameStateMainMenu menu;
	
	public GuiMainMenu(Triad t, InputHandler i, GameStateMainMenu m)
	{
		super(t, i);
		
		menu = m;
		
		playButton = new GuiObjectButton(this);
		
		playButton.setX(300);
		playButton.setY(300);
		playButton.setWidth(150);
		playButton.setContent("Play \\n test setset set set sgfdf geh rh");
		playButton.setLayer(1);
		playButton.setPressEvent(new GuiEventButtonPress()
		{
			public void onPress()
			{
				menu.onPlayButtonPress();
			}
		});
		
		setupGraphics();
	}

	public void render(RenderContext rc, Camera cam)
	{
		super.render(rc, cam);
		playButton.render(rc);
		
		RenderFormatManager.TEXT.setColour(new Color(255, 255, 255));
		TTF.renderStringWithFormating(rc, "TEST SETS ET #ff00ff sdglhr 0823y 5luetguh #ff0000 083tg24pty-gdlsdg 23t ", 0, 0, 1.0f);
	}

	public void onExit()
	{
		
	}
}
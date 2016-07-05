package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.gui.object.GuiObjectTextBox;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.input.InputHandler;

public class GuiChat extends Gui
{
	private GuiObjectTextBox mainChat;
	
	public GuiChat(Triad t, InputHandler i)
	{
		super(t, i);
		setupGraphics();
	}

	public void onExit()
	{
		
	}

	public void setupGraphics()
	{
		mainChat = new GuiObjectTextBox(this, "TESTING", 0, 0, 500, 1);
	}
	
	public void render()
	{
		RenderFormatManager.BOX.setRenderStyle(2);
		mainChat.render();
		RenderFormatManager.BOX.reset();
	}
}
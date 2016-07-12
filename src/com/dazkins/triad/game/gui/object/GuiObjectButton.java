package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.event.GuiEventButtonPress;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class GuiObjectButton extends GuiObjectTextBox
{
	private GuiEventButtonPress event;
	
	public GuiObjectButton(Gui g)
	{
		super(g);
	}
	
	public void setPressEvent(GuiEventButtonPress g)
	{
		event = g;
	}
	
	public void tick()
	{
		InputHandler input = gui.getInputHandler();
		
		if (input.mouse1JustDown)
		{
			if (getBounds().intersects(input.mouseX, input.mouseY))
				event.onPress();
		}
	}
}
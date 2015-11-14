package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.event.GuiEventButtonPress;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class GuiObjectButton extends GuiObject
{
	private GuiObjectTextBox box;
	
	private GuiEventButtonPress event;
	
	public GuiObjectButton(Gui g, String t, float x, float y, float w, int l)
	{
		super(g, l);
		box = new GuiObjectTextBox(g, t, x, y, w, l);
	}
	
	public void setPressEvent(GuiEventButtonPress g)
	{
		event = g;
	}
	
	public AABB getBounds()
	{
		return box.getBounds();
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
	
	public void render()
	{
		box.render();
	}
}
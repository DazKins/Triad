package com.dazkins.triad.game.gui;

import java.util.ArrayList;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.gui.object.GuiObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;

public abstract class Gui
{
	protected Triad triad;
	protected InputHandler input;
	protected Window win;
	
	protected ArrayList<GuiObject> objects;

	public Gui(Triad t, InputHandler i)
	{
		triad = t;
		input = i;
		win = t.win;
		objects = new ArrayList<GuiObject>();
	}

	public void tick()
	{
		if (win.wasResized())
			setupGraphics();
		
		for (GuiObject o : objects)
		{
			o.tick();
		}
	}
	
	public void addObject(GuiObject o)
	{
		objects.add(o);
	}

	public abstract void render(Camera cam);

	public abstract void onExit();

	public abstract void setupGraphics();
	
	public InputHandler getInputHandler()
	{
		return input;
	}
}
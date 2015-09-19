package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;

public abstract class Gui
{
	protected Triad triad;
	protected InputHandler input;
	protected Window win;

	public Gui(Triad t, InputHandler i)
	{
		triad = t;
		input = i;
		win = t.win;
	}

	public void tick()
	{
		if (win.wasResized())
			setupGraphics();
	}

	public abstract void render(Camera cam);

	public abstract void onExit();

	public abstract void setupGraphics();
}
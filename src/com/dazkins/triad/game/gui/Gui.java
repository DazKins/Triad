package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.WindowInfo;
import com.dazkins.triad.input.InputHandler;

public abstract class Gui {
	protected Triad triad;
	protected InputHandler input;
	protected WindowInfo winInfo;
	
	public Gui(Triad t, InputHandler i) {
		triad = t;
		input = i;
		winInfo = t.winInfo;
	}
	
	public abstract void tick();
	
	public abstract void render(Camera cam);
	
	public abstract void onExit();
}
package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.input.InputHandler;

public abstract class Gui {
	private Triad triad;
	private InputHandler input;
	
	public Gui(Triad t, InputHandler i) {
		triad = t;
		input = i;
	}
	
	public abstract void tick();
	
	public abstract void render(Bitmap b);
}
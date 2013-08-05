package com.dazkins.triad.game.gui;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.input.InputHandler;

public abstract class Gui {
	protected Triad triad;
	protected InputHandler input;
	
	public Gui(Triad t, InputHandler i) {
		triad = t;
		input = i;
	}
	
	public static void renderStatusBar(int xp, int yp, int fillColor, int length, float percent) {
		for (int x = 0; x < length; x++) {
			int sx = x == 0 ? 0 : x == length - 1 ? 2 : 1;
//			Image.iconSheet.renderSprite(sx * 8, 0, 8, 8, x *16 + xp, yp, 16, 16, 1.0f);
		}
		int fillAmmount = (int) (percent != 1 ? percent * (length << 3) : percent * (length << 3) - 4);
		
	}
	
	public abstract void tick();
	
	public abstract void render();
}
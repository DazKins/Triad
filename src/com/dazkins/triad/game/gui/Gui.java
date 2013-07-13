package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.GLRenderer;
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
//			Image.iconSheet.renderSprite(sx, 0, (x << 3) + xp, yp);
		}
		int fillAmmount = (int) (percent != 1 ? percent * (length << 3) : percent * (length << 3) - 4);
		for (int x = 2; x < fillAmmount; x++) {
			for (int y = 2; y < 6; y++) {
				//TODO : Implement health bar filling
			}
		}
	}
	
	public static void renderGuiBox(GLRenderer g, int xp, int yp, int w, int h) {
		int tx = xp >> 3;
		int ty = yp >> 3;
		int tw = w >> 3;
		int th = h >> 3;
		for (int x = tx; x < tw + tx; x++) {
			for (int y = ty; y < th + ty; y++) {
				if (x == tx && y == ty)
					Image.iconSheet.renderSprite(g, 3 * 8, 0, 8, 8, x << 3, y << 3, 8, 8);
				else if (x == tx && y == ty + th - 1)
					Image.iconSheet.renderSprite(g, 3 * 8, 2 * 8, 8, 8, x << 3, y << 3, 8, 8);
				else if (x == tx + tw - 1 && y == ty)
					Image.iconSheet.renderSprite(g, 5 * 8, 0, 8, 8, x << 3, y << 3, 8, 8);
				else if (x == tx + tw - 1 && y == ty + th - 1)
					Image.iconSheet.renderSprite(g, 5 * 8, 2 * 8, 8, 8, x << 3, y << 3, 8, 8);
				else if (x == tx)
					Image.iconSheet.renderSprite(g, 3 * 8, 8, 8, 8, x << 3, y << 3, 8, 8);
				else if (y == ty)
					Image.iconSheet.renderSprite(g, 4 * 8, 2 * 8, 8, 8, x << 3, y << 3, 8, 8);
				else if (x == tx + tw - 1)
					Image.iconSheet.renderSprite(g, 5 * 8, 8, 8, 8, x << 3, y << 3, 8, 8);
				else if (y == ty + th - 1)
					Image.iconSheet.renderSprite(g, 4 * 8, 0, 8, 8, x << 3, y << 3, 8, 8);
				else 
					Image.iconSheet.renderSprite(g, 4 * 8, 1 * 8, 8, 8, x << 3, y << 3, 8, 8);
			}
		}
	}
	
	public abstract void tick();
	
	public abstract void render(GLRenderer g);
}
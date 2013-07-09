package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.input.InputHandler;

public abstract class Gui {
	protected Triad triad;
	protected InputHandler input;
	
	public Gui(Triad t, InputHandler i) {
		triad = t;
		input = i;
	}
	
	public static void renderGuiBox(Bitmap b, int xp, int yp, int w, int h) {
		int tx = xp >> 3;
		int ty = yp >> 3;
		int tw = w >> 3;
		int th = h >> 3;
		for (int x = tx; x < tw + tx; x++) {
			for (int y = ty; y < th + ty; y++) {
				if (x == tx && y == ty)
					Art.iconSheet.renderSprite(3, 0, b, x << 3, y << 3);
				else if (x == tx && y == ty + th - 1)
					Art.iconSheet.renderSprite(3, 2, b, x << 3, y << 3);
				else if (x == tx + tw - 1 && y == ty)
					Art.iconSheet.renderSprite(5, 0, b, x << 3, y << 3);
				else if (x == tx + tw - 1 && y == ty + th - 1)
					Art.iconSheet.renderSprite(5, 2, b, x << 3, y << 3);
				else if (x == tx)
					Art.iconSheet.renderSprite(3, 1, b, x << 3, y << 3);
				else if (y == ty)
					Art.iconSheet.renderSprite(4, 0, b, x << 3, y << 3);
				else if (x == tx + tw - 1)
					Art.iconSheet.renderSprite(5, 1, b, x << 3, y << 3);
				else if (y == ty + th - 1)
					Art.iconSheet.renderSprite(4, 2, b, x << 3, y << 3);
				else 
					Art.iconSheet.renderSprite(4, 1, b, x << 3, y << 3);
			}
		}
	}
	
	public abstract void tick();
	
	public abstract void render(Bitmap b);
}
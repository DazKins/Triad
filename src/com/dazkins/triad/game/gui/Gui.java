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
	
	public static void renderGuiBox(int xp, int yp, int w, int h) {
		//Top left corner
//		Image.iconSheet.renderSprite(3 * 8, 0, 8, 8, xp, yp + h - 16, 16, 16, 1.0f);
//		//Bottom left corner
//		Image.iconSheet.renderSprite(3 * 8, 2 * 8, 8, 8, xp, yp, 16, 16, 1.0f);
//		//Top right corner
//		Image.iconSheet.renderSprite(5 * 8, 0, 8, 8, xp + w - 16, yp + h - 16, 16, 16, 1.0f);
//		//Bottom right corner
//		Image.iconSheet.renderSprite(5 * 8, 2 * 8, 8, 8, xp + w - 16, yp, 16, 16, 1.0f);
//		
//		//Central area
//		Image.iconSheet.renderSprite(4 * 8, 8, 8, 8, xp + 16, yp + 16, w - 32, h - 32, 1.0f);
//		
//		//Top band
//		Image.iconSheet.renderSprite(4 * 8, 0, 8, 8, xp + 16, yp + h - 16, w - 32, 16, 1.0f);
//		//Bottom band
//		Image.iconSheet.renderSprite(4 * 8, 2 * 8, 8, 8, xp + 16, yp, w - 32, 16, 1.0f);
//		//Left band
//		Image.iconSheet.renderSprite(3 * 8, 8, 8, 8, xp, yp + 16, 16, h - 32, 1.0f);
//		//Right band
//		Image.iconSheet.renderSprite(5 * 8, 8, 8, 8, xp + w - 16, yp + 16, 16, h - 32, 1.0f);
		
	}
	
	public abstract void tick();
	
	public abstract void render();
}
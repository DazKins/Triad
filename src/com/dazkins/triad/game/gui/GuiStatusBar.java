package com.dazkins.triad.game.gui;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class GuiStatusBar {
	private BufferObject model;
	
	private int x, y;
	private int col;
	private int len;
	
	private float fill;
	
	public GuiStatusBar(int xp, int yp, int color, int length) {
		this.x = xp;
		this.y = yp;
		this.col = color;
		this.len = length;
		model = new BufferObject(655360);
		model.start();

		int len0 = len >> 3;
		for (int x = 0; x < len0; x++) {
			int sx = x == 0 ? 0 : (x == len0 - 1 ? 2 : 1);
			Image.iconSheet.renderSprite(model, x * 8 + this.x, this.y, 16, 16, sx * 8, 0, 8, 8, 0.0f);
		}
		
		model.stop();
	}
	
	public void updateStatus(float perc) {
		fill = perc * len;
	}
	
	public void render() {
		model.render();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4ub((byte) ((col >> 16) & 0xFF), (byte) ((col >> 8) & 0xFF), (byte) ((col) & 0xFF), (byte) 255);
			GL11.glVertex3f(x + 4, y + 4, 5.0f);
			GL11.glVertex3f(x + 4 + fill, y + 4, 5.0f);
			GL11.glVertex3f(x + 4 + fill, y + 12, 5.0f);
			GL11.glVertex3f(x + 4, y + 12, 5.0f);
			GL11.glColor4ub((byte)255, (byte)255, (byte)255, (byte)255);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
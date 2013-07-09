package com.dazkins.triad.game.gui;

import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.bitmap.Bitmap;

public class StatusBar {
	private int x, y;
	private int fillColor;
	private int maxFill;
	private int currentFill;
	private int size;
	
	public StatusBar(int x, int y, int maxFill, int tSize, int fillColor) {
		this.x = x;
		this.y = y;
		this.maxFill = maxFill;
		this.currentFill = 0;
		this.size = tSize;
		this.fillColor = fillColor;
	}
	
	public void updateFill(int p) {
		this.currentFill = p;
	}
	
	public void render(Bitmap b) {
		for (int x = 0; x < size; x++) {
			int sx = x == 0 ? 0 : x == size - 1 ? 2 : 1;
			Art.iconSheet.renderSprite(sx, 0, b, (x << 3) + this.x, this.y);
		}
		float percent = (float) currentFill / (float) maxFill;
		int fillAmmount = (int) (percent != 1 ? percent * (size << 3) : percent * (size << 3) - 2);
		for (int x = 2; x < fillAmmount; x++) {
			for (int y = 2; y < 6; y++) {
				b.setPixel(x + this.x, y + this.y, fillColor);
			}
		}
	}
}
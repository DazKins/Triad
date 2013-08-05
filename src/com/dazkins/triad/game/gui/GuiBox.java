package com.dazkins.triad.game.gui;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class GuiBox {
	private int width, height;
	private int x, y;
	
	private BufferObject bo;
	
	public GuiBox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		generate();
	}
	
	private void generate() {
		bo = new BufferObject(655360);
		bo.start();
		//Top left corner
		Image.iconSheet.renderSprite(bo, x, y + height - 16, 16, 16, 24, 0, 8, 8, 0.0f);
		//Bottom left corner
		Image.iconSheet.renderSprite(bo, x, y, 16, 16, 24, 16, 8, 8, 0.0f);
		//Top right corner
		Image.iconSheet.renderSprite(bo, x + width - 16, y + height - 16, 16, 16, 40, 0, 8, 8, 0.0f);
		//Bottom right corner
		Image.iconSheet.renderSprite(bo, x + width - 16, y, 16, 16, 40, 16, 8, 8, 0.0f);
		
		//Central area
		Image.iconSheet.renderSprite(bo, x + 16, y + 16, width - 32, height - 32, 32, 8, 8, 8, 0.0f);
		
		//Top band
		Image.iconSheet.renderSprite(bo, x + 16, y + height - 16, width - 32, 16, 32, 0, 8, 8, 0.0f);
		//Bottom band
		Image.iconSheet.renderSprite(bo, x + 16, y, width - 32, 16, 32, 16, 8, 8, 0.0f);
		//Left band
		Image.iconSheet.renderSprite(bo, x, y + 16, 16, height - 32, 24, 8, 8, 8, 0.0f);
		//Right band
		Image.iconSheet.renderSprite(bo, x + width - 16, y + 16, 16, height - 32, 40, 8, 8, 8, 0.0f);
		
		bo.stop();
	}
	
	public void render() {
		bo.render();
	}
}
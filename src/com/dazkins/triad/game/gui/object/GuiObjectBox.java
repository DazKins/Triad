package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class GuiObjectBox {
	private float width, height;
	private float x, y;
	
	private int txOff;
	
	private int layer;
	
	private BufferObject bo;
	
	public GuiObjectBox(float x, float y, float width, float height, int layer, boolean simple) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.layer = layer;
		
		if (simple)
			txOff = 24;
		
		generate();
	}
	
	public boolean intersects(float x, float y) {
		return x > this.x && y > this.y && x < this.x + width && y < this.y + height;
	}
	
	private void generate() {
		float z = layer * 0.001f;
		bo = new BufferObject(350);
		bo.start();
		
		//Top left corner
		Image.getImageFromName("iconSheet").renderSprite(bo, x, y + height - 16, 16, 16, 24 + txOff, 0, 8, 8, z, 1.0f);
		//Bottom left corner
		Image.getImageFromName("iconSheet").renderSprite(bo, x, y, 16, 16, 24 + txOff, 16, 8, 8, z, 1.0f);
		//Top right corner
		Image.getImageFromName("iconSheet").renderSprite(bo, x + width - 16, y + height - 16, 16, 16, 40 + txOff, 0, 8, 8, z, 1.0f);
		//Bottom right corner
		Image.getImageFromName("iconSheet").renderSprite(bo, x + width - 16, y, 16, 16, 40 + txOff, 16, 8, 8, z, 1.0f);
		
		//Central area
		Image.getImageFromName("iconSheet").renderSprite(bo, x + 16, y + 16, width - 32, height - 32, 32 + txOff, 8, 8, 8, z, 1.0f);
		
		//Top band
		Image.getImageFromName("iconSheet").renderSprite(bo, x + 16, y + height - 16, width - 32, 16, 32 + txOff, 0, 8, 8, z, 1.0f);
		//Bottom band
		Image.getImageFromName("iconSheet").renderSprite(bo, x + 16, y, width - 32, 16, 32 + txOff, 16, 8, 8, z, 1.0f);
		//Left band
		Image.getImageFromName("iconSheet").renderSprite(bo, x, y + 16, 16, height - 32, 24 + txOff, 8, 8, 8, z, 1.0f);
		//Right band
		Image.getImageFromName("iconSheet").renderSprite(bo, x + width - 16, y + 16, 16, height - 32, 40 + txOff, 8, 8, 8, z, 1.0f);
		
		bo.stop();
	}
	
	public void render() {
		bo.render();
	}
}
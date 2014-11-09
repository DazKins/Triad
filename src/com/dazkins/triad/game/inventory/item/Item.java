package com.dazkins.triad.game.inventory.item;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class Item {
	protected String name;
	protected int ID;
	
	private BufferObject icon;
	
	public Item(String name) {
		this.name = name;
		loadImageIconModel();
	}

	private void loadImageIconModel() {
		icon = new BufferObject(36);
		icon.start();
		Image.getImageFromName("item_" + name).renderSprite(icon, 0, 0, 32, 32, 0, 0, 32, 32, 0.0f, 1.0f);
		icon.stop();
	}
	
	public String getName() {
		return name;
	}
	
	public void renderIcon(float x, float y, float z, float scale) {
		GL11.glPushMatrix();
		
		if (x != 0 || y != 0 || z != 0)
			GL11.glTranslatef(x, y, z);
		
		if (scale != 1)
			GL11.glScalef(scale, scale, scale);
		
		icon.render();
		
		if (x != 0 || y != 0 || z != 0)
			GL11.glTranslatef(x, y, z);
		
		if (scale != 1)
			GL11.glScalef(scale, scale, scale);

		GL11.glPopMatrix();
	}
}
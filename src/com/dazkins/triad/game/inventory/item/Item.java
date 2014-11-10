package com.dazkins.triad.game.inventory.item;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.EntityItemStack;
import com.dazkins.triad.game.world.World;
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
	
	public static void dropItemStack(World w, float x, float y, ItemStack is) {
		int no = is.getSize();
		float speed = 7.0f;
		if (no > 4) {
			EntityItemStack eStack = new EntityItemStack(w, x, y, is);
			eStack.setXA(((float)Math.random() - 0.5f) * speed);
			eStack.setYA(((float)Math.random() - 0.5f) * speed);
			w.addEntity(eStack);
		} else {
			for (int i = 0; i < no; i++) {
				Item type = is.getItemType();
				ItemStack stack = new ItemStack(type, 1);
				EntityItemStack eStack = new EntityItemStack(w, x, y, stack);
				eStack.setXA((float)Math.random() * speed);
				eStack.setYA((float)Math.random() * speed);
				w.addEntity(eStack);
			}
		}
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
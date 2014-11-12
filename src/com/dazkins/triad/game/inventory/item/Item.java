package com.dazkins.triad.game.inventory.item;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.EntityItemStack;
import com.dazkins.triad.game.inventory.item.equipable.armour.body.ItemTestChest;
import com.dazkins.triad.game.inventory.item.equipable.armour.feet.ItemTestFeet;
import com.dazkins.triad.game.inventory.item.equipable.armour.head.ItemTestHelmet;
import com.dazkins.triad.game.inventory.item.equipable.armour.legs.ItemTestLegs;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemTestSword;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class Item {
	protected String name;
	protected int ID;
	protected boolean stackable;
	
	private BufferObject icon;
	
	public static ItemTestChest testChest = new ItemTestChest();
	public static ItemTestFeet testFeet = new ItemTestFeet();
	public static ItemTestHelmet testHelmet = new ItemTestHelmet();
	public static ItemTestLegs testLegs = new ItemTestLegs();
	public static ItemTestSword testSword = new ItemTestSword();
	
	public Item(String name, boolean s) {
		this.name = name;
		this.stackable = s;
		loadImageIconModel();
	}
	
	public static void dropItemStack(World w, float x, float y, ItemStack is) {
		int no = is.getSize();
		float speed = 7.0f;
		if (no > 4 && is.getItemType().isStackable()) {
			EntityItemStack eStack = new EntityItemStack(w, x, y, is);
			eStack.setXA(((float)Math.random() - 0.5f) * speed);
			eStack.setYA(((float)Math.random() - 0.5f) * speed);
			w.addEntity(eStack);
		} else {
			speed *= Math.sqrt(no) * 4f;
			for (int i = 0; i < no; i++) {
				Item type = is.getItemType();
				ItemStack stack = new ItemStack(type, 1);
				EntityItemStack eStack = new EntityItemStack(w, x, y, stack);
				
				eStack.setXA(((float)Math.random() - 0.5f) * speed);
				eStack.setYA(((float)Math.random() - 0.5f) * speed);
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
	
	public boolean isStackable() {
		return stackable;
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
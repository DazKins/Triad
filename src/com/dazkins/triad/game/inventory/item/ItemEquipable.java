package com.dazkins.triad.game.inventory.item;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public abstract class ItemEquipable extends Item {
	protected BufferObject[] equiped;
	
	public ItemEquipable(String name) {
		super(name, false);
		
		equiped = new BufferObject[4];
		
		for (int i = 0; i < 4; i++) {
			BufferObject ic = new BufferObject(36);
			ic.start();
			Image.getImageFromName("item_" + name).renderSprite(ic, 0, 0, 32, 32, 32 * (i + 1), 0, 32, 32, 0.0f, 1.0f);
			ic.stop();
			equiped[i] = ic;
			
		}
	}
	
	public BufferObject getEquipIcon(int i) {
		return equiped[i];
	}
}
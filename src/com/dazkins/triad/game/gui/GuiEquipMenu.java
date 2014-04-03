package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class GuiEquipMenu extends Gui {
	private GuiBox headSlot;
	private GuiBox bodySlot;
	private GuiBox legSlot;
	private GuiBox weaponSlot;
	private GuiBox footSlot;
	
	private GuiBox mainBox;
	
	private int windowWidth = 1200;
	private int windowHeight = 706;
	
	private int windowPosX = winInfo.getW() / 2 - windowWidth / 2;
	private int windowPosY =  winInfo.getH() / 2 - windowHeight / 2;
	
	private int gridSpacingX = 5;
	private int gridSpacingY = 5;
	
	private EquipmentInventory inv;
	
	public GuiEquipMenu(Triad t, InputHandler i, EquipmentInventory ei) {
		super(t, i);
		
		inv = ei;
		
		mainBox = new GuiBox(windowPosX, windowPosY, windowWidth, windowHeight, - 1, false);
		
		headSlot = new GuiBox(windowPosX + 200, windowPosY + windowHeight - 200, 128, 128, 0, true);
		bodySlot = new GuiBox(windowPosX + 200, windowPosY + windowHeight - 200 - 128 - gridSpacingY, 128, 128, 0, true);
		legSlot = new GuiBox(windowPosX + 200, windowPosY + windowHeight - 200 - 128 * 2 - gridSpacingY * 2, 128, 128, 0, true);
		footSlot = new GuiBox(windowPosX + 200, windowPosY + windowHeight - 200 - 128 * 3 - gridSpacingY * 3, 128, 128, 0, true);
		
		weaponSlot = new GuiBox(windowPosX + 200 - 128 - gridSpacingX, windowPosY + windowHeight - 200 - 128 - gridSpacingY, 128, 128, 0, true);
	}

	public void tick() {
		
	}

	public void render(Camera cam) {
		mainBox.render();
		headSlot.render();
		bodySlot.render();
		legSlot.render();
		weaponSlot.render();
		footSlot.render();
		
		inv.getItemStack(EquipmentInventory.Type.HEAD.ordinal()).getItemType().renderIcon(windowPosX + 200, windowPosY + windowHeight - 200, 3, 4);
	}

	public void onExit() {
		
	}
}
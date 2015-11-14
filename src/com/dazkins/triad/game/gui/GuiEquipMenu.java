package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.gui.object.GuiObjectBox;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class GuiEquipMenu extends Gui
{
	private GuiObjectBox headSlot;
	private GuiObjectBox bodySlot;
	private GuiObjectBox legSlot;
	private GuiObjectBox weaponSlot;
	private GuiObjectBox footSlot;

	private GuiObjectBox mainBox;

	private int windowWidth = 1200;
	private int windowHeight = 706;

	private int windowPosX;
	private int windowPosY;

	private int gridSpacingX = 5;
	private int gridSpacingY = 5;

	private Mob mob;
	private EquipmentInventory einv;
	private Inventory inv;

	public GuiEquipMenu(Triad t, InputHandler i, Mob m)
	{
		super(t, i);

		mob = m;
		einv = m.getEquipmentInventory();
		inv = m.getInventory();

		setupGraphics();
	}

	public void attempUnequip(int i)
	{
		if (inv.addItemStack(einv.getItemStack(i)))
			einv.removeItemStack(i);
	}

	public void tick()
	{
		super.tick();

		if (input.mouse2JustDown)
		{
			if (headSlot.intersects(input.mouseX, input.mouseY))
				attempUnequip(EquipmentInventory.HEAD);
			if (bodySlot.intersects(input.mouseX, input.mouseY))
				attempUnequip(EquipmentInventory.BODY);
			if (legSlot.intersects(input.mouseX, input.mouseY))
				attempUnequip(EquipmentInventory.LEGS);
			if (weaponSlot.intersects(input.mouseX, input.mouseY))
				attempUnequip(EquipmentInventory.WEAPON);
			if (footSlot.intersects(input.mouseX, input.mouseY))
				attempUnequip(EquipmentInventory.FEET);
		}
	}

	public void render(Camera cam)
	{
		mainBox.render();
		headSlot.render();
		bodySlot.render();
		legSlot.render();
		weaponSlot.render();
		footSlot.render();

		ItemStack headItem = einv.getItemStack(EquipmentInventory.HEAD);
		ItemStack bodyItem = einv.getItemStack(EquipmentInventory.BODY);
		ItemStack legItem = einv.getItemStack(EquipmentInventory.LEGS);
		ItemStack footItem = einv.getItemStack(EquipmentInventory.FEET);

		if (headItem != null)
			headItem.getItemType().renderIcon(windowPosX + 200, windowPosY + windowHeight - 200, 3, 4);
		if (bodyItem != null)
			bodyItem.getItemType().renderIcon(windowPosX + 200, windowPosY + windowHeight - 200 - 128 - gridSpacingY, 3, 4);
		if (legItem != null)
			legItem.getItemType().renderIcon(windowPosX + 200, windowPosY + windowHeight - 200 - 128 * 2 - gridSpacingY * 2, 3, 4);
		if (footItem != null)
			footItem.getItemType().renderIcon(windowPosX + 200, windowPosY + windowHeight - 200 - 128 * 3 - gridSpacingY * 3, 3, 4);

		ItemStack weaponItem = einv.getItemStack(EquipmentInventory.WEAPON);

		if (weaponItem != null)
			weaponItem.getItemType().renderIcon(windowPosX + 200 - 128 - gridSpacingX, windowPosY + windowHeight - 200 - 128 - gridSpacingY, 3, 4);

	}

	public void onExit()
	{

	}

	public void setupGraphics()
	{
		windowPosX = win.getW() / 2 - windowWidth / 2;
		windowPosY = win.getH() / 2 - windowHeight / 2;
		
		mainBox = new GuiObjectBox(this, windowPosX, windowPosY, windowWidth, windowHeight, -1);
		
		headSlot = new GuiObjectBox(this, windowPosX + 200, windowPosY + windowHeight - 200, 128, 128, 0);
		bodySlot = new GuiObjectBox(this, windowPosX + 200, windowPosY + windowHeight - 200 - 128 - gridSpacingY, 128, 128, 0);
		legSlot = new GuiObjectBox(this, windowPosX + 200, windowPosY + windowHeight - 200 - 128 * 2 - gridSpacingY * 2, 128, 128, 0);
		footSlot = new GuiObjectBox(this, windowPosX + 200, windowPosY + windowHeight - 200 - 128 * 3 - gridSpacingY * 3, 128, 128, 0);

		weaponSlot = new GuiObjectBox(this, windowPosX + 200 - 128 - gridSpacingX, windowPosY + windowHeight - 200 - 128 - gridSpacingY, 128, 128, 0);
	}
}
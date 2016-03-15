package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.gui.object.GuiObjectInventory;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.networking.client.TriadClient;

public class GuiSingleInventory extends Gui
{
	private Inventory inv;
	private EquipmentInventory eInv;

	private GuiObjectInventory gInv;

	private ItemStack selectedItem;
	
	private TriadClient client;
	
	private EntityShell entity;
	private int entityID;

	public GuiSingleInventory(EntityShell e, Triad t, InputHandler i, TriadClient c)
	{
		super(t, i);
		this.inv = e.getInventory();
		entity = e;
		entityID = entity.getGlobalID();
		setupGraphics();
		client = c;
	}

	public GuiSingleInventory(Inventory inv, int eID, Triad t, InputHandler i, TriadClient c)
	{
		super(t, i);
		this.inv = inv;
		entityID = eID;
		setupGraphics();
		client = c;
	}

	public void setupGraphics()
	{
		gInv = new GuiObjectInventory(this, inv, win, input, 0, 0, 1);
	}

	public void tick()
	{
		super.tick();
		gInv.tick();
		int slotI = gInv.getHoveredSlotIndex();
		if (slotI >= 0)
		{
			if (input.mouse1JustDown)
			{
				if (selectedItem == null)
				{
					selectedItem = gInv.getHoveredItem();
					inv.removeItemStack(slotI);
				} else
				{
					inv.addItemStack(selectedItem, slotI);
					selectedItem = null;
				}
			}
			if (input.mouse2JustDown)
			{
				if (gInv.getHoveredItem() != null)
					if (eInv.addItemStack(gInv.getHoveredItem()))
						inv.removeItemStack(slotI);
			}
		}
	}

	public void render(Camera cam)
	{
		gInv.render(cam);

		float iScale = gInv.getItemScale();
		if (selectedItem != null)
		{
			selectedItem.getItemType().renderIcon(input.mouseX - (16.0f * iScale), input.mouseY - (16.0f * iScale), 2f, iScale);
		}
	}

	public void onExit()
	{
		inv.addItemStack(selectedItem);
		client.sendInventoryUpdate(inv, entityID);
		selectedItem = null;
	}

}
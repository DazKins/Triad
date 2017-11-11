package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.gui.object.GuiObjectInventory;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.RenderContext;
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
		client = c;
		
		gInv = new GuiObjectInventory(this, inv, i);
		gInv.setLayer(1);
		setupGraphics();
	}

	public GuiSingleInventory(Inventory inv, int eID, Triad t, InputHandler i, TriadClient c)
	{
		super(t, i);
		this.inv = inv;
		entityID = eID;
		client = c;
		
		gInv = new GuiObjectInventory(this, inv, i);
		gInv.setLayer(1);
		setupGraphics();
	}

	public void setupGraphics()
	{
		int width = 500;
		gInv.setWidth(500);
		
		float height = gInv.getOptimalHeightFromGivenWidth();
		gInv.setHeight(height);
		
		gInv.setX((win.getW() - width) / 2.0f);
		gInv.setY((win.getH() - height) / 2.0f);
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

	public void render(RenderContext rc, Camera cam)
	{
		gInv.render(rc, cam);

		float iScale = gInv.getItemScale();
		if (selectedItem != null)
		{
			selectedItem.getItemType().renderIcon(rc, input.mouseX - (16.0f * iScale), input.mouseY - (16.0f * iScale), iScale);
		}
	}

	public void onExit()
	{
		inv.addItemStack(selectedItem);
		client.sendInventoryUpdate(inv, entityID);
		selectedItem = null;
	}

}
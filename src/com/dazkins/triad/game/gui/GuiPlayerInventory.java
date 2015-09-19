package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.EntityPlayerClient;
import com.dazkins.triad.game.gui.object.GuiObjectInventory;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class GuiPlayerInventory extends Gui
{
	private EntityPlayerClient player;
	private Inventory inv;
	private EquipmentInventory eInv;

	private GuiObjectInventory gInv;

	private ItemStack selectedItem;

	public GuiPlayerInventory(Triad t, InputHandler i, EntityPlayerClient e)
	{
		super(t, i);
		player = e;
		inv = e.getInventory();
		eInv = player.getEquipmentInventory();

		setupGraphics();
	}

	public void setupGraphics()
	{
		gInv = new GuiObjectInventory(inv, win, input);
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
		selectedItem = null;
	}

}
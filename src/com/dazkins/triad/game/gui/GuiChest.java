package com.dazkins.triad.game.gui;

import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.EntityChest;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.gui.object.GuiObjectInventory;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class GuiChest extends Gui {
	private EntityChest chest;
	private EntityPlayer player;
	
	private Inventory pInv;
	private Inventory cInv;
	private EquipmentInventory eInv;
	
	private GuiObjectInventory gInvP;
	private GuiObjectInventory gInvC;
	
	private ItemStack selectedItem;
	
	public GuiChest(Triad t, InputHandler i, EntityPlayer p, EntityChest c) {
		super(t, i);
		chest = c;
		player = p;
		
		pInv = p.getInventory();
		cInv = c.getInventory();
		
		eInv = p.getEquipmentInventory();
		
		setupGraphics();
	}

	public void render(Camera cam) {
		gInvP.render(cam);
		gInvC.render(cam);
		
		float iScale =  gInvP.getItemScale();
		if (selectedItem != null) {
			selectedItem.getItemType().renderIcon(input.mouseX - (16.0f * iScale), input.mouseY - (16.0f * iScale), 2f, iScale);
		}
	}
	
	public void tick() {
		super.tick();
		
		gInvP.tick();
		gInvC.tick();
		
		int slotI = gInvP.getHoveredSlotIndex();
		if (slotI >= 0) {
			if (input.mouse1JustDown) {
				if (selectedItem == null) {
					selectedItem = gInvP.getHoveredItem();
					pInv.removeItemStack(slotI);
				} else {
					pInv.addItemStack(selectedItem, slotI);
					selectedItem = null;
				}
			}
			if (input.mouse2JustDown) {
				if (gInvP.getHoveredItem() != null)
					if (eInv.addItemStack(gInvP.getHoveredItem()))
						pInv.removeItemStack(slotI);
			}
		}
		slotI = gInvC.getHoveredSlotIndex();
		if (slotI >= 0) {
			if (input.mouse1JustDown) {
				if (selectedItem == null) {
					selectedItem = gInvC.getHoveredItem();
					cInv.removeItemStack(slotI);
				} else {
					cInv.addItemStack(selectedItem, slotI);
					selectedItem = null;
				}
			}
			if (input.mouse2JustDown) {
				if (gInvC.getHoveredItem() != null)
					if (eInv.addItemStack(gInvC.getHoveredItem()))
						cInv.removeItemStack(slotI);
			}
		}
		
		if (input.isKeyJustDown(GLFW.GLFW_KEY_Q))
			player.setInteractingObject(null);
	}

	public void setupGraphics() {	
		gInvP = new GuiObjectInventory(pInv, win, input);
		gInvC = new GuiObjectInventory(cInv, win, input);
		gInvP.setOffsetY(-gInvP.getWinH() / 2.0f);
		gInvC.setOffsetY(gInvC.getWinH() / 2.0f);
	}

	public void onExit() {
		
	}
}
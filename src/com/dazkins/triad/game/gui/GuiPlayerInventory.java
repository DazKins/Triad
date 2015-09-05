package  com.dazkins.triad.game.gui;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.input.InputHandler;

public class GuiPlayerInventory extends Gui {
	private EntityPlayer player;
	private Inventory inv;
	private EquipmentInventory einv;
	
	private GuiBox mainBox;
	
	private GuiBox[] slotSheet;
	
	//All these variables are relative so the window can be resized easier however there is no resizing functionality as of yet
	private float winOffsetX;
	private float winOffsetY;
	
	private float gridSpacingX;
	private float gridSpacingY;
	private float gridOffsetX;
	private float gridOffsetY;
	
	private float slotSize;
	
	private float windowWidth;
	private float windowHeight;
	
	private float windowPosX;
	private float windowPosY;
	
	private ItemStack hoveredItem;
	
	private ItemStack selectedItem;
	
	public GuiPlayerInventory(Triad t, InputHandler i, EntityPlayer e) {
		super(t, i);
		player = e;
		inv = e.getInventory();
		einv = player.getEquipmentInventory();
		
		setupGraphics();
	}
	
	protected void setOffsetX(int x) {
		winOffsetX = x;
	}
	
	protected void setOffsetY(int y) {
		winOffsetY = y;
	}
	
	public void setupGraphics() {
		float winWR = (float) win.getW() / 1920.0f;

		gridSpacingX = (int) (4.0f * winWR);
		gridSpacingY = gridSpacingX;
		
		gridOffsetX = 12;
		gridOffsetY = 10;
		
		slotSize = 64.0f * winWR;
		
		windowWidth = ((float) inv.width * (slotSize + gridSpacingX) + gridOffsetX * 2.0f - gridSpacingX);
		windowHeight = ((float) inv.height * ( slotSize + gridSpacingY) + gridOffsetY * 2.0f - gridSpacingY);
		
		windowPosX = (win.getW() / 2.0f - windowWidth / 2.0f);
		windowPosY =  (win.getH() / 2.0f - windowHeight / 2.0f);
		
		mainBox = new GuiBox(windowPosX, windowPosY, windowWidth, windowHeight, 0, false);
		slotSheet = new GuiBox[inv.width * inv.height];
		
		for (int x = 0; x < inv.width; x++) {
			for (int y = 0; y < inv.height; y++) {
				slotSheet[x + y * inv.width] = new GuiBox(windowPosX +  x * (slotSize + gridSpacingX) + gridOffsetX, windowPosY +  y * (slotSize + gridSpacingY) + gridOffsetY, slotSize, slotSize, 3, true);
			}
		}
	}

	public void tick() {
		super.tick();

		for (int i = 0; i < slotSheet.length; i++) {
			if (slotSheet[i].intersects(input.mouseX, input.mouseY)) {
				if (input.mouse1JustDown) {
					if (hoveredItem != null && selectedItem == null) {
						inv.removeItemStack(i);
						selectedItem = hoveredItem;
					}
					else if (selectedItem != null && hoveredItem == null) {
						inv.addItemStack(selectedItem, i);
						selectedItem = hoveredItem;
					}
					else if(selectedItem != null && hoveredItem != null) {
						inv.removeItemStack(i);
						ItemStack oldSI = selectedItem;
						selectedItem = hoveredItem;
						inv.addItemStack(oldSI, i);
					}
				}
				else if (input.mouse2JustDown) {
					ItemStack item = inv.getItemStack(i);
					if (item != null) {
						if(einv.addItemStack(item))
							inv.removeItemStack(i);
					}
				}
				else {
					hoveredItem = null;
					hoveredItem = inv.getItemStack(i);
				}
			}
		}
	}

	public void render(Camera cam) {
		GL11.glPushMatrix();
		GL11.glTranslatef(winOffsetX, winOffsetY, 0);
		mainBox.render();
		for (int x = 0; x < inv.width; x++) {
			for (int y = 0; y < inv.height; y++) {
				slotSheet[x + y * inv.width].render();
			}
		}
		for (int x = 0; x < inv.width; x++) {
			for (int y = 0; y < inv.height; y++) {
				ItemStack i = inv.getItemStack(x, y);
				if (i != null) {
					i.getItemType().renderIcon(windowPosX +  x * (slotSize + gridSpacingX) + gridOffsetX, windowPosY +  y * (slotSize + gridSpacingY) + gridOffsetY, 0.2f, slotSize / 32.0f);
					if (i.getSize() > 1)
						Font.drawString(i.getSize() + "", windowPosX +  x * (slotSize + gridSpacingX) + gridOffsetX, windowPosY +  y * (slotSize + gridSpacingY) + gridOffsetY + 3, 1.0f, 1.0f, 1.0f, 1.0f, 5, 1);
				}
			}
		}
		if (hoveredItem != null) {
			//TODO : Reimplement
		}
		if (selectedItem != null) {
			selectedItem.getItemType().renderIcon(input.mouseX - (slotSize / 2.0f), input.mouseY - (slotSize / 2.0f), 1, slotSize / 32.0f);
			if (selectedItem.getSize() > 1)
				Font.drawString(selectedItem.getSize() + "", input.mouseX - 32, input.mouseY - 32 + 3, 1.0f, 1.0f, 1.0f, 1.0f, 20, 1);
		}

		GL11.glPopMatrix();
	}

	public void onExit() {
		inv.addItemStack(selectedItem);
		selectedItem = null;
	}
	
}
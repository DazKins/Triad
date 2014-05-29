package  com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.input.InputHandler;

public class GuiInventory extends Gui {
	private Inventory inv;
	
	private GuiBox mainBox;
	
	private GuiBox[] slotSheet;
	private GuiBox previewBox;
	
	//All these variables are relative so the window can be resized easier however there is no resizing functionality as of yet
	private int gridSpacingX = 5;
	private int gridSpacingY = 5;
	private int offsetX = 12;
	private int offsetY = 10;
	
	private int windowWidth = 1200;
	private int windowHeight = 706;
	
	private int windowPosX = winInfo.getW() / 2 - windowWidth / 2;
	private int windowPosY =  winInfo.getH() / 2 - windowHeight / 2;
	
	private int previewBoxW = 487;
	private int previewBoxH = 4 * 64 + 4 * (gridSpacingX - 1);
	
	private int previewBoxX = windowPosX + windowWidth - 11 - previewBoxW;
	private int previewBoxY = windowPosY + windowHeight - 11 - previewBoxH;
	
	private ItemStack hoveredItem;
	
	private ItemStack selectedItem;
	
	public GuiInventory(Triad t, InputHandler i, Inventory in) {
		super(t, i);
		inv = in;
		mainBox = new GuiBox(windowPosX, windowPosY, windowWidth, windowHeight, 0, false);
		slotSheet = new GuiBox[inv.width * inv.height];
		previewBox = new GuiBox(previewBoxX, previewBoxY, previewBoxW, previewBoxH, 1, true);
		
		for (int x = 0; x < inv.width; x++) {
			for (int y = 0; y < inv.height; y++) {
				slotSheet[x + y * inv.width] = new GuiBox(windowPosX +  x * (64 + gridSpacingX) + offsetX, windowPosY +  y * (64 + gridSpacingY) + offsetY, 64, 64, 3, true);
			}
		}
	}

	public void tick() {
		if (input.mouseX >= windowPosX + offsetX && input.mouseY >= windowPosY + offsetY && input.mouseX <= windowPosX + offsetX + 10 * (64 + gridSpacingX) && input.mouseY <= windowPosY + offsetY + 10 * (64 + gridSpacingY)) {
			int mouseSlotX = ((input.mouseX - offsetX - windowPosX) / (64 + gridSpacingX));
			int mouseSlotY = ((input.mouseY - offsetY - windowPosY) / (64 + gridSpacingY));
				if (mouseSlotX >= 0 && mouseSlotY >= 0 && mouseSlotX < inv.width && mouseSlotY < inv.height)
					hoveredItem = inv.getItemStack(mouseSlotX, mouseSlotY);
				else
					hoveredItem = null;
			
	
			if (input.mouse1JustDown) {
				if (hoveredItem != null && selectedItem == null) {
					inv.removeItemStack(mouseSlotX, mouseSlotY);
					selectedItem = hoveredItem;
				}
				else if (selectedItem != null && hoveredItem == null) {
					inv.addItemStack(selectedItem, mouseSlotX, mouseSlotY);
					selectedItem = null;
				}
				else if(selectedItem != null && hoveredItem != null) {
					inv.removeItemStack(mouseSlotX, mouseSlotY);
					ItemStack oldSI = selectedItem;
					selectedItem = hoveredItem;
					inv.addItemStack(oldSI, mouseSlotX, mouseSlotY);
				}
			}
		} else {
			hoveredItem = null;
		}
	}

	public void render(Camera cam) {
		previewBox.render();
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
					i.getItemType().renderIcon(windowPosX +  x * (64 + gridSpacingX) + offsetX, windowPosY +  y * (64 + gridSpacingY) + offsetY, 0.2f, 2);
					if (i.getSize() > 1)
						Font.drawString(i.getSize() + "", 45 + windowPosX +  x * (64 + gridSpacingX) + offsetX, windowPosY +  y * (64 + gridSpacingY) + offsetY + 3, 1.0f, 1.0f, 1.0f, 5, 1);
				}
			}
		}
		if (hoveredItem != null) {
			hoveredItem.getItemType().renderIcon(previewBoxX + previewBoxW / 4, previewBoxY + 10, 3, 8);
			Font.drawString(hoveredItem.getItemType().getName(), previewBoxX, previewBoxY - 50, 0.7f, 0.0f, 0.0f, 4, 3);
		}
		if (selectedItem != null) {
			selectedItem.getItemType().renderIcon(input.mouseX - 32, input.mouseY - 32, 1, 2);
			if (selectedItem.getSize() > 1)
				Font.drawString(selectedItem.getSize() + "", 45 + input.mouseX - 32, input.mouseY - 32 + 3, 1.0f, 1.0f, 1.0f, 20, 1);
		}
	}

	public void onExit() {
		inv.addItemStack(selectedItem);
		selectedItem = null;
	}
	
}
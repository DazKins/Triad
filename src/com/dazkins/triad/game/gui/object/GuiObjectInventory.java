package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;

public class GuiObjectInventory extends GuiObject
{
	private Window win;

	private Inventory inv;

	private InputHandler input;

	private GuiObjectBox mainBox;

	private GuiObjectBox[] slotSheet;

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

	public GuiObjectInventory(Gui g, Inventory i, Window w, InputHandler inp, int x, int y, int l)
	{
		super(g, l);
		
		inv = i;
		win = w;
		input = inp;

		winOffsetX = x;
		winOffsetY = y;


		float winWR = (float) win.getW() / 1920.0f;

		gridSpacingX = (int) (4.0f * winWR);
		gridSpacingY = gridSpacingX;

		gridOffsetX = 12;
		gridOffsetY = 10;

		slotSize = 64.0f * winWR;

		windowWidth = ((float) inv.width * (slotSize + gridSpacingX) + gridOffsetX * 2.0f - gridSpacingX);
		windowHeight = ((float) inv.height * (slotSize + gridSpacingY) + gridOffsetY * 2.0f - gridSpacingY);

		windowPosX = (win.getW() / 2.0f - windowWidth / 2.0f) + winOffsetX;
		windowPosY = (win.getH() / 2.0f - windowHeight / 2.0f) + winOffsetY;

		mainBox = new GuiObjectBox(g, windowPosX, windowPosY, windowWidth, windowHeight, 0);
		slotSheet = new GuiObjectBox[inv.width * inv.height];

		for (int ix = 0; x < inv.width; x++)
		{
			for (int iy = 0; y < inv.height; y++)
			{
				slotSheet[ix + iy * inv.width] = new GuiObjectBox(g, windowPosX + ix * (slotSize + gridSpacingX) + gridOffsetX, windowPosY + iy * (slotSize + gridSpacingY) + gridOffsetY, slotSize, slotSize, 3);
			}
		}
	}

	public GuiObjectInventory(Gui g, Inventory i, Window w, InputHandler inp, int l)
	{
		super(g, l);
		
		inv = i;
		win = w;
		input = inp;

		winOffsetX = 0;
		winOffsetY = 0;
	}

	public float getWinW()
	{
		return windowWidth;
	}

	public float getWinH()
	{
		return windowHeight;
	}

	public void setOffsetX(float x)
	{
		winOffsetX = x;
	}

	public void setOffsetY(float y)
	{
		winOffsetY = y;
	}

	public void render(Camera cam)
	{
		mainBox.render();
		for (int x = 0; x < inv.width; x++)
		{
			for (int y = 0; y < inv.height; y++)
			{
				slotSheet[x + y * inv.width].render();
			}
		}
		for (int x = 0; x < inv.width; x++)
		{
			for (int y = 0; y < inv.height; y++)
			{
				ItemStack i = inv.getItemStack(x, y);
				if (i != null)
				{
					i.getItemType().renderIcon(windowPosX + x * (slotSize + gridSpacingX) + gridOffsetX, windowPosY + y * (slotSize + gridSpacingY) + gridOffsetY, 0.2f, slotSize / 32.0f);
					if (i.getSize() > 1)
						Font.drawString(i.getSize() + "", windowPosX + x * (slotSize + gridSpacingX) + gridOffsetX, windowPosY + y * (slotSize + gridSpacingY) + gridOffsetY + 3, 5, 1);
				}
			}
		}
	}

	public float getItemScale()
	{
		return slotSize / 32.0f;
	}

	public ItemStack getHoveredItem()
	{
		return hoveredItem;
	}

	public int getHoveredSlotIndex()
	{
		int mx = input.mouseX;
		int my = input.mouseY;

		for (int i = 0; i < slotSheet.length; i++)
		{
			GuiObjectBox b = slotSheet[i];
			if (b.intersects(mx, my))
			{
				return i;
			}
		}
		return -1;
	}

	public void tick()
	{
		hoveredItem = null;

		hoveredItem = inv.getItemStack(getHoveredSlotIndex());
	}
}
package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;

public class GuiObjectInventory extends GuiObjectBox
{
	private Window win;

	private Inventory inv;

	private InputHandler input;

	private GuiObjectBox[] slotSheet;

	private float gridOffsetX = 12;
	private float gridOffsetY = 10;

	private ItemStack hoveredItem;
	
	public GuiObjectInventory(Gui g, Inventory inv, InputHandler inp)
	{
		super(g);
		this.inv = inv;
		
		int width = inv.width;
		int height= inv.height;
		
		slotSheet = new GuiObjectBox[width * height];
		for (int i = 0; i < width * height; i++)
		{
			slotSheet[i] = new GuiObjectBox(this.gui);
			slotSheet[i].setLayer(2);
		}
		
		this.input = inp;
	}
	
	public void setX(float x)
	{
		super.setX(x);
		
		int width = inv.width;
		int height= inv.height;
		
		for (int ix = 0; ix < width; ix++)
		{
			for (int iy = 0; iy < height; iy++)
			{
				GuiObjectBox b = slotSheet[ix + iy * width];
				b.setX(x + gridOffsetX + ix * (getSlotSize() + getGridSpacingX()));
			}
		}
	}
	
	public void setY(float y)
	{
		super.setY(y);
		
		int width = inv.width;
		int height= inv.height;
		
		for (int ix = 0; ix < width; ix++)
		{
			for (int iy = 0; iy < height; iy++)
			{
				GuiObjectBox b = slotSheet[ix + iy * width];
				b.setY(y + gridOffsetY + iy * (getSlotSize() + getGridSpacingY()));
			}
		}
	}
	
	public void setWidth(float w)
	{
		super.setWidth(w);
		
		int invWidth = inv.width;
		int invHeight = inv.height;
		
		for (int ix = 0; ix < invWidth; ix++)
		{
			for (int iy = 0; iy < invHeight; iy++)
			{
				GuiObjectBox b = slotSheet[ix + iy * invWidth];
				b.setWidth(getSlotSize());
				b.setX(x + gridOffsetX + ix * (getSlotSize() + getGridSpacingX()));
			}
		}
	}
	
	public void setHeight(float h)
	{
		super.setHeight(h);

		int invWidth = inv.width;
		int invHeight = inv.height;
		
		for (int ix = 0; ix < invWidth; ix++)
		{
			for (int iy = 0; iy < invHeight; iy++)
			{
				GuiObjectBox b = slotSheet[ix + iy * invWidth];
				b.setHeight(getSlotSize());
				b.setY(y + gridOffsetY + iy * (getSlotSize() + getGridSpacingY()));
			}
		}
	}
	
	private float getGridSpacingX()
	{
		return 3.0f;
	}

	private float getGridSpacingY()
	{
		return 3.0f;
	}
	
	private float getSlotSize()
	{
		int invWidth = inv.width;
		
		float slotGridSize = width - 2 * gridOffsetX;
		float totalSlotWidth = slotGridSize - (invWidth - 1) * getGridSpacingX();
		float individualWidth = totalSlotWidth / invWidth;
		
		return individualWidth;
	}

	public void render(RenderContext rc, Camera cam)
	{
		super.render(rc);
		for (int x = 0; x < inv.width; x++)
		{
			for (int y = 0; y < inv.height; y++)
			{
				slotSheet[x + y * inv.width].render(rc);
			}
		}
		
		for (int ix = 0; ix < inv.width; ix++)
		{
			for (int iy = 0; iy < inv.height; iy++)
			{
				ItemStack i = inv.getItemStack(ix, iy);
				if (i != null)
				{
					i.getItemType().renderIcon(rc, this.x + ix * (getSlotSize() + getGridSpacingX()) + gridOffsetX, y + iy * (getSlotSize() + getGridSpacingY()) + gridOffsetY, getSlotSize() / 32.0f);
//					if (i.getStackSize() > 1)
//						TTF.renderString(i.getStackSize() + "", windowPosX + ix * (slotSize + gridSpacingX) + gridOffsetX, windowPosY + iy * (slotSize + gridSpacingY) + gridOffsetY + 3, 5);
				}
			}
		}
	}
	
	public float getOptimalHeightFromGivenWidth()
	{
		return ((getSlotSize() + getGridSpacingY()) * inv.height) + 2 * gridOffsetY;
	}

	public float getItemScale()
	{
		return getSlotSize() / 32.0f;
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

	public void setupGraphics()
	{
		
	}
}
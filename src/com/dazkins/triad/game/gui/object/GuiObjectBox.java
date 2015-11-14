package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.renderformat.BoxRenderFormat;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.Quad;
import com.dazkins.triad.math.AABB;

public class GuiObjectBox extends GuiObject
{
	private static Quad[] sections = new Quad[18];
	
	private float width;
	private float height;
	
	private float x;
	private float y;
	
	public GuiObjectBox(Gui g, float x, float y, float w, float h, int l)
	{
		super(g, l);
		
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public boolean intersects(float x, float y)
	{
		return x > this.x && y > this.y && x < this.x + width && y < this.y + height;
	}
	
	public static void init()
	{
		for (int i = 0; i < 2; i++)
		{
			int ind = i * 9;
			int xOff = (i + 1) * 24;
			for (int x = 0; x < 3; x++)
			{
				for (int y = 0; y < 3; y++)
				{
					int xo = x * 8;
					int yo = y * 8;
					Quad q = new Quad(0, 0, 16, 16, xOff + xo, yo, 8, 8);
					q.init(Image.getImageFromName("iconSheet"));
					q.generate();
					sections[ind + x + y * 3] = q;
				}
			}
		}
	}
	
	public AABB getBounds()
	{
		return new AABB(x, y, x + width, y + height);
	}

	public void render()
	{
		BoxRenderFormat b = RenderFormatManager.BOX;
		
		int ind = b.isSimple() ? 9 : 0;
		
		sections[ind + 0].setOffset(x, y + height - 16);
		sections[ind + 0].setRenderLayer(layer);
		sections[ind + 0].render();
		sections[ind + 1].setOffset(x + 16, y + height - 16);
		sections[ind + 1].stretch(width - 32, 16);
		sections[ind + 1].setRenderLayer(layer);
		sections[ind + 1].render();
		sections[ind + 2].setOffset(x + width - 16, y + height - 16);
		sections[ind + 2].setRenderLayer(layer);
		sections[ind + 2].render();
		sections[ind + 3].setOffset(x, y + 16);
		sections[ind + 3].stretch(16, height - 32);
		sections[ind + 3].setRenderLayer(layer);
		sections[ind + 3].render();
		sections[ind + 4].setOffset(x + 16, y + 16);
		sections[ind + 4].stretch(width - 32, height - 32);
		sections[ind + 4].setRenderLayer(layer);
		sections[ind + 4].render();
		sections[ind + 5].setOffset(x + width - 16, y + 16);
		sections[ind + 5].stretch(16, height - 32);
		sections[ind + 5].setRenderLayer(layer);
		sections[ind + 5].render();
		sections[ind + 6].setOffset(x, y);
		sections[ind + 6].setRenderLayer(layer);
		sections[ind + 6].render();
		sections[ind + 7].setOffset(x + 16, y);
		sections[ind + 7].stretch(width - 32, 16);
		sections[ind + 7].setRenderLayer(layer);
		sections[ind + 7].render();
		sections[ind + 8].setOffset(x + width - 16, y);
		sections[ind + 8].setRenderLayer(layer);
		sections[ind + 8].render();
	}
}
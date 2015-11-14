package com.dazkins.triad.game.gui.object;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class GuiObjectStatusBar extends GuiObject
{
	private BufferObject model;

	private int x, y;
	private int col;
	private int len;

	private float fill;

	public GuiObjectStatusBar(Gui g, int xp, int yp, int color, int length, int l)
	{
		super(g, l);
		
		this.x = xp;
		this.y = yp;
		this.col = color;
		this.len = length;

		int len0 = len >> 3;
		model = new BufferObject(len0 * 36);
		model.resetData();
		for (int x = 0; x < len0; x++)
		{
			int sx = x == 0 ? 0 : (x == len0 - 1 ? 2 : 1);
			Image.getImageFromName("iconSheet").renderSprite(model, x * 8 + this.x, this.y, 16, 16, sx * 8, 0, 8, 8, 1.0f, 1.0f);
		}

		model.compile();
	}

	public void updateStatus(float perc)
	{
		fill = perc * len;
	}

	public void render(float xp, float yp, float zp, float scale, float a)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(xp, yp, zp);
		GL11.glScalef(scale, scale, 1.0f);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4ub((byte) ((col >> 16) & 0xFF), (byte) ((col >> 8) & 0xFF), (byte) ((col) & 0xFF), (byte) (a * 255));
		GL11.glVertex3f(x + 4, y + 4, 0);
		GL11.glVertex3f(x + 4 + fill, y + 4, 0);
		GL11.glVertex3f(x + 4 + fill, y + 12, 0);
		GL11.glVertex3f(x + 4, y + 12, 0);
		GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
		model.render();
		GL11.glPopMatrix();
	}
}
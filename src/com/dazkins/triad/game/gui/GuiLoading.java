package com.dazkins.triad.game.gui;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.util.Loader;

public class GuiLoading extends Gui
{
	private Loader loader;

	private BufferObject bScreen;

	public GuiLoading(Triad t, Loader l)
	{
		super(t, null);
		loader = l;

		bScreen = new BufferObject(36);
		bScreen.resetData();

		bScreen.getData().setRGB(0.3f, 0.0f, 0.0f);
		bScreen.getData().addVertex(0, 0);
		bScreen.getData().addVertex(t.win.getW(), 0);
		bScreen.getData().addVertex(t.win.getW(), t.win.getH());
		bScreen.getData().addVertex(0, t.win.getH());

		bScreen.compile();
	}

	public void tick()
	{

	}

	public void render(Camera cam)
	{
		// GL11.glDisable(GL11.GL_TEXTURE_2D);
		bScreen.render();
		// GL11.glEnable(GL11.GL_TEXTURE_2D);

		String msg = "Loading...";

		TTF.renderString(msg, ((float) triad.win.getW() / 2.0f) - ((float) msg.length() * 8), triad.win.getH() / 2 + 16, 1.0f, 1.0f);
	}

	public void onExit()
	{

	}

	public void setupGraphics()
	{

	}
}
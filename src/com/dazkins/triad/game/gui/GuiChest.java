package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.EntityChest;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class GuiChest extends GuiPlayerInventory {
	private EntityChest chest;
	
	public GuiChest(Triad t, InputHandler i, EntityPlayer p, EntityChest c) {
		super(t, i, p);
		chest = c;
	}

	public void render(Camera cam) {
		super.render(cam);
	}

	public void setupGraphics() {	
		super.setupGraphics();
		super.setOffsetY(-200);
	}
}
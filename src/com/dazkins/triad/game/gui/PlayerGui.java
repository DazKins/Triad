package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.input.InputHandler;

public class PlayerGui extends Gui {
	private EntityPlayer player;
	private GuiBox mainBox;
	private GuiStatusBar statusBar;
	
	public PlayerGui(Triad t, InputHandler i, EntityPlayer player) {
		super(t, i);
		this.player = player;
		mainBox = new GuiBox(0, 0, 1000, 400);
		statusBar = new GuiStatusBar(0, 0, 0xff0000, 1024);
	}

	public void tick() {
		statusBar.updateStatus((player.lifeTicks % 60) / 60.0f);
	}
	
	int ticks = 0;

	public void render() {
		ticks++;
//		mainBox.render();
//		statusBar.render();
	}
}
package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.input.InputHandler;

public class PlayerGui extends Gui {
	private EntityPlayer player;
	
	public PlayerGui(Triad t, InputHandler i, EntityPlayer player) {
		super(t, i);
		this.player = player;

	}

	public void tick() {

	}
	
	int ticks = 0;

	public void render() {
		ticks++;
		super.renderGuiBox(0, 0, triad.WIDTH, 130);
		super.renderStatusBar(100, 100, 0xFF0000, 32, (float) player.getHealth() / (float) player.getMaxHealth());
	}
}
package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.Player;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.GLRenderer;
import com.dazkins.triad.input.InputHandler;

public class PlayerGui extends Gui {
	private Player player;
	
	public PlayerGui(Triad t, InputHandler i, Player player) {
		super(t, i);
		this.player = player;

	}

	public void tick() {

	}
	
	int ticks = 0;

	public void render(GLRenderer g) {
		ticks++;
		super.renderGuiBox(g, 0, 0, triad.WIDTH, 130);
		super.renderStatusBar(g, 100, 100, 0xFF0000, 32, (float) player.getHealth() / (float) player.getMaxHealth());
	}
}
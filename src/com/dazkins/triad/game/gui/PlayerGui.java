package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.Player;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.input.InputHandler;

public class PlayerGui extends Gui {
	private final int hpBarLength = 20;
	private Player player;
	private StatusBar hp;
	
	public PlayerGui(Triad t, InputHandler i, Player player) {
		super(t, i);
		this.player = player;
		hp = new StatusBar(100, 100, 40, 32, 0x00FFFF);
	}

	public void tick() {
		hp.updateFill(5);
	}

	public void render(Bitmap b) {
		super.renderGuiBox(b, 0, triad.HEIGHT - 48, triad.WIDTH, 48);
		hp.render(b);
	}
}
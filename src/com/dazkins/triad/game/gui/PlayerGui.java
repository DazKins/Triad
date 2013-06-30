package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.Player;
import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.input.InputHandler;

public class PlayerGui extends Gui {
	private final int hpBarLength = 20;
	private Player player;
	
	public PlayerGui(Triad t, InputHandler i, Player player) {
		super(t, i);
		this.player = player;
	}

	public void tick() {
		
	}

	public void render(Bitmap b) {
		for (int x = 0; x < hpBarLength; x++) {
			Art.iconSheet.renderSprite(x == 0 ? 0 : x == hpBarLength - 1 ? 2 : 1, 0, b, x * 8, 0);
		}
		float hpPercent = (float) player.getHealth() / (float)player.getMaxHealth();
		int fillAmmount = hpPercent != 1 ? (int) (hpPercent * hpBarLength * 8) : (int) (hpPercent * hpBarLength * 8 - 2);
		for (int x = 2; x < fillAmmount; x++) {
			for (int y = 2; y < 6; y++) {
				b.setPixel(x, y, 0xFF0000);
			}
		}
	}
}
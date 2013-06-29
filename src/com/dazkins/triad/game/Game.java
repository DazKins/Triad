package com.dazkins.triad.game;

import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.Bitmap;

public class Game {
	public Game() {
		
	}
	
	public void tick() {
		
	}
	
	public void render(Bitmap b) {
		Art.mainSpriteSheet.renderSprite(0, 0, b, 0, 0);
	}
}
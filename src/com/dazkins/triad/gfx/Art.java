package com.dazkins.triad.gfx;

import java.io.IOException;

public class Art {
	public static SpriteSheet mainSpriteSheet;
	
	public static boolean init() {
		try {
			mainSpriteSheet = new SpriteSheet("/art/spriteSheet.png", 16, 16);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
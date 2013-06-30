package com.dazkins.triad.gfx;

import java.io.IOException;

public class Art {
	public static SpriteSheet spriteSheet;
	public static SpriteSheet iconSheet;
	
	public static boolean init() {
		try {
			spriteSheet = new SpriteSheet("/art/spriteSheet.png", 16, 16);
			iconSheet = new SpriteSheet("/art/iconSheet.png", 8, 8);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
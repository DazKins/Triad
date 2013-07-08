package com.dazkins.triad.gfx;

import com.dazkins.triad.gfx.bitmap.Bitmap;

public class Font {
	public static String letters = "abcdefghijklmnopqrstuvwxyz0123456789.,:;'\"!?$%()-=+/";
	
	public static void drawString(Bitmap b, String s, int xp ,int yp) {
		String lower = s.toLowerCase();
		
		for (int x = 0; x < lower.length(); x++) {
			int sx = letters.contains("" + lower.charAt(x)) || lower.charAt(x) == ' ' ? letters.indexOf(lower.charAt(x)) : 43;
			if (sx >= 0)
				Art.fontSheet.renderSprite(sx, 0, b, xp + x * 8, yp);
		}
	}
}
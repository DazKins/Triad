package com.dazkins.triad.gfx;

public class Font {
	public static String letters = "abcdefghijklmnopqrstuvwxyz0123456789.,:;'\"!?$%()-=+/";
	
	public static void drawString(GLRenderer g, String s, int xp ,int yp) {
		String lower = s.toLowerCase();
		
		for (int x = 0; x < lower.length(); x++) {
			int sx = letters.contains("" + lower.charAt(x)) || lower.charAt(x) == ' ' ? letters.indexOf(lower.charAt(x)) : 43;
			if (sx >= 0)
				Image.fontSheet.renderSprite(g, sx * 8, 0, 8, 8, x * 8 + xp, yp, 8, 8);
		}
	}
}
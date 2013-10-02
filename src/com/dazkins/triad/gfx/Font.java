package com.dazkins.triad.gfx;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class Font {
	public static String letters = "abcdefghijklmnopqrstuvwxyz0123456789.,:;'\"!?$%()-=+/";
	
	private static Map<Character, BufferObject> characterToModel;
	private static Map<Character, BufferObject> shadedCharacterToModel;
	
	public static void initializeFont() {
		characterToModel = new HashMap<Character, BufferObject>();
		shadedCharacterToModel = new HashMap<Character, BufferObject>();
		
		for (int i = 0; i < letters.length(); i++) {
			BufferObject b = new BufferObject(4 * 8);
			b.start();
			Image.fontSheet.renderSprite(b, 0, 0, 16, 16, i	* 8, 0, 8, 8, 0.0f, 1.0f);
			b.stop();
			
			characterToModel.put(letters.charAt(i), b);
		}
		
		for (int i = 0; i < letters.length(); i++) {
			BufferObject b = new BufferObject(4 * 8);
			b.start();
			Image.fontSheet.renderSprite(b, 0, 0, 16, 16, i	* 8, 0, 8, 8, 0.0f, 0.2f);
			b.stop();
			
			shadedCharacterToModel.put(letters.charAt(i), b);
		}
	}
	
	public static void drawString(String s, int xp ,int yp) {
		String lower = s.toLowerCase();
		
		for (int x = 0; x < lower.length(); x++) {
			char cChar = lower.charAt(x);
			if (cChar != ' ') {
				GL11.glPushMatrix();
				GL11.glTranslatef(xp + x * 16, yp, 0.0f);
				characterToModel.get(cChar).render();
				GL11.glPopMatrix();
			}
		}
	}
	
	public static void drawStringWithShadow(String s, int xp ,int yp) {
		String lower = s.toLowerCase();
		
		for (int x = 0; x < lower.length(); x++) {
			char cChar = lower.charAt(x);
			if (cChar != ' ') {
				GL11.glPushMatrix();
				GL11.glColor3f(0.2f, 0.2f, 0.2f);
				GL11.glTranslatef((xp + x * 16) - 4.0f, yp - 4.0f, -0.001f);
				shadedCharacterToModel.get(cChar).render();
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glTranslatef(xp + x * 16, yp, 0.0f);
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				characterToModel.get(cChar).render();
				GL11.glPopMatrix();
			}
		}
	}
}
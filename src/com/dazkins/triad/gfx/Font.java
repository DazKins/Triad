package com.dazkins.triad.gfx;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class Font {
	public static String letters = "abcdefghijklmnopqrstuvwxyz0123456789.,:;'\"!?$%()-=+/";

	private static Map<Character, BufferObject> characterToModel;

	public static void initializeFont() {
		characterToModel = new HashMap<Character, BufferObject>();

		for (int i = 0; i < letters.length(); i++) {
			BufferObject b = new BufferObject(4 * 8);
			b.start();
			Image.fontSheet.renderSprite(b, 0, 0, 16, 16, i * 8, 0, 8, 8, 0.0f,1.0f);
			b.stop();
			
			characterToModel.put(letters.charAt(i), b);
		}
	}

	public static void drawString(String s, float xp, float yp, float r, float g, float b, int layer, int scale) {
		float z = layer * 0.001f;
		String lower = s.toLowerCase();

		for (int x = 0; x < lower.length(); x++) {
			char cChar = lower.charAt(x);
			if (cChar != ' ') {
				GL11.glPushMatrix();
				GL11.glTranslatef(xp + (x * scale) * 16, yp, z);
				GL11.glColor3f(r, g, b);
				GL11.glScalef(scale, scale, 1.0f);
				characterToModel.get(cChar).render();
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				GL11.glPopMatrix();
			}
		}
	}

	public static void drawStringWithShadow(String s, float xp, float yp, float r, float g, float b, int layer, int scale) {
		float z = layer * 0.001f;
		String lower = s.toLowerCase();

		for (int x = 0; x < lower.length(); x++) {
			char cChar = lower.charAt(x);
			if (cChar != ' ') {
				GL11.glPushMatrix();
				GL11.glColor3f(0.3f, 0.3f, 0.3f);
				GL11.glTranslatef((xp + (x * scale) * 16) - 4.0f, yp - 4.0f, z - 0.0001f);
				GL11.glScalef(scale, scale, 1.0f);
				characterToModel.get(cChar).render();
				GL11.glPopMatrix();
				
				GL11.glPushMatrix();
				GL11.glTranslatef((xp + (x * scale) * 16), yp, z);
				GL11.glColor3f(r, g, b);
				GL11.glScalef(scale, scale, 1.0f);
				characterToModel.get(cChar).render();
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				GL11.glPopMatrix();
			}
		}
	}
}
package com.dazkins.triad.gfx;

import org.lwjgl.opengl.GL11;

public class OpenGLHelper {
	public static void renderReferencePoint(float x, float y) {
		GL11.glPointSize(5);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		GL11.glBegin(GL11.GL_POINTS);
			GL11.glVertex3f(x, y, 5.0f);
		GL11.glEnd();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
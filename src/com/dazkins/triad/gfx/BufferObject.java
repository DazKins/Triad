package com.dazkins.triad.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.dazkins.triad.util.TriadProfiler;

public class BufferObject {
	private static boolean useVBO;

	private float[] rawBuffer;
	private FloatBuffer dataBuffer;

	private int ID;
	private int vertexCount;
	private Image img;

	private boolean editing;

	private float xOffset, yOffset;
	
	private float brightness;
	
	private boolean useColors;
	private boolean useTextures;
	
	//Store the temporary variables to be loaded into the VBO
	private float x;
	private float y;
	private float z;
	private float r;
	private float g;
	private float b;
	private float u;
	private float v;
	
	public static void init() {
		float version = Float.parseFloat(GL11.glGetString(GL11.GL_VERSION).substring(0, 3));
		System.err.println("OpenGL version: " + version);
		if (version <= 1.5) {
			useVBO = false;
			System.err.println("VBOs disabled");
		}
		else {
			useVBO = true;
			System.err.println("VBOs enabled");
		}
	}

	public BufferObject(int size) {
		if (useVBO) {
			rawBuffer = new float[size];
			dataBuffer = BufferUtils.createFloatBuffer(size);
		} else {
			if (ID == 0)
				ID = GL11.glGenLists(1);
		}
		brightness = 1.0f;
	}
	
	public void setBrightness(float b) {
		brightness = b;
	}

	public void start() {
		if (useVBO)
			vertexCount = 0;
		else {
			GL11.glNewList(ID, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
		}
		editing = true;
	}

	public void stop() {
		editing = false;
		if (useVBO) {
			compileVBO();
			generateVBO();
			closeVBO();
		} else {
			GL11.glEnd();
			GL11.glEndList();
		}
	}

	private void compileVBO() {
		dataBuffer.put(rawBuffer);
		dataBuffer.flip();
	}

	private void generateVBO() {
		ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
	}

	private void closeVBO() {
		rawBuffer = null;
		dataBuffer = null;
//		System.gc();
	}

	public void addVertexWithUV(float x, float y, float z, float u, float v) {
		if (!editing)
			throw new RuntimeException("Buffer object not editable!");
		if (!useTextures)
			throw new RuntimeException("Textures are not enabled in this current state!");
		
		if (useVBO) {
			rawBuffer[vertexCount * 8] = x + xOffset;
			rawBuffer[vertexCount * 8 + 1] = y + yOffset;
			rawBuffer[vertexCount * 8 + 2] = z;
			
			rawBuffer[vertexCount * 8 + 3] = brightness;
			rawBuffer[vertexCount * 8 + 4] = brightness;
			rawBuffer[vertexCount * 8 + 5] = brightness;
			
			rawBuffer[vertexCount * 8 + 6] = u;
			rawBuffer[vertexCount * 8 + 7] = v;

			vertexCount++;
		} else {
			GL11.glTexCoord2f(u, v);
			GL11.glColor3f(brightness, brightness, brightness);
			GL11.glVertex3f(x + xOffset, y + yOffset, z);
		}
	}

//	public void addVertexWithUV(float x, float y, float u, float v) {
//		addVertexWithUV(x, y, 0.0f, u, v);
//	}

	public void bindImage(Image i) {
		if (!editing)
			throw new RuntimeException("Buffer object not editable!");

		if (!useTextures) {
			this.useTextures = true;
			img = i;
		} else if (!useTextures && i != img) {
			throw new RuntimeException("Only one texture per buffer is supported!");
		}
	}

	public void setOffset(float x, float y) {
		this.xOffset = x;
		this.yOffset = y;
	}

	public void render() {
		if (editing)
			throw new RuntimeException("Buffer is still being edited!");
		
		if (useTextures);
			img.bindGLTexture();

		if (useVBO) {
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, 4 * 8, 0);
			GL11.glColorPointer(3, GL11.GL_FLOAT, 4 * 8, 4 * 3);
			GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 4 * 8, 4 * 6);
			GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		} else {
			GL11.glCallList(ID);
		}
	}
}
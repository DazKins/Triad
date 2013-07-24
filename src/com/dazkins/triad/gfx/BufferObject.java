package com.dazkins.triad.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class BufferObject {
private static boolean useVBO = false;

	private float[] rawBuffer;
	private FloatBuffer dataBuffer;

	private int ID;
	private int vertexCount;
	private int texID;

	private boolean editing;
	private boolean hasTexture;

	private float xOffset, yOffset;

	public BufferObject(int size) {
		if (useVBO) {
			rawBuffer = new float[size];
			dataBuffer = BufferUtils.createFloatBuffer(size);
		} else {
			ID = GL11.glGenLists(1);
		}
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
		System.gc();
	}

	public void addVertexWithUV(float x, float y, float z, float u, float v) {
		if (!editing)
			throw new RuntimeException("Buffer object not editable!");

		if (useVBO) {
			rawBuffer[vertexCount * 5] = x + xOffset;
			rawBuffer[vertexCount * 5 + 1] = y + yOffset;
			rawBuffer[vertexCount * 5 + 2] = z;
			rawBuffer[vertexCount * 5 + 3] = u;
			rawBuffer[vertexCount * 5 + 4] = v;

			vertexCount++;
		} else {
			GL11.glTexCoord2f(u, v);
			GL11.glVertex3f(x + xOffset, y + yOffset, z);
		}
	}

	public void addVertexWithUV(float x, float y, float u, float v) {
		addVertexWithUV(x, y, 0.0f, u, v);
	}

	public void setTexture(int t) {
		if (!editing)
			throw new RuntimeException("Buffer object not editable!");

		if (!hasTexture) {
			this.hasTexture = true;
			texID = t;
		} else if (!hasTexture && t != texID) {
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

		if (GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) != texID) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		}

		if (useVBO) {
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
			GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 4 * 3);
			GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		} else {
			GL11.glCallList(ID);
		}
	}
}
package com.dazkins.triad.gfx;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

public class BufferObject {
	private float[] rawBuffer;
	private FloatBuffer dataBuffer;
	private int ID;
	private int vertexCount;
	
	private boolean editing;
	
	public BufferObject(int size) {
		rawBuffer = new float[size];
		dataBuffer = BufferUtils.createFloatBuffer(size); 
	}
	
	public void start() {
		vertexCount = 0;
		editing = true;
	}
	
	public void stop() {
		editing = false;
		compileVBO();
		generateVBO();
		closeVBO();
		System.gc();
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
	}
	
	public void addVertexWithUV(float x, float y, float u, float v) {
		if(!editing)
			throw new RuntimeException("Buffer object not editable!");
		
		rawBuffer[vertexCount * 4] = x;
		rawBuffer[vertexCount * 4 + 1] = y;
		rawBuffer[vertexCount * 4 + 2] = u;
		rawBuffer[vertexCount * 4 + 3] = v;
		
		vertexCount++;
	}
	
	public void render() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 16, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 16, 8);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
}
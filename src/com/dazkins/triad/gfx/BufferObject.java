package com.dazkins.triad.gfx;

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
	private int texID;
	
	private boolean editing;
	private boolean hasTexture;
	
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
		if(!editing)
			throw new RuntimeException("Buffer object not editable!");
		
		rawBuffer[vertexCount * 5] = x;
		rawBuffer[vertexCount * 5 + 1] = y;
		rawBuffer[vertexCount * 5 + 2] = z;
		rawBuffer[vertexCount * 5 + 3] = u;
		rawBuffer[vertexCount * 5 + 4] = v;
		
		vertexCount++;
	}
	
	public void addVertexWithUV(float x, float y, float u, float v) {
		if(!editing)
			throw new RuntimeException("Buffer object not editable!");
		
		rawBuffer[vertexCount * 5] = x;
		rawBuffer[vertexCount * 5 + 1] = y;
		rawBuffer[vertexCount * 5 + 2] = 0.0f;
		rawBuffer[vertexCount * 5 + 3] = u;
		rawBuffer[vertexCount * 5 + 4] = v;
		
		vertexCount++;
	}
	
	public void setTexture(int t) {
		if(!editing)
			throw new RuntimeException("Buffer object not editable!");
			
		if(!hasTexture) {
			this.hasTexture = true;
			texID = t;
		} 
		else if(!hasTexture && t != texID) {
			throw new RuntimeException("Only one texture per buffer is supported!");
		}
	}
	
	public void render() {
		if(editing)
			throw new RuntimeException("Buffer is still being edited!");
		
		if(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) != texID)
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, ID);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 4 * 3);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
}
package com.dazkins.triad.gfx;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class GLRenderer {
	public static GLRenderer instance;
	
	static {
		instance = new GLRenderer();
	}
	
	private Vector<FloatBuffer> vertexBuffer;
	private Vector<FloatBuffer> textureBuffer;
	private boolean editable;
	private int vertexCount;
	
	private boolean useTexture;
	private int texID;
	
	private HashMap textureToBuffer;
	private List<Integer> bufferSizes;
	private List<List<Float>> rawVertexDataList;
	private List<List<Float>> rawTextureDataList;
	
	public void open() {
		vertexCount = 0;
		editable = true;
		useTexture = false;
		rawVertexDataList = new ArrayList<List<Float>>();
		for (int i = 0; i < 10; i++) {
			rawVertexDataList.add(new ArrayList<Float>());
		}
		rawTextureDataList = new ArrayList<List<Float>>();
		for (int i = 0; i < 10; i++) {
			rawTextureDataList.add(new ArrayList<Float>());
		}
		bufferSizes = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			bufferSizes.add(0);
		}
		vertexBuffer = new Vector<FloatBuffer>();
		textureBuffer = new Vector<FloatBuffer>();
	}
	
	public void bindTexture(int texID) {
		this.texID = texID;
		useTexture = true;
	}
	
	public void addVertexUV(float u, float v) {
		if (!editable)
			throw new RuntimeException("VBO not editable!");
		rawTextureDataList.get(texID - 1).add(u);
		rawTextureDataList.get(texID - 1).add(v);
	}
	
	public void addVertex(float x, float y) {
		if (!editable)
			throw new RuntimeException("VBO not editable!");
		rawVertexDataList.get(texID - 1).add(x);
		rawVertexDataList.get(texID - 1).add(y);
		bufferSizes.set(texID - 1, bufferSizes.get(texID - 1) + 1);
	}
	
	public void draw() {
		editable = false;
		
		for (int i = 0; i < rawVertexDataList.size(); i++) {
			FloatBuffer fb = BufferUtils.createFloatBuffer(rawVertexDataList.get(i).size());
			for (int u = 0; u < fb.capacity(); u++) {
				fb.put(rawVertexDataList.get(i).get(u));
			}
			fb.flip();
			vertexBuffer.add(fb);

			fb = BufferUtils.createFloatBuffer(rawTextureDataList.get(i).size());
			for (int u = 0; u < fb.capacity(); u++) {
				fb.put(rawTextureDataList.get(i).get(u));
			}
			fb.flip();
			textureBuffer.add(fb);
		}

		if (useTexture)
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		
		for (int i = 0; i < textureBuffer.size(); i++) {
			if(GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) != i + 1)
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i + 1);
			
			GL11.glVertexPointer(2, 0, vertexBuffer.get(i));
			
			if(useTexture)
				GL11.glTexCoordPointer(2, 0, textureBuffer.get(i));
			
			GL11.glDrawArrays(GL11.GL_QUADS, 0, bufferSizes.get(i));
		}
		
		if (useTexture)
			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		vertexBuffer.clear();
		textureBuffer.clear();
	}
}
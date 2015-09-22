package com.dazkins.triad.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.dazkins.triad.math.MathHelper;

public class BufferObject
{
	public static BufferObject shadow;

	//TODO reimplement display lists
	private static boolean useVBO;

	private BufferObjectData data;
	private FloatBuffer dataBuffer;

	private int ID;
	
	//Stores seperate properties for the rendering context in case the data is changed
	private BufferObjectRenderProperties renderProps;

	private static void initStaticVBOs()
	{
		shadow = new BufferObject(36);
		shadow.start();
		shadow.getData().setRGB(0.0f, 0.0f, 0.0f);
		shadow.getData().setA(0.2f);
		shadow.getData().addVertex(0, 0);
		shadow.getData().addVertex(32, 0);
		shadow.getData().addVertex(32, 32);
		shadow.getData().addVertex(0, 32);
		shadow.stop();
	}
	
	public static boolean useVBOS() 
	{
		return useVBO;
	}
	
	public BufferObjectData getData()
	{
		return data;
	}
	
	public void assignData(BufferObjectData d)
	{
		data = d.clone();
		dataBuffer = BufferUtils.createFloatBuffer(d.getSize());
	}

	public static void init()
	{
		float version = Float.parseFloat(GL11.glGetString(GL11.GL_VERSION).substring(0, 3));
		System.err.println("OpenGL version: " + version);
		if (version <= 1.5)
		{
			useVBO = false;
			System.err.println("VBOs disabled");
		} else
		{
			useVBO = true;
			System.err.println("VBOs enabled");
		}
		initStaticVBOs();
	}
	
	public int getID()
	{
		return ID;
	}

	public void deleteBuffer()
	{
		GL15.glDeleteBuffers(ID);
	}

	public BufferObject(int size)
	{
		if (useVBO)
		{
			data = new BufferObjectData(size);
			dataBuffer = BufferUtils.createFloatBuffer(size);
		} else
		{
			ID = GL11.glGenLists(1);
		}
		renderProps = new BufferObjectRenderProperties();
	}

	public void start()
	{
		data.reset();
	}

	public void stop()
	{
		compileVBO();
		generateVBO();
	}

	private void compileVBO()
	{
		dataBuffer.put(data.getRawData());
		dataBuffer.flip();
		renderProps = data.getRenderProperties().clone();
	}

	private void generateVBO()
	{
		ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
	}

	public void render()
	{
		if (renderProps.isUseTextures()) 
		{ 
			data.getImg().bindGLTexture();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		
		if (useVBO)
		{
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			
			if (renderProps.isUseColours())
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			
			if (renderProps.isUseTextures())
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);

			GL11.glVertexPointer(3, GL11.GL_FLOAT, MathHelper.SIZE_OF_FLOAT * 9, 0);

			if (renderProps.isUseColours())
				GL11.glColorPointer(4, GL11.GL_FLOAT, MathHelper.SIZE_OF_FLOAT * 9, MathHelper.SIZE_OF_FLOAT * 3);

			if (renderProps.isUseTextures())
				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, MathHelper.SIZE_OF_FLOAT * 9, MathHelper.SIZE_OF_FLOAT * 7);

			GL11.glDrawArrays(GL11.GL_QUADS, 0, renderProps.getVertexCount());
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

			if (renderProps.isUseTextures())
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			if (renderProps.isUseColours())
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		} else
		{
			GL11.glCallList(ID);
		}
	}
}
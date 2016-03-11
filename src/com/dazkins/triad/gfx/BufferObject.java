package com.dazkins.triad.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.util.TriadLogger;

public class BufferObject
{
	public static BufferObject shadow;

	private static boolean useVBO;

	private BufferObjectData data;
	private FloatBuffer dataBuffer;

	private int ID;
	
	//Stores seperate properties for the rendering context in case the data is changed
	private BufferObjectRenderProperties renderProps;
	private Image image;

	private static void initStaticVBOs()
	{
		shadow = new BufferObject(36);
		shadow.resetData();
		shadow.getData().setRGB(0.0f, 0.0f, 0.0f);
		shadow.getData().setA(0.2f);
		shadow.getData().addVertex(0, 0);
		shadow.getData().addVertex(32, 0);
		shadow.getData().addVertex(32, 32);
		shadow.getData().addVertex(0, 32);
		shadow.compile();
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
		TriadLogger.log("OpenGL version: " + version, false);
		if (version <= 1.5)
		{
			useVBO = false;
			TriadLogger.log("VBOs disabled", false);
		} else
		{
			useVBO = true;
			TriadLogger.log("VBOs enabled", false);
		}
		initStaticVBOs();
	}
	
	public int getID()
	{
		return ID;
	}

	public void deleteBuffer()
	{
		if (useVBO)
			GL15.glDeleteBuffers(ID);
		else
			GL11.glDeleteLists(ID, 1);
	}

	public BufferObject(int size)
	{
		if (useVBO)
		{
			dataBuffer = BufferUtils.createFloatBuffer(size);
		}
		data = new BufferObjectData(size);
		renderProps = new BufferObjectRenderProperties();
	}

	public void resetData()
	{
		data.reset();
	}

	public void compile()
	{
		if (useVBO)
		{
			dataBuffer.clear();
			dataBuffer.put(data.getRawData());
			dataBuffer.flip();
			
			ID = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
		} else 
		{
			ID = GL11.glGenLists(1);
			float[] raw = data.getRawData();
			BufferObjectRenderProperties prop = data.getRenderProperties();
			int vCount = prop.getVertexCount();
			
			GL11.glNewList(ID, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			for (int i = 0; i < vCount; i++)
			{
				if (prop.isUseColours())
				{
					float r = raw[i * 9 + 3];
					float g = raw[i * 9 + 4];
					float b = raw[i * 9 + 5];
					float a = raw[i * 9 + 6];
					
					GL11.glColor4f(r, g, b, a);
				}
				if (prop.isUseTextures())
				{
					float u = raw[i * 9 + 7];
					float v = raw[i * 9 + 8];
					
					GL11.glTexCoord2f(u, v);
				}
				
				float x = raw[i * 9 + 0];
				float y = raw[i * 9 + 1];
				float z = raw[i * 9 + 2];
				
				GL11.glVertex3f(x, y, z);
			}
			GL11.glEnd();
			GL11.glEndList();
		}
		
		renderProps = data.getRenderProperties().clone();
		image = data.getImg();
	}

	public void render()
	{
		if (renderProps.isUseTextures()) 
		{
			image.bindGLTexture();
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
package com.dazkins.triad.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class BufferObject
{
	public static BufferObject shadow;

	private static boolean useVBO;

	private float[] rawBuffer;
	private FloatBuffer dataBuffer;

	private int ID;
	private int vertexCount;
	private Image img;

	private boolean editing;

	private boolean useColors;
	private boolean useTextures;

	// Store the temporary variables to be loaded into the VBO
	private float x;
	private float y;
	private float z;
	private float r;
	private float g;
	private float b;
	private float a;
	private float u;
	private float v;

	private static void initStaticVBOs()
	{
		shadow = new BufferObject(36);
		shadow.start();
		shadow.setRGB(0.0f, 0.0f, 0.0f);
		shadow.setA(0.2f);
		shadow.addVertex(0, 0);
		shadow.addVertex(32, 0);
		shadow.addVertex(32, 32);
		shadow.addVertex(0, 32);
		shadow.stop();
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

	public void deleteBuffer()
	{
		GL15.glDeleteBuffers(ID);
	}

	public BufferObject(int size)
	{
		if (useVBO)
		{
			rawBuffer = new float[size];
			dataBuffer = BufferUtils.createFloatBuffer(size);
		} else
		{
			if (ID == 0)
				ID = GL11.glGenLists(1);
		}
		a = 1.0f;
		setRGB(1.0f, 1.0f, 1.0f);
	}

	public void start()
	{
		if (useVBO)
			vertexCount = 0;
		else
		{
			GL11.glNewList(ID, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
		}
		editing = true;
		useColors = false;
		useTextures = false;
	}

	public void stop()
	{
		editing = false;
		if (useVBO)
		{
			compileVBO();
			generateVBO();
		} else
		{
			GL11.glEnd();
			GL11.glEndList();
		}
	}

	private void compileVBO()
	{
		dataBuffer.put(rawBuffer);
		dataBuffer.flip();
	}

	private void generateVBO()
	{
		ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
	}

	public void setRGB(float r, float g, float b)
	{
		useColors = true;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public void setColor(Color c)
	{
		useColors = true;
		this.r = c.getDR();
		this.g = c.getDG();
		this.b = c.getDB();
	}

	public void setA(float a)
	{
		useColors = true;
		this.a = a;
	}

	public void setUV(float u, float v)
	{
		if (!useTextures)
			System.err.println("WARNING! No image assigned");
		this.u = u;
		this.v = v;
	}

	public void setDepth(float z)
	{
		this.z = z;
	}

	public void addVertex(float x, float y)
	{
		this.x = x;
		this.y = y;
		loadVertexDataIntoArray();
		vertexCount++;
	}

	// public void addVertexWithUV(float x, float y, float u, float v) {
	// addVertexWithUV(x, y, 0.0f, u, v);
	// }

	public void loadVertexDataIntoArray()
	{
		if (useVBO)
		{
			rawBuffer[vertexCount * 9] = x;
			rawBuffer[vertexCount * 9 + 1] = y;
			rawBuffer[vertexCount * 9 + 2] = z;

			rawBuffer[vertexCount * 9 + 3] = r;
			rawBuffer[vertexCount * 9 + 4] = g;
			rawBuffer[vertexCount * 9 + 5] = b;
			rawBuffer[vertexCount * 9 + 6] = a;

			rawBuffer[vertexCount * 9 + 7] = u;
			rawBuffer[vertexCount * 9 + 8] = v;
		} else
		{
			// TODO reconsider Display List loading
			GL11.glTexCoord2f(u, v);
			if (useColors)
				GL11.glColor3f(r, g, b);
			GL11.glVertex3f(x, y, z);
		}
	}

	public void bindImage(Image i)
	{
		if (!editing)
			throw new RuntimeException("Buffer object not editable!");

		if (!useTextures)
		{
			this.useTextures = true;
			img = i;
		} else if (!useTextures && i != img)
		{
			throw new RuntimeException("Only one texture per buffer is supported!");
		}
	}

	public void render()
	{
		if (editing)
			throw new RuntimeException("Buffer is still being edited!");

		if (useTextures)
		{
			img.bindGLTexture();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		} else
			GL11.glDisable(GL11.GL_TEXTURE_2D);

		if (useVBO)
		{
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			if (useColors)
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			if (useTextures)
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ID);

			GL11.glVertexPointer(3, GL11.GL_FLOAT, 4 * 9, 0);

			if (useColors)
				GL11.glColorPointer(4, GL11.GL_FLOAT, 4 * 9, 4 * 3);

			if (useTextures)
				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 4 * 9, 4 * 7);

			GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

			if (useTextures)
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			if (useColors)
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		} else
		{
			GL11.glCallList(ID);
		}
	}
}
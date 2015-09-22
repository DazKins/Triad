package com.dazkins.triad.gfx;

public class BufferObjectData
{
	private float[] data;
	private int size;
	
	private BufferObjectRenderProperties props;

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
	
	private Image img;
	
	public BufferObjectData(int s)
	{
		props = new BufferObjectRenderProperties();
		data = new float[s];
		size = s;
		a = 1.0f;
		r = 1.0f;
		g = 1.0f;
		b = 1.0f;
	}
	
	public BufferObjectData(float[] d)
	{
		data = new float[d.length];
		size = d.length;
		for (int i = 0; i < d.length; i++)
		{
			data[i] = d[i];
		}
		props.setVertexCount(d.length / 9);
		size = data.length;
		a = 1.0f;
		r = 1.0f;
		g = 1.0f;
		b = 1.0f;
		x = 0;
		y = 0;
		z = 0;
		u = 0;
		v = 0;
	}
	
	public BufferObjectData clone()
	{
		BufferObjectData d = new BufferObjectData(data);
		d.img = img;
		return d;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public float[] getRawData()
	{
		return data;
	}
	
	public void reset()
	{
		props.reset();
	}
	
	public Image getImg()
	{
		return img;
	}
	
	public BufferObjectRenderProperties getRenderProperties()
	{
		return props;
	}
	
	public void setRGB(float r, float g, float b)
	{
		props.setUseColours(true);
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public void setColor(Color c)
	{
		props.setUseColours(true);
		this.r = c.getDR();
		this.g = c.getDG();
		this.b = c.getDB();
	}

	public void setA(float a)
	{
		props.setUseColours(true);
		this.a = a;
	}

	public void setUV(float u, float v)
	{
		props.setUseTextures(true);
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
		props.incrementVertexCount();
	}
	
	public void loadVertexDataIntoArray()
	{
		int vert = props.getVertexCount();
		
		data[vert * 9 + 0] = x;
		data[vert * 9 + 1] = y;
		data[vert * 9 + 2] = z;

		data[vert * 9 + 3] = r;
		data[vert * 9 + 4] = g;
		data[vert * 9 + 5] = b;
		data[vert * 9 + 6] = a;

		data[vert * 9 + 7] = u;
		data[vert * 9 + 8] = v;
	}

	public void bindImage(Image i)
	{
		props.setUseTextures(true);
		img = i;
	}
}
package com.dazkins.triad.gfx;

public class BufferObjectData
{
	public static final int VERTEX_ATTRIB_SIZE = 4;
	
	//TODO make private again
	public float[] data;
	
	private int size;
	
	private BufferObjectRenderProperties props;

	// Store the temporary variables to be loaded into the VBO
	private float x;
	private float y;
	private float u;
	private float v;
	
	private Image img;
	
	public BufferObjectData(int s)
	{
		props = new BufferObjectRenderProperties();
		data = new float[s];
		size = s;
	}
	
	public BufferObjectData(float[] d)
	{
		data = new float[d.length];
		size = d.length;
		for (int i = 0; i < d.length; i++)
		{
			data[i] = d[i];
		}
		props.setVertexCount(d.length / VERTEX_ATTRIB_SIZE);
		size = data.length;
		x = 0.0f;
		y = 0.0f;
		u = 0.0f;
		v = 0.0f;
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

	public BufferObjectData setUV(float u, float v)
	{
		props.setUseTextures(true);
		this.u = u;
		this.v = v;
		
		return this;
	}
	
	public BufferObjectData setXY(float x, float y)
	{
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public BufferObjectData pushVertex()
	{
		loadVertexDataIntoArray();
		props.incrementVertexCount();
		
		return this;
	}
	
	public void loadVertexDataIntoArray()
	{
		int vert = props.getVertexCount();
		
		data[vert * VERTEX_ATTRIB_SIZE + 0] = x;
		data[vert * VERTEX_ATTRIB_SIZE + 1] = y;

		data[vert * VERTEX_ATTRIB_SIZE + 2] = u;
		data[vert * VERTEX_ATTRIB_SIZE + 3] = v;
	}

	public void bindImage(Image i)
	{
		props.setUseTextures(true);
		img = i;
	}
}
package com.dazkins.triad.gfx;

public class BufferObjectRenderProperties
{
	private int vertexCount;
	private boolean useColors;
	private boolean useTextures;
	
	public BufferObjectRenderProperties clone()
	{
		BufferObjectRenderProperties p = new BufferObjectRenderProperties();
		p.vertexCount = vertexCount;
		p.useColors = useColors;
		p.useTextures = useTextures;
		return p;
	}

	public int getVertexCount()
	{
		return vertexCount;
	}

	public void setVertexCount(int vertexCount)
	{
		this.vertexCount = vertexCount;
	}

	public boolean isUseColours()
	{
		return useColors;
	}

	public void setUseColours(boolean useColours)
	{
		this.useColors = useColours;
	}

	public boolean isUseTextures()
	{
		return useTextures;
	}

	public void setUseTextures(boolean useTextures)
	{
		this.useTextures = useTextures;
	}
	
	public void incrementVertexCount()
	{
		vertexCount++;
	}
	
	public void reset()
	{
		useTextures = false;
		useColors = false;
		vertexCount = 0;
	}
}
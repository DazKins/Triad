package com.dazkins.triad.game.world;

import com.dazkins.triad.util.Loader;

//TODO merge with server chunk loader into a generic style class
public class ClientChunkGraphicsLoader 
{
	private Loader[] lds;

	private int topThreadcount = 5;

	public ClientChunkGraphicsLoader()
	{
		lds = new Loader[topThreadcount];
		for (int i = 0; i < topThreadcount; i++)
		{
			lds[i] = new Loader("Chunk Graphics");
		}
	}
	
	public boolean hasSpace()
	{
		for (int i = 0; i < topThreadcount; i++)
		{
			if (lds[i].isDone())
			{
				return true;
			}
		}
		return false;
	}

	public boolean addChunk(ChunkRenderer c)
	{
		for (int i = 0; i < topThreadcount; i++)
		{
			if (lds[i].isDone())
			{
				lds[i] = new Loader("Chunk");
				lds[i].addLoad(c);
				Loader.startThreadLoad(lds[i]);
				return true;
			}
		}
		return false;
	}
}
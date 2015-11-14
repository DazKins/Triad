package com.dazkins.triad.util;

public class LoaderManager
{
	private Loader[] lds;

	private int topThreadCount;

	public LoaderManager(int mThread)
	{
		topThreadCount = mThread;
		lds = new Loader[topThreadCount];
		for (int i = 0; i < topThreadCount; i++)
		{
			lds[i] = new Loader();
		}
	}
	
	public boolean hasSpace()
	{
		for (int i = 0; i < topThreadCount; i++)
		{
			if (lds[i].isDone())
			{
				return true;
			}
		}
		return false;
	}

	public boolean addLoadable(Loadable l)
	{
		for (int i = 0; i < topThreadCount; i++)
		{
			if (lds[i].isDone())
			{
				lds[i] = new Loader();
				lds[i].addLoad(l);
				Loader.startThreadLoad(lds[i]);
				return true;
			}
		}
		return false;
	}
}
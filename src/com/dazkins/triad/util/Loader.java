package com.dazkins.triad.util;

import java.util.ArrayList;
import java.util.List;

public class Loader implements Runnable
{
	protected List<Loadable> lds;

	private float percent;

	private boolean isLoadEmpty = true;

	private Thread t;

	private boolean done = true;

	public void setPercent(float p)
	{
		percent = p;
	}

	public boolean isDone()
	{
		return done;
	}

	public synchronized float getPercent()
	{
		return percent;
	}

	public Loader()
	{
		lds = new ArrayList<Loadable>();
	}

	public void addLoad(Loadable l)
	{
		if (l != null)
		{
			lds.add(l);
			isLoadEmpty = false;
		}
	}

	public void stop()
	{
		done = true;
		t = null;
	}

	public boolean contains(Loadable l)
	{
		return lds.contains(l);
	}

	public Loadable getLoad(int i)
	{
		return lds.get(i);
	}

	public static void startThreadLoad(Loader l)
	{
		Thread t = new Thread(l);
		t.start();
	}

	public void run()
	{
		done = false;
		if (lds.isEmpty())
			return;
		for (int i = 0; i < lds.size(); i++)
		{
			getLoad(i).load();
			setPercent((float) (i + 1) / (float) lds.size());
		}
		stop();
	}
}
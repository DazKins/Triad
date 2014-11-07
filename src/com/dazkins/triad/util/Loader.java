package com.dazkins.triad.util;

import java.util.ArrayList;
import java.util.List;

public class Loader implements Runnable {
	private List<Loadable> lds;
	
	private float percent;
	
	public synchronized void setPercent(float p) {
		percent = p;
	}
	
	public synchronized float getPercent() {
		return percent;
	}
	
	public Loader() {
		lds = new ArrayList<Loadable>();
	}
	
	public void addLoad(Loadable l) {
		lds.add(l);
	}

	public void run() {
		for (int i = 0; i < lds.size(); i++) {
			lds.get(i).load();
			setPercent((float) (i + 1) / (float) lds.size());
		}
	}
}
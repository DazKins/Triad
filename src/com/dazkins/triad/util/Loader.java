package com.dazkins.triad.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GLContext;

public class Loader implements Runnable {
	private List<Loadable> lds;
	
	private float percent;
	
	private boolean isLoadEmpty = true;
	
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
		if (l != null) {
			lds.add(l);
			isLoadEmpty = false;
		}
	}
	
	public void beginLoading() {
		if(!isLoadEmpty) {
			new Thread(this).run();
		}
	}
	
	public void run() {
		for (int i = 0; i < lds.size(); i++) {
			lds.get(i).load();
			setPercent((float) (i + 1) / (float) lds.size());
		}
	}
}
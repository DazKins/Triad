package com.dazkins.triad.util.pool;

public abstract class PoolableObject {
	private boolean destroyed;
	
	abstract Object create(Object[] args);
	
	public void destroy() {
		destroyed = true;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
}
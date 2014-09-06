package com.dazkins.triad.util.pool;

import java.lang.reflect.Array;


public class ObjectPool<T> {
	private int poolSize;
	private T objs[];
	private Class<? extends PoolableObject> c;
	private boolean usedIDs[];
	
	public ObjectPool(Class<? extends PoolableObject> c, int ps) {
		poolSize = ps;
		usedIDs = new boolean[ps];
		this.c = c;
		objs = (T[]) Array.newInstance(c, poolSize);
		for (int i = 0; i < objs.length; i++) {
			try {
				objs[i] = (T) c.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public T create(Object[] args) {
		int index = -1;
		for (int i = 0; i < poolSize; i++) {
			if (usedIDs[i] == false)
				index = i;
		}
		
		if (index == -1)
			return null;
		
		PoolableObject po = ((PoolableObject) objs[index]);
		
		po.create(args);
		
		usedIDs[index] = true;
		
		return (T) po;
	}
	
	public boolean isObjectDestroyed(PoolableObject o) {
		for (int i = 0; i < objs.length; i++) {
			PoolableObject po = ((PoolableObject) objs[i]);
			if (po == o) {
				return usedIDs[i];
			}
		}
		return false;
	}
	
	public void destroy(PoolableObject o) {
		for (int i = 0; i < objs.length; i++) {
			PoolableObject po = ((PoolableObject) objs[i]);
			if (po == o) {
				usedIDs[i] = false;
				return;
			}
		}
	}
}
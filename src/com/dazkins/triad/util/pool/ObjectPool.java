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
	
	public void create(Object[] args) {
		int index = -1;
		for (int i = 0; i < poolSize; i++) {
			if (((PoolableObject) objs[i]).isDestroyed() == false)
				index = 0;
		}
		
		if (index == -1)
			return;
		
		PoolableObject po = ((PoolableObject) objs[index]);
		
		po.create(args);
		
		usedIDs[index] = true;
	}
	
	public void destroy(PoolableObject o) {
		for (int i = 0; i < objs.length; i++) {
			PoolableObject po = ((PoolableObject) objs[i]);
			if (po == o)
				po.destroy();
		}
	}
	
//	public 
}
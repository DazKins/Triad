package com.dazkins.triad.util.pool;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;

import com.dazkins.triad.util.pool.factory.ObjectFactory;

public class ObjectPool<T> {
	private int poolSize;
	private T objs[];
	private Class<? extends PoolableObject> c;
	private boolean usedIDs[];
	
	private ObjectFactory<?> factory;
	
	public ObjectPool(Class<? extends PoolableObject> c, ObjectFactory<?> of, int ps) {
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
		factory = of;
	}
	
	public T getEmptyObjectForCreation() {
		int index = -1;
		for (int i = 0; i < poolSize; i++) {
			if (usedIDs[i] == false || ((PoolableObject)objs[i]).needDestruction())
				index = i;
		}
		
		if (index == -1) {
			return null;
		}
		
		usedIDs[index] = true;
		
		PoolableObject po = ((PoolableObject) objs[index]);
		
		return (T) po;
	}
	
	public ObjectFactory<?> getCurrentFactory() {
		return factory;
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
	
	public ArrayList<T> getUsedObjects() {
		ArrayList<T> r = new ArrayList<T>();
		
		for (int i = 0; i < usedIDs.length; i++) {
			if (usedIDs[i])
				r.add(objs[i]);
		}
		
		return r;
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
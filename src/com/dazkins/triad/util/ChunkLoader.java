package com.dazkins.triad.util;

import com.dazkins.triad.game.world.Chunk;

public class ChunkLoader {
	private Loader[] lds;
	
	private int topThreadcount = 8;
	
	public ChunkLoader() {
		lds = new Loader[topThreadcount];
		for (int i = 0; i < topThreadcount; i++) {
			lds[i] = new Loader("Chunk");
		}
	}
	
	public void addChunk(Chunk c) {
		for (int i = 0; i < topThreadcount; i++) {
			if (lds[i].isDone()) {
				lds[i] = new Loader("Chunk");
				lds[i].addLoad(c);
				Loader.startThreadLoad(lds[i]);
				return;
			}
		}
	}
}
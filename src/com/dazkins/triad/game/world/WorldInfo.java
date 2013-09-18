package com.dazkins.triad.game.world;

import com.dazkins.triad.file.SingleLineDatabaseFile;

public class WorldInfo {
	private int nChunksX;
	private int nChunksY;
	
	private byte ambientLightLevel;
	
	private String name;
	
	public void loadFromDatabase(SingleLineDatabaseFile dbs) {
		nChunksX = dbs.getInt("NCHUNKSX");
		nChunksY = dbs.getInt("NCHUNKSY");
		name = dbs.getString("NAME");
		ambientLightLevel = dbs.getByte("AMB_LIGHT");
	}

	public int getnChunksX() {
		return nChunksX;
	}

	public int getnChunksY() {
		return nChunksY;
	}

	public byte getAmbientLightLevel() {
		return ambientLightLevel;
	}

	public String getName() {
		return name;
	}
}
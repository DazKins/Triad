package com.dazkins.triad.game.world;

import com.dazkins.triad.file.SingleLineDatabaseFile;

public class WorldInfo {
	public int nChunksX;
	public int nChunksY;
	
	public byte ambientLightLevel;
	
	public String name;
	
	public void loadFromDatabase(SingleLineDatabaseFile dbs) {
		nChunksX = dbs.getInt("NCHUNKSX");
		nChunksY = dbs.getInt("NCHUNKSY");
		name = dbs.getString("NAME");
		ambientLightLevel = dbs.getByte("AMB_LIGHT");
	}
}
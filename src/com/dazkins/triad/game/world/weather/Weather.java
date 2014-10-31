package com.dazkins.triad.game.world.weather;

import com.dazkins.triad.game.world.World;

public abstract class Weather {
	public World operatingWorld;
	
	public void init(World o) {
		operatingWorld = o;
	}
	
	public abstract void tick();
}
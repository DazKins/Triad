package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.GLRenderer;

public interface GameState {
	public void init(Triad triad);
	
	public void render(GLRenderer t);
	
	public void tick();
}
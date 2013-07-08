package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.bitmap.Bitmap;

public interface GameState {
	public void init(Triad triad);
	
	public void render(Bitmap b);
	
	public void tick();
}
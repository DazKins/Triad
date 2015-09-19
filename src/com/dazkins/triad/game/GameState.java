package com.dazkins.triad.game;

import com.dazkins.triad.Triad;

public interface GameState
{
	public void init(Triad triad);

	public void render();

	public void tick();
}
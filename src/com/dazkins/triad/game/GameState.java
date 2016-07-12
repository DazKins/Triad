package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.input.InputHandler;

public interface GameState
{
	public void init(Triad triad, InputHandler input);

	public void render();

	public void tick();
}
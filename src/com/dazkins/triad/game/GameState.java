package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.input.InputHandler;

public interface GameState
{
	public void init(Triad triad, InputHandler input);

	public void render(RenderContext rc);

	public void tick();
}
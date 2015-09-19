package com.dazkins.triad.game.world;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Color;

public interface IWorldAccess
{
	public Tile getTile(int x, int y);

	public Color getTileColor(int x, int y);
}
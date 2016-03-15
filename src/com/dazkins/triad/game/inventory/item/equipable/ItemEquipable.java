package com.dazkins.triad.game.inventory.item.equipable;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.NormalisedRenderable;
import com.dazkins.triad.gfx.model.Quad;

public abstract class ItemEquipable extends Item
{
	private Quad equipQuads[];

	public ItemEquipable(int id, String name)
	{
		super(id, name, false);
		equipQuads = new Quad[20];
	}

	public Quad getEquipQuad(int f)
	{
		return equipQuads[f];
	}

	public boolean hasEquipQuad(int f)
	{
		return equipQuads[f] != null;
	}

	public void assignEquipQuad(Quad q, int f)
	{
		equipQuads[f] = q;
	}
}
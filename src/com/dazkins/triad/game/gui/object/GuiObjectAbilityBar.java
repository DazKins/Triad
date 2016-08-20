package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.ability.Ability;
import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.gfx.Camera;

public class GuiObjectAbilityBar extends GuiObject
{
	public static final float ABILITY_BOX_SIZE = 40.0f;
	public static final float ABILITY_BOX_SPACING = 5.0f;
	
	private AbilityBar abilityBar;
	private GuiObjectBox[] boxes;
	
	public GuiObjectAbilityBar(Gui g, AbilityBar b)
	{
		super(g);
		this.abilityBar = b;
		
		boxes = new GuiObjectBox[b.getSize()];
		for (int i = 0; i < boxes.length; i++)
		{
			boxes[i] = new GuiObjectBox(g);
		}
	}

	public void setupGraphics()
	{
		for (int i = 0; i < boxes.length; i++)
		{
			GuiObjectBox b = boxes[i];
			b.setX(x + i * (ABILITY_BOX_SIZE + ABILITY_BOX_SPACING));
			b.setY(y);
			b.setWidth(ABILITY_BOX_SIZE);
			b.setHeight(ABILITY_BOX_SIZE);
			b.setLayer(2);
		}
	}
	
	public void render()
	{
		for (int i = 0; i < boxes.length; i++)
		{
			boxes[i].render();
			Ability a = abilityBar.getAbility(i);
			if (a != null)
				a.renderImageIcon(x + i * (ABILITY_BOX_SIZE + ABILITY_BOX_SPACING), y, 2.0f, ABILITY_BOX_SIZE / Ability.ABILITY_ICON_SIZE);
		}
	}
}
package com.dazkins.triad.game.gui;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.gui.object.GuiObjectAbilityBar;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.util.TriadLogger;

public class GuiPlayerAbilityBar extends Gui
{
	private GuiObjectAbilityBar abilityBar;
	
	public GuiPlayerAbilityBar(Triad t, InputHandler i)
	{
		super(t, i);
	}
	
	public void setAbilityBar(AbilityBar b)
	{
		abilityBar = new GuiObjectAbilityBar(this, b);
		abilityBar.setX(win.getW() / 2.0f - b.getSize() * (GuiObjectAbilityBar.ABILITY_BOX_SIZE + GuiObjectAbilityBar.ABILITY_BOX_SPACING) / 2.0f);
		abilityBar.setupGraphics();
	}
	
	public boolean hasAbilityBar()
	{
		return abilityBar != null;
	}

	public void onExit()
	{
		
	}
	
	public void render()
	{
		if (abilityBar != null)
		{
			abilityBar.render();
		}
	}
}
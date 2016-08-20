package com.dazkins.triad.game.gui.object;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.ability.Ability;
import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.gfx.OpenGLHelper;

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
			{
				int bCd = a.getCooldown();
				int cd = abilityBar.getCooldown(i);
				float perc = (float) cd / bCd;
				
				float ax = x + i * (ABILITY_BOX_SIZE + ABILITY_BOX_SPACING);
				
				a.renderImageIcon(ax, y, 2.0f, ABILITY_BOX_SIZE / Ability.ABILITY_ICON_SIZE);
				
				GL11.glColor4f(0.4f, 0.4f, 0.4f, 0.7f);
				OpenGLHelper.immDrawQuad(ax, y, ax + ABILITY_BOX_SIZE, y + ABILITY_BOX_SIZE * perc, 3.0f);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
	}
}
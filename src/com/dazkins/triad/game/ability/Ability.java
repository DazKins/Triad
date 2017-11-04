package com.dazkins.triad.game.ability;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public abstract class Ability
{
	public static final float ABILITY_ICON_SIZE = 32.0f;
	public static final Ability[] abilities = new Ability[256];
	
	public static final Ability swingWeapon = new AbilitySwingWeapon(0);
	
	private String name;
	private int id;
	
	private boolean isImageIconLoaded;
	private Image img;
	private BufferObject imgIcon;
	
	public Ability(int id, String n)
	{
		this.id = id;
		this.name = n;
		
		abilities[id] = this;
	}
	
	public abstract int getCooldown();
	
	public int getID()
	{
		return id;
	}
	
	private void loadImageIconModel()
	{
		img = Image.getImageFromName("ability_" + name);
		imgIcon = new BufferObject(36);
		imgIcon.resetData();
		img.loadSpriteBufferObject(imgIcon, 0, 0, 32, 32, 0, 0, 16, 16, 0.0f, 0.0f);
		imgIcon.compile();
		
		isImageIconLoaded = true;
	}
	
	public void renderImageIcon(float x, float y, float z, float scale)
	{
		if (!isImageIconLoaded)
			loadImageIconModel();
		
		GL11.glPushMatrix();

		if (x != 0 || y != 0 || z != 0)
			GL11.glTranslatef(x, y, z);

		if (scale != 1)
			GL11.glScalef(scale, scale, 1.0f);

		imgIcon.render();

		GL11.glPopMatrix();
	}
	
	public abstract void onUse(World w, Mob m);
}
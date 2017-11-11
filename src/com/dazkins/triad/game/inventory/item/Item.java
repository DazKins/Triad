package com.dazkins.triad.game.inventory.item;

import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.math.Matrix3;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.EntityItemStack;
import com.dazkins.triad.game.inventory.item.equipable.armour.body.ItemTestChest;
import com.dazkins.triad.game.inventory.item.equipable.armour.feet.ItemTestFeet;
import com.dazkins.triad.game.inventory.item.equipable.armour.head.ItemTestHelmet;
import com.dazkins.triad.game.inventory.item.equipable.armour.legs.ItemTestLegs;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemTestSword;
import com.dazkins.triad.game.inventory.item.equipable.weapon.harvestTool.ItemAxe;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class Item
{
	public static Item items[] = new Item[256];
	
	public static final ItemTestChest testChest = new ItemTestChest(0);
	public static final ItemTestFeet testFeet = new ItemTestFeet(1);
	public static final ItemTestHelmet testHelmet = new ItemTestHelmet(2);
	public static final ItemTestLegs testLegs = new ItemTestLegs(3);
	public static final ItemTestSword testSword = new ItemTestSword(4);
	public static final ItemAxe axe = new ItemAxe(5);
	public static final ItemLog log = new ItemLog(6);
	
	protected String name;
	protected int ID;
	protected boolean stackable;
	private Image img;
	private BufferObject icon;
	
	private boolean imageIconLoaded = false;

	public Item(int id, String name, boolean s)
	{
		this.ID = id;
		this.name = name;
		this.stackable = s;
		
		items[id] = this;
	}

	public static void dropItemStack(World w, float x, float y, Item i, int n)
	{
		dropItemStack(w, x, y, new ItemStack(i, n));
	}

	public static void dropItemStack(World w, float x, float y, ItemStack is)
	{
		int no = is.getStackSize();
		if (no > 4 && is.getItemType().isStackable())
		{
			dropStackWithVelocity(w, x, y, is, no);
		} else
		{
			for (int i = 0; i < no; i++)
			{
				Item type = is.getItemType();
				ItemStack stack = new ItemStack(type, 1);
				dropStackWithVelocity(w, x, y, stack, no);
			}
		}
	}

	private static void dropStackWithVelocity(World w, float x, float y, ItemStack stack, int no)
	{
		float speed = (float) Math.sqrt(no) * 28.0f;
		EntityItemStack eStack = new EntityItemStack(w, x, y, stack);
		float xa = ((float) Math.random() - 0.5f) * speed;
		float ya = ((float) Math.random() - 0.5f) * speed;
		eStack.push(xa, ya);
		w.addEntity(eStack);
	}

	private void loadImageIconModel()
	{
		img = Image.getImageFromName("item_" + name);
		icon = new BufferObject(36);
		icon.resetData();
		img.loadSpriteBufferObject(icon, 0, 0, 32, 32, 0, 0, 32, 32, 0.0f, 0.0f);
		icon.compile();
		
		imageIconLoaded = true;
	}

	public String getName()
	{
		return name;
	}

	public boolean isStackable()
	{
		return stackable;
	}
	
	public int getID()
	{
		return ID;
	}

	public void renderIcon(RenderContext rc, float x, float y, float scale)
	{
		if (!imageIconLoaded)
			loadImageIconModel();

		rc.getMatrixStack().push();

		if (x != 0 || y != 0)
			rc.getMatrixStack().transform(Matrix3.translate(x, y));

		if (scale != 1)
			rc.getMatrixStack().transform(Matrix3.scale(scale));

		icon.render();

		rc.getMatrixStack().pop();
	}

	public Image getImage()
	{
		if (!imageIconLoaded)
			loadImageIconModel();
		
		return img;
	}
}
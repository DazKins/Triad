package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.inventory.item.equipable.armour.head.ItemArmourHead;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Image;

public class ModelHumanoid extends Model {
	protected Quad head[] = new Quad[4];
	protected Quad rightArm[] = new Quad[4];
	protected Quad leftArm[] = new Quad[4];
	protected Quad body[] = new Quad[4];
	protected Quad leftLeg[] = new Quad[4];
	protected Quad rightLeg[] = new Quad[4];
	
	public Quad[] getHead() {
		return head;
	}

	public Quad[] getRightArm() {
		return rightArm;
	}

	public Quad[] getLeftArm() {
		return leftArm;
	}

	public Quad[] getBody() {
		return body;
	}

	public Quad[] getLeftLeg() {
		return leftLeg;
	}

	public Quad[] getRightLeg() {
		return rightLeg;
	}

	public ModelHumanoid(Image i) {
		super(i);
		
		int up = Facing.UP;
		int down = Facing.DOWN;
		int left = Facing.LEFT;
		int right = Facing.RIGHT;
		
		head[up] = new Quad(-9, 32, 18, 16, 27, 0, 9, 8);
		head[up].setRenderLayer(2);
		head[down] = new Quad(-9, 32, 18, 16, 0, 0, 9, 8);
		head[down].setRenderLayer(2);
		head[left] = new Quad(-9, 32, 18, 16, 18, 0, 9, 8);
		head[left].setRenderLayer(2);
		head[right] = new Quad(-9, 32, 18, 16, 9, 0, 9, 8);
		head[right].setRenderLayer(2);
		addQuads(head);

		rightArm[up] = new Quad(4, 16, 10, 18, 0, 8, 5, 9);
		rightArm[up].setCenterOfRotation(9, 34);
		rightArm[up].setRenderLayer(1);
		rightArm[down] = new Quad(-14, 16, 10, 18, 5, 8, 5, 9);
		rightArm[down].setCenterOfRotation(-9, 34);
		rightArm[down].setRenderLayer(-1);
		rightArm[left] = new Quad(-4, 16, 10, 18, 10, 8, 5, 9);
		rightArm[left].setCenterOfRotation(1, 34);
		rightArm[left].setRenderLayer(1);
		rightArm[right] = new Quad(-4, 16, 10, 18, 15, 8, 5, 9);
		rightArm[right].setCenterOfRotation(1, 34);
		rightArm[right].setRenderLayer(-1);
		addQuads(rightArm);
		
		
		rightLeg[up] = new Quad(-1, 0, 10, 18, 20, 8, 5, 9);
		rightLeg[up].setCenterOfRotation(4, 18);
		rightLeg[up].setRenderLayer(-2);
		rightLeg[down] = new Quad(-9, 0, 10, 18, 25, 8, 5, 9);
		rightLeg[down].setCenterOfRotation(-4, 18);
		rightLeg[down].setRenderLayer(-2);
		rightLeg[left] = new Quad(-4, 0, 10, 18, 30, 8, 5, 9);
		rightLeg[left].setCenterOfRotation(1, 18);
		rightLeg[left].setRenderLayer(-2);
		rightLeg[right] = new Quad(-4, 0, 10, 18, 35, 8, 5, 9);
		rightLeg[right].setCenterOfRotation(1, 18);
		rightLeg[right].setRenderLayer(0);
		addQuads(rightLeg);
		
		
		leftArm[up] = new Quad(-14, 16, 10, 18, 0, 17, 5, 9);
		leftArm[up].setCenterOfRotation(-9, 34);
		leftArm[up].setRenderLayer(-1);
		leftArm[down] = new Quad(4, 16, 10, 18, 5, 17, 5, 9);
		leftArm[down].setCenterOfRotation(9, 34);
		leftArm[down].setRenderLayer(1);
		leftArm[left] = new Quad(-4, 16, 10, 18, 10, 17, 5, 9);
		leftArm[left].setCenterOfRotation(1, 34);
		leftArm[left].setRenderLayer(-1);
		leftArm[right] = new Quad(-4, 16, 10, 18, 15, 17, 5, 9);
		leftArm[right].setCenterOfRotation(1, 34);
		leftArm[right].setRenderLayer(1);
		addQuads(leftArm);
		
		
		leftLeg[up] = new Quad(-9, 0, 10, 18, 20, 17, 5, 9);
		leftLeg[up].setCenterOfRotation(-4, 18);
		leftLeg[up].setRenderLayer(-2);
		leftLeg[down] = new Quad(-1, 0, 10, 18, 25, 17, 5, 9);
		leftLeg[down].setCenterOfRotation(4, 18);
		leftLeg[down].setRenderLayer(-2);
		leftLeg[left] = new Quad(-4, 0, 10, 18, 30, 17, 5, 9);
		leftLeg[left].setCenterOfRotation(1, 18);
		leftLeg[left].setRenderLayer(0);
		leftLeg[right] = new Quad(-4, 0, 10, 18, 35, 17, 5, 9);
		leftLeg[right].setCenterOfRotation(1, 18);
		leftLeg[right].setRenderLayer(-2);
		addQuads(leftLeg);
		
		
		body[up] = new Quad(-9, 16, 18, 18, 0, 26, 9, 9);
		body[up].setRenderLayer(0);
		body[down] = new Quad(-9, 16, 18, 18, 9, 26, 9, 9);
		body[down].setRenderLayer(0);
		body[left] = new Quad(-4, 16, 10, 18, 18, 26, 5, 9);
		body[left].setRenderLayer(0);
		body[right] = new Quad(-4, 16, 10, 18, 23, 26, 5, 9);
		body[right].setRenderLayer(0);
		addQuads(body);
	}
		
	public void render(Entity e) {
		setOffset(e.getX(), e.getY());
		setDepth(Tile.yPosToDepth(e.getY()));
		
		int f = e.getFacing();
		
		enableSelectiveRendering();

		addQuadToRenderQueue(body[f]);
		addQuadToRenderQueue(head[f]);
		addQuadToRenderQueue(rightLeg[f]);
		addQuadToRenderQueue(leftLeg[f]);
		addQuadToRenderQueue(rightArm[f]);
		addQuadToRenderQueue(leftArm[f]);
		
		Mob m = (Mob) e;
		EquipmentInventory einv = m.getEquipmentInventory();
		
		addHeadPiece(head[f], f, einv);
		
		super.render();
	}
	
	public void addHeadPiece(Quad q, int f, EquipmentInventory einv) {
		ItemStack is = einv.getItemStack(EquipmentInventory.HEAD);
		if (is != null) {
			ItemArmourHead item = (ItemArmourHead) is.getItemType();
			Quad q0 = null;
			if (!item.hasEquipQuad(f)) {
				q0 = new Quad(-24, -20, 64, 64, (f + 1) * 32, 0, 32, 32);
				q0.setRenderLayer(0.1f);
				q0.init(item.getImage());
				q0.generate();
				item.assignEquipQuad(q0, f);
			} else {
				q0 = item.getEquipQuad(f);
			}
			q.addTemporaryChildQuad(q0);
		}
	}
}
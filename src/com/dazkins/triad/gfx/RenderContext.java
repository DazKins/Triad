package com.dazkins.triad.gfx;

import java.util.ArrayList;

import com.dazkins.triad.math.Matrix3;
import com.dazkins.triad.math.MatrixStack;

public class RenderContext
{
	private MatrixStack matrixStack;
	private ArrayList<Renderable> renderables;
	
	public RenderContext()
	{
		renderables = new ArrayList<>();
		matrixStack = new MatrixStack();
	}
	
	public void addToRender(BufferObject bo)
	{
		Matrix3 curMat = matrixStack.peek();
		Renderable r = new Renderable(bo, curMat);
		renderables.add(r);
	}
	
	public MatrixStack getMatrixStack()
	{
		return matrixStack;
	}
	
	public void render()
	{
		for (Renderable r : renderables)
		{
			r.render();
		}
	}
	
	public void refresh()
	{
		matrixStack.loadIdentity();
		renderables.clear();
	}
}
package com.dazkins.triad.math;

import java.util.Stack;

public class MatrixStack
{
	private Stack<Matrix3> stack;
	
	public MatrixStack()
	{
		stack = new Stack<>();
		loadIdentity();
	}
	
	public Matrix3 peek()
	{
		return stack.peek();
	}
	
	int pushpop = 0;
	
	public void pop()
	{
		stack.pop();
	}
	
	public void push()
	{
		stack.push(peek());
	}
	
	public void transform(Matrix3 m)
	{
		Matrix3 cur = stack.pop();
		Matrix3 n = Matrix3.mult(cur, m);
		stack.push(n);
	}
	
	public void loadIdentity()
	{
		stack.clear();
		stack.push(Matrix3.identity());
	}
}